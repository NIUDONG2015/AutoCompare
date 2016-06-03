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
			
			XSSFSheet sheetWell = xssfWorkbook.getSheetAt(0);		//钻井数据表
			XSSFSheet sheetBigLayer = xssfWorkbook.getSheetAt(1);	//大层数据表
			XSSFSheet sheetSmallLayer = xssfWorkbook.getSheetAt(3);	//小层数据表

			//读钻井数据表
			for(int sheetWellRowsNum = sheetWell.getFirstRowNum() + 2; 
					sheetWellRowsNum <= sheetWell.getLastRowNum(); ++sheetWellRowsNum){
				
				XSSFRow wellInfo = sheetWell.getRow(sheetWellRowsNum);	
				XSSFCell wellName = wellInfo.getCell(0);
				String t_wellName = getValue(wellName);
				
				XSSFCell wellPosX = wellInfo.getCell(1);
				double t_wellPosX = Double.valueOf(getValue(wellPosX));
				
				XSSFCell wellPosY = wellInfo.getCell(2);
				double t_wellPosY = Double.valueOf(getValue(wellPosY));
				
				//获取油井编号和坐标
				Well well = new Well();	
				well.setName(t_wellName);
				well.setX(t_wellPosX);
				well.setY(t_wellPosY);
				wellList.add(well);
			}
			
			//获取油井大层信息
			for(int sheetBigLayerNum = sheetBigLayer.getFirstRowNum() + 2;
					sheetBigLayerNum <= sheetBigLayer.getLastRowNum(); ++sheetBigLayerNum){
				//获得当前行
				XSSFRow bigLayerInfo = sheetBigLayer.getRow(sheetBigLayerNum);	
				//遍历油井链表
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
					
					//获取大层层位和底深
					BigLayer bigLayer = new BigLayer();
					bigLayer.setName(getValue(bigLayerName));
					bigLayer.setDepth(bigLayerDepth);
					
					well.getBigLayers().add(bigLayer);
				}
			}
			
			//获取小层信息
			for(int sheetSmallLayerNum = sheetSmallLayer.getFirstRowNum() + 2;
					sheetSmallLayerNum <= sheetSmallLayer.getLastRowNum(); ++sheetSmallLayerNum){
				//获得当前行
				XSSFRow smallLayerInfo = sheetSmallLayer.getRow(sheetSmallLayerNum);
				//遍历油井链表
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
	 * 判断表格中数据是哪种类型并返回
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
