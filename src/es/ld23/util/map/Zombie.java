package es.ld23.util.map;

import es.ld23.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class Zombie extends PC {

	private int nivel;
	private int hp;

	public Zombie(int w, int h) {
		super(Game.random.nextInt(w - Tile.tile_width), Game.random.nextInt(h - Tile.tile_height));
		nivel = Game.random.nextInt(8);
		hp = 50 + nivel * 45;
		int f = nivel / 4;
		int c = nivel % 4;
		this.setTextureCol(c * 3, 16);
		this.setTextureFil(f * 4, 16);
	}

	@Override
	public void renderText(TrueTypeFont font, Color color) {
		String texto = "Lv" + nivel + "-" + hp;
		font.drawString((float) left, (float) top-font.getLineHeight(), texto, color);
	}
}
