package com.gdut.net;

public class FrontMesPack {
	
	//方向
	private int dir;
		
	/*
	 * 路径类型 
	 * 1--直线  	2--转弯  	3--旋转  	4--停止
	 */
	private int type;
		
	//路程   单位m
	//路程为-1 表示没用到
	private int distance;
		
	//速度    线速度  单位 m/s
	private int speed;
	
	//转弯半径
	private int radius;
	
	//转角
	private int angle;
	
	//方向    帮助Parse解析   dir
	public static final int  UP = 1;
	public static final int  DOWN = 2;
	public static final int  LEFT = 3;
	public static final int  RIGHT = 4;
	public static final int  STOP_MANUAL = 5;
	
	//路径类型    type
	public static final int  LINE = 1;
	public static final int  ROUND = 2;
	public static final int  ROTATE = 3;
	public static final int  STOP = 4;   //配置路径时用到
		//按钮手动
	public static final int  START = 5;
	public static final int  CLOSE = 6;
	public static final int  MANUAL = 7;

	public FrontMesPack(int dir, int type, int distance, int speed, int radius, int angle) {
		this.dir = dir;
		this.type = type;
		this.distance = distance;
		this.speed = speed;
		this.radius = radius;
		this.angle = angle;
	}


	
	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	

}
