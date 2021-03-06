package com.gdut.net;

/*
 * 信息包   10个字节的字符流 
 * 格式：
 * 	第1、2位 ---- 起始位
 *  第3位       ---- 方向
 *  第4、5位 ---- 速度    
 *  第6、7位 ---- delta
 *  第8位   ----   拍照
 *  第9位    ----   状态
 *  第10位     ---- 结束位
 * 
 * */
public class MessagePackage {
	
	//起始位
	private byte start1 = 'g';
	private byte start2 = 'h';
	
	//方向
	private int dir;
	
	//两轮差速  用来控制方向和转弯半径
	private int delta;
	
	//速度    线速度  单位 m/s  
	private int speed;
	
	//拍照指令位
	private int capturePhoto;
	
	//运行时间，供上位机使用，并不发送到ARM上
	private int time;
	
	private int status;
	
	//结束位
	private byte end = 'j';
	
	
	public byte[] creatMesByte(){
		
		byte[] mes = new byte[]{start1, start2, 0, 0, 0, 0, 0, 0, 0, end};
		
		mes[2] = (byte) dir;
		
		mes[3] = (byte) (speed / 256);
		mes[4] = (byte) (speed % 256);
		
		//是否需要2个字节
		mes[5] = (byte) (delta / 256);
		mes[6] = (byte) (delta % 256);
				
		mes[7] = (byte) capturePhoto;
		
		mes[8] = (byte) status;
		
		return mes;		
	}


	
	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getCapturePhoto() {
		return capturePhoto;
	}

	public void setCapturePhoto(int capturePhoto) {
		this.capturePhoto = capturePhoto;
	}

	public byte getStart1() {
		return start1;
	}


	public void setStart1(byte start1) {
		this.start1 = start1;
	}


	public byte getStart2() {
		return start2;
	}


	public void setStart2(byte start2) {
		this.start2 = start2;
	}


	public int getDir() {
		return dir;
	}


	public void setDir(int dir) {
		this.dir = dir;
	}


	public int getDelta() {
		return delta;
	}


	public void setDelta(int delta) {
		this.delta = delta;
	}


	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public byte getEnd() {
		return end;
	}


	public void setEnd(byte end) {
		this.end = end;
	}
	
	
}





