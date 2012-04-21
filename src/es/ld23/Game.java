package es.ld23;

import org.newdawn.slick.openal.Audio;
import org.lwjgl.util.Point;
import java.io.IOException;
import org.newdawn.slick.opengl.Texture;
import es.ld23.util.Noise;
import java.util.Random;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

public class Game {

	private static final boolean debug = true;
	private static final Random r = new Random();
	private int width;
	private int height;
	private double d_width;
	private double d_height;
	private double colorFlag;
	private boolean backgroundListRebuild = true;
	private int backgroundList;
	private int m_d_x;
	private int m_d_y;
	private Point punteroLocation = new Point(0, 0);
	
	//recursos
	private Texture puntero = null;
	private Audio explosion = null;
	private Audio salto = null;

	static void debug(String string) {
		if (debug) {
			System.out.println(string);
		}
	}

	public Game(int w, int h) {
		width = w;
		height = h;

		d_width = 7.0 / width;
		d_height = 14.0 / height;

		colorFlag = r.nextDouble();
		System.out.println(colorFlag);
	}

	public void initGL() {
		glViewport(0, 0, width, height);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		Mouse.setGrabbed(true);
	}

	public void loadResources() {
		try {
			puntero = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/puntero_red.png"));
			m_d_x = (int) (puntero.getTextureWidth() * 0.7);
			m_d_y = (int) (puntero.getTextureHeight() * 0.7);
			explosion = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/Explosion.wav"));
			salto = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/Jump.wav"));
		} catch (IOException ex) {
			Game.debug("Game::loadResources  ->  " + ex.getMessage());
		}
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glDisable(GL_BLEND);
		if (backgroundListRebuild) {
			backgroundList = glGenLists(1);
			glNewList(backgroundList, GL_COMPILE);
			renderBackground();
			glEndList();
			backgroundListRebuild = false;
		}
		glCallList(backgroundList);
		glEnable(GL_BLEND);
		renderPuntero();
	}

	public void tick() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(punteroLocation.getX(), punteroLocation.getY());
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_F1)
					explosion.playAsMusic(1, 1, false);
				if (Keyboard.getEventKey() == Keyboard.KEY_F2)
					salto.playAsMusic(1, 1, false);
//				if (Keyboard.getEventKey() == Keyboard.KEY_F3)
//				if (Keyboard.getEventKey() == Keyboard.KEY_F4)
				if (Keyboard.getEventKey() == Keyboard.KEY_F5) {
					backgroundListRebuild = true;
				}
			}
		}

		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (!Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			}
		}

		punteroLocation.setLocation(Mouse.getX(), Mouse.getY());
	}

	private void renderBackground() {
		Noise.randomize();
		System.out.println("Creando background");
		glBegin(GL_QUADS);
		for (int x = 0; x < width; x++) {
			int x2 = x + 1;
			for (int y = 0; y < height; y++) {
				int y2 = y + 1;
				color(x, y);
				glVertex2i(x, y);
				color(x, y2);
				glVertex2i(x, y2);
				color(x2, y2);
				glVertex2i(x2, y2);
				color(x2, y);
				glVertex2i(x2, y);
			}
		}
		glEnd();
		System.out.println("Background creado");
	}

	private void renderPuntero() {

		if (Mouse.isGrabbed()) {

			int m_x = punteroLocation.getX();
			int m_y = height - punteroLocation.getY();
			glEnable(GL_TEXTURE_2D);
			glColor3d(1, 1, 1);
			puntero.bind();
			glBegin(GL_QUADS);
			glTexCoord2d(0, 0);
			glVertex2i(m_x, m_y);
			glTexCoord2d(0, 1);
			glVertex2i(m_x, m_y + m_d_y);
			glTexCoord2d(1, 1);
			glVertex2i(m_x + m_d_x, m_y + m_d_y);
			glTexCoord2d(1, 0);
			glVertex2i(m_x + m_d_x, m_y);
			glEnd();
		}
	}

	private void color(int x, int y) {
//		double red = Noise.noiseNormalizado(x * d_width, y * d_height * 0.33, 5);
//		double green = Noise.noiseNormalizado(x * d_width, y * d_height * 0.17, 5);
//		double blue = Noise.noiseNormalizado(x * d_width, y * d_height * 0.76, 5);
//		double c = red * green * blue;
//		c = Math.sqrt(Math.sqrt(c));
		double c = Noise.noiseNormalizado(x * d_width, y * d_height, 9);
		glColor3d(c * 0.2, c * 0.8, c * 0.2);
	}
}
