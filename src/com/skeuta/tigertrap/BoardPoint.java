package com.skeuta.tigertrap;

import android.graphics.PointF;
import android.graphics.RectF;

public class BoardPoint {
	private PointF point;
	private RectF region;
	private int neighbors[];
	private boolean isOccupied;
	
	public BoardPoint(PointF Point, int Neighbors[]) {
		point = Point;
		region = new RectF(point.x - 50, point.y - 50, point.x+50, point.y+50);//a small rect area around each board point
		neighbors = new int[Neighbors.length];
		System.arraycopy(Neighbors, 0, neighbors, 0, Neighbors.length);// copying input Neighbors[] into a diff local array neighbors[]
		isOccupied = false;
	}
	// if the coin is inside the rect border
	public boolean touchContains(PointF touchPoint){
		return region.contains(touchPoint.x,touchPoint.y);
	}
	//to check if the coin moved to a position is a neighbor of the coin from which it is moved from
	public boolean neighborCheck(int index){
		for(int i=0; i<neighbors.length; i++){
			if(index == neighbors[i] )
				return true;
		}
		return false;
	}
	
	public PointF getBoardPoint(){
		return point;
	}
	
	public void setIsOccupied(boolean value){
		isOccupied = value;
	}
	
	public boolean getIsOccupied(){
		return isOccupied;
	}
}
