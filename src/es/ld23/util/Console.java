package es.ld23.util;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import static org.lwjgl.opengl.GL11.*;

public class Console {

	private TrueTypeFont font;

	public class ConsoleLine {

		String linea;
		Color color;

		public ConsoleLine(String linea, Color color) {
			this.linea = linea;
			this.color = color;
		}
	}
	private int left;
	private int top;
	private int width;
	private int height;
	private ArrayList<ConsoleLine> lineas;

	public Console(int x, int y, int w, int h) {
		this.left = x;
		this.top = y;
		this.width = w;
		this.height = h;

		if (width < 64) {
			width = 64;
		}
		if (height < 64) {
			height = 64;
		}

		lineas = new ArrayList<ConsoleLine>();
	}

	public void setFont(TrueTypeFont font) {
		this.font = font;
	}

	public void render() {
		int l_h = font.getLineHeight();
		int filas = 0;
		for (ConsoleLine cl : lineas) {
			font.drawString(left + 15, top + filas * l_h + 15, cl.linea, cl.color);
			filas++;
		}
	}

	public void renderBackground() {
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		{
			glColor3d(0.3, 0.3, 0.9);
			glVertex2i(left + 7, top + 7);
			glColor3d(0.1, 0.1, 0.8);
			glVertex2i(left + 7, top + height-7);
			glColor3d(0.3, 0.3, 0.9);
			glVertex2i(left + width - 7, top + height - 7);
			glColor3d(0.5, 0.5, 1);
			glVertex2i(left + width-7, top + 7);
			glColor3d(1, 1, 1);
		}
		glEnd();

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		{
			glTexCoord2d(0, 0.5);
			glVertex2i(left, top);
			glTexCoord2d(0, 0.625);
			glVertex2i(left, top + 32);
			glTexCoord2d(0.125, 0.625);
			glVertex2i(left + 32, top + 32);
			glTexCoord2d(0.125, 0.5);
			glVertex2i(left + 32, top);

			glTexCoord2d(0, 0.625);
			glVertex2i(left, top + height - 32);
			glTexCoord2d(0, 0.75);
			glVertex2i(left, top + height);
			glTexCoord2d(0.125, 0.75);
			glVertex2i(left + 32, top + height);
			glTexCoord2d(0.125, 0.625);
			glVertex2i(left + 32, top + height - 32);

			glTexCoord2d(0.125, 0.5);
			glVertex2i(left + width - 32, top);
			glTexCoord2d(0.125, 0.625);
			glVertex2i(left + width - 32, top + 32);
			glTexCoord2d(0.25, 0.625);
			glVertex2i(left + width, top + 32);
			glTexCoord2d(0.25, 0.5);
			glVertex2i(left + width, top);

			glTexCoord2d(0.125, 0.625);
			glVertex2i(left + width - 32, top + height - 32);
			glTexCoord2d(0.125, 0.75);
			glVertex2i(left + width - 32, top + height);
			glTexCoord2d(0.25, 0.75);
			glVertex2i(left + width, top + height);
			glTexCoord2d(0.25, 0.625);
			glVertex2i(left + width, top + height - 32);

			glTexCoord2d(0.0625, 0.5);
			glVertex2i(left + 32, top);
			glTexCoord2d(0.0625, 0.625);
			glVertex2i(left + 32, top + 32);
			glTexCoord2d(0.1875, 0.625);
			glVertex2i(left + width - 32, top + 32);
			glTexCoord2d(0.1875, 0.5);
			glVertex2i(left + width - 32, top);

			glTexCoord2d(0.0625, 0.625);
			glVertex2i(left + 32, top + height - 32);
			glTexCoord2d(0.0625, 0.75);
			glVertex2i(left + 32, top + height);
			glTexCoord2d(0.1875, 0.75);
			glVertex2i(left + width - 32, top + height);
			glTexCoord2d(0.1875, 0.625);
			glVertex2i(left + width - 32, top + height - 32);

			glTexCoord2d(0, 0.5625);
			glVertex2i(left, top + 32);
			glTexCoord2d(0, 0.6875);
			glVertex2i(left, top + height - 32);
			glTexCoord2d(0.125, 0.6875);
			glVertex2i(left + 32, top + height - 32);
			glTexCoord2d(0.125, 0.5625);
			glVertex2i(left + 32, top + 32);

			glTexCoord2d(0.125, 0.5625);
			glVertex2i(left + width - 32, top + 32);
			glTexCoord2d(0.125, 0.6875);
			glVertex2i(left + width - 32, top + height - 32);
			glTexCoord2d(0.25, 0.6875);
			glVertex2i(left + width, top + height - 32);
			glTexCoord2d(0.25, 0.5625);
			glVertex2i(left + width, top + 32);
		}
		glEnd();
	}

	private void addRawString(String linea, Color color) {
		if (lineas.size() >= 7) {
			lineas.remove(0);
		}
		lineas.add(new ConsoleLine(linea, color));
	}

	public void addString(String linea, Color color) {
		String siguiente;
		String actual = "";
		int size = font.getWidth(linea);
		if (size < width - 30) {
			addRawString(" " + linea, color);
		} else {
			String partes[] = linea.split(" ");
			for (String parte : partes) {
				siguiente = actual + " " + parte;
				if (font.getWidth(siguiente) >= width - 30) {
					addRawString(actual, color);
					actual = "   " + parte;
				} else {
					actual = siguiente;
				}
			}
			addRawString(actual, color);
		}
	}
}
