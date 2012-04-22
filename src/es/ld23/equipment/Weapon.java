package es.ld23.equipment;

import java.awt.Rectangle;
import static org.lwjgl.opengl.GL11.*;

public class Weapon {

	private static final double ty_top = 48.0 / 256.0;
	public static final double dy = 24.0 / 256.0;
	public static final double dx = 24.0 / 256.0;
	public static final Weapon bow = new Weapon(0, true, 20,10,350);
	private boolean createBullet;
	private double dmg;
	private int delay;
	private int deltaTime;
	private int weaponCol;

	public Weapon(int weaponCol, boolean createBullet, double dmg, int deltaTime, int delay) {
		this.dmg = dmg;
		this.createBullet = createBullet;
		this.deltaTime = deltaTime*100;
		this.weaponCol = weaponCol;
		this.delay = delay;
	}

	public void render(Rectangle posicion) {
		glDisable(GL_BLEND);
		glBegin(GL_QUADS);
		{
			glColor3d(0, 0, 0);
			glVertex2d(posicion.x, posicion.y);
			glVertex2d(posicion.x, posicion.y + posicion.height);
			glVertex2d(posicion.x + posicion.width, posicion.y + posicion.height);
			glVertex2d(posicion.x + posicion.width, posicion.y);
		}
		glEnd();

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		{
			glColor3d(1, 1, 1);
			glTexCoord2d(0, ty_top);
			glVertex2d(posicion.x, posicion.y);
			glTexCoord2d(0, ty_top + dy);
			glVertex2d(posicion.x, posicion.y + posicion.height);
			glTexCoord2d(dx, ty_top + dy);
			glVertex2d(posicion.x + posicion.width, posicion.y + posicion.height);
			glTexCoord2d(dx, ty_top);
			glVertex2d(posicion.x + posicion.width, posicion.y);
		}
		glEnd();
	}

	public Bullet fire() {
		if (createBullet) {
			return new Bullet(dmg, deltaTime);
		}
		return null;
	}
	public int getDelay() {return delay;}
}
