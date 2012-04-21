package es.ld23;

import es.ld23.util.Console;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;
import es.ld23.util.map.Map;
import org.newdawn.slick.Color;

public class Game {

	private static final boolean debug = true;
	private int width;
	private int height;
	private int m_d_x;
	private int m_d_y;
	private Point punteroLocation = new Point(0, 0);
	//propios
	private Console console;
	private Color textoNormal;
	private Map map;
	//recursos
	private Texture puntero = null;
	private Audio explosion = null;
	private Audio salto = null;

	static public void debug(String string) {
		if (debug) {
			System.out.println(string);
		}
	}

	public Game(int w, int h) {
		width = w;
		height = h;
		textoNormal = Color.blue;
		console = new Console(100, 100, 200, 75);
		console.addString("Test Line. All this text is the same line. The code fit this text inside the area designated to console...at least for width, will fix height later :)", textoNormal);
		map = new Map();
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

//		Mouse.setGrabbed(true);
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
		map.loadResources();
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		map.render();
//		console.render();
		renderPuntero();
	}

	public void tick() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(punteroLocation.getX(), punteroLocation.getY());
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
					explosion.playAsMusic(1, 1, false);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_F2) {
					salto.playAsSoundEffect(1, 1, false);
				}
//				if (Keyboard.getEventKey() == Keyboard.KEY_F3)
//				if (Keyboard.getEventKey() == Keyboard.KEY_F4)
				if (Keyboard.getEventKey() == Keyboard.KEY_F5) {
					map.nuevo();
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
}
