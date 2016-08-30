package exetuor.game;

import com.road.yishi.log.core.event.ObjectEvent;
import com.road.yishi.log.core.event.ObjectListener;

public class DefenceCondtion extends GamePlayerCondition<GamePlayer,Integer> {

	public DefenceCondtion(GamePlayer observiable, Integer v, GamePlayerDTO gamePlayerDTO) {
		super(observiable, v, gamePlayerDTO);
	}

	@Override
	public void addEvent() {
		ObjectListener<GamePlayer> listener = new ObjectListener<GamePlayer>() {

			@Override
			public void onEvent(ObjectEvent<?> event) {
				int defence = (int) event.getValue();
				DefenceCondtion.this.setValue(defence);
				DefenceCondtion.this.handler();
			}
		};
		getObserviable().addListener(listener, GamePlayer.PROPERTY_DEFENCE_CHANGE);
	}

	@Override
	public boolean isFinished() {
		
		return getGamePlayerDTO().getTarget() <= getValue();
	}
}
