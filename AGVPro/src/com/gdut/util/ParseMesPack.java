package com.gdut.util;

import com.gdut.net.FrontMesPack;
import com.gdut.net.MessagePackage;

public class ParseMesPack {
	
	public static MessagePackage parse(FrontMesPack intputMes){
		
		MessagePackage resultMes = new MessagePackage();
		
		
		if(intputMes.getType() == FrontMesPack.MANUAL){
			resultMes.setStatus(1);
			resultMes.setTime(1);
			resultMes.setDir(intputMes.getDir());
			
			if(intputMes.getDir() != FrontMesPack.STOP_MANUAL){
				resultMes.setSpeed(intputMes.getSpeed());
			}
			
			//手动模式下  如果是转弯要设转弯半径
			if(intputMes.getDir() == FrontMesPack.LEFT || intputMes.getDir() == FrontMesPack.RIGHT){
				//todo 两轮差速与转弯半径之间的函数关系未知
				resultMes.setDelta(intputMes.getRadius());
			
			}
		}
		
		
		//配置路径时的解析规则
		//直线   需将路程转化成时间
		else if(intputMes.getType() == FrontMesPack.LINE){
			resultMes.setStatus(1);
			resultMes.setSpeed(intputMes.getSpeed());
			resultMes.setDir(intputMes.getDir());
			if(intputMes.getSpeed() != 0){
				int time = intputMes.getDistance() / intputMes.getSpeed();
				resultMes.setTime(time);
			}
		}
		 
		//转弯      需将转弯半径和转角 转化成时间和转速差            
		//旋转 
		else if(intputMes.getType() == FrontMesPack.ROUND || intputMes.getType() == FrontMesPack.ROTATE){
			resultMes.setStatus(1);
			resultMes.setSpeed(intputMes.getSpeed());
			resultMes.setDir(intputMes.getDir());
			if(intputMes.getSpeed() != 0){
				int time = intputMes.getAngle() * intputMes.getRadius() / intputMes.getSpeed();
				resultMes.setTime(time);
			}
			
			//不是旋转时，还要求转弯半径
			if(intputMes.getType() != FrontMesPack.ROTATE){
				//todo 两轮差速与转弯半径之间的函数关系未知
				resultMes.setDelta(intputMes.getRadius());		
			}			
		} 
		
		else if (intputMes.getType() == FrontMesPack.CLOSE) {
			resultMes.setStatus(0);
			resultMes.setSpeed(intputMes.getSpeed());
			
		} 
		
		else if(intputMes.getType() == FrontMesPack.START){
			resultMes.setStatus(1);
		}
		
		
		return resultMes;
	}
}
