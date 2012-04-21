package es.ld23.util.map;

import static org.lwjgl.opengl.GL11.*;

public class Tile {

	public static final Tile Mar[] = new Tile[]{new Tile(2, false), new Tile(3, false)};
	public static final Tile MarHierva[] = new Tile[]{
		new Tile(32, true), new Tile(48, true), new Tile(64, true),
		new Tile(33, true), new Tile(49, true), new Tile(65, true),
		new Tile(34, true), new Tile(50, true), new Tile(66, true)
	};
	public static final Tile HiervaMar[] = new Tile[]{
		new Tile(0, true), new Tile(16, true),
		new Tile(1, true), new Tile(17, true)
	};

	public static void setWaterLayer(Tile water) {
		for (int i = 0; i < MarHierva.length; i++) {
			MarHierva[i].next = water;
		}
		for (int i = 0; i < HiervaMar.length; i++) {
			HiervaMar[i].next = water;
		}
	}
	private static final double texture_prop = 32.0 / 256.0;
	public static final int tile_width = 16;
	public static final int tile_height = 16;
	private int renderData;
	private boolean walk;
	private Tile next;

	public Tile(int renderData, boolean walk) {
		this.renderData = renderData;
		this.walk = walk;
	}

	public void render(int f, int c) {
		if (next != null) {
			next.render(f, c);
		}
		int t_f = renderData % 16;
		int t_c = renderData / 16;

		double text_left = t_c * texture_prop;
		double text_top = t_f * texture_prop;

		glTexCoord2d(text_left, text_top);
		glVertex2d(c * tile_width, f * tile_height);

		glTexCoord2d(text_left, text_top + texture_prop);
		glVertex2d(c * tile_width, (1 + f) * tile_height);

		glTexCoord2d(text_left + texture_prop, text_top + texture_prop);
		glVertex2d((1 + c) * tile_width, (1 + f) * tile_height);

		glTexCoord2d(text_left + texture_prop, text_top);
		glVertex2d((1 + c) * tile_width, f * tile_height);
	}

	public boolean canWalkOn() {
		return walk;
	}

	public boolean canBeDestroyed() {
		return false;
	}
}
