package es.ld23.util;

import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;

public class BBRectangle {

	private final double height;
	private final double width;
	private double top;
	private double left;

	public BBRectangle(double left, double top, double width, double height) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
	}

	public boolean collision(BBRectangle other) {
		if (left > other.left + other.width || other.left > left + width
			|| top > other.top + other.height || other.top > top + height) {
			return false;
		}

		return true;
	}

	public boolean isInside(BBRectangle interior) {
		if (left > interior.left || left + width < interior.left + interior.width
			|| top > interior.top || top + height < interior.top + interior.height) {
			return false;
		}
		return true;
	}

	public boolean is(BBRectangle other) {
		return (left == other.left) && (top == other.top)
			&& (width == other.width) && (height == other.height);
	}

	public void move(double dx, double dy) {
		left += dx;
		top += dy;
	}

	public BBRectangle createMoved(double dx, double dy) {
		return new BBRectangle(left + dx, top + dy, width, height);
	}

	public void render(Color color) {
		color.bind();
		glDisable(GL_BLEND);

		glBegin(GL_QUADS);
		glVertex2d(left, top);
		glVertex2d(left, top + height);
		glVertex2d(left + width, top + height);
		glVertex2d(left + width, top);
		glEnd();
	}

	public void render() {
		render(Color.red);
	}
}
