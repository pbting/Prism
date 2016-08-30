package exetuor.game;

import java.util.ArrayList;
import java.util.List;

import com.road.yishi.log.core.event.AbstractEventObject;
import com.road.yishi.log.core.event.ObjectEvent;

import exetuor.event.EventNameType;

/**
 * 
 * <pre>
 * 	监听每个属性达到200 以上的数值，都是：技能达人称号,40以上 超能达人
 * </pre>
 */
public class GamePlayer extends AbstractEventObject implements EventNameType{

	private int attack ;
	private int defence ;
	private int magicalAttack ;
	private int magicalDefence ;
	private int live ;
	private int forceHit ;
	private int reCrit ;
	private int wreck ;
	private int parry ;
	List<GamePlayerCondition<GamePlayer,?>> playerConditions = new ArrayList<GamePlayerCondition<GamePlayer,?>>();
	public GamePlayer() {
	}
	
	public void init(){
		DefenceCondtion defenceCondtion1 = new DefenceCondtion(this, null, new GamePlayerDTO(200));
		defenceCondtion1.addEvent();
		playerConditions.add(defenceCondtion1);
		DefenceCondtion defenceCondtion2 = new DefenceCondtion(this, null, new GamePlayerDTO(400));
		defenceCondtion2.addEvent();
		playerConditions.add(defenceCondtion2);
		
		AttackCondition defenceCondtion3 = new AttackCondition(this, null, new GamePlayerDTO(200));
		defenceCondtion3.addEvent();
		playerConditions.add(defenceCondtion3);
		AttackCondition defenceCondtion4 = new AttackCondition(this, null, new GamePlayerDTO(400));
		defenceCondtion2.addEvent();
		playerConditions.add(defenceCondtion4);
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
		notifyListeners(new ObjectEvent<Integer>(this,attack,GamePlayer.PROPERTY_ATTACK_CHANGE));
	}
	public int getDefence() {
		return defence;
	}
	public void setDefence(int defence) {
		this.defence = defence;
		notifyListeners(new ObjectEvent<Integer>(this, defence, GamePlayer.PROPERTY_DEFENCE_CHANGE));
	}
	public int getMagicalAttack() {
		return magicalAttack;
	}
	public void setMagicalAttack(int magicalAttack) {
		this.magicalAttack = magicalAttack;
	}
	public int getMagicalDefence() {
		return magicalDefence;
	}
	public void setMagicalDefence(int magicalDefence) {
		this.magicalDefence = magicalDefence;
	}
	public int getLive() {
		return live;
	}
	public void setLive(int live) {
		this.live = live;
	}
	public int getForceHit() {
		return forceHit;
	}
	public void setForceHit(int forceHit) {
		this.forceHit = forceHit;
	}
	public int getReCrit() {
		return reCrit;
	}
	public void setReCrit(int reCrit) {
		this.reCrit = reCrit;
	}
	public int getWreck() {
		return wreck;
	}
	public void setWreck(int wreck) {
		this.wreck = wreck;
	}
	public int getParry() {
		return parry;
	}
	public void setParry(int parry) {
		this.parry = parry;
	}
}
