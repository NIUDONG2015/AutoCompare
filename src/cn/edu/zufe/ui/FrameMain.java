package cn.edu.zufe.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
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

public class FrameMain extends JFrame implements ActionListener, ChangeListener, TextListener, FocusListener {

	private JMenuBar menubar;
	private JMenu menuFile, menuTest, menuSort, menuAutoSorting;
	private JMenuItem miOpen, miSave, miExport, miExit, miSaveAsImage, miManualSorting, miSort1, miSort2; // �˵���

	private JLabel labelSCWellWidth, labelPixelRatio;
	private JSlider sliderSCWellWidth, sliderPixelRatio;
	private JTextField txtSCWellWidth, txtPixelRatio;

	private PAppletWellView pwv; // �;���ͼ
	private PAppletSC psc; // �ز�Ա�ͼ
	private LinkedList<Well> wellList;
	private MatchFactory matchFactory;

	private final static int MENU_WIDTH = 40, MENU_HEIGHT = 20;
	private final static int PWV_WIDTH = 460, PWV_HEIGHT = 410;
	private final static int PSC_WIDTH = 500, PSC_HEIGHT = 600;

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
		addMenuBar();
		// ��Ӳ������
		addParamPanel();

		// ������� PApplet ���� // ��������Ȼ����
		psc = new PAppletSC(PSC_WIDTH, PSC_HEIGHT);
		FrameSC fSC = new FrameSC("Stratigraphic Correlation", PSC_WIDTH + 16, PSC_HEIGHT, psc); // 16��������ScrollBar��
		pwv = new PAppletWellView(PWV_WIDTH, PWV_HEIGHT, psc);
		addPApplet(pwv);
		// addPApplet(this, psc);
	}

	private void addMenuBar() {
		menubar = new JMenuBar();
		// �ļ��˵���
		menuFile = new JMenu("File");
		menuFile.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
		// menuFile.setHorizontalTextPosition(JMenu.CENTER);
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

		// �ֶ�������Զ�����
		menuSort = new JMenu("Sort");
		menuSort.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
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

		// ���Թ��߲˵���
		menuTest = new JMenu("Test");
		menuTest.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
		miSaveAsImage = new JMenuItem("Save As Image");
		menuTest.add(miSaveAsImage);
		menubar.add(menuTest);
		miSaveAsImage.addActionListener(this);

		this.setJMenuBar(menubar);
	}

	private void addParamPanel() {
		// paramPanel ʹ�� GridBagLayout ����
		GridBagLayout gb = new GridBagLayout();
		JPanel paramPanel = new JPanel(gb);
		paramPanel.setPreferredSize(new Dimension(PWV_WIDTH, 100));
		// ����������ӽ����������ʾλ��
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// �ز�Ա�ͼ��������
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
		txtSCWellWidth.setHorizontalAlignment(JTextField.CENTER);
		txtSCWellWidth.addFocusListener(this);
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gb.setConstraints(txtSCWellWidth, gbc);
		paramPanel.add(txtSCWellWidth);

		// m/px ���ر�
		labelPixelRatio = new JLabel("Pixel Ratio");
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(labelPixelRatio, gbc);
		paramPanel.add(labelPixelRatio);

		sliderPixelRatio = new JSlider(JSlider.HORIZONTAL, 1, 200, 100);
		sliderPixelRatio.addChangeListener(this);
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gb.setConstraints(sliderPixelRatio, gbc);
		paramPanel.add(sliderPixelRatio);

		txtPixelRatio = new JTextField(String.valueOf(sliderPixelRatio.getValue() * 0.01), 10);
		txtPixelRatio.setHorizontalAlignment(JTextField.CENTER);
		txtPixelRatio.addFocusListener(this);
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gb.setConstraints(txtPixelRatio, gbc);
		paramPanel.add(txtPixelRatio);

		this.add(paramPanel);
	}

	public static void main(String[] args) {
		// ����Ϊ windows �Ľ�����
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ʵ����������
		FrameMain fMain = new FrameMain("AutoCompare", 475, 575);

		// ˢ�½�����
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
					LinkedList<PWell> pwList = Generator.wellToPWells(wellList);
					// set and draw
					pwv.setPWells(pwList);
					pwv.drawPGBottom();
				} else {
					JOptionPane.showMessageDialog(null, "��ѡ��Excel�ļ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "�ز�Ա�ͼ�Ѿ����浽��Data\\well.png��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
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
		if (e.getSource() == sliderSCWellWidth) {
			txtSCWellWidth.setText(String.valueOf(sliderSCWellWidth.getValue()));
		} else if (e.getSource() == sliderPixelRatio) {
			txtPixelRatio.setText(String.format("%.2f", sliderPixelRatio.getValue() * 0.01));
		}
	}

	@Override
	public void textValueChanged(TextEvent e) {
		if (e.getSource() == txtSCWellWidth) {
			sliderSCWellWidth.setValue(Integer.parseInt(txtSCWellWidth.getText()));
			System.out.println(txtSCWellWidth.getText());
		} else if (e.getSource() == txtPixelRatio) {
			sliderPixelRatio.setValue(Integer.parseInt(txtPixelRatio.getText()) * 100);
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == txtSCWellWidth) {
			int t = Integer.parseInt(txtSCWellWidth.getText());
			// ���Ʒ�Χ
			if (t > sliderSCWellWidth.getMaximum()) {
				t = sliderSCWellWidth.getMaximum();
			}
			if (t < sliderSCWellWidth.getMinimum()) {
				t = sliderSCWellWidth.getMinimum();
			}
			sliderSCWellWidth.setValue(t);
		} else if (e.getSource() == txtPixelRatio) {
			int t = (int)(Double.parseDouble(txtPixelRatio.getText()) * 100);
			// ���Ʒ�Χ
			if (t > sliderPixelRatio.getMaximum()) {
				t = sliderPixelRatio.getMaximum();
			}
			if (t < sliderPixelRatio.getMinimum()) {
				t = sliderPixelRatio.getMinimum();
			}
			System.out.println(t);
			sliderPixelRatio.setValue(t);
		}
	}

}
