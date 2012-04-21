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

		lineas = new ArrayList<ConsoleLine>();
	}
	public void setFont(TrueTypeFont font) {
		this.font = font;
	}

	public void render() {
		glDisable(GL_BLEND);
		glColor3d(1, 0, 0);
		glBegin(GL_LINES);
		glVertex2i(left, top);
		glVertex2i(left + width, top);
		glVertex2i(left + width, top);
		glVertex2i(left + width, top + height);
		glVertex2i(left + width, top + height);
		glVertex2i(left, top + height);
		glVertex2i(left, top + height);
		glVertex2i(left, top);
		glEnd();
		glEnable(GL_BLEND);
		int l_h = font.getLineHeight();
		int filas = 0;
		for (ConsoleLine cl : lineas) {
			font.drawString(left, top + filas * l_h, cl.linea, cl.color);
			filas++;
		}
	}

	private void addRawString(String linea, Color color) {
		if (lineas.size() >= 10) {
			lineas.remove(0);
		}
		lineas.add(new ConsoleLine(linea, color));
	}

	public void addString(String linea, Color color) {
		String siguiente;
		String actual = "";
		int size = font.getWidth(linea);
		if (size < width) {
			addRawString(" " + linea, color);
		} else {
			String partes[] = linea.split(" ");
			for (String parte : partes) {
				siguiente = actual + " " + parte;
				if (font.getWidth(siguiente) >= width) {
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
