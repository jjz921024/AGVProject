package com.gdut.net;

/*
 * 信息包   10个字节的字符流 
 * 格式：
 * 	第1、2位 ---- 起始位
 *  第3位       ---- 模式选择   (暂时没用)
 *  第4位       ---- 方向    (由delta值决定，delta可正可负)
 *  第5、6位 ---- 速度   16位
 *  第7位       ---- 时间    暂时代替距离
 *  第8、9位 ---- 预留
 *  第10位     ---- 结束位
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





