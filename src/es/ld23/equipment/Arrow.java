package es.ld23.equipment;

public class Arrow extends Weapon{
	public static final Arrow Default = new Arrow(0,7,250,250);
	
	public Arrow(int weaponCol, double dmg, int deltaTime, int delay) {
		super(weaponCol, true, dmg, deltaTime, delay);
		ty_top += dy;
	}
}
