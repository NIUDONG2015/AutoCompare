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
	public static LinkedList<Well> loadData(String urlFile) throws IOException
	{
		if(urlFile == null	|| urlFile == ""){
			return null;
		}
		LinkedList<Well> wellList = new LinkedList<Well>();
		try{
			InputStream is = new FileInputStream(urlFile);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			
			XSSFSheet sheetWell = xssfWorkbook.getSheetAt(0);		//�꾮���ݱ�
			XSSFSheet sheetBigLayer = xssfWorkbook.getSheetAt(1);	//������ݱ�
			XSSFSheet sheetSmallLayer = xssfWorkbook.getSheetAt(3);	//С�����ݱ�

			//���꾮���ݱ�
			for(int sheetWellRowsNum = sheetWell.getFirstRowNum() + 2; 
					sheetWellRowsNum <= sheetWell.getLastRowNum(); ++sheetWellRowsNum){
				
				XSSFRow wellInfo = sheetWell.getRow(sheetWellRowsNum);	
				XSSFCell wellName = wellInfo.getCell(0);
				String t_wellName = getValue(wellName);
				
				XSSFCell wellPosX = wellInfo.getCell(1);
				double t_wellPosX = Double.valueOf(getValue(wellPosX));
				
				XSSFCell wellPosY = wellInfo.getCell(2);
				double t_wellPosY = Double.valueOf(getValue(wellPosY));
				
				//��ȡ�;���ź�����
				Well well = new Well();	
				well.setName(t_wellName);
				well.setX(t_wellPosX);
				well.setY(t_wellPosY);
				wellList.add(well);
			}
			
			//��ȡ�;������Ϣ
			for(int sheetBigLayerNum = sheetBigLayer.getFirstRowNum() + 2;
					sheetBigLayerNum <= sheetBigLayer.getLastRowNum(); ++sheetBigLayerNum){
				//��õ�ǰ��
				XSSFRow bigLayerInfo = sheetBigLayer.getRow(sheetBigLayerNum);	
				//�����;�����
				for(int wellNum = 0; wellNum < wellList.size(); ++wellNum){

					Well well = wellList.get(wellNum);
					if(well.getName() != getValue(bigLayerInfo.getCell(0))){
						continue;
					}
					XSSFCell bigLayerName = bigLayerInfo.getCell(1);
					String t_bigLayerName = getValue(bigLayerName);
					
					XSSFCell bigLayerDepth0 = bigLayerInfo.getCell(4);
					double bigLayerDepth[] = new double[1];//= new double[1];
					double t_bigLayerDepth0 = 0;
					try{
						t_bigLayerDepth0 = Double.valueOf(getValue(bigLayerDepth0));
					}
					catch(Exception e){
					}
					bigLayerDepth[0] = t_bigLayerDepth0;
					
					//��ȡ����λ�͵���
					BigLayer bigLayer = new BigLayer();
					bigLayer.setName(getValue(bigLayerName));
					bigLayer.setDepth(bigLayerDepth);
					
					well.getBigLayers().add(bigLayer);
				}
			}
			
			//��ȡС����Ϣ
			for(int sheetSmallLayerNum = sheetSmallLayer.getFirstRowNum() + 2;
					sheetSmallLayerNum <= sheetSmallLayer.getLastRowNum(); ++sheetSmallLayerNum){
				//��õ�ǰ��
				XSSFRow smallLayerInfo = sheetSmallLayer.getRow(sheetSmallLayerNum);
				//�����;�����
				for(int wellNum = 0; wellNum < wellList.size(); ++wellNum){
					Well well = wellList.get(wellNum);
					if(well.getName() != getValue(smallLayerInfo.getCell(0))){
						continue;
					}
					//XSSFCell small
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return wellList;
	}
	
	/**
	 * 
	 * �жϱ�����������������Ͳ�����
	 * @param xssfRow
	 * @return
	 */
	private static String getValue(XSSFCell xssfRow)
	{
		if(xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN){
			return String.valueOf(xssfRow.getBooleanCellValue());
		}
		else if(xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC){
			return String.valueOf(xssfRow.getNumericCellValue());
		}
		else{
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}
}
