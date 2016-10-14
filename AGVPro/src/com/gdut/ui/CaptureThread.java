package com.gdut.ui;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.ImageIcon;

import com.gdut.camera.CapturePhoto;
import com.gdut.net.FrontMesPack;
import com.gdut.net.MessagePackage;
import com.gdut.util.ParseMesPack;

public class CaptureThread extends Thread {

	public static byte[] buf = new byte[3 * 1024 * 1024];;
	
	@Override
	public void run() {
		
		try {
			/*
			 * ���հ�ť�¼���ͨ��9191�˿ڷ���һ���ֽ���������ָ��
			 * 		����socket(�˿�9999)��
			 * 		ARM����ȷ�Ϻ󣬷���ͼƬ
			 * 		socket����ͼƬ����������
			 * */		
			OutputStream captureOutputStream = CarFrame.carSocket.getOutputStream();
			
	
			MessagePackage capturePack = new MessagePackage();
			capturePack.setCapturePhoto(1);
			captureOutputStream.write(buf, 0, buf.length);					
			
			Socket imgSocket = new Socket(InetAddress.getByName(CarFrame.ipText.getText()), 9999);			
			InputStream imgInputStream = imgSocket.getInputStream();
			BufferedInputStream imgBufInputStream = new BufferedInputStream(imgInputStream);
			
			int data = 0;
			int count = 0;
			while ((data = imgBufInputStream.read()) != -1) {			
				buf[count] = (byte) data;
				count++;
			}		
			
			
			System.out.println("finish!");
			ImageIcon image = new ImageIcon(buf);
			image.setImage(image.getImage().getScaledInstance(CapturePhoto.photo.getWidth(), CapturePhoto.photo.getHeight(), Image.SCALE_DEFAULT));
			CapturePhoto.photo.setIcon(image);
			
			imgInputStream.close();
			imgSocket.close();
			
			//captureOutputStream.close();   //�ر�9191�˿ڵ�socket�������������socket����ر�
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
