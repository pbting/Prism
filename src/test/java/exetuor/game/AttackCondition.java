package exetuor.game;

import com.road.yishi.log.core.event.ObjectEvent;
import com.road.yishi.log.core.event.ObjectListener;

public class AttackCondition extends GamePlayerCondition<GamePlayer,Integer> {

	public AttackCondition(GamePlayer observiable, Integer v,GamePlayerDTO gamePlayerDTO) {
		super(observiable, v,gamePlayerDTO);
	}

	@Override
	public void addEvent() {
		ObjectListener<GamePlayer> objectListener = new ObjectListener<GamePlayer>() {

			@Override
			public void onEvent(ObjectEvent<?> event) {
				AttackCondition.this.handler();
			}
		};
		getObserviable().addListener(objectListener, GamePlayer.PROPERTY_ATTACK_CHANGE);
	}

	@Override
	public boolean isFinished() {
		
		return getGamePlayerDTO().getTarget()<= getValue();
	}
}
