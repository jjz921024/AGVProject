package com.gdut.net;

public class FrontMesPack {
	
	//����
	private int dir;
		
	/*
	 * ·������ 
	 * 1--ֱ��  	2--ת��  	3--��ת  	4--ֹͣ
	 */
	private int type;
		
	//·��   ��λm
	//·��Ϊ-1 ��ʾû�õ�
	private int distance;
		
	//�ٶ�    ���ٶ�  ��λ m/s
	private int speed;
	
	//ת��뾶
	private int radius;
	
	//ת��
	private int angle;
	
	//����    ����Parse����   dir
	public static final int  UP = 1;
	public static final int  DOWN = 2;
	public static final int  LEFT = 3;
	public static final int  RIGHT = 4;
	public static final int  STOP_MANUAL = 5;
	
	//·������    type
	public static final int  LINE = 1;
	public static final int  ROUND = 2;
	public static final int  ROTATE = 3;
	public static final int  STOP = 4;   //����·��ʱ�õ�
		//��ť�ֶ�
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
