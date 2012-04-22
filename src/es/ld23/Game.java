package es.ld23;

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
	private Rectangle weaponRectangle;
	private int playerDir;
	//propios
	private Console console;
	private Color textoNormal;
	private Map map;
	private Player player;
	private ArrayList<PC> mobs;
	private ArrayList<Bullet> bullets;
	//recursos
	private TrueTypeFont font;
	private Texture puntero;
	private Texture textureItems;
	private Texture textureTiles;
	private Texture textureMobs;
	private Audio explosion;
	private Audio disparo;
	private Audio salto;

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
		textoNormal = Color.green.brighter();
		weaponRectangle = new Rectangle(width - 50, 152, 48, 48);
		console = new Console(width - 200, 0, 200, 150);
		console.setFont(font);
		console.addString("Test Line. All this text is the same line. The code fit this text inside the area designated to console...at least for width, will fix height later :)", textoNormal);
		map = new Map();
		player = new Player();
		mobs = new ArrayList<PC>();
		bullets = new ArrayList<Bullet>();
		for (int i = 0; i < 40; i++) {
			mobs.add(new Zombie(map.getWidth(), map.getHeight()));
		}
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
			explosion = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/explosion.wav"));
			salto = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/jump.wav"));
			disparo = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/bullet.wav"));
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
		if (bullets.isEmpty()) {
			return;
		}
		glBegin(GL_QUADS);
		{
			for (Bullet bullet : bullets) {
				bullet.render();
			}
		}
		glEnd();
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
		console.render();
		textureItems.bind();
		Weapon.bow.render(weaponRectangle);
		renderPuntero();
	}

	public void tick(long delta) {
		if (delta == 0) {
			return;
		}

		boolean izq = Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean der = Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		boolean arr = Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean aba = Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN);
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
			for (PC mob : mobs) {
				mob.tick(delta);
			}
		}
		if (!bullets.isEmpty()) {
			for (Bullet bullet : bullets) {
				bullet.tick(delta);
			}
		}


		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(punteroLocation.getX(), punteroLocation.getY());
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
					Weapon arma = player.getWeapon();
					if (arma != null) {
						Bullet b = arma.fire();
						if (b != null) {
							b.setLocation(player.getX(), player.getY());
							b.setDirection(playerDir);
							bullets.add(b);
							disparo.playAsSoundEffect(1, 1, false);
						}
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
					explosion.playAsMusic(1, 1, false);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_F2) {
					salto.playAsSoundEffect(1, 1, false);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_F5) {
					mobs.clear();
					bullets.clear();
					map.nuevo();
					for (int i = 0; i < 400; i++) {
						mobs.add(new Zombie(map.getWidth(), map.getHeight()));
					}
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

	private boolean checkBB(BBRectangle playerbb) {
		if (!map.isFree(playerbb)) {
			return false;
		}
		if (!mobs.isEmpty()) {
			for (PC mob : mobs) {
				if (mob.getBB().collision(playerbb)) {
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
}
