package com.piezo.util;

import android.graphics.Color;
import android.os.Message;

import com.piezo.util.BufferData;
import com.piezo.util.Data;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

public class IOIOThreadExt extends IOIOThread{
//	private AnalogInput input_;
	private DigitalOutput led_;
	private AnalogInput input_;
	private AnalogInput input_33;
	byte currentState = BufferData.UNDETERMINED, returnState;
//	private int color = Color.TRANSPARENT;
	// BufferData buffer;
	Data data = new Data();
	Command command;
	boolean left=false,right=false;
	ClassificationThread childThread;
	public void setup() throws ConnectionLostException {
		try {
			System.out.println("in the setup of ioio");
			led_ = ioio_.openDigitalOutput(IOIO.LED_PIN, true);
			input_ = ioio_.openAnalogInput(31);
			input_33 = ioio_.openAnalogInput(33);
			childThread=new ClassificationThread();
			childThread.start();
		} catch (ConnectionLostException e) {
			throw e;
		}
	}
	public void loop() throws ConnectionLostException{
		/*try{
			
			System.out.println(" in the loop");
			float reading = input_.getVoltage();
			float reading33 = input_33.getVoltage();
			System.out.println("reading "+reading+ " inv "+reading33);
			if(reading>2 && !left){
				command= PoolStore.commandPool.obtain();
				command.left=true;
				QueueCommand.enQueue(command);
				System.out.println("add left");
				left=true;
			}else if( reading<2)left=false;
			if (reading33>2 && !right){
				command= PoolStore.commandPool.obtain();
				command.left=false;
				QueueCommand.enQueue(command);
				System.out.println("add right");
				right=true;
			}else if(reading33 < 2)right=false;
				
//			led_.write(state);
			sleep(100);
		} catch (InterruptedException e) {
			ioio_.disconnect();
		} catch (ConnectionLostException e) {
			
			throw e;
		}*/
		try {
			float reading = input_.getVoltage();
			float reading33 = input_33.getVoltage();
			if (reading < 1)
				reading = 0;
			if (reading33 < 1)
				reading33 = 0;
			final float display1 = (float) (Math.round((reading * 100)) / 100.0);
			final float display2 = (float) (Math.round((reading33 * 100)) / 100.0);
//			System.out.println("in the send messeage data " + display1
//					+ " inver " + display2);
			if (ClassificationThread.mChildHandler != null) {
//				System.out.println("in the send messeage data " + display1
//						+ " inver " + display2);
				data.value = display1;
				data.valueInver = display2;
				Message msg = ClassificationThread.mChildHandler.obtainMessage();
				msg.obj = data;
				ClassificationThread.mChildHandler.sendMessage(msg);

			}
			

			
			sleep(50);
		} catch (InterruptedException e) {
			ioio_.disconnect();
		} catch (ConnectionLostException e) {
		
			throw e;
		}
	}

}