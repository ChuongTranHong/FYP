package com.piezo.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.piezo.util.Config;
import com.piezo.util.PoolStore;
import com.piezo.util.TextOutput;
import com.piezo.util.Timer;

public class Bomb extends CuttingObject{

	public Bomb(){
		lifeSpan=Config.asShort("bomb.LifeSpan", (short) 10);
		currentLife=lifeSpan;
		texture=new Texture(Gdx.files.internal(Config.asString("bombTexture")));
		textureRegion = new TextureRegion(texture);
		initTimer = Config.asByte("bomb.Timer", (byte) 3);
		timer = new Timer(initTimer);
		this.x=0;
		this.y=0;
		this.width=0;
		this.height=0;
		bomb=true;
	}
	public Bomb(float x, float y, float width, float height){
		lifeSpan=Config.asShort("bomb.LifeSpan", (short) 10);
		currentLife=lifeSpan;
		texture=new Texture(Gdx.files.internal(Config.asString("bombTexture")));
		textureRegion = new TextureRegion(texture);
		initTimer = Config.asByte("bomb.Timer", (byte) 3);
		timer = new Timer(initTimer);

		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		bomb=true;
	}
	
	@Override
	public boolean supportCut(byte typeCut) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean takeCut(byte typeCut, boolean left) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void takeCut(byte typeCut, byte times) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void free() {
		// TODO Auto-generated method stub
		PoolStore.bombPool.free(this);
	}
	public static  CuttingObject obtain() {
		// TODO Auto-generated method stub
		return PoolStore.bombPool.obtain();

	}

}
