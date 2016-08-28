package com.gdut.camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by kofee on 2016/3/28.
 */
public class OpenCVTest {
    private JLabel imageView;

    private Mat image;
    private String windowName;
    
    public static void main(String[] args) {
    	
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	OpenCVTest imageViewer = new OpenCVTest(Highgui.imread("C:/Users/Jun/Desktop/temp.jpeg"), "第一幅图片");
/*    	Mat mat = Highgui.imread("C:/Users/Jun/Desktop/1.png", 1);
    	System.out.println("x:" + mat.width());
    	System.out.println("y:" + mat.height());
    	System.out.println("mat is null? " + mat.size()==null);*/
        imageViewer.imshow();
		
    	
	}

    /**
     * 如果使用junit测试时调用该方法，图像会一闪而过，可通过sleep()等方式暂时显示
     *
     * @param
     */

    public OpenCVTest(Mat image) {
        this.image = image;
    }


    /**
     * @param image      要显示的mat
     * @param windowName 窗口标题
     */
    public OpenCVTest(Mat image, String windowName) {
        this.image = image;
        this.windowName = windowName;
    }

    /**
     * 图片显示
     */
    public void imshow() {
        setSystemLookAndFeel();
        Image loadedImage = toBufferedImage(image);
        
        JFrame frame = createJFrame(windowName, 400, 350); //image.width(), image.height()
        
        imageView.setIcon(new ImageIcon(loadedImage));
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 用户点击窗口关闭
    }

    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private JFrame createJFrame(String windowName, int width, int height) {
        JFrame frame = new JFrame(windowName);
        imageView = new JLabel();
        final JScrollPane imageScrollPane = new JScrollPane(imageView);
        imageScrollPane.setPreferredSize(new Dimension(width, height));
        frame.add(imageScrollPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }


    private Image toBufferedImage(Mat matrix) {
    	
        int type = BufferedImage.TYPE_BYTE_GRAY; //表示无符号 byte 灰度级图像（无索引）。
        if (matrix.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
        byte[] buffer = new byte[bufferSize];
        
        matrix.get(0, 0, buffer); // 获取所有的像素点
        
        //构造一个类型为预定义图像类型之一的 BufferedImage
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type); 
        
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}






