package es.ld23.util.map;

import static org.lwjgl.opengl.GL11.*;

public class Tile {

	public static final Tile Mar[] = new Tile[]{new Tile(2, false), new Tile(3, false)};
	public static final Tile MarHierva[] = new Tile[]{
		new Tile(16, true), new Tile(24, true), new Tile(32, true),
		new Tile(17, true), new Tile(25, true), new Tile(33, true),
		new Tile(18, true), new Tile(26, true), new Tile(34, true)
	};
	public static final Tile HiervaMar[] = new Tile[]{
		new Tile(0, true), new Tile(8, true),
		new Tile(1, true), new Tile(9, true)
	};
	public  static final Tile FloresHierva[][] = new Tile [][]{
		new Tile[]{new Tile(40, true), new Tile(41, true)},
		new Tile[]{new Tile(42, true), new Tile(43, true)},
		new Tile[]{new Tile(48, true), new Tile(49, true)},
		new Tile[]{new Tile(50, true), new Tile(51, true)},
		new Tile[]{new Tile(56, true), new Tile(57, true)},
		new Tile[]{new Tile(58, true), new Tile(59, true)},
	};
	
	static {
		for (int i = 0; i < FloresHierva.length; i++) {
			FloresHierva[i][0].next = MarHierva[4];
			FloresHierva[i][1].next = MarHierva[4];
		}
	}

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
		int t_f = renderData % 8;
		int t_c = renderData / 8;

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
