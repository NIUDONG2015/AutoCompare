package cn.edu.zufe.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class FrameSC extends JFrame {

	private PAppletSC psc; // �ز�Ա�ͼ

	public FrameSC(String s, int width, int height, PAppletSC psc) {
		this.setSize(width, height); // ���ô�С
		this.setMinimumSize(new Dimension(100, 100));
		this.setLocationRelativeTo(null); // ������ʾ����
		this.setVisible(true); // �ɼ�
		// this.setLayout(new GridLayout(1,1)); // GridLayout
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �˳�ʱ�رպ�̨����
		// ��� psc
		this.psc = psc;

		psc.init();
		psc.start();
		this.add(psc);
	}

}
