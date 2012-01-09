package com.piezo.util;

public class Setting {
	public static final byte PIEZO = 0;
	public static final byte TOUCHSCREEN = 1;
	public static final byte ACCELEROMETER = 2;
	public static boolean androidMode = true;
	public static float sound=Config.asFloat("defaultSound", 0);
	public static float music=0;// Config.asFloat("defaultMusic", 0);
	public static boolean debug = true;
	public static byte inputType =PIEZO;
	
}
