package es.ld23.util.map;

import es.ld23.equipment.Armor;
import es.ld23.equipment.Arrow;
import es.ld23.equipment.Bullet;
import es.ld23.equipment.Weapon;
import es.ld23.util.BBRectangle;

public class Player extends PC {

	private int nivel;
	private Weapon arma;
	private Arrow arrow;
	private Armor armor;
	private double hp;
	private int exp;
	private int score;
	private int gold;

	public Player() {
		start();
		gold = 0;
		nivel = 1;
		setTextureFil(0, 16);
		setTextureCol(0, 16);
		walk_frame = 0;
		walk_direction = PC.PC_MOVE_DER;
		this.arma = Weapon.bow;
		this.arrow = Arrow.Default;
		this.armor = Armor.Default;
		this.score = 0;
		this.hp = 10;
	}

	public void updateStat() {
		gold += score / 10;
		exp += score;
		if (exp > 1000 * nivel) {
			exp -= 100 * nivel;
			nivel++;
		}
	}

	public final void start() {
		left = 0;
		top = 0;
		BB = new BBRectangle(5, 5, Tile.tile_width - 10, Tile.tile_height - 10);
		score = 0;
		hp = nivel * 50;
	}

	public void addScore(int puntos) {
		score += puntos;
	}

	public int getScore() {
		return score;
	}

	@Override
	public boolean hurt(double dmg) {
		hp -= dmg;
		return hp <= 0;
	}

	public Weapon getWeapon() {
		return arma;
	}

	public Arrow getArrow() {
		return arrow;
	}

	public Armor getArmor() {
		return armor;
	}

	public void setWeapon(Weapon nueva) {
		if (nueva != null) {
			arma = nueva;
		}
	}

	public void setArrow(Arrow arrow) {
		if (arrow != null) {
			this.arrow = arrow;
		}
	}

	public double getCameraY(int height, int h) {
		if (height > h) {
			return (height - h) / 2;
		}
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
		if (width > w) {
			return (width - w) / 2;
		}
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

	public Bullet fire() {
		this.delay = arma.getDelay();
		return arma.fire();
	}

	public double getHP() {
		return hp;
	}

	public int getExp() {
		return exp;
	}

	public int getNextExp() {
		return nivel * 1000;
	}

	public int getGold() {
		return gold;
	}

	public int getNivel() {
		return nivel;
	}

	public double getDmg() {
		return arma.getDmg() + arrow.getDmg();
	}
	public int getDefense() {
		return armor.getDefense();
	}
}
