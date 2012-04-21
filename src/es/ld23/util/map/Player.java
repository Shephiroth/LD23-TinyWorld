package es.ld23.util.map;

import es.ld23.util.BBRectangle;
import static org.lwjgl.opengl.GL11.*;

public class Player extends PC {

	private int walk_frame;//cols
	private int walk_direction;//fils
	private long deltaAcumulado;

	public Player() {
		this.left = 0;
		this.top = 0;
		BB = new BBRectangle(5, 5, Tile.tile_width - 10, Tile.tile_height - 10);
		this.setTextureFil(0, 16);
		this.setTextureCol(0, 16);

		this.walk_frame = 0;
		this.walk_direction = PC.PC_MOVE_DER;
	}

	public double getCameraY(int height, int h) {
		double half = (height - Tile.tile_height) / 2.0;
		double max = h - height + half;
		double res = top - half;
		if (res < 0) {
			return 0;
		}
		if (top > max) {
			res = max - half;
		}
		return (int) -res;
	}

	public double getCameraX(int width, int w) {
		double half = (width - Tile.tile_width) / 2.0;
		double max = w - width + half;
		double res = left - half;
		if (res < 0) {
			return 0;
		}
		if (left > max) {
			res = max - half;
		}
		return (int) -res;
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

	@Override
	public void tick(long delta) {
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

	public void setTecladoState(boolean left, boolean up, boolean right, boolean down) {
		if (left) {
			this.walk_direction = PC.PC_MOVE_IZQ;
		} else if (right) {
			this.walk_direction = PC.PC_MOVE_DER;
		} else if (down) {
			this.walk_direction = PC.PC_MOVE_ABA;
		} else if (up) {
			this.walk_direction = PC.PC_MOVE_ARR;
		}
	}
}
