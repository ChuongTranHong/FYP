package com.piezo.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;

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

public class RunningScreen extends GameScreen {
	public static int score = 0;
	boolean running = true;
	public IOIOThread ioio_thread;
	int screenHeight, screenWidth;
	PiezoGame game;
	List<CuttingObject> objectList;
	Sound sound;
	SpriteBatch spriteBatch;
	TextureRegion background, sword;
	TextureRegion cutleft, cutRight;
	long lastTime = -1, currentTime;
	BitmapFont font;
	Skin skin;
	Stage ui;
	byte currentPosition = 0;
	int swordX = 50;
	public static VoltageDiagram voltageDiagram;
	byte index;
	boolean getAcceleration = false;
	float lastYAccel=0,currentYAccel=0,differentAccel=0;;
	public RunningScreen(final PiezoGame game) {
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		this.game = game;
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(ui);
		sound = Gdx.audio.newSound(Gdx.files.internal(Config
				.asString("knifeSound")));

		background = new TextureRegion(new Texture(Gdx.files.internal(Config
				.asString("backgroundTexture"))));
		cutleft = new TextureRegion(new Texture((Gdx.files.internal(Config
				.asString("cutLeftTexture")))));
		cutRight = new TextureRegion(new Texture((Gdx.files.internal(Config
				.asString("cutRightTexture")))));
		sword = new TextureRegion(new Texture(
				Gdx.files.internal("data/sword1.png")));
		objectList = new ArrayList<CuttingObject>();
		objectList.add(new Apple(0, 0, screenWidth / 2, screenHeight - 50));
		if (!Setting.debug)
			objectList.add(new Egg(screenWidth / 2, 0, screenWidth / 2,
				screenHeight - 50));
		else
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
				game.setScreen(new MainMenu(game));
			}
		});

		ui.addActor(menuButton);
		if(Setting.androidMode){
			ioio_thread=createIOIOThread();
			ioio_thread.start();
		}
		
		System.out.println("ioio thread start ");
		score = 0;
		currentPosition = 0;
		running = true;

	}

	public void render(float delta) {

		currentTime = System.currentTimeMillis() / 1000;
		if (lastTime != currentTime && running) {
			for(index= 0;index<objectList.size();index++)
				objectList.get(index).decreaseTime();

			lastTime = currentTime;

		}
		update();

		
//		if (QueueCommand.size() > 0) {
//			Command command = QueueCommand.deQueue();
//			 System.out.println("dequeue new command");
////			if(command.left){
////				System.out.println(" cut left");
////				commandLeft(command);
////				
////			}else{
////				System.out.println("cut right");
////				commandRight(command);
////			}
//			if (objectList.get(0).takeCut(command.currentCommand, command.left))
//				running = true;
//			else
//				running = false;
//			sound.play(Setting.sound);
//			PoolStore.commandPool.free(command);
//		}

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.draw(background, 0, 0, screenWidth, screenHeight);
		spriteBatch.enableBlending();
		objectList.get(0).draw(spriteBatch, font, cutleft, cutRight);

		if (!Setting.debug) {

			objectList.get(1).draw(spriteBatch, font, cutleft, cutRight);
		}

		font.draw(spriteBatch, "Score: " + score, screenWidth - 300,
				screenHeight - 30);
		spriteBatch.draw(sword, swordX, screenHeight - 400);
		
		spriteBatch.end();
		ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

		Table.drawDebug(ui);

		ui.draw();
		for (index = 0; index < objectList.size(); index++) {
			if (objectList.get(index).isDead()) {
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
		if (Setting.debug)
			voltageDiagram.render();
		if (!running) {
			this.dispose();
			game.setScreen(new GameOverScreen(game));
		}
	}

	private void commandRight(Command command){
		switch (currentPosition) {
		case 0:
			if (!(objectList.get(0).takeCut(command.currentCommand, command.left)
					&& running) )
			

				running = false;
//			} else {
//				running = false;
//
//			}
			System.out.println("position 0 cut right");
			swordX = screenWidth / 2;
			currentPosition = 1;
			break;
		case 1:
			if(Setting.debug)break;
			if (!(objectList.get(1).takeCut(command.currentCommand, command.left)
					&& running))
			{

				running = false;
			} 
//			else {
//				running = false;
//
//			}
			System.out.println("position 1 cut right");
			swordX = screenWidth - 100;
			currentPosition = 2;
			break;
		}
		sound.play(Setting.sound);
	}
	private void commandLeft(Command command){
		switch (currentPosition) {
		case 1:
			if (!(objectList.get(0).takeCut(command.currentCommand, command.left)
					&& running))
				running = false;
			else
				running = true;
			swordX = 50;
			System.out.println("position 1 cut left");
			currentPosition = 0;
			break;
		case 2:
			if(Setting.debug)break;
			if (!(objectList.get(1).takeCut(command.currentCommand, command.left)
					&& running))
				running = false;
			else
				running = true;
			System.out.println("position 2 cut left");
			swordX = screenWidth / 2;
			currentPosition = 1;
			break;
		}
		sound.play(Setting.sound);
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
	
		if (ioio_thread != null && Setting.androidMode) {
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

		System.out.println("ioio thread start ");
	}

	public void dispose() {
		// TODO Auto-generated method stub
		if(Setting.androidMode){
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
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		switch (Setting.inputType) {
		case Setting.ACCELEROMETER:
			accelerometerInput();
			break;

		case Setting.TOUCHSCREEN:
			touchScreenInput();
			break;
		default:
			piezoInput();
		}
	}
	private void touchScreenInput(){
		if (Gdx.input.justTouched()) {
//			int x = Gdx.input.getX();
			Command command = PoolStore.commandPool.obtain();
			command.currentCommand= Command.NORMAL_SHORT_FORCE;
			if (Gdx.input.getX() > screenWidth / 2) {
				
				command.left = false;
				commandRight(command);
			} else {
				command.left = true;
				commandLeft(command);
			}
			PoolStore.commandPool.free(command);
			// System.out.println(" x "+x+" y " + y);
		}
	}
	private void accelerometerInput(){
		currentYAccel = Gdx.input.getAccelerometerY();
		differentAccel = currentYAccel- lastYAccel;
		System.out.println("different accel "+ differentAccel);
		if(Math.abs(differentAccel)<1) getAcceleration =false;
		else {
			if(!getAcceleration){
				Command command =PoolStore.commandPool.obtain();
				if(Math.abs(differentAccel)<2){
					command.currentCommand = Command.NORMAL_SHORT_FORCE;
					
				}else command.currentCommand = Command.STRONG_SHORT_FORCE;
				if(differentAccel < 0 )command.left =true;
				else command.left = false;
				objectList.get(0).takeCut(command.currentCommand, command.left);
				PoolStore.commandPool.free(command);
				getAcceleration=true;
			}
		}
		lastYAccel =currentYAccel;
		/*if((int)Gdx.input.getAccelerometerY()<0 && !getAcceleration){
			objectList.get(0).takeCut(Command.NORMAL_SHORT_FORCE, true);
//			System.out.println("acceleration left");
			getAcceleration= true;
		}else if((int)Gdx.input.getAccelerometerY()>0 && ! getAcceleration){
			objectList.get(0).takeCut(Command.NORMAL_SHORT_FORCE, false);
//			System.out.println("acceleration right");
			getAcceleration= true;
		}else if((int)Gdx.input.getAccelerometerY()==0){
//			System.out.println("reset acceleration");
			getAcceleration = false;
		}*/
	}
	private void piezoInput(){
		if (QueueCommand.size() > 0) {
			Command command = QueueCommand.deQueue();
			 System.out.println("dequeue new command");
//			if(command.left){
//				System.out.println(" cut left");
//				commandLeft(command);
//				
//			}else{
//				System.out.println("cut right");
//				commandRight(command);
//			}
			if (objectList.get(0).takeCut(command.currentCommand, command.left))
				running = true;
			else
				running = false;
			sound.play(Setting.sound);
			PoolStore.commandPool.free(command);
		}
	}
	protected IOIOThread createIOIOThread() {
		return new IOIOThreadExt();
	}

}
