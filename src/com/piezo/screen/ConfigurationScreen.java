package com.piezo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.ValueChangedListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.esotericsoftware.tablelayout.Cell;
import com.piezo.maingame.PiezoGame;
import com.piezo.util.Setting;

public class ConfigurationScreen extends GameScreen {
	PiezoGame game;
	Skin skin;
	Stage ui;
	private Table window;
	CheckBox checkBox;
	public ConfigurationScreen(final PiezoGame game) {
		this.game = game;
		skin = new Skin(Gdx.files.internal("data/uiskinconfig.json"),
				Gdx.files.internal("data/uiskin.png"));
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(ui);
		window = new Table("window");
		window.width = ui.width();
		window.height = ui.height();
		window.x = 0;
		window.y = 0;
		window.debug();
		Texture texture = new Texture(
				Gdx.files.internal("data/optionbackground.jpg"));
		Image background = new Image(texture);
		float ratiox = ui.width() / texture.getWidth();
		float ratioy = ui.height() / texture.getHeight();
		background.scaleX = ratiox;
		background.scaleY = ratioy;
		ui.addActor(background);

		Label musicLabel = new Label("Music volume",
				skin.getStyle(LabelStyle.class), "music");

		Label soundLabel = new Label("Sound volume",
				skin.getStyle(LabelStyle.class), "sound");

		Slider musicSlider = new Slider(0, 10, 1,
				skin.getStyle(SliderStyle.class), "musicSlider");
		musicSlider.setValue(Setting.music * 10);
		musicSlider.setValueChangedListener(new ValueChangedListener() {
			public void changed(Slider slider, float value) {

				Setting.music = value / 10;
				game.music.setVolume(value / 10);
			}
		});
		Slider soundSlider = new Slider(0, 10, 1,
				skin.getStyle(SliderStyle.class), "soundSlider");
		soundSlider.setValue(Setting.sound * 10);
		soundSlider.setValueChangedListener(new ValueChangedListener() {

			public void changed(Slider slider, float value) {
				// TODO Auto-generated method stub
				Setting.sound = value / 10;
			}
		});
		
		
		
		
		
		
		Button trainingButton = new TextButton("Training",
				skin.getStyle(TextButtonStyle.class), "training");
		Button resetButton = new TextButton("Reset",
				skin.getStyle(TextButtonStyle.class), "reset");
		Button backButton = new TextButton("Back",
				skin.getStyle(TextButtonStyle.class), "back");
		 checkBox = new CheckBox("Debug",
				skin.getStyle(CheckBoxStyle.class), "checkbox");
		 checkBox.setChecked(Setting.debug);
		checkBox.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				Setting.debug = checkBox.isChecked();
				if(Setting.debug)System.out.println("in debug");
				else System.out.println("not in debug");
			}
		});
		window.align("top");
		window.padLeft(20).padTop(20);
		window.padRight(20);
		window.x = 0;
		window.y = 0;
		window.row().padBottom(10).padTop(10);
		window.add(musicLabel).padRight(20);
		window.add(musicSlider).fillX().expandX();
		window.row().padBottom(10).padTop(10);
		window.add(soundLabel).padRight(20);
		window.add(soundSlider).fillX().expandX();
		window.row().padBottom(10).padTop(10);
		window.add(trainingButton).align("left");
		window.row().padBottom(10).padTop(10);
		window.add(resetButton).align("left");
		window.row().padBottom(10).padTop(10);
		window.add(checkBox).align("left");
		ui.addActor(window);
		backButton.align("bottom").align("left");
		backButton.padTop(10).padBottom(10).padRight(20).padLeft(10);
		backButton.setClickListener(new ClickListener() {

			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				game.setScreen(new MainMenu(game));
			}
		});
		ui.addActor(backButton);

	}

	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		// Table.drawDebug(ui);
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
