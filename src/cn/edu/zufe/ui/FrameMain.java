package cn.edu.zufe.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import processing.core.PApplet;
import cn.edu.zufe.io.*;
import cn.edu.zufe.match.MatchFactory;
import cn.edu.zufe.model.*;
import cn.edu.zufe.drawable.*;

public class FrameMain extends JFrame implements ActionListener, ChangeListener {

	private JMenuBar menubar;
	private JMenu menuFile, menuTest, menuSort, menuAutoSorting;
	private JMenuItem miOpen, miSave, miExport, miExit, miSaveAsImage, miManualSorting, miSort1, miSort2; // 菜单项

	private JLabel labelSCWellWidth;
	private JSlider sliderSCWellWidth;
	private JTextField txtSCWellWidth;

	private PAppletWellView pwv; // 油井视图
	private PAppletSC psc; // 地层对比图
	private LinkedList<Well> wellList;
	private MatchFactory matchFactory;

	private final static int PWV_WIDTH = 460, PWV_HEIGHT = 410;
	private final static int PSC_WIDTH = 500, PSC_HEIGHT = 600;

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
		addMenuBar();
		// 添加参数面板
		addParamPanel();

		// 添加两个 PApplet 窗口 // 必须放最后不然报错
		psc = new PAppletSC(PSC_WIDTH, PSC_HEIGHT);
		FrameSC fSC = new FrameSC("Stratigraphic Correlation", PSC_WIDTH + 16, PSC_HEIGHT, psc); // 16是用来放ScrollBar的
		pwv = new PAppletWellView(PWV_WIDTH, PWV_HEIGHT, psc);
		addPApplet(pwv);
		// addPApplet(this, psc);
	}

	private void addMenuBar() {
		menubar = new JMenuBar();
		// 文件菜单项
		menuFile = new JMenu("File");
		miOpen = new JMenuItem("Open");
		miSave = new JMenuItem("Save");
		miExport = new JMenuItem("Export");
		miExit = new JMenuItem("Exit");
		menuFile.add(miOpen);
		menuFile.add(miSave);
		menuFile.addSeparator();
		menuFile.add(miExport);
		menuFile.add(miExit);
		menubar.add(menuFile);
		miOpen.addActionListener(this);
		miSave.addActionListener(this);

		// 手动排序和自动排序
		menuSort = new JMenu("Sort");
		miManualSorting = new JMenuItem("ManualSorting");
		menuAutoSorting = new JMenu("AutoSorting");
		miSort1 = new JMenuItem("sort1");
		miSort2 = new JMenuItem("sort2");
		menuAutoSorting.add(miSort1);
		menuAutoSorting.add(miSort2);
		menuSort.add(miManualSorting);
		menuSort.add(menuAutoSorting);
		menubar.add(menuSort);
		miManualSorting.addActionListener(this);
		miSort1.addActionListener(this);
		miSort2.addActionListener(this);

		// 测试工具菜单项
		menuTest = new JMenu("Test Tool");
		miSaveAsImage = new JMenuItem("Save As Image");
		menuTest.add(miSaveAsImage);
		menubar.add(menuTest);
		miSaveAsImage.addActionListener(this);

		this.setJMenuBar(menubar);
	}

	private void addParamPanel() {
		// paramPanel 使用 GridBagLayout 布局
		GridBagLayout gb = new GridBagLayout();
		JPanel paramPanel = new JPanel(gb);
		paramPanel.setPreferredSize(new Dimension(PWV_WIDTH, 300));
		// 用来控制添加进的组件的显示位置
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 地层对比图井宽设置
		labelSCWellWidth = new JLabel("Well Width");
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(labelSCWellWidth, gbc);
		paramPanel.add(labelSCWellWidth);

		sliderSCWellWidth = new JSlider(JSlider.HORIZONTAL, 20, 100, 50);
		sliderSCWellWidth.addChangeListener(this);
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gb.setConstraints(sliderSCWellWidth, gbc);
		paramPanel.add(sliderSCWellWidth);

		txtSCWellWidth = new JTextField(String.valueOf(sliderSCWellWidth.getValue()), 10);
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gb.setConstraints(txtSCWellWidth, gbc);
		paramPanel.add(txtSCWellWidth);

		this.add(paramPanel);
	}

	public static void main(String[] args) {
		// 设置为 windows 的界面风格
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 实例化主窗体
		FrameMain fMain = new FrameMain("AutoCompare", 475, 775);

		// 刷新界面风格
		SwingUtilities.updateComponentTreeUI(fMain);
	}

	private void addPApplet(PApplet p) {
		p.setPreferredSize(new Dimension(p.width, p.height));
		p.init();
		p.start();
		this.add(p);
	}

	private void openFile() throws IOException {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setCurrentDirectory(new File("./Data"));
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
					LinkedList<PWell> pwList = Generator.wellToPWells(wellList);
					// set and draw
					pwv.setPWells(pwList);
					pwv.drawPGBottom();
				} else {
					JOptionPane.showMessageDialog(null, "请选择Excel文件", "提示", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
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
		} else if (e.getSource() == miSaveAsImage) {
			psc.savePGBottom("Data\\well.png");
			JOptionPane.showMessageDialog(null, "地层对比图已经保存到“Data\\well.png”", "提示", JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == miManualSorting) {
			pwv.setSort(0);
			pwv.drawPGBottom();
		} else if (e.getSource() == miSort1) {
			pwv.setSort(1);
			pwv.drawPGBottom();
		} else if (e.getSource() == miSort2) {
			pwv.setSort(2);
			pwv.drawPGBottom();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO 自动生成的方法存根
		if (e.getSource() == sliderSCWellWidth) {
			txtSCWellWidth.setText(String.valueOf(sliderSCWellWidth.getValue()));
		}
	}

}
