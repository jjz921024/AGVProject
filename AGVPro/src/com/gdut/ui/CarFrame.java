package com.gdut.ui;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.gdut.camera.CapturePhoto;
import com.gdut.net.FrontMesPack;
import com.gdut.util.ParseMesPack;

public class CarFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JButton ipConnButton;
	private JButton ipDisconnButton;
	private JCheckBox keyCtrl;
	private JTextField speedText;
	private JButton exitButton;
	private JButton upButton;
	private JButton leftButton;
	private JButton stopButton;
	private JButton downButton;
	private JButton rightButton;
	public static JTextField ipText;
	public static Socket carSocket;
	private OutputStream carOutputStream;
	private ImageIcon ipIconR = new ImageIcon(this.getClass().getResource("/image/RedLight.png"));  
	private ImageIcon ipIconG = new ImageIcon(this.getClass().getResource("/image/GreenLight.png"));	//"./src/image/GreenLight.png"
	private JLabel ipFlag = new JLabel(ipIconR);
	private JButton loadButton;
	public static JTextField loadTextField;
	public static JButton executeButton;
	private Thread configThread;
	private JTextField radiusText;
	private JLabel speedSetText;
	private int dir_last = 0;


	/**
	 * Create the frame.
	 */
	public CarFrame() {
		setTitle("AGV\u4E0A\u4F4D\u673A");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 435, 343);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	
		ipIconR.setImage(ipIconR.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
		ipIconG.setImage(ipIconG.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
		
		ipConnButton = new JButton("\u8FDE\u63A5");
		
		/**
		 *  连接socket，拿到OutputStream
		 */
		ipConnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {	
					
					carSocket = new Socket(InetAddress.getByName(ipText.getText()), 9191);
					if(!carSocket.isClosed()){

						carOutputStream = carSocket.getOutputStream();
						FrontMesPack frontMesPack = new FrontMesPack(-1, FrontMesPack.START, -1, 0, -1, -1);
						byte[] buf = ParseMesPack.parse(frontMesPack).creatMesByte();
						carOutputStream.write(buf, 0, buf.length);
						
						ipFlag.setIcon(ipIconG);							
					}				
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(contentPane, e1.toString()+e1.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);		
				}		
			}
		});

		/**
		 * 断开socket连接
		 */
		ipDisconnButton = new JButton("\u65AD\u5F00");
		ipDisconnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					
					FrontMesPack frontMesPack = new FrontMesPack(-1, FrontMesPack.CLOSE, -1, 0, -1, -1);
					byte[] buf = ParseMesPack.parse(frontMesPack).creatMesByte();
					carOutputStream.write(buf, 0, buf.length);
					//carOutputStream.close();
					carSocket.close();
					
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.toString()+e1.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
				}
				
				if(carSocket.isClosed()){
					ipFlag.setIcon(ipIconR);
				}
			}
		});
		
		JPanel ctrlPanel = new JPanel();

		exitButton = new JButton("\u9000\u51FA");
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});

		keyCtrl = new JCheckBox("\u952E\u76D8\u63A7\u5236");
		keyCtrl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(keyCtrl.isSelected()){
					
					int dir = 0;
					if(e.getKeyCode() == KeyEvent.VK_UP ){
						dir = FrontMesPack.UP;
						System.out.println("前进");
					}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
						dir = FrontMesPack.DOWN;
					}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
						dir = FrontMesPack.LEFT;
					}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
						dir = FrontMesPack.RIGHT;
					}else if(e.getKeyCode() == KeyEvent.VK_ENTER){
						dir = FrontMesPack.STOP_MANUAL;
					}
					
					//加速减速按钮
					else if(e.getKeyCode() == KeyEvent.VK_W){
						int temp = Integer.parseInt(speedText.getText()) - 500;
						if(temp <= 0){
							temp = 0;
						} else if(temp >= 5000){
							temp = 5000;
						}
						speedText.setText(String.valueOf((temp)));
						dir = dir_last;
						System.out.println("加速");
					} 
					else if(e.getKeyCode() == KeyEvent.VK_S){
						int temp = Integer.parseInt(speedText.getText()) - 500;
						if(temp <= 0){
							temp = 0;
						} else if(temp >= 5000){
							temp = 5000;
						}
						speedText.setText(String.valueOf((temp)));
						dir = dir_last;
					}
					else if(e.getKeyCode() == KeyEvent.VK_E){
						int temp = Integer.parseInt(radiusText.getText()) + 300;
						if(temp <= 0){
							temp = 0;
						} else if(temp >= 5000){
							temp = 5000;
						}
						radiusText.setText(String.valueOf((temp)));
						dir = dir_last;
					}	
					else if(e.getKeyCode() == KeyEvent.VK_D){
						int temp = Integer.parseInt(radiusText.getText()) - 300;
						if(temp <= 0){
							temp = 0;
						} else if(temp >= 5000){
							temp = 5000;
						}
						radiusText.setText(String.valueOf((temp)));
						dir = dir_last;
					}	
					
					if(dir != 0){
						try {
							FrontMesPack frontMesPack = new FrontMesPack(dir, FrontMesPack.MANUAL, -1, Integer.parseInt(speedText.getText()), Integer.parseInt(radiusText.getText()), -1);
							byte[] buf = ParseMesPack.parse(frontMesPack).creatMesByte();
							carOutputStream.write(buf, 0, buf.length);					
							dir_last = dir;
							
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(contentPane, e1.toString()+e1.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
						}
					}
					
				}	
			}
		});

		speedText = new JTextField();
		speedText.setText("1000");
		speedText.setColumns(10);

		JLabel speedUnit = new JLabel("m/s");

		ipText = new JTextField();
		ipText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if(ipText.getText().equals("请输入小车IP地址")){
					ipText.setText("192.168.2.88");
				}
				
			}
		});
		ipText.setText("\u8BF7\u8F93\u5165\u5C0F\u8F66IP\u5730\u5740");
		ipText.setColumns(10);
		
		JButton configButton = new JButton("\u914D\u7F6E\u8DEF\u5F84");
		/**
		 * 配置路径窗口   单独一个线程执行
		 */
		configButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ConfigFrame frame = new ConfigFrame();
							frame.setVisible(true);
							frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		loadButton = new JButton("\u5BFC\u5165\u914D\u7F6E");
		/**
		 * 加载配置按钮， 作用只是将配置文件的路径写入loadText文本框
		 */
		loadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FileDialog loadDialog = new FileDialog(CarFrame.this, "加载配置", FileDialog.LOAD);
				loadDialog.setVisible(true);
				loadTextField.setText(loadDialog.getDirectory() + loadDialog.getFile());
			}
		});
		
		loadTextField = new JTextField();
		loadTextField.setColumns(10);
		
		executeButton = new JButton("\u6267\u884C");
		/**
		 * 执行配置文件，单独线程
		 */
		executeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				configThread = new ExecuteThread(carOutputStream);
				configThread.start();
			}
		});
		
		/*
		 * 中止轨迹   要立即中断正在执行的线程  （未实现）
		 * */
		JButton suspendButton = new JButton("\u4E2D\u6B62");
		suspendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
		JButton monitorButton = new JButton("\u76D1\u63A7");
		/**
		 * 摄像头监控按钮，单击启动另一窗口
		 */
		monitorButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							if (carSocket == null || carSocket.isClosed()) {
								JOptionPane.showMessageDialog(CarFrame.this, "尚未连接！", "警告", JOptionPane.WARNING_MESSAGE);
							} else {
								CapturePhoto frame = new CapturePhoto();
								frame.setVisible(true);
								frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
			}
		});
		
		JLabel radiusSetText = new JLabel("\u8F6C\u5F2F\u534A\u5F84:");
		
		radiusText = new JTextField();
		radiusText.setText("1000");
		radiusText.setColumns(10);
		
		speedSetText = new JLabel("\u901F\u5EA6:");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(ipText, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ipConnButton, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ipDisconnButton, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(ctrlPanel, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(executeButton)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(suspendButton))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(configButton, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(loadButton, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(monitorButton)
												.addComponent(speedText, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(speedUnit)
											.addGap(2)
											.addComponent(exitButton, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
										.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
											.addComponent(keyCtrl)
											.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
											.addComponent(ipFlag, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
										.addComponent(radiusSetText, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
										.addComponent(radiusText, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
										.addComponent(speedSetText, Alignment.LEADING)))
								.addComponent(loadTextField, 168, 168, 168))))
					.addGap(2))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(ipText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(ipConnButton)
						.addComponent(ipDisconnButton))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(keyCtrl)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(speedSetText)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(speedText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(speedUnit))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(radiusSetText)
									.addGap(2)
									.addComponent(radiusText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(ipFlag, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(loadButton)
								.addComponent(configButton))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(loadTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(executeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(suspendButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(monitorButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(exitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ctrlPanel, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)))
					.addGap(3))
		);
		
		upButton = new JButton("\u524D");
		/**
		 * 上下左右控制按钮
		 * 前进/后退由速度正负决定     后退时将得到的速度取相反数
		 * 左转/右转有两轮转速差决定   
		 */
		upButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
						
				try {
					FrontMesPack frontMesPack = new FrontMesPack(FrontMesPack.UP, FrontMesPack.MANUAL, -1, Integer.parseInt(speedText.getText()), -1, -1);
					byte[] buf = ParseMesPack.parse(frontMesPack).creatMesByte();
					carOutputStream.write(buf, 0, buf.length);		
					
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.toString()+e1.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		downButton = new JButton("\u540E");
		downButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {	
				try {
					FrontMesPack frontMesPack = new FrontMesPack(FrontMesPack.DOWN, FrontMesPack.MANUAL, -1, Integer.parseInt(speedText.getText()), -1, -1);
					byte[] buf = ParseMesPack.parse(frontMesPack).creatMesByte();
					carOutputStream.write(buf, 0, buf.length);	
									
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.toString()+e1.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		leftButton = new JButton("\u5DE6");
		leftButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					FrontMesPack frontMesPack = new FrontMesPack(FrontMesPack.LEFT, FrontMesPack.MANUAL, -1, Integer.parseInt(speedText.getText()), Integer.parseInt(radiusText.getText()), -1);
					byte[] buf = ParseMesPack.parse(frontMesPack).creatMesByte();
					carOutputStream.write(buf, 0, buf.length);
									
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.toString()+e1.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		
		rightButton = new JButton("\u53F3");
		rightButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					FrontMesPack frontMesPack = new FrontMesPack(FrontMesPack.RIGHT, FrontMesPack.MANUAL, -1, Integer.parseInt(speedText.getText()), Integer.parseInt(radiusText.getText()), -1);
					byte[] buf = ParseMesPack.parse(frontMesPack).creatMesByte();
					carOutputStream.write(buf, 0, buf.length);
								
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.toString()+e1.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		
		stopButton = new JButton("\u505C");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		stopButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {	
					FrontMesPack frontMesPack = new FrontMesPack(FrontMesPack.STOP_MANUAL, FrontMesPack.MANUAL, -1, -1, -1, -1);
					byte[] buf = ParseMesPack.parse(frontMesPack).creatMesByte();
					carOutputStream.write(buf, 0, buf.length);
					
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.toString()+e1.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		
		GroupLayout gl_ctrlPanel = new GroupLayout(ctrlPanel);
		gl_ctrlPanel.setHorizontalGroup(
			gl_ctrlPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ctrlPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(leftButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_ctrlPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_ctrlPanel.createSequentialGroup()
							.addComponent(upButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_ctrlPanel.createSequentialGroup()
							.addGroup(gl_ctrlPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(downButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
								.addComponent(stopButton, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(rightButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addGap(7))))
		);
		gl_ctrlPanel.setVerticalGroup(
			gl_ctrlPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_ctrlPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(upButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ctrlPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(leftButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(stopButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(rightButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(downButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addGap(59))
		);
		ctrlPanel.setLayout(gl_ctrlPanel);
		contentPane.setLayout(gl_contentPane);
	}
}
