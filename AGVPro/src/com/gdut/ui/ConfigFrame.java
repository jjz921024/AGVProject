package com.gdut.ui;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

public class ConfigFrame extends JFrame {

	private JPanel contentPane;
	private JTextField speedTextField;
	private JTextField timeTextField;
	private JTextField markTextField;
	private JComboBox typesComboBox;
	private JList<String> configList;
	private JSlider angleSlider;
	private JCheckBox turnCheckBox;
	private int markCount;
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private ArrayList<LinkedHashMap<String, String>> routeList = new ArrayList<LinkedHashMap<String, String>>();
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		
	}*/

	/**
	 * Create the frame.
	 */
	public ConfigFrame() {
		setTitle("\u914D\u7F6E\u8F68\u8FF9");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 572, 302);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 279, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel_1, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		JLabel lblNewLabel_1 = new JLabel("\u7C7B\u578B");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 14));
		
		typesComboBox = new JComboBox();
		typesComboBox.setModel(new DefaultComboBoxModel(new String[] {"\u76F4\u7EBF", "\u8F6C\u5F2F", "\u505C\u6B62", "\u6807\u8BB0", "\u56DE\u6EDA"}));
		typesComboBox.setToolTipText("");
		
		JLabel lblNewLabel_2 = new JLabel("\u901F\u5EA6");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 14));
		
		speedTextField = new JTextField();
		speedTextField.setColumns(10);
		
		JLabel label = new JLabel("\u65F6\u95F4");
		label.setFont(new Font("宋体", Font.PLAIN, 14));
		
		timeTextField = new JTextField();
		timeTextField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("\u8F6C\u89D2");
		lblNewLabel_3.setFont(new Font("宋体", Font.PLAIN, 14));
		
		angleSlider = new JSlider();
		angleSlider.setMinimum(-500);
		angleSlider.setValue(0);
		angleSlider.setMaximum(500);
		
		turnCheckBox = new JCheckBox("\u539F\u5730\u8F6C\u5411");
		turnCheckBox.setFont(new Font("宋体", Font.PLAIN, 14));
		
		JLabel lblid = new JLabel("\u6807\u8BB0ID");
		lblid.setFont(new Font("宋体", Font.PLAIN, 14));
		
		markTextField = new JTextField();
		markTextField.setColumns(10);
		
		/*
		 * 添加按钮单击事件
		 * 读取信息，并写入右边List框中，写入位置光标下一行
		 * */
		JButton addButton = new JButton("\u6DFB\u52A0");
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				StringBuffer sb = new StringBuffer();
				
				LinkedHashMap<String, String> routeMap = new LinkedHashMap<String, String>();
				
				if(typesComboBox.getSelectedItem().equals("直线")){	
					sb.append("直线     " + 
							  "Speed:" + speedTextField.getText() + "  " +
							  "Time:" + timeTextField.getText());	
					routeMap.put("type", "直线");
					routeMap.put("speed", speedTextField.getText());
					routeMap.put("time", timeTextField.getText());				
				}
				else if (typesComboBox.getSelectedItem().equals("转弯")) {
					sb.append("转弯     " + 
							  "Speed:" + speedTextField.getText() + "  " +
							  "Time:" + timeTextField.getText() + "  " +
							  "Delta:" + angleSlider.getValue() );
					routeMap.put("type", "转弯");
					routeMap.put("speed", speedTextField.getText());
					routeMap.put("time", timeTextField.getText());
					routeMap.put("delta", String.valueOf(angleSlider.getValue()));
								
					if(turnCheckBox.isSelected()){
						sb.append("  原地转向");
						routeMap.put("isTurn", String.valueOf(turnCheckBox.isSelected()));
					}
				}				
				else if (typesComboBox.getSelectedItem().equals("停止")) {
					sb.append("停止     " + 
							  "Time:" + timeTextField.getText());
					routeMap.put("type", "停止");
					routeMap.put("time", timeTextField.getText());					
				}
				else if (typesComboBox.getSelectedItem().equals("标记")) {
					//markCount 标记ID 从0开始	
					sb.append("标记     " + 							  
							  "ID:"  + markCount);				
					routeMap.put("type", "标记");
					routeMap.put("id", String.valueOf(markCount));
					markCount++;
				}
				else if (typesComboBox.getSelectedItem().equals("回滚")) {
					sb.append("回滚     " + 
							  "ID:" + markTextField.getText());
					routeMap.put("type", "回滚");
					routeMap.put("id", markTextField.getText());
				}
							
				
				/*
				 * 插入前先判断是否有选中行
				 * 在选中行的下一行插入
				 * */
				int index = configList.getSelectedIndex();
				//没有选中任何一行，在最后一行插入
				if (index != -1) {
					model.add(index + 1, sb.toString());
					//将map添加进list里		
					routeList.add(routeMap);
				}else {
					//选中了一行，在该行下插入
					model.addElement(sb.toString());
					routeList.add(routeMap);
				}	
				configList.setModel(model);
							
			}
		});
		
	
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(turnCheckBox)
							.addGap(22)
							.addComponent(lblid)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(markTextField)
							.addGap(14))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblNewLabel_3)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(angleSlider, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(172)
							.addComponent(addButton, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_2)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(typesComboBox, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(speedTextField, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
									.addGap(18)
									.addComponent(label)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(timeTextField, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
									.addGap(14)))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
						.addComponent(typesComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(speedTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label)
						.addComponent(timeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(angleSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(turnCheckBox)
						.addComponent(lblid)
						.addComponent(markTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
					.addComponent(addButton)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		JLabel lblNewLabel = new JLabel("\u8F68\u8FF9\u4FE1\u606F");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		
		configList = new JList();
		/*ConfigList.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});*/
		
		/*
		 * 清空轨迹信息   
		 * 重置Model、MarkCount
		 * */
		JButton clearButton = new JButton("\u91CD\u7F6E");
		clearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model.clear();
				markCount = 1;
				configList.setModel(model);
				routeList.clear();
			}
		});
		
		JButton deleteButton = new JButton("\u5220\u9664");
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//获取索引值，
				int index = configList.getSelectedIndex();
				//判断是否有选中行
				if(index != -1){
					model.remove(configList.getSelectedIndex());
					//删除list中的指定行
					routeList.remove(index);
				}
				
			}
		});
		
		JButton exportButton = new JButton("\u751F\u6210");
		exportButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				FileDialog saveConfigDialog = new FileDialog(ConfigFrame.this, "保存配置", FileDialog.SAVE);
				saveConfigDialog.setVisible(true);
				
				//解析routelist，生成xml文件
				Document document = DocumentHelper.createDocument();
				//创建根节点
				Element rootElement = document.addElement("route");
				ListIterator<LinkedHashMap<String, String>> routeListIter = routeList.listIterator();
				
				while (routeListIter.hasNext()) {
					//一个list生成一个节点
					//创建HashMap的迭代器
					Set<Map.Entry<String, String>> entrySet = routeListIter.next().entrySet();
					Iterator<Map.Entry<String, String>> itMap = entrySet.iterator();
					Element element = null;
					
					//遍历HashMap
					while(itMap.hasNext()){
						//一个map代表一个标签中的属性
						Entry<String, String> entry = itMap.next();
										
						
						if (entry.getKey().equals("type")){
							//一个新的节点
							String value = null;
							if(entry.getValue().equals("直线")){
								value = "line";
							} else if (entry.getValue().equals("转弯")) {
								value = "round";
							} else if (entry.getValue().equals("停止")) {
								value = "stop";
							} else if (entry.getValue().equals("标记")) {
								value = "mark";
							} else if (entry.getValue().equals("回滚")) {
								value = "return";
							}							
							element = rootElement.addElement(value);
							
						} else {
							//该节点下的属性
							element.addAttribute(entry.getKey(), entry.getValue());
						}
					}
				}
				
				//生成XML文件
				XMLWriter xmlWriter = null;
				try {
					xmlWriter = new XMLWriter(new FileOutputStream(new File(saveConfigDialog.getDirectory() + saveConfigDialog.getFile() + ".xml")));
					xmlWriter.write(document);
					
					
					
				} catch (UnsupportedEncodingException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					try {
						xmlWriter.close();
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				System.out.println(saveConfigDialog.getDirectory() + saveConfigDialog.getFile() + ".xml");
				System.out.println(routeList.toString());
			}
		});
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(1)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(31)
							.addComponent(clearButton, GroupLayout.PREFERRED_SIZE, 72, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 72, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(exportButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addGap(15))
						.addComponent(configList, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(configList, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE, false)
							.addComponent(deleteButton)
							.addComponent(exportButton))
						.addComponent(clearButton))
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
	}
}
