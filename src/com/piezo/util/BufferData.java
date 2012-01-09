package com.piezo.util;

import com.piezo.util.Command;

public class BufferData {
	
	public static final byte UNDETERMINED = 0;
	public static final byte STRONG_SHORT_FORCE = 1;
	public static final byte NORMAL_SHORT_FORCE = 2;
	public static final byte SMALL_LONG_FORCE = 3;
	public static final float NORMAL = 0xFFF;
	public static final float BUFFERFULL = 0xFFE;
	
	public static final byte LEFT=1;
	public static final byte RIGHT=2;
	public static final byte STABLE=3;
	public static final byte BOTHHIGH=4;
	private byte currentDirection;
	
	public static byte currentState = UNDETERMINED;
	private short bufferLength = 200;
	private float[] value;
	private float[] valueInver;

	private short index = 0;
	private short maxIndexLeft = -1;
	private short maxIndexRight = -1;

	private short notZeroBeginIndexLeft = -1;
	private short notZeroBeginIndexRight = -1;

	private short timeStep = 5;// 0.25 second
	private final float STRONG_FORCE_THRESHOLD = 3f;
	private short lastCommandLeftIndex=-1,lastCommandRightIndex=-1;
	private final float NORMAL_FORCE_UPPER_BOUND_THRESHOLD = 3f;
	private final float NORMAL_FORCE_LOWER_BOUND_THRESHOLD = 1;
	private final float WEAK_FORCE_THRESHOLD = 1.5f;
	private final short STRONG_TIME_THRESHOLD = 4;
	private final short NORMAL_TIME_THRESHOLD = 10;
	// private final short WEAK_TIME_THRESHOLD=7;
	private short numberOfZeroTime = 0;
	private boolean alreadyClassify = true;
	// private DiagramActivity diagramActivity;
	private boolean reachZero = true;// for testing only
	// private IOIOSimpleApp mainActivity;
	private Command command=new Command();
	boolean refresh = false;
	public short getIndex() {
		return index;
	}

	public BufferData() {
		value = new float[bufferLength];
		valueInver = new float[bufferLength];

		initializeArray(value);
		initializeArray(valueInver);
		currentDirection= STABLE;
		index = 0;
		// this.diagramActivity = diagramActivity;
		// initializeBufferLength();
	}

	private void initializeArray(float[] array) {
		for (int i = 0; i < array.length; i++)
			array[i] = 0;
	}

	public Command insertData(float data, float dataInver) {
		/*
		 * if (data < 0.2) { data = 0; reachZero = true; } if (dataInver < 0.2)
		 * dataInver = 0; value[index] = (float) Math.round((data * 100)) / 100;
		 * valueInver[index] = (float) Math.round((dataInver * 100)) / 100;
		 */
		command.index=-1;
		command.currentCommand= Command.NOCOMMAND;
		command.left=true;
		value[index] = data;
		valueInver[index] = dataInver;
		System.out.println("insert  value "+value[index]+" inver "+valueInver[index]+" index "+index);
		analyseAcceleration();
		index = (short) ((index + 1) % bufferLength);

		return command;
	}

	public void analyseAcceleration() {
		
	// find the maximum value's index
//		System.out.println("maxIndexLeft "+maxIndexLeft);
		if(value[index] > ((maxIndexLeft==-1)?0:value[maxIndexLeft])) maxIndexLeft = index;
		if(valueInver[index]>((maxIndexRight==-1)?0:valueInver[maxIndexRight])) maxIndexRight =index;
		
		//tracking the zero index in both array
	
		
		
		//case at least one value is not zero
		if(value[index]>0 && valueInver[index]>0){
			System.out.println("in the both hign before condition ");
			// the first time come to the both high stage ( the zone is inside the range step)
			if(!alreadyClassify && currentDirection!=BOTHHIGH){
				System.out.println("in the both high classify right");
				if(currentDirection==LEFT) classifyLeft(false);
				if(currentDirection==RIGHT) classifyRight(false);
			}
			maxIndexLeft=maxIndexRight=-1;
			alreadyClassify=false;
			currentDirection=BOTHHIGH;		
		}
		else if (value[index] > 0 ){
			if(currentDirection == RIGHT){

				System.out.println(" right in the left zone");
				if(!alreadyClassify) {
					System.out.println("in the classify left in the right zone");
					classifyRight(false);
				}
				maxIndexRight=-1;
				alreadyClassify=false;
			}
			currentDirection = LEFT;
			if (((index - notZeroBeginIndexLeft + bufferLength) % bufferLength)
					% timeStep == 0 && !alreadyClassify) {
				System.out.println("in the classify left");
				classifyLeft(true);
				if(refresh)refresh=false;
				else alreadyClassify=true;
			}
		}
		else if ( valueInver[index]>0 ){
			if(currentDirection ==LEFT){
				
				System.out.println(" left in the right zone");
				if(!alreadyClassify) {
					System.out.println("in the classify left in the right zone");
					classifyLeft(false);
				}
				maxIndexLeft=-1;
				alreadyClassify=false;
			}
			currentDirection = RIGHT;
			if (((index - notZeroBeginIndexRight + bufferLength) % bufferLength)
					% timeStep == 0 && !alreadyClassify) {
				System.out.println("in the normal classify right");
				classifyRight(true);
				if(refresh)refresh=false;
				else alreadyClassify=true;
			}
		}

		
		
	
		
		

		if(value[index]==0 && valueInver[index]==0){
			System.out.println("in th stable before condition");
			if(!alreadyClassify && currentDirection!=STABLE && numberOfZeroTime ==0){
				
				System.out.println("classify in stable ");
				if(currentDirection==LEFT) {
//					if((index+bufferLength-notZeroBeginIndexLeft)%bufferLength > 1){
//						System.out.println(" quick fluctuate left");
						classifyLeft(false);
//					}
				}
				if(currentDirection==RIGHT){
//					if((index + bufferLength - notZeroBeginIndexRight)%bufferLength > 1){
//						System.out.println(" quick fluctuate right");
						classifyRight(false);
//					}
				}
				
			}
			numberOfZeroTime++;
			maxIndexLeft=maxIndexRight=-1;
//			if(numberOfZeroTime == 2){
				currentDirection = STABLE;
				currentState= UNDETERMINED;
//			}
			alreadyClassify = false;
		}else numberOfZeroTime = 0;
		if(value[index]==0)notZeroBeginIndexLeft=index;
		if(valueInver[index]==0)notZeroBeginIndexRight=index;
		
		
		
	/*	if(currentDirection==)
		if (value[index] == 0) {// setup the zero zone and zero index
			numberOfZeroTime++;
			if (alreadyClassify)
				maxIndex = -1;

			if (!alreadyClassify) {
				classify();
			
				alreadyClassify = true;
			}
			if (numberOfZeroTime > 2){
				currentState = UNDETERMINED;

			}
		} else {
			// from zero to not zero voltage
			if (numberOfZeroTime != 0) {
				notZeroBeginIndex = (short) ((index - 1 + bufferLength) % bufferLength);
				numberOfZeroTime = 0;
				if (alreadyClassify) {
					alreadyClassify = false;
				}
			}

			// compare to zero value
			if (maxIndex == -1)
				maxIndex = index;
			else if (value[index] > value[maxIndex])
				maxIndex = index;

			if (((index - notZeroBeginIndex + bufferLength) % bufferLength)
					% timeStep == 0) {
				classify();
				alreadyClassify = true;
			}

			

		}*/



	}
	private void classifyLeft(Boolean possibleToContiue){
		int deltaIndex = (maxIndexLeft - notZeroBeginIndexLeft + bufferLength)
				% bufferLength;
		System.out.println("max Index left "+maxIndexLeft);
		int distanceFromLastCommand= (maxIndexLeft - lastCommandLeftIndex+bufferLength) % bufferLength;
		if(distanceFromLastCommand < 10 ){
			System.out.println("distace left "+distanceFromLastCommand+" lastcommd "+lastCommandLeftIndex);
			return;
		}
		 distanceFromLastCommand=(maxIndexLeft - lastCommandRightIndex+bufferLength)% bufferLength;
		if(distanceFromLastCommand<5){
			System.out.println("distance left from right "+distanceFromLastCommand + " last command right "+lastCommandRightIndex);
			return;
		}
		if ((value[maxIndexLeft] > STRONG_FORCE_THRESHOLD
				&& deltaIndex <= STRONG_TIME_THRESHOLD) ){
//				|| (value[maxIndexLeft]>=3.29 && !alreadyClassify)){
//				&& deltaIndex>1) {
			command.index=maxIndexLeft;
			lastCommandLeftIndex=maxIndexLeft;
			command.left=true;
			command.currentCommand= Command.STRONG_SHORT_FORCE;
			currentState = STRONG_SHORT_FORCE;
			System.out.println("be strong short " + value[maxIndexLeft]
					+ " at index " + maxIndexLeft);
		} else if (//value[maxIndexLeft] < NORMAL_FORCE_UPPER_BOUND_THRESHOLD
				 value[maxIndexLeft] > NORMAL_FORCE_LOWER_BOUND_THRESHOLD
				&& deltaIndex <= NORMAL_TIME_THRESHOLD
				&& !alreadyClassify){
//				&& deltaIndex>1) {
			command.index=maxIndexLeft;
			lastCommandLeftIndex=maxIndexLeft;
			command.left=true;
			command.currentCommand= Command.NORMAL_SHORT_FORCE;
			currentState = NORMAL_SHORT_FORCE;
			System.out.println("be normal short " + value[maxIndexLeft]
					+ " at index " + maxIndexLeft);
		}else if(possibleToContiue && value[maxIndexLeft]<1){
			notZeroBeginIndexLeft = index;
			refresh = true;
		}
	}
	private void classifyRight(Boolean possibleToContiue){
		int deltaIndex = (maxIndexRight - notZeroBeginIndexRight + bufferLength)
				% bufferLength;
		System.out.println("max Index right "+maxIndexRight+ " deltaIndex "+deltaIndex+ " valueinver "+valueInver[maxIndexRight]);
		int distanceFromLastCommand= (maxIndexRight - lastCommandRightIndex+bufferLength) % bufferLength;
		if(distanceFromLastCommand < 10 ){
			System.out.println("distace right "+distanceFromLastCommand+" lastcommd "+lastCommandRightIndex);
			return;
		}
		distanceFromLastCommand=(maxIndexRight - lastCommandLeftIndex+bufferLength)% bufferLength;
		if(distanceFromLastCommand<5){
			System.out.println("distance right from left "+distanceFromLastCommand + " last command right "+lastCommandLeftIndex);
			return;
		}
		if ((valueInver[maxIndexRight] > STRONG_FORCE_THRESHOLD
				&& deltaIndex <= STRONG_TIME_THRESHOLD)){
				//||(valueInver[maxIndexRight]>=3.29 && !alreadyClassify)){
//				&& deltaIndex>1) {
			command.index=maxIndexRight;
			lastCommandRightIndex=maxIndexRight;
			command.left=false;
			command.currentCommand= Command.STRONG_SHORT_FORCE;
			currentState = STRONG_SHORT_FORCE;
			System.out.println("be strong short " + valueInver[maxIndexRight]
					+ " at index " + maxIndexRight);
		} else if (//valueInver[maxIndexRight] < NORMAL_FORCE_UPPER_BOUND_THRESHOLD
				 valueInver[maxIndexRight] > NORMAL_FORCE_LOWER_BOUND_THRESHOLD
				&& deltaIndex <= NORMAL_TIME_THRESHOLD
				&& !alreadyClassify){ 
//				&& deltaIndex>1) {
			command.index=maxIndexRight;
			lastCommandRightIndex=maxIndexRight;
			command.left=false;
			command.currentCommand= Command.NORMAL_SHORT_FORCE;
			currentState = NORMAL_SHORT_FORCE;
			System.out.println("be normal short " + valueInver[maxIndexRight]
					+ " at index " + maxIndexRight);
		} else if(possibleToContiue && valueInver[maxIndexRight]<1){
			notZeroBeginIndexRight = index;
			refresh = true;
		}
	}
	public void resetClassify(){
		maxIndexLeft=-1;
		maxIndexRight=-1;
	}

	private boolean downgradeSlope(short index, short range) {
		boolean returnValue = true;
		short temp = 0;
		short currentIndex = index;
		short previousIndex;
		while (temp < range) {
			previousIndex = (short) ((currentIndex - 1 + bufferLength) % bufferLength);
			if (value[currentIndex] > value[previousIndex]) {
				returnValue = false;
				break;
			}
			currentIndex = previousIndex;
			temp++;
		}
		return returnValue;

	}
	public byte getCurrentDirection(){
		return currentDirection;
	}
	public int getLength() {
		return bufferLength;
	}

	public float[] getBuffer() {
		return value;
	}

	public float[] getBufferInver() {
		return valueInver;
	}
}
