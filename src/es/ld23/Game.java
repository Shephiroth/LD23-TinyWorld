package es.ld23;

import es.ld23.equipment.Armor;
import es.ld23.equipment.Arrow;
import es.ld23.equipment.Bullet;
import es.ld23.equipment.Weapon;
import java.awt.Rectangle;
import es.ld23.util.map.Zombie;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;
import java.util.Random;
import es.ld23.util.map.PC;
import es.ld23.util.BBRectangle;
import es.ld23.util.map.Player;
import es.ld23.util.Console;
import es.ld23.util.map.Map;
import java.io.IOException;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

public class Game {

	public static final Random random = new Random();
	private static final boolean debug = true;
	private int width;
	private int height;
	private int m_d_x;
	private int m_d_y;
	private Point punteroLocation = new Point(0, 0);
	//lista para el gui y consola
	private boolean listRebuild = true;
	private int list;
	private int playerDir;
	private int gameoverDelay = -2;
	private boolean gameover;
	//propios
	private Rectangle menuMapOptions;
	private Rectangle rectWeapon;
	private Rectangle rectArrow;
	private Rectangle rectArmor;
	private Console console;
	private Color textoNormal;
	private Map map;
	private Player player;
	private ArrayList<PC> mobs;
	private ArrayList<Bullet> bullets;
	private ArrayList<Bullet> bulletsEnemigas;
	//shop
	private int defShop;
	private int defShopPrize;
	private Rectangle defShopRect;
	private int arrShop;
	private int arrShopPrize;
	private Rectangle arrShopRect;
	//recursos
	private TrueTypeFont font;
	private TrueTypeFont bigfont;
	private Texture puntero;
	private Texture textureItems;
	private Texture textureMobs;
	private Texture textureTiles;
	private Texture textureTitulos;
	private Audio explosion;
	private Audio damage;
	private Audio disparo;
	private Audio impacto;
	private Audio salto;
	private Audio select;
	private Audio shop;

	static public void textureInfo(Texture target) {
		System.out.println("Texture info: " + target.getTextureRef());
		System.out.println("ID=" + target.getTextureID() + ". Alpha? " + target.hasAlpha());
		System.out.println("ImgHeight=" + target.getImageHeight() + ". TextureHeight=" + target.getTextureHeight() + ". Height=" + target.getHeight());
		System.out.println("ImgWidth=" + target.getImageWidth() + ". TextureWidth=" + target.getTextureWidth() + ". Width=" + target.getWidth());
	}

	static public void debug(String string) {
		if (debug) {
			System.out.println(string);
		}
	}

	public Game(int w, int h) {
		width = w;
		height = h;
		Font awtFont = new Font("Times New Roman", Font.BOLD, 14);
		font = new TrueTypeFont(awtFont, true);
		awtFont = new Font("Times New Roman", Font.BOLD, 24);
		bigfont = new TrueTypeFont(awtFont, true);
		textoNormal = Color.green.brighter();
		menuMapOptions = new Rectangle(85, 325, bigfont.getWidth("Map 1 : 24x24"), bigfont.getLineHeight() * 10);
		rectWeapon = new Rectangle(width - 178, 177, 48, 48);
		rectArrow = new Rectangle(width - 124, 177, 48, 48);
		rectArmor = new Rectangle(width - 70, 177, 48, 48);
		arrShopRect = new Rectangle(width - 190, 375, 48, 48);
		defShopRect = new Rectangle(width - 190, 425, 48, 48);
		console = new Console(width - 200, 0, 200, 150);
		console.setFont(font);
		console.addString("Test Line. All this text is the same line. The code fit this text inside the area designated to console...at least for width, will fix height later :)", textoNormal);
		map = new Map();
		player = new Player();
		mobs = new ArrayList<PC>();
		bullets = new ArrayList<Bullet>();
		bulletsEnemigas = new ArrayList<Bullet>();
		gameover = true;

		arrShop = player.getArrowInt() + 1;
		arrShopPrize = arrShop * arrShop * arrShop * 250;
		defShop = player.getArmorInt() + 1;
		defShopPrize = defShop * defShop * defShop * 250;
	}

	private void juegoNuevo(int f, int c) {
		mobs.clear();
		bullets.clear();
		bulletsEnemigas.clear();
		player.start();
		map.nuevo(f, c);
		for (int i = 0; i < f; i++) {
			PC m = new Zombie(map.getWidth(), map.getHeight(), random.nextInt(4) + 24 - f);
			map.findSpawLocation(m, mobs);
			mobs.add(m);
		}
		map.findSpawLocation(player, mobs);
		gameover = false;
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
			textureTiles = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/tiles.png"), GL_NEAREST);
			textureItems = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/items.png"));
			textureMobs = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/mobs.png"));
			textureTitulos = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/titulos.png"));
			explosion = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/explosion.wav"));
			salto = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/jump.wav"));
			select = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/select.wav"));
			damage = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/damage.wav"));
			disparo = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/disparo.wav"));
			impacto = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/disparo_impacto.wav"));
			shop = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/shop.wav"));
		} catch (IOException ex) {
			Game.debug("Game::loadResources  ->  " + ex.getMessage());
		}
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		glTranslated(player.getCameraX(width - 200, map.getWidth()), player.getCameraY(height, map.getHeight()), 0);

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		Color.white.bind();
		textureTiles.bind();
		map.render();
		textureItems.bind();
		renderBullets();
		textureMobs.bind();
		renderMobs();
		glLoadIdentity();
		if (gameover) {
			renderGameover();
		}
		renderGui();
	}

	private void renderMobs() {
		glBegin(GL_QUADS);
		if (!mobs.isEmpty()) {
			for (PC mob : mobs) {
				mob.render();
			}
		}
		player.render();
		glEnd();
		if (!mobs.isEmpty()) {
			glDisable(GL_TEXTURE_2D);
			for (PC mob : mobs) {
				mob.renderText(font, Color.white);
			}
		}
	}

	private void renderBullets() {
		if (bullets.isEmpty() && bulletsEnemigas.isEmpty()) {
			return;
		}
		glBegin(GL_QUADS);
		{
			if (!bullets.isEmpty()) {
				for (Bullet bullet : bullets) {
					bullet.render();
				}
			}
			if (!bulletsEnemigas.isEmpty()) {
				for (Bullet bullet : bulletsEnemigas) {
					bullet.render();
				}
			}
		}
		glEnd();
		glColor3d(1, 1, 1);
	}

	private void renderPuntero() {
		if (Mouse.isGrabbed()) {
			int m_x = punteroLocation.getX();
			int m_y = height - punteroLocation.getY();
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
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

	private void renderGui() {
		textureTiles.bind();
		if (listRebuild) {
			list = glGenLists(1);
			glNewList(list, GL_COMPILE);
			renderRawGui();
			glEndList();
			listRebuild = false;
		}
		glCallList(list);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		int left = width - 180;
		int right = left + font.getWidth("Score: 999999  ");
		font.drawString(left, 255, "Nivel: " + player.getNivel(), Color.blue);
		font.drawString(right, 255, "HP: " + player.getHP(), Color.blue);
		font.drawString(left, 255 + font.getLineHeight(), "Score: " + player.getScore(), Color.blue);
		font.drawString(right, 255 + font.getLineHeight(), "Gold: " + player.getGold(), Color.blue);
		font.drawString(left, 255 + font.getLineHeight() * 2, "Att: " + player.getDmg(), Color.blue);
		font.drawString(right, 255 + font.getLineHeight() * 2, "Speed: " + (1000.0 / player.getWeapon().getDelay()), Color.blue);
		font.drawString(left, 255 + font.getLineHeight() * 3, "EXP: " + (player.getNextExp() - player.getExp()), Color.blue);
		font.drawString(right, 255 + font.getLineHeight() * 3, "Def: " + player.getDefense(), Color.blue);
		font.drawString(defShopRect.x + defShopRect.width + 5, defShopRect.y + font.getLineHeight() / 2, "Update Defense: " + defShopPrize, Color.blue);
		font.drawString(defShopRect.x + defShopRect.width + 5, defShopRect.y + font.getLineHeight() * 3 / 2, "New Def = " + Armor.armors[defShop].getDefense(), Color.blue);
		font.drawString(arrShopRect.x + arrShopRect.width + 5, arrShopRect.y + font.getLineHeight() / 2, "Update Weapon: " + arrShopPrize, Color.blue);
		font.drawString(arrShopRect.x + arrShopRect.width + 5, arrShopRect.y + font.getLineHeight() * 3 / 2, "New Att: " + (player.getWeapon().getDmg() + Arrow.arrows[arrShop].getDmg()), Color.blue);
		console.render();
		textureItems.bind();
		player.getWeapon().render(rectWeapon);
		player.getArrow().render(rectArrow);
		player.getArmor().render(rectArmor);
		Armor.armors[defShop].render(defShopRect);
		Arrow.arrows[arrShop].render(arrShopRect);
		renderPuntero();
	}

	private void tickGameover(long delta) {
		if (gameoverDelay > 0) {
			gameoverDelay -= delta;
		} else {
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
						Mouse.setGrabbed(false);
						Mouse.setCursorPosition(punteroLocation.getX(), punteroLocation.getY());
					}
				}
			}
			while (Mouse.next()) {
				if (Mouse.getEventButtonState()) {
					if (!Mouse.isGrabbed()) {
						Mouse.setGrabbed(true);
					} else {
						int x = Mouse.getEventX();
						int y = height - Mouse.getEventY();
						if (menuMapOptions.contains(x, y)) {
							y -= menuMapOptions.y;
							int p = (int) (y / (bigfont.getLineHeight() * 1.5));
							switch (p) {
								case 0:
									console.addString("You clicked 24x24", textoNormal);
									juegoNuevo(24, 24);
									select.playAsSoundEffect(1, 1, false);
									break;
								case 1:
									if (!Map.map_1) {
										console.addString("Level not open yet. WORK HARDER!!!", textoNormal);
										break;
									}
									console.addString("You clicked 20x20", textoNormal);
									juegoNuevo(20, 20);
									select.playAsSoundEffect(1, 1, false);
									break;
								case 2:
									if (!Map.map_2) {
										console.addString("Level not open yet. WORK HARDER!!!", textoNormal);
										break;
									}
									console.addString("You clicked 16x16", textoNormal);
									juegoNuevo(16, 16);
									select.playAsSoundEffect(1, 1, false);
									break;
								case 3:
									if (!Map.map_3) {
										console.addString("Level not open yet. WORK HARDER!!!", textoNormal);
										break;
									}
									console.addString("You clicked 12x12", textoNormal);
									juegoNuevo(12, 12);
									select.playAsSoundEffect(1, 1, false);
									break;
								case 4:
									if (Map.map_4) {
										console.addString("You clicked 8x8", textoNormal);
										juegoNuevo(8, 8);
										select.playAsSoundEffect(1, 1, false);
									}
									break;
							}
						}
						if (defShopRect.contains(x, y)) {
							if (defShopPrize == 0) {
								console.addString("Sorry, you are maxed.", textoNormal);
							} else if (player.getGold() >= defShopPrize) {
								player.setArmor(defShop);
								player.removeGold(defShopPrize);
								shop.playAsSoundEffect(1, 1, false);
								defShop++;
								if (defShop < Armor.armors.length) {
									defShopPrize = defShop * defShop * defShop * 250;
									console.addString("Defense Upgraded", textoNormal);
								} else {
									console.addString("Good job!!! Defense Maxed", textoNormal);
									defShop--;
									defShopPrize = 0;
								}
							} else {
								console.addString("No money, no upgrade :(.", textoNormal);
							}
						}
						if (arrShopRect.contains(x, y)) {
							if (arrShopPrize == 0) {
								console.addString("Sorry, you are maxed.", textoNormal);
							} else if (player.getGold() >= arrShopPrize) {
								player.setArrow(arrShop);
								player.removeGold(arrShopPrize);
								shop.playAsSoundEffect(1, 1, false);
								arrShop++;
								if (arrShop < Arrow.arrows.length) {
									arrShopPrize = arrShop * arrShop * 250;
									console.addString("Weapon Upgraded", textoNormal);
								} else {
									console.addString("Good job!!! Weapon Maxed", textoNormal);
									arrShop--;
									arrShopPrize = 0;
								}
							} else {
								console.addString("No money, no upgrade :(.", textoNormal);
							}
						}
					}
				}
			}
		}
	}

	public void tick(long delta) {
		if (delta == 0) {
			return;
		}
		punteroLocation.setLocation(Mouse.getX(), Mouse.getY());
		if (gameover) {
			tickGameover(delta);
			return;
		}
		boolean izq = Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean der = Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		boolean arr = Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean aba = Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		boolean action = Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);

		if (izq && der) {
			izq = der = false;
		}
		if (arr && aba) {
			arr = aba = false;
		}
		double dx = 0;
		double dy = 0;
		if (izq) {
			if (arr) {
				dx -= delta * PC.walk_speed * PC.diagonal_change;
				dy -= delta * PC.walk_speed * PC.diagonal_change;
				playerDir = PC.PC_DIA_ARRIZQ;
			} else if (aba) {
				dx -= delta * PC.walk_speed * PC.diagonal_change;
				dy += delta * PC.walk_speed * PC.diagonal_change;
				playerDir = PC.PC_DIA_ABAIZQ;
			} else {
				dx -= delta * PC.walk_speed;
				playerDir = PC.PC_MOVE_IZQ;
			}
		} else if (der) {
			if (arr) {
				dx += delta * PC.walk_speed * PC.diagonal_change;
				dy -= delta * PC.walk_speed * PC.diagonal_change;
				playerDir = PC.PC_DIA_ARRDER;
			} else if (aba) {
				dx += delta * PC.walk_speed * PC.diagonal_change;
				dy += delta * PC.walk_speed * PC.diagonal_change;
				playerDir = PC.PC_DIA_ABADER;
			} else {
				dx += delta * PC.walk_speed;
				playerDir = PC.PC_MOVE_DER;
			}
		} else if (arr) {
			dy -= delta * PC.walk_speed;
			playerDir = PC.PC_MOVE_ARR;
		} else if (aba) {
			dy += delta * PC.walk_speed;
			playerDir = PC.PC_MOVE_ABA;
		}
		player.tick(delta);
		if (dx != 0 || dy != 0) {
			BBRectangle playerbb = player.getBB().createMoved(dx, 0);
			if (!checkBB(playerbb)) {
				dx = 0;
			}
			playerbb = player.getBB().createMoved(dx, dy);
			if (!checkBB(playerbb)) {
				dy = 0;
			}
			player.walkTick(delta);
			player.setTecladoState(izq, arr, der, aba);
		}
		if (dx != 0 || dy != 0) {
			player.move(dx, dy);
		}
		if (!mobs.isEmpty()) {
			BBRectangle p = player.getBB();
			for (PC mob : mobs) {
				mob.tick(delta);
				if (!checkBB(mob.getBB()) || mob.getBB().collision(p)) {
					mob.cancelMovement(delta);
				}
				if (mob.canShoot()) {
					if (Game.random.nextDouble() > 0.9) {
						bulletsEnemigas.add(mob.fire());
					} else {
						mob.fakeFire();
					}
				}
			}
		}
		if (action && player.canShoot()) {
			Weapon arma = player.getWeapon();
			if (arma != null) {
				Bullet b = player.fire();
				if (b != null) {
					b.setDmg(player.getDmg());
					b.setLocation(player.getX(), player.getY());
					b.setDirection(playerDir);
					bullets.add(b);
					disparo.playAsSoundEffect(1, 0.8f, false);
				}
			}
		}
		if (!bullets.isEmpty()) {
			BBRectangle mapBB = map.getBB();
			for (int b = 0; b < bullets.size(); b++) {
				Bullet bullet = bullets.get(b);
				if (bullet.tick(delta)) {
					if (!mapBB.isInside(bullet.getBB())) {
						bullets.remove(b);
						b--;
					}
					if (!mobs.isEmpty()) {
						for (int m = 0; m < mobs.size(); m++) {
							PC mob = mobs.get(m);
							if (mob.getBB().collision(bullet.getBB())) {
								if (mob.hurt(bullet.getDmg())) {
									mobs.remove(m);
									console.addString(mob.getScore() + " exp points.", textoNormal);
									player.addScore(mob.getScore());
								}
								bullets.remove(b);
								b--;
								m = mobs.size();
								impacto.playAsSoundEffect(1, 1, false);
							}
						}
						if (mobs.isEmpty()) {
							//last mob die
							map.openNextLevel();
							gameover = true;
							player.updateStat();
						}
					}
				} else {
					bullets.remove(b);
					b--;
				}
			}
		}
		if (!bulletsEnemigas.isEmpty()) {
			BBRectangle mapBB = map.getBB();
			for (int b = 0; b < bulletsEnemigas.size(); b++) {
				Bullet bullet = bulletsEnemigas.get(b);
				if (bullet.tick(delta)) {
					if (!mapBB.isInside(bullet.getBB())) {
						bulletsEnemigas.remove(b);
						b--;
					}
					if (bullet.getBB().collision(player.getBB())) {
						if (player.hurt(bullet.getDmg())) {
							player.updateStat();
							gameover = true;
							gameoverDelay = 2000;
							explosion.playAsSoundEffect(1, 1, false);
						} else {
							double dmg = bullet.getDmg() - player.getDefense();
							if (dmg > 0) {
								console.addString(bullet.getDmg() + " dmg points.", textoNormal);
								damage.playAsSoundEffect(1, 1, false);
							}
						}
						bulletsEnemigas.remove(b);
						b = bulletsEnemigas.size();
					}
				} else {
					bulletsEnemigas.remove(b);
					b--;
				}
			}
		}
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
				if (Keyboard.getEventKey() == Keyboard.KEY_F5) {
					gameover = true;
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
	}

	private boolean checkBB(BBRectangle targetBB) {
		if (!map.isFree(targetBB)) {
			return false;
		}
		if (!mobs.isEmpty()) {
			for (PC mob : mobs) {
				if (mob.getBB().collision(targetBB)) {
					return false;
				}
			}
		}
		return true;
	}

	private void renderRawGui() {
		glColor3d(1, 1, 1);
		int h = 100;
		glBegin(GL_QUADS);
		{
			while (h < height) {
				glTexCoord2d(0, 0.75);
				glVertex2i(width - 200, h);
				glTexCoord2d(0, 1);
				glVertex2i(width - 200, h + 64);
				glTexCoord2d(0.25, 1);
				glVertex2i(width, h + 64);
				glTexCoord2d(0.25, 0.75);
				glVertex2i(width, h);
				h += 64;
			}
		}
		glEnd();
		console.renderBackground();
	}

	private void renderGameover() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		textureTitulos.bind();
		if (gameoverDelay > 0) {
			double prop = 1 - (gameoverDelay / 2000.0);
			glColor4d(1, 1, 1, prop);
			glBegin(GL_QUADS);
			{
				glTexCoord2d(0, 0);
				glVertex2d(0, 0);
				glTexCoord2d(0, 0.5);
				glVertex2d(0, height);
				glTexCoord2d(0.75, 0.5);
				glVertex2d(width - 200, height);
				glTexCoord2d(0.75, 0);
				glVertex2d(width - 200, 0);
			}
			glEnd();
		} else {
			Color c = Color.decode("0x40FFFFFF");
			glColor4d(1, 1, 1, 1);
			glBegin(GL_QUADS);
			{
				glTexCoord2d(0, 0.5);
				glVertex2d(0, 0);
				glTexCoord2d(0, 1);
				glVertex2d(0, height);
				glTexCoord2d(0.75, 1);
				glVertex2d(width - 200, height);
				glTexCoord2d(0.75, 0.5);
				glVertex2d(width - 200, 0);
			}
			glEnd();
			glDisable(GL_TEXTURE_2D);
			glColor3d(1, 1, 1);
			bigfont.drawString(menuMapOptions.x, menuMapOptions.y, "Map 1 : 24x24");
			bigfont.drawString(menuMapOptions.x, menuMapOptions.y + bigfont.getLineHeight() * 3 / 2, "Map 2 : 20x20", Map.map_1 ? Color.white : c);
			bigfont.drawString(menuMapOptions.x, menuMapOptions.y + bigfont.getLineHeight() * 3, "Map 3 : 16x16", Map.map_2 ? Color.white : c);
			bigfont.drawString(menuMapOptions.x, menuMapOptions.y + bigfont.getLineHeight() * 9 / 2, "Map 4 : 12x12", Map.map_3 ? Color.white : c);
			if (Map.map_4) {
				bigfont.drawString(menuMapOptions.x, menuMapOptions.y + bigfont.getLineHeight() * 6, "Map 5 :  8x 8", Color.white);
			}
		}
	}
}
