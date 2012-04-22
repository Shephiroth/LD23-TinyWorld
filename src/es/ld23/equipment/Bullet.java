package es.ld23.equipment;

import es.ld23.util.BBRectangle;
import es.ld23.util.map.PC;
import static org.lwjgl.opengl.GL11.*;

public class Bullet {

	private static final int l = 48;
	protected static final double dx = Weapon.dx;
	protected static final double dy = Weapon.dy;
	protected double x, y;
	protected double tx, ty;
	protected double mx, my;
	protected int frameDir;
	protected BBRectangle BB;
	protected double dmg;

	public Bullet(double dmg) {
		this.x = 0;
		this.y = 0;
		this.dmg = dmg;
		BB = new BBRectangle(0, 0, 24, 24);
		tx = 0;
		ty = dy * 4;
	}

	public double getDmg() {
		return dmg;
	}

	public BBRectangle getBB() {
		return BB;
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		BB.moveTo(x, y);
	}

	public void setDirection(int dir) {
		double recto = PC.walk_speed * 1.4;
		double diagonal = recto * PC.diagonal_change;
		switch (dir) {
			case PC.PC_DIA_ABADER:
				frameDir = 3;
				mx = diagonal;
				my = diagonal;
				break;
			case PC.PC_DIA_ABAIZQ:
				frameDir = 5;
				mx = -diagonal;
				my = diagonal;
				break;
			case PC.PC_DIA_ARRDER:
				frameDir = 1;
				mx = diagonal;
				my = -diagonal;
				break;
			case PC.PC_DIA_ARRIZQ:
				frameDir = 7;
				mx = -diagonal;
				my = -diagonal;
				break;
			case PC.PC_MOVE_ABA:
				frameDir = 4;
				mx = 0;
				my = recto;
				break;
			case PC.PC_MOVE_IZQ:
				frameDir = 6;
				mx = -recto;
				my = 0;
				break;
			case PC.PC_MOVE_DER:
				frameDir = 2;
				mx = recto;
				my = 0;
				break;
			case PC.PC_MOVE_ARR:
				frameDir = 0;
				mx = 0;
				my = -recto;
				break;
		}

	}

	public void render() {
		double ntx = tx + dx * frameDir;

		glTexCoord2d(ntx, ty);
		glVertex2d(x, y);
		glTexCoord2d(ntx, ty + dy);
		glVertex2d(x, y + l);
		glTexCoord2d(ntx + dx, ty + dy);
		glVertex2d(x + l, y + l);
		glTexCoord2d(ntx + dx, ty);
		glVertex2d(x + l, y);
	}

	public void tick(long delta) {
		double nx = mx * delta;
		double ny = my * delta;

		x += nx;
		y += ny;
		BB.move(nx, ny);
	}
}
