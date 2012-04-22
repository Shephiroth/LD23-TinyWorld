package es.ld23.equipment;

public class Weapon {

	public static final Weapon bow = new Weapon(10, true, 10);
	private boolean createBullet;
	private double dmg;
	private double range;

	public Weapon(double dmg, boolean createBullet, double range) {
		this.dmg = dmg;
		this.createBullet = createBullet;
		this.range = range;
	}
}
