package com.piezo.model;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.piezo.util.Command;

public class VoltageDiagram {
	final float MAX_VALUE=4;
	final short ARRAYLENGTH=200;
	float x,y,width,height;
	Mesh lineMesh,lineMeshInver;
	float [] lineVertices,lineVerticesInver;
//	private byte [] maxValue,maxValueInver;
	int vertexIndex=0;
	float incrementX,currentX;
//	Pixmap pixmap;
//	Texture texture;
	public VoltageDiagram (float x, float y, float width, float height){
		lineMesh = new Mesh(false,200,0,new VertexAttribute(Usage.Position, 2, "a_pos") );
		lineMeshInver = new Mesh(false,200,0,new VertexAttribute(Usage.Position, 2, "a_pos") );
		lineVertices = new float[400];
		lineVerticesInver = new float [400];
		this.x = x;
		this.y = y;
		this.width= width;
		this.height= height;
		incrementX= (float)width/ARRAYLENGTH;
		currentX=x;
	
//		for(int i=0;i<100;i++){
//			this.addVertex(3);
//		}
//		for(int i=0;i<100;i++){
//			this.addVertex(2);
//		}
	}
	public void render(SpriteBatch spriteBatch){
//		pixmap =new Pixmap((int)width,(int)height,Pixmap.Format.RGBA8888);
	
		if(vertexIndex >=4){
			lineMesh.render(GL10.GL_LINE_STRIP);
			lineMeshInver.render(GL10.GL_LINE_STRIP);
		}
		
		
		
	}
	public void addVertex(float value,float valueInver){
		
		if(vertexIndex>=400){
			vertexIndex=0;
			currentX=x;
		}
		
		Float val = (float)(value/MAX_VALUE) * (height /3);
		Float valInver = (float)(valueInver/MAX_VALUE)*(height/3);
		currentX+=incrementX;
		lineVertices[vertexIndex]= currentX;
		lineVerticesInver[vertexIndex++]=currentX;
		lineVertices[vertexIndex]= height*2/3+val;
		lineVerticesInver[vertexIndex++]=50+valInver;
		if(vertexIndex % 5 ==0){
			lineMesh.setVertices(lineVertices,0,vertexIndex);
		
			lineMeshInver.setVertices(lineVerticesInver,0,vertexIndex);
		}
	}
}
