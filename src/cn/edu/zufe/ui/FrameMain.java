package cn.edu.zufe.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import cn.edu.zufe.io.*;
public class FrameMain extends JFrame implements ActionListener {
	public Data data;
	static JMenuBar menubar;
	JMenu menu;
	JMenuItem miOpen, miSave, miExport, miExit;

	public FrameMain(String s, int x, int y, int width, int height) {
		super(s);
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setVisible(true);
		
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
	}

	public static void main(String[] args) {
		// 设置为 windows 的界面风格
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 实例化主窗体
		FrameMain fMain = new FrameMain("AutoCompare", 260, 80, 800, 620);
		fMain.setLayout(null);
		fMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == miOpen) {
			openFile();
		} else if (e.getSource() == miSave) {
			System.out.println("121212");
		}
	}

	public void openFile() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		if (file.isDirectory()) {
			System.out.println("文件夹:" + file.getAbsolutePath());
			
			//data = new Data();
			//data.loadData(file.getAbsolutePath());
			
		} else if (file.isFile()) {
			System.out.println("文件:" + file.getAbsolutePath());

		}
		System.out.println(jfc.getSelectedFile().getName());
	}
}
