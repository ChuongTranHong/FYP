package com.piezo.maingame;

import android.app.Activity;
import android.os.Bundle;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.piezo.util.Setting;

import android.app.Activity;
import android.os.Bundle;

public class GameFYPActivity extends  AndroidApplication {
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		
		initialize(new PiezoGame(), false);
		Setting.androidMode = true;
	}
}