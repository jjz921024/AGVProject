package com.gdut.net;

/*
 * ��Ϣ��   10���ֽڵ��ַ��� 
 * ��ʽ��
 * 	��1��2λ ---- ��ʼλ
 *  ��3λ       ---- ģʽѡ��   (��ʱû��)
 *  ��4λ       ---- ����    (��deltaֵ������delta�����ɸ�)
 *  ��5��6λ ---- �ٶ�   16λ
 *  ��7λ       ---- ʱ��    ��ʱ�������
 *  ��8��9λ ---- Ԥ��
 *  ��10λ     ---- ����λ
 * 
 * */
public class MessagePackage {
	
	private byte[] mes = new byte[]{(byte)0xaa, (byte)0xbb, 0, 0, 0, 0, 0, 0, 0, (byte)0xff};
	
	public byte[] creatMes(byte mod, byte dir, int speed, int time){
		mes[2] = mod;
		mes[3] = dir;	
		
		mes[4] = (byte)(speed / 256);
		mes[5] = (byte)(speed % 256);
		
		mes[6] = (byte)time;
		
		return mes;		
	}
}





