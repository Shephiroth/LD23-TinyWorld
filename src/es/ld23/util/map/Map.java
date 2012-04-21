package es.ld23.util.map;

import java.util.ArrayList;
import es.ld23.Game;
import es.ld23.util.BBRectangle;
import es.ld23.util.Noise;
import java.io.IOException;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

public class Map {

	private int map_f = 30;
	private int map_c = 30;
	private Texture textureTiles;
	private Tile tiles[];
	private boolean listRebuild = true;
	private int list;
	private ArrayList<BBRectangle> boundingbox = new ArrayList<BBRectangle>();
	private BBRectangle mapBB;
	private int map_w;
	private int map_h;

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
		} catch (IOException ex) {
			Game.debug("Map::loadResources  ->  " + ex.getMessage());
		}
	}

	private void renderMap() {
		Noise.randomize();
		glBegin(GL_QUADS);
		for (int f = 0; f < map_f; f++) {
			for (int c = 0; c < map_c; c++) {
				getTile(f, c).render(f, c);
			}
		}
		glEnd();
	}

	public final void nuevo() {
		listRebuild = true;
		boundingbox.clear();
		tiles = new Tile[map_f * map_c];
		map_w = Tile.tile_width * map_c;
		map_h = Tile.tile_height * map_f;
		mapBB = new BBRectangle(0, 0, map_w, map_h);
		Tile water = Tile.Mar[Game.random.nextInt(Tile.Mar.length)];
		Tile.setWaterLayer(water);
		for (int f = 0; f < map_f; f++) {
			double n_f = f * 37.5 / map_w;
			for (int c = 0; c < map_c; c++) {
				Tile obj = null;
				double noise = Noise.noiseNormalizado(c * 29.25 / map_h, n_f, 3);
				if (noise > 0.65) {
					obj = water;
				} else {
					obj = Tile.MarHierva[4];
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
							setTile(f, c, Tile.MarHierva[6]);
						} else {
							setTile(f, c, Tile.MarHierva[3]);
						}
					} else if (right == water) {
						if (top == water) {
							if (down == water) {
								setTile(f + 1, c, Tile.MarHierva[4]);
							}
							setTile(f, c, Tile.MarHierva[2]);
						} else if (down == water) {
							setTile(f, c, Tile.MarHierva[8]);
						} else {
							setTile(f, c, Tile.MarHierva[5]);
						}
					} else if (top == water) {
						setTile(f, c, Tile.MarHierva[1]);
					} else if (down == water) {
						setTile(f, c, Tile.MarHierva[7]);
					} else if (getTile(f - 1, c - 1) == water) {
						setTile(f, c, Tile.HiervaMar[0]);
					} else if (getTile(f - 1, c + 1) == water) {
						setTile(f, c, Tile.HiervaMar[1]);
					} else if (getTile(f + 1, c - 1) == water) {
						setTile(f, c, Tile.HiervaMar[2]);
					} else if (getTile(f + 1, c + 1) == water) {
						setTile(f, c, Tile.HiervaMar[3]);
					} else {
					}
				}
			}
		}

		for (int f = 0; f < map_f; f++) {
			for (int c = 0; c < map_c; c++) {
				if (getTile(f, c) == Tile.MarHierva[4]) {
					int n = Game.random.nextInt(500);
					if (n < Tile.FloresHierva.length) {
						int pos = Game.random.nextBoolean() ? 1 : 0;
						setTile(f, c, Tile.FloresHierva[n][pos]);
						createBB(f, c);
					}
				}
			}
		}
	}

	public boolean isFree(BBRectangle playerBB) {
		if (!mapBB.isInside(playerBB)) {
			return false;
		}
		if (!boundingbox.isEmpty()) {
			for (BBRectangle aux : boundingbox) {
				if (playerBB.collision(aux)) {
					return false;
				}
			}
		}
		return true;
	}

	private void createBB(int f, int c) {
		boundingbox.add(new BBRectangle(c * Tile.tile_width, f * Tile.tile_height, Tile.tile_width, Tile.tile_height));
	}

	public int getWidth() {
		return map_w;
	}

	public int getHeight() {
		return map_h;
	}
}
