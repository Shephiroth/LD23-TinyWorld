package es.ld23;

import es.ld23.util.Noise;
import java.util.Random;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

public class Game {

	private static final boolean debug = true;
	private static final Random r = new Random();
	private int width;
	private int height;
	private double d_width;
	private double d_height;
	private boolean backgroundListRebuild = true;
	private double colorFlag;
	private int backgroundList;

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
	}

	public void loadResources() {
	}

	public void render() {
		if (backgroundListRebuild) {
			backgroundList = glGenLists(1);
			glNewList(backgroundList, GL_COMPILE);
			renderBackground();
			glEndList();
			backgroundListRebuild = false;
		}
		glCallList(backgroundList);
	}

	public void tick() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				backgroundListRebuild=true;
			}
		}
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

	private void color(int x, int y) {
		double red = Noise.noiseNormalizado(x * d_width, y * d_height * 0.33, 5);
		double green = Noise.noiseNormalizado(x * d_width, y * d_height * 0.17, 5);
		double blue = Noise.noiseNormalizado(x * d_width, y * d_height * 0.76, 5);
		double c = red*green*blue;
		c = Math.sqrt(Math.sqrt(c));
		glColor3d(c*0.2,c*0.8,c*0.2);
	}
}
