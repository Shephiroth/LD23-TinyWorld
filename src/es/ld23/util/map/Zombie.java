package es.ld23.util.map;

import es.ld23.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class Zombie extends PC {

	private int nivel = 0;
	private int hp = 0;
	private int lazyness = 0;
	private boolean wantToMove = false;
	private double dirx = 0;
	private double diry = 0;

	public Zombie(int w, int h) {
		super(Game.random.nextInt(w - Tile.tile_width), Game.random.nextInt(h - Tile.tile_height));
		nivel = Game.random.nextInt(8);
		hp = 50 + nivel * 45;
		setTextureCol((nivel % 4) * 3, 16);
		setTextureFil((nivel / 4) * 4, 16);

		lazyness = Game.random.nextInt(250) + 500;
	}

	@Override
	public void renderText(TrueTypeFont font, Color color) {
		String texto = "Lv" + nivel + "-" + hp;
		font.drawString((float) left, (float) top - font.getLineHeight(), texto, color);
	}

	public void tick(long delta) {
		lazyness -= delta;
		if (wantToMove) {
			if (lazyness < 0) {
				lazyness += Game.random.nextInt(250) + 500;
				wantToMove = false;
			} else {
				double movex = dirx * delta;
				double movey = diry * delta;
				move(movex, movey);
				walkTick(delta);
			}
		} else {
			if (lazyness < 0) {
				lazyness += Game.random.nextInt(150) + 100;
				wantToMove = true;
				generaDireccion();
			}
		}
	}

	private void generaDireccion() {
		int dir = Game.random.nextInt(8);
		double recto = PC.walk_speed;
		double diagonal = recto * PC.diagonal_change;
		switch (dir) {
			case 0: // N
				walk_direction = PC.PC_MOVE_ARR;
				diry = -recto;
				dirx = 0;
				break;
			case 1: // NE
				walk_direction = PC.PC_MOVE_ARR;
				dirx = +diagonal;
				diry = -diagonal;
				break;
			case 2: //  E
				walk_direction = PC.PC_MOVE_DER;
				dirx = +recto;
				diry = 0;
				break;
			case 3: // SE
				walk_direction = PC.PC_MOVE_DER;
				dirx = +diagonal;
				diry = +diagonal;
				break;
			case 4: // S
				walk_direction = PC.PC_MOVE_ABA;
				dirx = 0;
				diry = +recto;
				break;
			case 5: //WS
				walk_direction = PC.PC_MOVE_ABA;
				dirx = -diagonal;
				diry = +diagonal;
				break;
			case 6: //W
				walk_direction = PC.PC_MOVE_IZQ;
				dirx = -recto;
				diry = 0;
				break;
			case 7: //WN
				walk_direction = PC.PC_MOVE_IZQ;
				dirx = -diagonal;
				diry = -diagonal;
				break;
		}
	}
}
