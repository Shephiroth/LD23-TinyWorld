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
	private static final int tile_width = 16;
	private static final int tile_height = 16;
	private static final double texture_prop = 32.0 / 256.0;
	private Texture tiles;
	private int pixels[];
	private int p_w;
	private int p_h;
	private boolean listRebuild = true;
	private int list;

	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		tiles.bind();
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
			tiles = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/tiles.png"), GL_NEAREST);
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
		int w = tile_width * 40;
		int h = tile_height * 40;
		for (int f = 0; f < 40; f++) {
			double n_f = f * 37.5 / w;
			for (int c = 0; c < 40; c++) {
				double n_c = c * 19.25 / h;
				double noise = Noise.noiseNormalizado(n_c, n_f, 3);
				if (noise > 0.7) {
					if (r.nextDouble() > 0.83) {
						renderData(2, f, c);
					}else
					renderData(3, f, c);
				} else {
					renderData(49,f,c);
				}
			}
		}
		glEnd();
	}

	private void renderData(int data, int f, int c) {
		int t_f = data % 16;
		int t_c = data / 16;

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

	public void nuevo() {
		listRebuild = true;
	}
}
