package com.piezo.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Region;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.piezo.util.Config;
import com.piezo.util.PoolStore;
import com.piezo.util.Setting;
import com.piezo.util.TextOutput;
import com.piezo.util.Timer;

public abstract class CuttingObject {

	protected boolean bomb=false;
	protected byte initTimer;
	Timer timer;
	float x,y,width,height;
	byte cutStep=0;
//	protected Pool<TextOutput> pool;
	protected  short  lifeSpan;
	protected short currentLife;
	protected List<TextOutput> textOutList=new ArrayList<TextOutput>();
	protected byte compatibleCut;
	protected Texture texture;
	protected TextureRegion textureRegion;
	protected float beginingRatio=2f;
	protected float currentRatio=beginingRatio;
	protected float targetRatio=beginingRatio;
	protected TextureRegion textureLifeSpan= new TextureRegion(new Texture((Gdx.files.internal(Config.asString("lifeSpanTexture")))));
	protected int index;
	boolean cutLeft=false, cutRight=false;
	public abstract boolean supportCut(byte typeCut);
	public abstract boolean  takeCut(byte typeCut,boolean left);
	public abstract void takeCut(byte typeCut,byte times);
	public float currentPercentLifeSpan(){
		return (float)currentLife/lifeSpan;
	}
	public TextureRegion getTexture(){
		return textureRegion;
	}
	public void draw(SpriteBatch spriteBatch,BitmapFont font,TextureRegion leftTexture,TextureRegion rightTexture){
		if(currentRatio>targetRatio){
			currentRatio-=0.05;
		}else currentRatio=targetRatio;
//		spriteBatch.draw(textureLifeSpan, x, y, 0f, 0f, textureLifeSpan.getRegionWidth(), textureLifeSpan.getRegionHeight(), 2f, 1f, 0);
		if(!bomb)spriteBatch.draw(textureLifeSpan,this.x+50,height -50,0f,0f,textureLifeSpan.getRegionWidth(),textureLifeSpan.getRegionHeight(),currentRatio,1f,0f);

		timer.render(spriteBatch, font,this.x+width-50,this.y+height-50);
		if(!Setting.debug)
			spriteBatch.draw(textureRegion, this.x+50, height- 400);
		else spriteBatch.draw(textureRegion, this.x+50, height- 300);
		for(index=0;index<textOutList.size();index++){
			TextOutput temp=textOutList.get(index);
			font.draw(spriteBatch, temp.text, this.x+50, height-150+temp.y);
			temp.y +=2;
			if(temp.y == 20){
				textOutList.remove(index);
				PoolStore.textPool.free(temp);
			}
		}
		if(cutRight){
			if(!Setting.debug)
				spriteBatch.draw(leftTexture,this.x+50+20*cutStep,height-300);
			else spriteBatch.draw(leftTexture,this.x+50+20*cutStep,height-300); 
			cutStep++;
			if(cutStep>10){
				cutRight=false;
				cutStep=0;
			}
		}else if(cutLeft){
			if(!Setting.debug)
				spriteBatch.draw(rightTexture,this.x+textureRegion.getRegionWidth()+50-20*cutStep,height-300);
			else spriteBatch.draw(rightTexture,this.x+textureRegion.getRegionWidth()+50-20*cutStep,height-100);
			cutStep++;
			if(cutStep>10){
				cutLeft=false;
				cutStep=0;
			}
		}
		
	}
//	public void setPool(Pool<TextOutput> pool){
//		this.pool=pool;
//	}
	public void decreaseTime(){
		timer.decreaseTime();
	}
	public boolean isDead(){
		return (timer.timeCountDown<=0 || currentLife<=0);
	}
//	public void setPool(Pool pool){
//		this.pool = pool;
//	}
	public void setRegion(float x, float y ,float width, float height){
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	public void copySetting(CuttingObject object){
		this.x=object.x;
		this.y=object.y;
		this.width= object.width;
		this.height= object.height;
	
	}
	public void reset(){
		currentLife=lifeSpan;
		timer.timeCountDown=initTimer;
		currentRatio=targetRatio=beginingRatio;
		cutStep=0;
		cutLeft=false;
		cutRight=false;
		while(textOutList.size()>0){
			PoolStore.textPool.free(textOutList.get(0));
			textOutList.remove(0);
		}
		

	}
	public abstract void free();
	public  static CuttingObject obtain() {
		return null;
	}
	
}
