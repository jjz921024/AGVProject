package com.gdut.ui;

import java.io.File;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.gdut.ui.CarFrame;

public class ExecuteThread extends Thread{

	public static boolean flag = true;
	
	@Override
	public void run(){
		
		while(flag){
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new File(CarFrame.loadTextField.getText()));
				Element rootElement = document.getRootElement();
				
				if(rootElement.getName().equals("route")){
					//获取根节点的迭代器
					Iterator<Element> itElement = rootElement.elementIterator();
					while (itElement.hasNext()) {
						//获取一个节点名和其所有属性
						Element element = itElement.next();
						
						//处理 直线和 转弯的情况
						if(element.getName().equals("line") || element.getName().equals("round")){					
							/*byte[] buf = new MessagePackage().creatMes((byte) 0x01,
									(byte) Integer.parseInt(element.attributeValue("delta")),
									Integer.parseInt(element.attributeValue("speed")),
									Integer.parseInt(element.attributeValue("time")));*/
							
						} else if (element.getName().equals("stop")) {
							//程序暂停相应秒数
							try {
								System.out.println(Calendar.getInstance().getTime());
								Thread.sleep(Integer.parseInt(element.attributeValue("time")) * 1000);
								System.out.println(Calendar.getInstance().getTime());
								
							} catch (NumberFormatException | InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							
						} else if (element.getName().equals("return")) {
							
							//获得回滚标签的id
							String id = element.attributeValue("id");
							//一个回滚标签只有效一次
							rootElement.remove(element);
							
							//重新遍历xml,重新获得一个迭代器
							itElement = rootElement.elementIterator();
							
							//找到对应标记，从标记处开始执行 
							while (itElement.hasNext()) {
								element = itElement.next();					
								if (element.getName().equals("mark") && element.attributeValue("id").equals(id)) {	
									break;
								}
							}	
						}		
					}
									
				} else {
					//根节点不为 route 提示错误
					JOptionPane.showMessageDialog(CarFrame.executeButton, "文件格式不符", "警告", JOptionPane.WARNING_MESSAGE);
				}
				
				
				
				
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				JOptionPane.showMessageDialog(CarFrame.executeButton, "执行完毕", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
			flag = false;
		}	
	}
}


