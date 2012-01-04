package com.piezo.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Timer {
	public byte timeCountDown;

	public Timer(byte time){
		this.timeCountDown=time;

	}
	public void render(SpriteBatch spriteBatch,BitmapFont font , float x, float y){
		if(timeCountDown>=0){
			font.draw(spriteBatch,Byte.toString(timeCountDown),x,y);
		}
	}
	public void decreaseTime(){
		timeCountDown--;
	}
}
