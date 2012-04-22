package es.ld23.equipment;

import es.ld23.util.BBRectangle;
import es.ld23.util.map.PC;
import static org.lwjgl.opengl.GL11.*;

public class Bullet {

	private static final int l = 48;
	private static final double dx = Weapon.dx;
	private static final double dy = Weapon.dy;
	private double x, y;
	private double tx, ty;
	private double mx, my;
	private int frameDir;
	private BBRectangle BB;
	private int deltaTime;
	private double dmg;

	public Bullet(double dmg, int deltaTime) {
		this.x = 0;
		this.y = 0;
		this.dmg = dmg;
		this.deltaTime = deltaTime;
		BB = new BBRectangle(6, 6, 12, 12);
		tx = 0;
		ty = dy * 4;
	}

	public double getDmg() {
		return dmg;
	}

	public void upgrade() {
		ty = dy * 5;
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
//		glColor3d(1,0,0);
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

	public boolean tick(long delta) {
		deltaTime -= delta;
		double nx = mx * delta;
		double ny = my * delta;

		x += nx;
		y += ny;
		BB.move(nx, ny);

		return (deltaTime > 0);
	}
}
