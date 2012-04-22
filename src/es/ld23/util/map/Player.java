package es.ld23.util.map;

import es.ld23.equipment.Weapon;
import es.ld23.util.BBRectangle;
import static org.lwjgl.opengl.GL11.*;

public class Player extends PC {

	private Weapon arma;
	private int score;

	public Player() {
		left = 0;
		top = 0;
		BB = new BBRectangle(5, 5, Tile.tile_width - 10, Tile.tile_height - 10);
		setTextureFil(0, 16);
		setTextureCol(0, 16);
		walk_frame = 0;
		walk_direction = PC.PC_MOVE_DER;


		this.arma = Weapon.bow;
		this.score = 0;
	}

	public void addScore(int puntos) {
		score += puntos;
	}

	public int getScore() {
		return score;
	}

	public Weapon getWeapon() {
		return arma;
	}

	public void setWeapon(Weapon nueva) {
		if (nueva != null) {
			arma = nueva;
		}
	}

	public double getX() {
		return left;
	}

	public double getY() {
		return top;
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

	public void render2() {
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
