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
			
			//�ֶ�ģʽ��  �����ת��Ҫ��ת��뾶
			if(intputMes.getDir() == FrontMesPack.LEFT || intputMes.getDir() == FrontMesPack.RIGHT){
				//todo ���ֲ�����ת��뾶֮��ĺ�����ϵδ֪
				resultMes.setDelta(intputMes.getRadius());
			
			}
		}
		
		
		//����·��ʱ�Ľ�������
		//ֱ��   �轫·��ת����ʱ��
		else if(intputMes.getType() == FrontMesPack.LINE){
			resultMes.setStatus(1);
			resultMes.setSpeed(intputMes.getSpeed());
			resultMes.setDir(intputMes.getDir());
			if(intputMes.getSpeed() != 0){
				int time = intputMes.getDistance() / intputMes.getSpeed();
				resultMes.setTime(time);
			}
		}
		 
		//ת��      �轫ת��뾶��ת�� ת����ʱ���ת�ٲ�            
		//��ת 
		else if(intputMes.getType() == FrontMesPack.ROUND || intputMes.getType() == FrontMesPack.ROTATE){
			resultMes.setStatus(1);
			resultMes.setSpeed(intputMes.getSpeed());
			resultMes.setDir(intputMes.getDir());
			if(intputMes.getSpeed() != 0){
				int time = intputMes.getAngle() * intputMes.getRadius() / intputMes.getSpeed();
				resultMes.setTime(time);
			}
			
			//������תʱ����Ҫ��ת��뾶
			if(intputMes.getType() != FrontMesPack.ROTATE){
				//todo ���ֲ�����ת��뾶֮��ĺ�����ϵδ֪
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
