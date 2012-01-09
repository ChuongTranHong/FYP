package com.piezo.util;

import com.piezo.screen.RunningScreen;
import com.piezo.util.BufferData;
import com.piezo.util.Command;
import com.piezo.util.Data;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ClassificationThread extends Thread{
	Data data = new Data();

	Command returnState, currentState =new Command();
	Byte		returnDirection, currentDirection = BufferData.STABLE;
	BufferData buffer = new BufferData();
	public static Handler mChildHandler;
	public void run() {
		Looper.prepare();
		mChildHandler = new Handler() {
			public void handleMessage(Message msg) {

				data = (Data) msg.obj;
//				 System.out.println("in the mchildhandler value "+data.value+" inver "+data.valueInver);
				returnState = buffer
						.insertData(data.value, data.valueInver);
				if( RunningScreen.voltageDiagram!=null) {
					System.out.println("add value to voltage diagram");
					RunningScreen.voltageDiagram.addVertex(data.value,data.valueInver,returnState);
				}
//				diagram.insertValue(data.value, data.valueInver, index,returnState);
				if (returnState.compareTo( currentState)!=0 ){
					if(returnState.currentCommand==Command.NORMAL_SHORT_FORCE||
							returnState.currentCommand==Command.STRONG_SHORT_FORCE){
						Command command= PoolStore.commandPool.obtain();
						
						command.currentCommand=returnState.currentCommand;
						command.left=returnState.left;
						QueueCommand.listCommand.add(command);
						System.out.println("enqueue new command ");
					}
				currentState.clone(returnState);


				}
				
//				index = (index + 1) % bufferLength;
			}
		};
		Looper.loop();
	}
}