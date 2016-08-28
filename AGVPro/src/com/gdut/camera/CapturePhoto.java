package com.gdut.camera;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.xml.crypto.Data;

import com.gdut.ui.CarFrame;


public class CapturePhoto extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JButton captureButton;
	private JButton savaButton;
	private JLabel photo;
	
	/**
	 * Create the frame.
	 */
	public CapturePhoto() {
		setTitle("\u6444\u50CF\u76D1\u63A7");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300); //450,300
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(panel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
		);
		
		
		
		captureButton = new JButton("\u62CD\u7167");
		captureButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					/*
					 * 拍照按钮事件：通过9191端口发送一个字节流，拍照指令
					 * 		创建socket(端口9999)，
					 * 		ARM接收确认后，发送图片
					 * 		socket接收图片，存至数组
					 * */
					
					OutputStream captureOutputStream = CarFrame.carSocket.getOutputStream();
					
					byte[] cmd_buf = new byte[]{(byte)0x66};
					captureOutputStream.write(cmd_buf);
					
					
					Socket imgSocket = new Socket(InetAddress.getByName(CarFrame.ipText.getText()), 9999);			
					InputStream imgInputStream = imgSocket.getInputStream();
							
					byte[] buf = new byte[1024 * 1024 * 3];
					
					int data = 0;
					int count = 0;
					while ((data = imgInputStream.read()) != -1) {			
						buf[count] = (byte) data;
						count++;
					}
					
					System.out.println("finish!");
					ImageIcon image = new ImageIcon(buf);
					image.setImage(image.getImage().getScaledInstance(photo.getWidth(), photo.getHeight(), Image.SCALE_DEFAULT));
					photo.setIcon(image);
					
					imgInputStream.close();
					imgSocket.close();
					
					//captureOutputStream.close();   //关闭9191端口的socket的输出流后，整个socket都会关闭
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			    
			}
		});
		captureButton.setVerticalAlignment(SwingConstants.TOP);
		
		
		
		
		
		savaButton = new JButton("\u4FDD\u5B58");
		savaButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				FileDialog saveDialog = new FileDialog(CapturePhoto.this, "保存图像", FileDialog.SAVE);
				saveDialog.setVisible(true);
				String savepath = saveDialog.getDirectory() + saveDialog.getFile();
				
				if(saveDialog.getDirectory() != null && saveDialog.getFile() != null){ //保存路径不为空时，才复制
					
					FileInputStream fileInputStream = null;
					FileOutputStream fileOutputStream = null;
					try {
						fileInputStream = new FileInputStream(new File("D:/JAVA程序/eclipse/AGVPro/src/temp.jpg"));
						fileOutputStream = new FileOutputStream(new File(savepath));
						
						int data;
						while ((data = fileInputStream.read()) != -1) {
							fileOutputStream.write(data);
						}
						
						
						
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} finally {
						if(fileInputStream != null){
							try {
								fileInputStream.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (fileOutputStream != null) {
							try {
								fileOutputStream.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(272, Short.MAX_VALUE)
					.addComponent(captureButton)
					.addGap(18)
					.addComponent(savaButton)
					.addGap(20))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(captureButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(savaButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		
		photo = new JLabel("");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(photo, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(5)
					.addComponent(photo, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
					.addGap(0, 0, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}
}
