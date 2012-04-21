package es.ld23.util.map;

import java.util.Random;
import org.newdawn.slick.Color;
import java.awt.image.BufferedImage;
import es.ld23.Game;
import es.ld23.util.Noise;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

public class Map {

	private static final Random r = new Random();
	private Texture textureTiles;
	private int pixels[];
	private int p_w;
	private int p_h;
	private Tile tiles[];
	private boolean listRebuild = true;
	private int list;

	public Map() {
		nuevo();
	}

	private Tile getTile(int f, int c) {
		return tiles[f * 40 + c];
	}

	private void setTile(int f, int c, Tile t) {
		tiles[c + f * 40] = t;
	}

	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		textureTiles.bind();
		Color.white.bind();
		if (listRebuild) {
			list = glGenLists(1);
			glNewList(list, GL_COMPILE);
			renderMap();
			glEndList();
			listRebuild = false;
		}
		glCallList(list);
	}

	public void loadResources() {
		try {
			textureTiles = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/tiles.png"), GL_NEAREST);
			BufferedImage bi = ImageIO.read(Map.class.getResource("/res/level.bmp"));
			p_w = bi.getWidth();
			p_h = bi.getHeight();
			pixels = new int[p_w * p_h];
			bi.getRGB(0, 0, p_w, p_h, pixels, 0, p_w);
			bi = null;
		} catch (IOException ex) {
			Game.debug("Map::loadResources  ->  " + ex.getMessage());
		}
	}

	private void renderMap() {
		Noise.randomize();
		glBegin(GL_QUADS);
//		for (int f = 0; f < p_h; f++) {
//			for (int c = 0; c < p_w; c++) {
//				int color = pixels[f * p_w + c];
//				if ((color & 0x0F) > 0) {
//					renderData((color & 0x0000FF00)/ 0x000100, f, c);
//				}
//				renderData((color & 0x00FF0000) / 0x00010000, f, c);
//			}
//		}
		for (int f = 0; f < 40; f++) {
			for (int c = 0; c < 40; c++) {
				getTile(f,c).render(f, c);
			}
		}
		glEnd();
	}

	public final void nuevo() {
		listRebuild = true;
		tiles = new Tile[40 * 40];
		int w = Tile.tile_width * 40;
		int h = Tile.tile_height * 40;
		for (int f = 0; f < 40; f++) {
			double n_f = f * 37.5 / w;
			for (int c = 0; c < 40; c++) {
				double n_c = c * 29.25 / h;
				double noise = Noise.noiseNormalizado(n_c, n_f, 3);
				if (noise > 0.65) {
					if (r.nextDouble() > 0.83) {
						setTile(f,c,Tile.Mar[0]);
					} else {
						setTile(f,c,Tile.Mar[1]);
					}
				} else {
					setTile(f,c,Tile.MarHierva[4]);
				}
			}
		}
	}
}
