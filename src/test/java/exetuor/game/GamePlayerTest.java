package exetuor.game;

public class GamePlayerTest {

	public static void main(String[] args) throws Exception {
		GamePlayer gamePlayer = new GamePlayer();
		gamePlayer.init();
		gamePlayer.setDefence(110);
		Thread.sleep(1000);
		gamePlayer.setDefence(120);
		Thread.sleep(1000);
		gamePlayer.setDefence(130);
		Thread.sleep(1000);
		gamePlayer.setDefence(140);
		Thread.sleep(1000);
		gamePlayer.setDefence(150);
		Thread.sleep(1000);
		gamePlayer.setDefence(160);
		gamePlayer.setAttack(260);
		Thread.sleep(1000);
		gamePlayer.setDefence(170);
		Thread.sleep(1000);
		gamePlayer.setDefence(180);
		Thread.sleep(1000);
		gamePlayer.setDefence(190);
		Thread.sleep(1000);
		gamePlayer.setDefence(200);
		Thread.sleep(1000);
		gamePlayer.setDefence(210);
		Thread.sleep(1000);
	}
}
