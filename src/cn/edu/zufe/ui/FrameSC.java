package cn.edu.zufe.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class FrameSC extends JFrame {

	private PAppletSC psc; // 地层对比图

	public FrameSC(String s, int width, int height, PAppletSC psc) {
		this.setSize(width, height); // 设置大小
		this.setMinimumSize(new Dimension(100, 100));
		this.setLocationRelativeTo(null); // 中央显示窗口
		this.setVisible(true); // 可见
		// this.setLayout(new GridLayout(1,1)); // GridLayout
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 退出时关闭后台进程
		// 添加 psc
		this.psc = psc;

		psc.init();
		psc.start();
		this.add(psc);
	}

}
