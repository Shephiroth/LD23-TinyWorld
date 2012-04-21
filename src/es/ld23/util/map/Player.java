package es.ld23.util.map;

import es.ld23.util.BBRectangle;

public class Player {

	BBRectangle BB;
	private double dx;
	private double dy;

	public Player() {
		dx = 0;
		dy = 0;
		BB = new BBRectangle(dx, dy, Tile.tile_width, Tile.tile_height);
	}

	public BBRectangle getBB() {
		return BB;
	}

	public void render() {
		BB.render();
	}

	public void move(double dx, double dy) {
		BB.move(dx, dy);
		this.dx += dx;
		this.dy += dy;
	}

	public double getCameraY(int height, int h) {
		double half = (height - Tile.tile_height) / 2.0;
		double max = h - height + half;
		double res = dy - half;
		if (res < 0) {
			return 0;
		}
		if (dy > max) {
			res = max - half;
		}
		return (int)-res;
	}

	public double getCameraX(int width, int w) {
		double half = (width - Tile.tile_width) / 2.0;
		double max = w - width + half;
		double res = dx - half;
		if (res < 0) {
			return 0;
		}
		if (dx > max) {
			res = max - half;
		}
		return (int)-res;
	}
}
