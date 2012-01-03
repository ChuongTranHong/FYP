package com.piezo.screen;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.Pool;

import com.piezo.maingame.PiezoGame;
import com.piezo.model.Apple;
import com.piezo.model.Bomb;
import com.piezo.model.CuttingObject;
import com.piezo.model.Egg;
import com.piezo.model.VoltageDiagram;
import com.piezo.util.Command;
import com.piezo.util.Config;
import com.piezo.util.IOIOThread;
import com.piezo.util.IOIOThreadExt;
import com.piezo.util.PoolStore;
import com.piezo.util.QueueCommand;
import com.piezo.util.Setting;
import com.piezo.util.TextOutput;

public class RunningScreen extends GameScreen {
	public static int score = 0;
	boolean running = true;
	public IOIOThread ioio_thread;
	int screenHeight, screenWidth;
	PiezoGame app;
	List<CuttingObject> objectList;
	Pool<TextOutput> pool;
	Sound sound;
	SpriteBatch spriteBatch;
	TextureRegion region, background,sword;
	TextureRegion cutleft, cutRight;
	long lastTime = -1, currentTime;
	BitmapFont font;
	float ratio = 1;
	float startXLeft = 50f, startXRight = 200f;
	boolean cutting = false;
	short change = 0;
	List<String> list = new ArrayList<String>();
	Skin skin;
	Stage ui;
	byte currentPosition=0;
	int swordX=50;
	public static VoltageDiagram voltageDiagram;
	byte index;
	public RunningScreen(final PiezoGame app) {
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		this.app = app;
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(ui);
		sound = Gdx.audio.newSound(Gdx.files.internal(Config
				.asString("knifeSound")));

		pool = new Pool<TextOutput>(8, 16) {

			@Override
			protected TextOutput newObject() {
				// TODO Auto-generated method stub
				return new TextOutput(null, 0, 0);
			}

		};
		background = new TextureRegion(new Texture(Gdx.files.internal(Config
				.asString("backgroundTexture"))));
		cutleft = new TextureRegion(new Texture((Gdx.files.internal(Config
				.asString("cutLeftTexture")))));
		cutRight = new TextureRegion(new Texture((Gdx.files.internal(Config
				.asString("cutRightTexture")))));
		sword = new TextureRegion(new Texture(Gdx.files.internal("data/sword1.png")));
		objectList = new ArrayList<CuttingObject>();
		objectList
				.add(new Apple(pool, 0, 0, screenWidth / 2, screenHeight - 50));
		objectList.add(new Egg(pool, screenWidth / 2, 0, screenWidth / 2,
				screenHeight - 50));
		if (Setting.debug)
			voltageDiagram = new VoltageDiagram(screenWidth / 2, 0,
					screenWidth / 2, screenHeight);
		spriteBatch = new SpriteBatch();

		font = new BitmapFont(Gdx.files.internal("data/c.fnt"),
				Gdx.files.internal("data/c.png"), false);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"),
				Gdx.files.internal("data/uiskin.png"));
		Button menuButton = new TextButton("Menu",
				skin.getStyle(TextButtonStyle.class), "menu");
		menuButton.x = screenWidth - 150;
		menuButton.y = screenHeight - 50;
		menuButton.setClickListener(new ClickListener() {

			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				System.out.println("in click menu");
				app.setScreen(new MainMenu(app));
			}
		});

		ui.addActor(menuButton);
//		 ioio_thread=createIOIOThread();
//		 ioio_thread.start();
		System.out.println("ioio thread start ");
		score = 0;
		currentPosition=0;
		running = true;

	}

	public void render(float delta) {

		currentTime = System.currentTimeMillis() / 1000;
		if (lastTime != currentTime && running) {

			objectList.get(0).decreaseTime();
			objectList.get(1).decreaseTime();
			lastTime = currentTime;

		}

		if (Gdx.input.justTouched()) {
			int x = Gdx.input.getX();

			if (x > screenWidth / 2) {
				switch(currentPosition){
					case 0 : 
						if (objectList.get(0).takeCut(Command.NORMAL_SHORT_FORCE, true) && running) {
//					System.out.println("state is true");
							running = true;
						} else{
							running= false;
//					System.out.println("state is false");
						}
						swordX = screenWidth/2;
						currentPosition = 1;
						break;
					case 1:
						if (objectList.get(1).takeCut(Command.NORMAL_SHORT_FORCE, true) && running) {
//							System.out.println("state is true");
									running = true;
								} else{
									running= false;
//							System.out.println("state is false");
								}
						swordX = screenWidth -100;
						currentPosition =2 ;
								break;
				}
				sound.play(Setting.sound);
			} else {
				switch(currentPosition){
				case 1:
					if (objectList.get(0)
							.takeCut(Command.STRONG_SHORT_FORCE, false) && running)
						running = true;
					else running= false;
					swordX = 50;
					currentPosition = 0;
					break;
				case 2:
					if (objectList.get(1)
							.takeCut(Command.STRONG_SHORT_FORCE, false) && running)
						running = true;
					else running= false;
					swordX = screenWidth/2;
					currentPosition = 1;
					break;
				}
			
				sound.play(Setting.sound);
			}
			cutting = true;
			// System.out.println(" x "+x+" y " + y);
		}

		if (QueueCommand.size() > 0) {
			Command command = QueueCommand.deQueue();
//			System.out.println("dequeue new command");
			if(objectList.get(0).takeCut(command.currentCommand, command.left))
				running = true;
			else running = false;
			sound.play(Setting.sound);
			PoolStore.commandPool.free(command);
		}

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.draw(background, 0, 0, screenWidth, screenHeight);
		spriteBatch.enableBlending();
		objectList.get(0).draw(spriteBatch, font, cutleft, cutRight);
		if (Setting.debug)
			voltageDiagram.render(spriteBatch);
		else {

			objectList.get(1).draw(spriteBatch, font, cutleft, cutRight);
		}

		font.draw(spriteBatch, "Score: " + score, screenWidth - 300,
				screenHeight - 30);
		spriteBatch.draw(sword,swordX, screenHeight-400);
		if (!running) {
			this.dispose();
			app.setScreen(new GameOverScreen(app));
			
//			font.draw(spriteBatch, "GAME OVER", screenWidth / 2,
//					screenHeight / 2);
		}
		spriteBatch.end();
		ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

		Table.drawDebug(ui);
		//
		ui.draw();
		for(index=0;index<=1;index++){
			if (objectList.get(index).isDead()) {
	//			System.out.println("is dead");
				CuttingObject object = objectList.get(index);
	
				CuttingObject newObject = PoolStore.poolArray.get(
						(int) (Math.random() * 3)).obtain();
				newObject.copySetting(object);
				newObject.reset();
	
				objectList.remove(index);
				object.free();
	
				objectList.add(index, newObject);
			}
		}
	}

	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	public void show() {
		// TODO Auto-generated method stub

	}

	public void hide() {
		// TODO Auto-generated method stub

	}

	public void pause() {
		// TODO Auto-generated method stub
		if (ioio_thread != null) {
			ioio_thread.abort();
			try {
				ioio_thread.join();
			} catch (InterruptedException e) {
			}
			System.out.println("end on Pause");
		}
	}

	public void resume() {
		// TODO Auto-generated method stub
//		ioio_thread = createIOIOThread();
//		ioio_thread.start();
		System.out.println("ioio thread start ");
	}

	public void dispose() {
		// TODO Auto-generated method stub
		if (ioio_thread != null) {
			ioio_thread.abort();
			try {
				ioio_thread.join();
			} catch (InterruptedException e) {
			}
			System.out.println("end on Pause");
		}
		sound.dispose();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	protected IOIOThread createIOIOThread() {
		return new IOIOThreadExt();
	}

}