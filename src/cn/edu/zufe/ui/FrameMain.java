package cn.edu.zufe.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.*;

import processing.core.PApplet;
import cn.edu.zufe.io.*;
import cn.edu.zufe.match.MatchFactory;
import cn.edu.zufe.model.*;
import cn.edu.zufe.drawable.*;

public class FrameMain extends JFrame implements ActionListener {
	// private LinkedList<Well> wellList;
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem miOpen, miSave, miExport, miExit; // �˵���
	private PAppletWellView pwv; // �;���ͼ
	private PAppletSC psc; // �ز�Ա�ͼ
	private static LinkedList<Well> wellList;
	private MatchFactory matchFactory;

	public FrameMain(String s, int width, int height) {
		super(s);
		this.setSize(width, height); // ���ô�С
		this.setLocationRelativeTo(null); // ������ʾ����
		// this.setLocation(x, y); // ����λ��
		this.setVisible(true); // �ɼ�
		this.setLayout(new FlowLayout()); // ������
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �˳�ʱ�رպ�̨����
		this.setResizable(false); // ���ô�С����

		// ��Ӳ˵���
		menubar = new JMenuBar();
		menu = new JMenu("File");
		miOpen = new JMenuItem("Open");
		miSave = new JMenuItem("Save");
		miExport = new JMenuItem("Export");
		miExit = new JMenuItem("Exit");
		menu.add(miOpen);
		menu.add(miSave);
		menu.addSeparator();
		menu.add(miExport);
		menu.add(miExit);
		menubar.add(menu);
		miOpen.addActionListener(this);
		miSave.addActionListener(this);
		this.setJMenuBar(menubar);

		// ������� PApplet ����
		pwv = new PAppletWellView(height - 75, height - 75, psc);
		psc = new PAppletSC(width - height + 30, height - 75);
		addPApplet(this, pwv);
		addPApplet(this, psc);
	}

	private void addPApplet(FrameMain f, PApplet p) {
		p.setPreferredSize(new Dimension(p.width, p.height));
		p.init();
		p.start();
		f.add(p);
	}

	public static void main(String[] args) {
		// ����Ϊ windows �Ľ�����
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ʵ����������
		FrameMain fMain = new FrameMain("AutoCompare", 1200, 475);
		// ˢ�½�����
		SwingUtilities.updateComponentTreeUI(fMain);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miOpen) {
			try {
				openFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else if (e.getSource() == miSave) {
			System.out.println("Save");
		}
	}

	private void openFile() throws IOException {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setCurrentDirectory(new File("."));
		if (JFileChooser.APPROVE_OPTION == jfc.showDialog(new JLabel(), "ѡ��")) {
			File file = jfc.getSelectedFile();
			if (file != null && file.isFile()) {
				String name = file.getName();
				String ext = name.substring(name.indexOf(".") + 1);
				if (ext.equals("xls") || ext.equals("xlsx")) {
					// ���ļ�
					wellList = Data.loadData(file.getAbsolutePath());

					matchFactory = new MatchFactory(wellList.get(1), wellList);
					matchFactory.doMatch(1);
					// Well to PWell
					LinkedList<PWell> pwList = Generator.toPWells(wellList);
					// set and draw
					pwv.setPWells(pwList);
					pwv.drawPGBottom();
				} else {
					JOptionPane.showMessageDialog(null, "��ѡ��Excel�ļ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
				}
			}
		}
	}
}
