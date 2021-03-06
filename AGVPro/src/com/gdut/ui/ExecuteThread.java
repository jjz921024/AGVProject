package com.gdut.ui;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gdut.net.FrontMesPack;
import com.gdut.net.MessagePackage;
import com.gdut.ui.CarFrame;
import com.gdut.util.ParseMesPack;

public class ExecuteThread extends Thread{

	public static boolean flag = true;
	
	private OutputStream carOutputStream;
	
	public ExecuteThread(OutputStream carOutputStream) {
		super();
		this.carOutputStream = carOutputStream;
	}

	@Override
	public void run(){
		
		/*while(flag){*/
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
						
						FrontMesPack frontMesPack = null;
						//处理 直线
						if(element.getName().equals("line")){		
							frontMesPack = new FrontMesPack(Integer.parseInt(element.attributeValue("dir")), 
																		 FrontMesPack.LINE, 
																		 Integer.parseInt(element.attributeValue("distance")), 
																		 Integer.parseInt(element.attributeValue("speed")), 
																		 -1, -1);
							
							MessagePackage resultMes = ParseMesPack.parse(frontMesPack);
							byte[] buf = resultMes.creatMesByte();
							carOutputStream.write(buf, 0, buf.length);
							// 值大于127时，会出错
							// getTime得到的是int型的time值， buf[6]得到的是byte类型的time，有可能溢出成为负数
							Thread.sleep(resultMes.getTime() * 1000);
							System.out.println(resultMes.getTime() * 1000);
							
						} else if(element.getName().equals("round")){
							frontMesPack = new FrontMesPack(Integer.parseInt(element.attributeValue("dir")), 
																		 FrontMesPack.ROUND, 
																		 -1, 
																		 Integer.parseInt(element.attributeValue("speed")), 
																		 Integer.parseInt(element.attributeValue("radius")), 
																		 Integer.parseInt(element.attributeValue("angle")));
							MessagePackage resultMes = ParseMesPack.parse(frontMesPack);
							byte[] buf = resultMes.creatMesByte();
							carOutputStream.write(buf, 0, buf.length);
							System.out.println(resultMes.getTime() * 1000);
							Thread.sleep(resultMes.getTime() * 1000);
							
																
						} else if(element.getName().equals("rotate")){
							frontMesPack = new FrontMesPack(Integer.parseInt(element.attributeValue("dir")), 
																		 FrontMesPack.ROTATE, 
																		 -1, 
																		 Integer.parseInt(element.attributeValue("speed")), 
																		 1, //旋转时，车身的半径长
																		 Integer.parseInt(element.attributeValue("angle")));
							
							MessagePackage resultMes = ParseMesPack.parse(frontMesPack);
							byte[] buf = resultMes.creatMesByte();
							carOutputStream.write(buf, 0, buf.length);
							System.out.println(resultMes.getTime() * 1000);
							Thread.sleep(resultMes.getTime() * 1000);
							
							
						} else if (element.getName().equals("stop")) {
							//程序暂停相应秒数
							System.out.println(Calendar.getInstance().getTime());
							//停止时也要发相应的包
							frontMesPack = new FrontMesPack(-1, 
															FrontMesPack.STOP, 
															-1, 
															0,  //速度为0
															-1, //旋转时，车身的半径长
															-1);
							MessagePackage resultMes = ParseMesPack.parse(frontMesPack);
							byte[] buf = resultMes.creatMesByte();
							carOutputStream.write(buf, 0, buf.length);
							
							Thread.sleep(Integer.parseInt(element.attributeValue("time")) * 1000);
							System.out.println(Calendar.getInstance().getTime());
						
									
							
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
				
				
				
				
			} catch (DocumentException | IOException | NumberFormatException | InterruptedException e1) {
				e1.printStackTrace();
			} finally {
				JOptionPane.showMessageDialog(CarFrame.executeButton, "执行完毕", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
			/*flag = false;
		}	*/
	}
}


