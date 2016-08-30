package exetuor.game;

import exetuor.condition.BaseCondition;

@SuppressWarnings("hiding")
public abstract class GamePlayerCondition<GamePlayer,V> extends BaseCondition<GamePlayer,V> {
	protected GamePlayerDTO gamePlayerDTO ;
	public GamePlayerCondition(GamePlayer observiable, V v,GamePlayerDTO gamePlayerDTO ) {
		super(observiable, v);
		this.gamePlayerDTO = gamePlayerDTO;
	}
	
	@Override
	public abstract void addEvent() ;

	@Override
	public abstract boolean isFinished() ;

	@Override
	public void handler() {
		if(this instanceof AttackCondition | this instanceof DefenceCondtion){
			boolean isFinish = isFinished();
			if(isFinish){
				System.out.println("value:"+this.getValue()+"----已经达成:"+this.getGamePlayerDTO().getTarget());
			}
			return ;
		}
	}

	public GamePlayerDTO getGamePlayerDTO() {
		return gamePlayerDTO;
	}

	public void setGamePlayerDTO(GamePlayerDTO gamePlayerDTO) {
		this.gamePlayerDTO = gamePlayerDTO;
	}
}
