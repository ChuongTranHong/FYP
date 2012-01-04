package com.piezo.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.piezo.screen.RunningScreen;
import com.piezo.util.Command;
import com.piezo.util.Config;
import com.piezo.util.PoolStore;
import com.piezo.util.TextOutput;
import com.piezo.util.Timer;

public class Apple extends CuttingObject {
	protected final byte DAMAGE_STRONG_FORCE = Config.asByte("apple.StrongDamage", (byte) 10);
	protected final byte DAMAGE_NORMAL_FORCE =  Config.asByte("apple.NormalDamage", (byte) 5);

	public Apple(){
		lifeSpan =Config.asShort("apple.LifeSpan", (short) 100);
		currentLife= lifeSpan;
		
		texture=new Texture(Gdx.files.internal(Config.asString("appleTexture")));
		textureRegion = new TextureRegion(texture);
		initTimer = Config.asByte("apple.Timer", (byte) 10);
		timer = new Timer(initTimer);
		this.x=this.y=this.width=this.height=0;
		
	}

	public Apple(float x, float y, float width, float height){
		lifeSpan=Config.asShort("apple.LifeSpan", (short) 100);;
		currentLife=lifeSpan;
		texture=new Texture(Gdx.files.internal(Config.asString("appleTexture")));
		textureRegion = new TextureRegion(texture);
		initTimer = Config.asByte("apple.Timer", (byte) 10);
		timer = new Timer(initTimer);
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	@Override
	public boolean supportCut(byte typeCut) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean takeCut(byte typeCut,boolean left) {
		// TODO Auto-generated method stub
		if(currentLife<=0) return false;
		if(typeCut== Command.NOCOMMAND || typeCut==Command.UNDETERMINED)return false;
		TextOutput temp = PoolStore.textPool.obtain();
		temp.x=0;
		temp.y=0;
		if(typeCut== Command.STRONG_SHORT_FORCE){
			currentLife-=DAMAGE_STRONG_FORCE;
			temp.text="-"+DAMAGE_STRONG_FORCE+" damage";
			RunningScreen.score+=DAMAGE_STRONG_FORCE;
		}
		else if(typeCut== Command.NORMAL_SHORT_FORCE){
			currentLife -= DAMAGE_NORMAL_FORCE;
			temp.text="-"+DAMAGE_NORMAL_FORCE+" damage";
			RunningScreen.score+=DAMAGE_NORMAL_FORCE;
		}
		if(currentLife<0 ){
			currentLife=0;
		}
		targetRatio=currentPercentLifeSpan() * beginingRatio;
		
		
		
		textOutList.add(temp);
		if(left)cutLeft=true;
		else cutRight=true;
		return true;
	}

	@Override
	public void takeCut(byte typeCut, byte times) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void free() {
		// TODO Auto-generated method stub
		PoolStore.applePool.free(this);
	}
	public static CuttingObject obtain() {
		// TODO Auto-generated method stub
		return PoolStore.applePool.obtain();
	}
	
}
