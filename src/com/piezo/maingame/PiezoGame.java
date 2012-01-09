package com.piezo.maingame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.piezo.screen.ConfigurationScreen;
import com.piezo.screen.GameOverScreen;
import com.piezo.screen.MainMenu;
import com.piezo.screen.RunningScreen;

import com.piezo.util.Config;

public class PiezoGame implements ApplicationListener{

	public Music music;
	private Screen screen;
	private boolean isInitialized = false;

	public void create() {
		// TODO Auto-generated method stub
		if(!isInitialized){
			isInitialized=true;
			music = Gdx.audio.newMusic(Gdx.files.internal(Config.asString("backGroundMusic")));
			music.setVolume(0.5f);
			music.setLooping(true);
			music.play();
			setScreen(new MainMenu(this));
//			setScreen(new TestDiagram(this));
		}
		
	}

	

	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		if (screen != null) screen.resize(width, height);
	}

	public void render() {
		// TODO Auto-generated method stub

		screen.render( Gdx.graphics.getDeltaTime());

		
	}

	public void pause() {
		// TODO Auto-generated method stub
		if (screen != null) screen.pause();
		
	}
	

	public void resume() {
		// TODO Auto-generated method stub
		if (screen != null) screen.resume();

	}

	public void dispose() {
		// TODO Auto-generated method stub
		music.dispose();
		if (screen != null) screen.hide();
	}
	public void setScreen(Screen screen) {
		// TODO Auto-generated method stub
		this.screen= screen;
	}
	public Screen getScreen(){
		return screen;
	}
	
}
