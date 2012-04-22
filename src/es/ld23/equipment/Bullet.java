package es.ld23.equipment;

import es.ld23.util.BBRectangle;
import static org.lwjgl.opengl.GL11.*;

public class Bullet {

	private static final int l = 24;
	protected static final double dx = Weapon.dx;
	protected static final double dy = Weapon.dy;
	protected double x, y;
	protected double tx, ty;
	protected int dir;
	protected BBRectangle BB;

	public Bullet(int dir) {
		this.dir = dir;
		this.x = 0;
		this.y = 0;
		BB = new BBRectangle(0, 0, 24, 24);
		tx = 0;
		ty = dy * 4;
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		BB.moveTo(x, y);
	}

	public void render() {
		glTexCoord2d(tx, ty);
		glVertex2d(x, y);
		glTexCoord2d(tx, ty + dy);
		glVertex2d(x, y + l);
		glTexCoord2d(tx + dx, ty + dy);
		glVertex2d(x + l, y + l);
		glTexCoord2d(tx + dx, ty);
		glVertex2d(x + l, y);
	}

	public void tick(long delta) {
	}
}
