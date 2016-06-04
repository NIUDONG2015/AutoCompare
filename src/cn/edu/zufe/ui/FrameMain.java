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
	private JMenuItem miOpen, miSave, miExport, miExit; // 菜单项
	private PAppletWellView pwv; // 油井视图
	private PAppletSC psc; // 地层对比图
	private static LinkedList<Well> wellList;
	private MatchFactory matchFactory;

	public FrameMain(String s, int width, int height) {
		super(s);
		this.setSize(width, height); // 设置大小
		this.setLocationRelativeTo(null); // 中央显示窗口
		// this.setLocation(x, y); // 设置位置
		this.setVisible(true); // 可见
		this.setLayout(new FlowLayout()); // 流布局
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 退出时关闭后台进程
		this.setResizable(false); // 禁用大小缩放

		// 添加菜单项
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

		// 添加两个 PApplet 窗口
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
		// 设置为 windows 的界面风格
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 实例化主窗体
		FrameMain fMain = new FrameMain("AutoCompare", 1200, 475);
		// 刷新界面风格
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
		if (JFileChooser.APPROVE_OPTION == jfc.showDialog(new JLabel(), "选择")) {
			File file = jfc.getSelectedFile();
			if (file != null && file.isFile()) {
				String name = file.getName();
				String ext = name.substring(name.indexOf(".") + 1);
				if (ext.equals("xls") || ext.equals("xlsx")) {
					// 打开文件
					wellList = Data.loadData(file.getAbsolutePath());

					matchFactory = new MatchFactory(wellList.get(1), wellList);
					matchFactory.doMatch(1);
					// Well to PWell
					LinkedList<PWell> pwList = Generator.toPWells(wellList);
					// set and draw
					pwv.setPWells(pwList);
					pwv.drawPGBottom();
				} else {
					JOptionPane.showMessageDialog(null, "请选择Excel文件", "提示", JOptionPane.INFORMATION_MESSAGE); 
				}
			}
		}
	}
}
