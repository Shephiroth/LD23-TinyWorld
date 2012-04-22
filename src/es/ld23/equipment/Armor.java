package es.ld23.equipment;

import java.awt.Rectangle;
import static org.lwjgl.opengl.GL11.*;

public class Armor {

	public static final Armor Default = new Armor(0, 1);
	private static final double dy = 24.0 / 256.0;
	private static final double dx = 24.0 / 256.0;
	private static final double ty_top = 48.0 / 256.0 - dy;
	private int columna;
	private int defense;

	public Armor(int col, int defense) {
		this.columna = col;
		this.defense = defense;
	}

	public int getDefense() {
		return defense;
	}

	public void render(Rectangle posicion) {
		glDisable(GL_BLEND);
		glBegin(GL_QUADS);
		{
			glColor3d(0, 0, 0);
			glVertex2d(posicion.x, posicion.y);
			glVertex2d(posicion.x, posicion.y + posicion.height);
			glVertex2d(posicion.x + posicion.width, posicion.y + posicion.height);
			glVertex2d(posicion.x + posicion.width, posicion.y);
		}
		glEnd();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		{
			double init = columna * dx;
			glColor3d(1, 1, 1);
			glTexCoord2d(init, ty_top);
			glVertex2d(posicion.x, posicion.y);
			glTexCoord2d(init, ty_top + dy);
			glVertex2d(posicion.x, posicion.y + posicion.height);
			glTexCoord2d(init + dx, ty_top + dy);
			glVertex2d(posicion.x + posicion.width, posicion.y + posicion.height);
			glTexCoord2d(init + dx, ty_top);
			glVertex2d(posicion.x + posicion.width, posicion.y);
		}
		glEnd();
	}
}
