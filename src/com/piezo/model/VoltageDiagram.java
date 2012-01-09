package com.piezo.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.piezo.util.Command;

public class VoltageDiagram {
	final float MAX_VALUE=4;
	final short ARRAYLENGTH=200;
	float x,y,width,height;
	Mesh lineMesh,lineMeshInver;
	Mesh lineMeshUnit,lineMeshInverUnit;
	private byte [] maxValue,maxValueInver;
	float [] lineVertices,lineVerticesInver,lineVerticesUnit,lineVerticesInverUnit;
//	private byte [] maxValue,maxValueInver;
	int vertexIndex=0;
	float incrementX,currentX;
	ImmediateModeRenderer10 renderer;

	public VoltageDiagram (float x, float y, float width, float height){
//		lineMesh = new Mesh(false,200,0,new VertexAttribute(Usage.Position, 2, "a_pos"));
//			,new VertexAttribute(Usage.ColorPacked, 4, "a_color") );
//		lineMeshInver = new Mesh(false,200,0,new VertexAttribute(Usage.Position, 2, "a_pos"));
//			,new VertexAttribute(Usage.ColorPacked, 4, "a_color") );
//		lineMeshUnit =new Mesh(false,6,0,new VertexAttribute(Usage.Position, 2, "a_pos") );
//		lineMeshInverUnit = new Mesh(false,6,0,new VertexAttribute(Usage.Position, 2, "a_pos") );
		lineVertices = new float[400];
		lineVerticesInver = new float [400];
		lineVerticesUnit = new float[32];
		maxValue=new byte[200];
		maxValueInver=new byte[200];

		renderer = new ImmediateModeRenderer10();
		this.x = x;
		this.y = y;
		this.width= width;
		this.height= height;
		incrementX= (float)width/ARRAYLENGTH;
		currentX=x;
	

		initUnitLineVertices();
		System.out.println(" in the voltage diagram construcure ");
	}
	public void render(){
		renderer.begin(GL10.GL_LINES);
		for(int i=0;i<lineVerticesUnit.length/2;i++){
			renderer.color(0.74f, 0.74f, 0.74f, 1f);
			renderer.vertex(lineVerticesUnit[2*i],lineVerticesUnit[2*i+1],0);
		}
		renderer.end();

		if(vertexIndex >=4){
			renderer.begin(GL10.GL_LINE_STRIP);
			for(int i=0;i<vertexIndex/2;i++){
				if(maxValue[i]==Command.NORMAL_SHORT_FORCE){
					renderer.color(0f, 0f, 1f, 1f);
				}else if(maxValue[i]==Command.STRONG_SHORT_FORCE)
					renderer.color(0f, 1f, 0f, 1f);
				else
					renderer.color(1f, 0f, 0f, 1f);
				renderer.vertex(lineVertices[2*i],lineVertices[2*i+1],0);
				
			}
			renderer.end();
			renderer.begin(GL10.GL_LINE_STRIP);
			for(int i=0;i<vertexIndex/2;i++){
				if(maxValueInver[i]==Command.NORMAL_SHORT_FORCE){
					renderer.color(0f, 0f, 1f, 1f);
				}else if(maxValueInver[i]==Command.STRONG_SHORT_FORCE)
					renderer.color(0f, 1f, 0f, 1f);
				else
					renderer.color(1f, 0f, 0f, 1f);
				renderer.vertex(lineVerticesInver[2*i],lineVerticesInver[2*i+1],0);
				
			}
			renderer.end();

		}
		
		
		
	}
	public void initUnitLineVertices(){
		for(int i=0;i<4;i++){
			lineVerticesUnit[i*4]=x;
			lineVerticesUnit[i*4+1]= height*2/3-50+i*(height /3)/MAX_VALUE;
			lineVerticesUnit[i*4+2]=x+width;
			lineVerticesUnit[i*4+3]= height*2/3-50+ i* (height /3)/MAX_VALUE;
		}
		for(int i=4;i<8;i++){
			lineVerticesUnit[i*4]=x;
			lineVerticesUnit[i*4+1]= 50+(i-4)*(height /3)/MAX_VALUE;
			lineVerticesUnit[i*4+2]=x+width;
			lineVerticesUnit[i*4+3]= 50+ (i-4)* (height /3)/MAX_VALUE;
		}
		
	}
	public void addVertex(float value,float valueInver,Command command){
		
		if(vertexIndex>=400){
			vertexIndex=0;
			currentX=x;
		}
		maxValue[vertexIndex/2]=0;
		maxValueInver[vertexIndex/2]=0;
		if(command.currentCommand == Command.NORMAL_SHORT_FORCE || command.currentCommand == Command.STRONG_SHORT_FORCE){
			if(command.left){
				if(command.currentCommand==Command.STRONG_SHORT_FORCE)
					maxValue[command.index]=1;
				else if(command.currentCommand==Command.NORMAL_SHORT_FORCE)
					maxValue[command.index]=2;
			}
			else {
				if(command.currentCommand==Command.STRONG_SHORT_FORCE)
					maxValueInver[command.index]=1;
				else if(command.currentCommand==Command.NORMAL_SHORT_FORCE)
					maxValueInver[command.index]=2;

			}
		}
		Float val = (float)(value/MAX_VALUE) * (height /3);
		Float valInver = (float)(valueInver/MAX_VALUE)*(height/3);
		currentX+=incrementX;
		lineVertices[vertexIndex]= currentX;
		lineVerticesInver[vertexIndex++]=currentX;
		lineVertices[vertexIndex]= height*2/3-50+val;
//		System.out.println("current x "+currentX+ " y "+height*2/3+val+" inv "+50+valInver);
		lineVerticesInver[vertexIndex++]=50+valInver;

	}
}
