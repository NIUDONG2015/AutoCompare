package cn.edu.zufe.io;

import java.util.LinkedList;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.edu.zufe.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Data {
	public static LinkedList<Well> loadData(String urlFile) throws IOException {
		if (urlFile == null || urlFile == "") {
			return null;
		}
		LinkedList<Well> wellList = new LinkedList<Well>();
		try {
			InputStream is = new FileInputStream(urlFile);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

			XSSFSheet sheetWell = xssfWorkbook.getSheetAt(0); // �꾮���ݱ�
			XSSFSheet sheetBigLayer = xssfWorkbook.getSheetAt(1); // ������ݱ�
			XSSFSheet sheetSmallLayer = xssfWorkbook.getSheetAt(3); // С�����ݱ�

			// ���꾮���ݱ�
			for (int sheetWellRowsNum = sheetWell.getFirstRowNum() + 2; sheetWellRowsNum <= sheetWell
					.getLastRowNum(); ++sheetWellRowsNum) {

				XSSFRow wellInfo = sheetWell.getRow(sheetWellRowsNum);
				XSSFCell wellName = wellInfo.getCell(0);
				String t_wellName = getValue(wellName);

				XSSFCell wellPosX = wellInfo.getCell(1);
				double t_wellPosX = Double.valueOf(getValue(wellPosX));

				XSSFCell wellPosY = wellInfo.getCell(2);
				double t_wellPosY = Double.valueOf(getValue(wellPosY));

				// ��ȡ�;���ź�����
				Well well = new Well();
				well.setName(t_wellName);
				well.setX(t_wellPosX);
				well.setY(t_wellPosY);
				wellList.add(well);
			}

			// ��ȡ�;������Ϣ
			for (int sheetBigLayerNum = sheetBigLayer.getFirstRowNum() + 2; sheetBigLayerNum <= sheetBigLayer
					.getLastRowNum(); ++sheetBigLayerNum) {
				// ��õ�ǰ��
				XSSFRow bigLayerInfo = sheetBigLayer.getRow(sheetBigLayerNum);

				// �����;�����
				for (int wellNum = 0; wellNum < wellList.size(); ++wellNum) {

					Well well = wellList.get(wellNum);

					if (well.getName().equals(getValue(bigLayerInfo.getCell(0))) == false) {
						continue;
					}
					XSSFCell bigLayerName = bigLayerInfo.getCell(1);
					String t_bigLayerName = getValue(bigLayerName);

					XSSFCell bigLayerDepth0 = bigLayerInfo.getCell(4);
					double[] t_bigLayerDepth = { 0 };
					try {
						t_bigLayerDepth[0] = Double.valueOf(getValue(bigLayerDepth0));
					} catch (Exception e) {
					}

					// ��ȡ����λ�͵���
					BigLayer bigLayer = new BigLayer();
					bigLayer.setName(getValue(bigLayerName));
					bigLayer.setDepth(t_bigLayerDepth);

					well.getBigLayers().add(bigLayer);
				}
			}

			// ��ȡС����Ϣ
			for (int sheetSmallLayerNum = sheetSmallLayer.getFirstRowNum() + 2; sheetSmallLayerNum <= sheetSmallLayer
					.getLastRowNum(); ++sheetSmallLayerNum) {
				// ��õ�ǰ��
				XSSFRow smallLayerInfo = sheetSmallLayer.getRow(sheetSmallLayerNum);
				// �����;�����
				for (int wellNum = 0; wellNum < wellList.size(); ++wellNum) {
					Well well = wellList.get(wellNum);
					if (well.getName().equals(getValue(smallLayerInfo.getCell(0))) == false) {
						continue;
					}

					for (int bigLayerNum = 0; bigLayerNum < well.getBigLayers().size(); ++bigLayerNum) {
						BigLayer bigLayer = well.getBigLayers().get(bigLayerNum);
						String strBigLayer = getValue(smallLayerInfo.getCell(1));
						// ����(��ΪС�����ݵĲ�λNg10 �ȼ��� ������ݵĲ�λNgb)
						if (strBigLayer.contains("Ng10")) {
							strBigLayer = "Ngb";
						} else {
							strBigLayer = strBigLayer.substring(0, 3);
						}
						if (strBigLayer.equals(bigLayer.getName()) == true) {

							XSSFCell smallLayerName = smallLayerInfo.getCell(1);
							String t_smallLayerName = getValue(smallLayerName);

							XSSFCell smallLayerDepth0 = smallLayerInfo.getCell(8);
							double[] t_smallLayerDepth = { 0, 0 };
							try {
								t_smallLayerDepth[0] = Double.valueOf(getValue(smallLayerDepth0));
							} catch (Exception e) {
							}
							XSSFCell smallLayerDepth1 = smallLayerInfo.getCell(9);
							try {
								t_smallLayerDepth[1] = Double.valueOf(getValue(smallLayerDepth1));
							} catch (Exception e) {
							}

							XSSFCell smallLayerEleResult = smallLayerInfo.getCell(13);
							String t_smallLayerEleResult = getValue(smallLayerEleResult);

							SmallLayer smallLayer = new SmallLayer();
							smallLayer.setName(t_smallLayerName);
							smallLayer.setDepth(t_smallLayerDepth);
							smallLayer.setEleResult(t_smallLayerEleResult);

							well.getBigLayers().get(bigLayerNum).getSmallLayers().add(smallLayer);
							break;
						}
					}
					// XSSFCell small
				}
			}
			
//			for (int i = 0; i < wellList.size(); ++i) {
//				Well well = wellList.get(i);
//				System.out.println("����:" + well.getName() + "  X:" + well.getX() + "  Y:" + well.getY());
//				for (int j = 0; j < well.getBigLayers().size(); ++j) {
//					BigLayer bigLayer = well.getBigLayers().get(j);
//					System.out.println("	��λ:" + bigLayer.getName() + "  ����(MD):" + bigLayer.getDepth()[0]);
//					for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
//						SmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
//						System.out.println("			��λ:" + smallLayer.getName() + " ɰ�Ҷ���:" + smallLayer.getDepth()[0] 
//								+ " ɰ�ҵ���:"+smallLayer.getDepth()[1] + "  �����:"+smallLayer.getEleResult());
//					}
//					System.out.println("");
//				}
//				System.out.println("");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wellList;
	}

	/**
	 * 
	 * �жϱ�����������������Ͳ�����
	 * 
	 * @param xssfRow
	 * @return
	 */
	private static String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}
}
