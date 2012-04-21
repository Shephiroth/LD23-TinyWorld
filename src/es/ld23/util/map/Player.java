package es.ld23.util.map;

import es.ld23.util.BBRectangle;

public class Player {

	BBRectangle BB;

	public Player() {
		BB = new BBRectangle(0,0,20,20);
	}

	public BBRectangle getBB() {
		return BB;
	}

	public void render() {
		BB.render();
	}
}
