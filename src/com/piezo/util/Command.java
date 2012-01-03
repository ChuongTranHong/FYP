package com.piezo.util;

public class Command implements Comparable<Command>,Cloneable {
	public static final byte UNDETERMINED = 0;
	public static final byte NOCOMMAND = 3;
	public static final byte STRONG_SHORT_FORCE = 1;
	public static final byte NORMAL_SHORT_FORCE = 2;
	public short index;
	public boolean left;
	public byte currentCommand;
	public Command(){
		index=-1;
		left=true;
		currentCommand=NOCOMMAND;
	}
	public int compareTo(Command another) {
		if(this.index==another.index && this.left ==  another.left &&  this.currentCommand== another.currentCommand)
		// TODO Auto-generated method stub
			return 0;
		return 1;
	}
	public void clone(Command object){
//		Command command=new Command();
		this.currentCommand=object.currentCommand;
		this.index=object.index;
		this.left=object.left;

	}
	
}
