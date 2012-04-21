package es.ld23.util.map;

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
	protected BBRectangle BB;
	protected double dx, dy;
	protected double tx, ty;
	protected double left, top;

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

	public void setTextureFil(int fil, int max_fil) {
		dy = 1.0 / max_fil;
		ty = dy * fil;
	}

	public void setTextureCol(int col, int col_max) {
		dx = 1.0 / col_max;
		tx = dx * col;
	}

	public void render() {
		glTexCoord2d(tx, ty);
		glVertex2d(left, top);
		glTexCoord2d(tx, ty + dy);
		glVertex2d(left, top + Tile.tile_height);
		glTexCoord2d(tx + dx, ty + dy);
		glVertex2d(left + Tile.tile_width, top + Tile.tile_height);
		glTexCoord2d(tx + dx, ty);
		glVertex2d(left + Tile.tile_width, top);
	}

	public void tick(long delta) {
	}

	public void renderText(TrueTypeFont font, Color color) {
	}
}
