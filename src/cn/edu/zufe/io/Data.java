package cn.edu.zufe.io;

import java.util.LinkedList;

import cn.edu.zufe.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;

public class Data {
	public static LinkedList<Well> g_WellList = new LinkedList<Well>();
	public static boolean loadData(String urlFile)
	{
		jxl.Workbook readwb = null;
		try
		{
			InputStream instream = new FileInputStream(urlFile);
			readwb = Workbook.getWorkbook(instream);
			
			//获取完钻井数据表
			Sheet sheet0 = readwb.getSheet(0);
			//获取Sheet表中所包含的总列数
			int rsColumns0 = sheet0.getColumns();
			//获得总行数
			int rsRows0 = sheet0.getRows();
			
			for(int i = 0; i < rsRows0; ++i)
			{
				Cell cell = sheet0.getCell(i,0);
				System.out.println(cell.getContents()+"");
			}
		}
		catch(Exception e)
		{
			System.out.println("no");
		}
		return true;
	}
}
