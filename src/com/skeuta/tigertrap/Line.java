package com.skeuta.tigertrap;

public class Line {
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	
	public void setLineInfo(float StartX, float StartY, float EndX, float EndY) {
		startX = StartX;
		startY = StartY;
		endX = EndX;
		endY = EndY;
	}
	public float getStartX(){
		return startX;
	}
	public float getStartY(){
		return startY;
	}
	public float getEndX(){
		return endX;
	}
	public float getEndY(){
		return endY;
	}
}
