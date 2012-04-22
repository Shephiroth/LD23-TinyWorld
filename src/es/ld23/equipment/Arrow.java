package es.ld23.equipment;

public class Arrow extends Weapon {

	public static final Arrow arrows[] = new Arrow[]{
		new Arrow(0, 7, 250, 250),new Arrow(1, 12, 250, 250),
		new Arrow(2, 17, 250, 250),new Arrow(3, 25, 250, 250),
		new Arrow(4, 51, 250, 250),new Arrow(5, 100, 250, 250)
	};

	public Arrow(int weaponCol, double dmg, int deltaTime, int delay) {
		super(weaponCol, true, dmg, deltaTime, delay);
		ty_top += dy;
	}
}
