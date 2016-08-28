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
					//��ȡ���ڵ�ĵ�����
					Iterator<Element> itElement = rootElement.elementIterator();
					while (itElement.hasNext()) {
						//��ȡһ���ڵ���������������
						Element element = itElement.next();
						
						//���� ֱ�ߺ� ת������
						if(element.getName().equals("line") || element.getName().equals("round")){					
							/*byte[] buf = new MessagePackage().creatMes((byte) 0x01,
									(byte) Integer.parseInt(element.attributeValue("delta")),
									Integer.parseInt(element.attributeValue("speed")),
									Integer.parseInt(element.attributeValue("time")));*/
							
						} else if (element.getName().equals("stop")) {
							//������ͣ��Ӧ����
							try {
								System.out.println(Calendar.getInstance().getTime());
								Thread.sleep(Integer.parseInt(element.attributeValue("time")) * 1000);
								System.out.println(Calendar.getInstance().getTime());
								
							} catch (NumberFormatException | InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							
						} else if (element.getName().equals("return")) {
							
							//��ûع���ǩ��id
							String id = element.attributeValue("id");
							//һ���ع���ǩֻ��Чһ��
							rootElement.remove(element);
							
							//���±���xml,���»��һ��������
							itElement = rootElement.elementIterator();
							
							//�ҵ���Ӧ��ǣ��ӱ�Ǵ���ʼִ�� 
							while (itElement.hasNext()) {
								element = itElement.next();					
								if (element.getName().equals("mark") && element.attributeValue("id").equals(id)) {	
									break;
								}
							}	
						}		
					}
									
				} else {
					//���ڵ㲻Ϊ route ��ʾ����
					JOptionPane.showMessageDialog(CarFrame.executeButton, "�ļ���ʽ����", "����", JOptionPane.WARNING_MESSAGE);
				}
				
				
				
				
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				JOptionPane.showMessageDialog(CarFrame.executeButton, "ִ�����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			}
			flag = false;
		}	
	}
}


