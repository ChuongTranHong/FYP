package com.piezo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.piezo.maingame.PiezoGame;
import com.piezo.util.ImageButton;

public class GameOverScreen extends GameScreen {
	PiezoGame game;
	Skin skin;
	Stage ui;
	int screenHeight, screenWidth;
	SpriteBatch spriteBatch;
	TextureRegion backgoundTexture;

	public GameOverScreen(final PiezoGame game) {
		this.game = game;
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		skin = new Skin(Gdx.files.internal("data/uiskinconfig.json"),
				Gdx.files.internal("data/uiskinover.png"));
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(ui);
		Label label = new Label("Game Over", skin.getStyle(LabelStyle.class),
				"label");
		label.x = screenWidth / 2;
		label.y = screenHeight / 2 + 30;
		backgoundTexture = new TextureRegion(new Texture(
				Gdx.files.internal("data/gameoverbackground.jpg")));
		Texture replay = new Texture(
				Gdx.files.internal("data/retryTexture.png"));
		ImageButton replayButton = new ImageButton(new TextureRegion(replay,
				0f, 0f, 0.5f, 1f), new TextureRegion(replay, 0.5f, 0f, 1f, 1f));
		replayButton.x = screenWidth / 2 - 200;
		replayButton.y = screenHeight / 2 - 80;
		replayButton.scaleX = 2;
		replayButton.scaleY = 2;
		replayButton.setClickListener(new ClickListener() {

			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				// System.out.println("return button click ");
				game.setScreen(new RunningScreen(game));
			}
		});
		Texture quit = new Texture(Gdx.files.internal("data/quitTexture.png"));
		ImageButton quitButton = new ImageButton(new TextureRegion(quit, 0f,
				0f, 0.5f, 1f), new TextureRegion(quit, 0.5f, 0f, 1f, 1f));
		quitButton.x = screenWidth / 2 + 100;
		quitButton.y = screenHeight / 2 - 70;
		quitButton.scaleX = 2;
		quitButton.scaleY = 2;
		quitButton.setClickListener(new ClickListener() {

			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				// System.out.println("return quit click ");
				game.setScreen(new MainMenu(game));
			}
		});

		ui.addActor(label);
		ui.addActor(replayButton);
		ui.addActor(quitButton);
		spriteBatch = new SpriteBatch();
	}

	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		// Table.drawDebug(ui);

		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.draw(backgoundTexture, 0, 0, screenWidth, screenHeight);
		spriteBatch.enableBlending();
		spriteBatch.end();
		ui.draw();
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

	}

	public void resume() {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
