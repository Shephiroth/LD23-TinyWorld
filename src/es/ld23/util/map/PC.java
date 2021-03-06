package es.ld23.util.map;

import es.ld23.equipment.Bullet;
import es.ld23.util.BBRectangle;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public abstract class PC {

	public static final int PC_MOVE_ABA = 0;
	public static final int PC_MOVE_IZQ = 1;
	public static final int PC_MOVE_DER = 2;
	public static final int PC_MOVE_ARR = 3;
	public static final int PC_MOVE_EMPTY = 4;
	public static final int PC_DIA_ARRIZQ = 4;
	public static final int PC_DIA_ABAIZQ = 5;
	public static final int PC_DIA_ARRDER = 6;
	public static final int PC_DIA_ABADER = 7;
	public static final double walk_speed = 0.27;
	public static final double diagonal_change = Math.sqrt(0.5);
	protected BBRectangle BB;
	//texture
	protected double dx, dy;
	protected double tx, ty;
	protected double left, top;
	//walking 
	protected int walk_frame;//cols
	protected int walk_direction;//fils
	protected long deltaAcumulado;
	//shooting
	protected int delay;
	protected int shootDelay;

	protected PC() {
	}

	public PC(double x, double y) {
		this.left = x;
		this.top = y;
		BB = new BBRectangle(x, y, Tile.tile_width, Tile.tile_height);
	}

	public void moveTo(double x, double y) {
		this.left = x;
		this.top = y;
		BB.moveTo(x, y);
	}

	public void move(double dx, double dy) {
		this.left += dx;
		this.top += dy;
		BB.move(dx, dy);
	}

	public BBRectangle getBB() {
		return BB;
	}

	public double getX() {
		return left;
	}

	public double getY() {
		return top;
	}

	public void setTextureFil(int fil, int max_fil) {
		dy = 1.0 / max_fil;
		ty = dy * fil;
	}

	public void setTextureCol(int col, int col_max) {
		dx = 1.0 / col_max;
		tx = dx * col;
	}

	public void render() {
		double ntx = tx + dx * walk_frame;
		double nty = ty + dy * walk_direction;


		glTexCoord2d(ntx, nty);
		glVertex2d(left, top);
		glTexCoord2d(ntx, nty + dy);
		glVertex2d(left, top + Tile.tile_height);
		glTexCoord2d(ntx + dx, nty + dy);
		glVertex2d(left + Tile.tile_width, top + Tile.tile_height);
		glTexCoord2d(ntx + dx, nty);
		glVertex2d(left + Tile.tile_width, top);
	}

	public void cancelMovement(long delta) {
	}

	public boolean hurt(double dmg) {
		return false;
	}

	public void walkTick(long delta) {
		//empty
		deltaAcumulado += delta;
		if (deltaAcumulado > 250) {
			deltaAcumulado -= 250;
			walk_frame++;
			if (walk_frame >= 3) {
				walk_frame = 0;
			}
		}
	}

	public void renderText(TrueTypeFont font, Color color) {
	}

	public int getScore() {
		return 0;
	}

	public boolean canShoot() {
		return delay <= 0;
	}

	public void tick(long delta) {
		delay -= delta;
	}

	public Bullet fire() {
		delay = shootDelay;
		return null;
	}
	public void fakeFire() {
		delay = shootDelay;
	}
}
