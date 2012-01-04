package com.piezo.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ImageButton  extends Image{
	TextureRegion texture, clickTexture;
	public ImageButton(TextureRegion normalTexture,TextureRegion clickTexture){
		super(normalTexture);
		this.texture = normalTexture;
		this.clickTexture= clickTexture;
	}

	public boolean touchDown (float x, float y, int pointer) {
		System.out.println("in the touch down");
		this.setRegion(clickTexture);
		return super.touchDown(x, y, pointer);
	}
	public void touchUp (float x, float y, int pointer) {
		this.setRegion(texture);
		super.touchUp(x, y, pointer);
	}
}
