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
	private int map_f = 40;
	private int map_c = 40;
	private Texture textureTiles;
	private int pixels[];
	private int p_w;
	private int p_h;
	private Tile tiles[];
	private boolean listRebuild = true;
	private int list;

	public Map() {
		Noise.randomize();
		nuevo();
	}

	private Tile getTile(int f, int c) {
		if (f < 0 || f >= map_f || c < 0 || c >= map_c) {
			return null;
		}
		return tiles[f * map_c + c];
	}

	private void setTile(int f, int c, Tile t) {
		tiles[c + f * map_c] = t;
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
		for (int f = 0; f < map_f; f++) {
			for (int c = 0; c < map_c; c++) {
				getTile(f, c).render(f, c);
			}
		}
		glEnd();
	}

	public final void nuevo() {
		listRebuild = true;
		tiles = new Tile[map_f * map_c];
		int w = Tile.tile_width * map_c;
		int h = Tile.tile_height * map_f;
		Tile water = Tile.Mar[r.nextInt(Tile.Mar.length)];
		Tile.setWaterLayer(water);
		for (int f = 0; f < map_f; f++) {
			double n_f = f * 37.5 / w;
			for (int c = 0; c < map_c; c++) {
				Tile obj = null;
				if (f == 0 || f == map_f - 1 || c == 0 || c == map_c - 1) {
					obj = water;
				} else {
					double noise = Noise.noiseNormalizado(c * 29.25 / h, n_f, 3);
					if (noise > 0.65) {
						obj = water;
					} else {
						obj = Tile.MarHierva[4];
					}
				}
				setTile(f, c, obj);
			}
		}
		for (int f = 0; f < map_f; f++) {
			for (int c = 0; c < map_c; c++) {
				Tile center = getTile(f, c);
				Tile left = getTile(f, c - 1);
				Tile right = getTile(f, c + 1);
				Tile top = getTile(f - 1, c);
				Tile down = getTile(f + 1, c);
				if (center != water) {
					if (left == water) {
						if (right == water) {
							setTile(f, c + 1, Tile.MarHierva[4]);
						}
						if (top == water) {
							if (down == water) {
								setTile(f + 1, c, Tile.MarHierva[4]);
							}
							setTile(f, c, Tile.MarHierva[0]);
						} else if (down == water) {
							setTile(f,c,Tile.MarHierva[6]);
						} else {
							setTile(f,c,Tile.MarHierva[3]);
						}
					} else if (right == water) {
						
					} else if (top == water) {
						setTile(f,c,Tile.MarHierva[1]);
					} else if (down==water)
						setTile(f,c,Tile.MarHierva[7]);
				}
			}
		}
	}
}
