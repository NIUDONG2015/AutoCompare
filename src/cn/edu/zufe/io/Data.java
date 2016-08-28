package cn.edu.zufe.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.edu.zufe.model.DBigLayer;
import cn.edu.zufe.model.DSmallLayer;
import cn.edu.zufe.model.DWell;
import cn.edu.zufe.model.DWellLogs;
import cn.edu.zufe.model.DWellLogsAttribute;

public class Data {
	//测井曲线的枚举类型
	enum Menu{
		DEPTH, AC, CAL, GR, COND, RLML, 
		RNML,R04, R25 ,R4, SP , RFOC, RILD, RILM;
	}
	public static LinkedList<DWell> loadData(String urlFile) throws IOException {
		if (urlFile == null || urlFile == "") {
			return null;
		}
		LinkedList<DWell> wellList = new LinkedList<DWell>();
		try {
			InputStream is = new FileInputStream(urlFile);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

			XSSFSheet sheetWell = xssfWorkbook.getSheetAt(0); // 钻井数据表
			XSSFSheet sheetBigLayer = xssfWorkbook.getSheetAt(1); // 大层数据表
			XSSFSheet sheetSmallLayer = xssfWorkbook.getSheetAt(3); // 小层数据表

			// 读钻井数据表
			for (int sheetWellRowsNum = sheetWell.getFirstRowNum() + 2; sheetWellRowsNum <= sheetWell.getLastRowNum(); ++sheetWellRowsNum) {

				XSSFRow wellInfo = sheetWell.getRow(sheetWellRowsNum);
				XSSFCell wellName = wellInfo.getCell(0);
				String t_wellName = getValue(wellName);

				XSSFCell wellPosX = wellInfo.getCell(1);
				double t_wellPosX = Double.valueOf(getValue(wellPosX));

				XSSFCell wellPosY = wellInfo.getCell(2);
				double t_wellPosY = Double.valueOf(getValue(wellPosY));

				XSSFCell highBushing = wellInfo.getCell(3);
				double t_highBushing = Double.valueOf(getValue(highBushing));

				XSSFCell wellDepth = wellInfo.getCell(4);
				double t_wellDepth = t_wellDepth = Double.valueOf(getValue(wellDepth));

				// 获取油井编号,坐标,补心高和井深
				DWell well = new DWell();
				well.setName(t_wellName);
				well.setX(t_wellPosX);
				well.setY(t_wellPosY);
				well.setHighBushing(t_highBushing);
				well.setWellDepth(t_wellDepth);
				wellList.add(well);
			}

			// 获取油井大层信息
			for (int sheetBigLayerNum = sheetBigLayer.getFirstRowNum() + 2; sheetBigLayerNum <= sheetBigLayer.getLastRowNum(); ++sheetBigLayerNum) {
				// 获得当前行
				XSSFRow bigLayerInfo = sheetBigLayer.getRow(sheetBigLayerNum);

				// 遍历油井链表
				for (int wellNum = 0; wellNum < wellList.size(); ++wellNum) {

					DWell well = wellList.get(wellNum);

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

					// 获取大层层位和底深
					DBigLayer bigLayer = new DBigLayer();
					bigLayer.setName(getValue(bigLayerName));
					bigLayer.setDepth(t_bigLayerDepth);

					well.getBigLayers().add(bigLayer);
				}
			}

			// 获取小层信息
			for (int sheetSmallLayerNum = sheetSmallLayer.getFirstRowNum() + 2; sheetSmallLayerNum <= sheetSmallLayer.getLastRowNum(); ++sheetSmallLayerNum) {
				// 获得当前行
				XSSFRow smallLayerInfo = sheetSmallLayer.getRow(sheetSmallLayerNum);
				// 遍历油井链表
				for (int wellNum = 0; wellNum < wellList.size(); ++wellNum) {
					DWell well = wellList.get(wellNum);
					if (well.getName().equals(getValue(smallLayerInfo.getCell(0))) == false) {
						continue;
					}

					for (int bigLayerNum = 0; bigLayerNum < well.getBigLayers().size(); ++bigLayerNum) {
						DBigLayer bigLayer = well.getBigLayers().get(bigLayerNum);
						String strBigLayer = getValue(smallLayerInfo.getCell(1));
						// 特判(因为小层数据的层位Ng10 等价于 大层数据的层位Ngb)
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

							DSmallLayer smallLayer = new DSmallLayer();
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

			// 输出xls数据
			// for (int i = 0; i < wellList.size(); ++i) {
			// DWell well = wellList.get(i);
			// System.out.println("井号:" + well.getName() + " X:" + well.getX() +
			// " Y:" + well.getY());
			// for (int j = 0; j < well.getBigLayers().size(); ++j) {
			// DBigLayer bigLayer = well.getBigLayers().get(j);
			// System.out.println(" 层位:" + bigLayer.getName() + " 底深(MD):" +
			// bigLayer.getDepth()[0]);
			// for (int k = 0; k < bigLayer.getSmallLayers().size(); ++k) {
			// DSmallLayer smallLayer = bigLayer.getSmallLayers().get(k);
			// System.out.println(" 层位:" + smallLayer.getName() + " 砂岩顶深:" +
			// smallLayer.getDepth()[0]
			// + " 砂岩底深:"+smallLayer.getDepth()[1] + "
			// 电解结果:"+smallLayer.getEleResult());
			// }
			// System.out.println("");
			// }
			// System.out.println("");
			// }

			String xlsfolderPath = urlFile.substring(0, urlFile.lastIndexOf("\\"));
			String newURLFile = xlsfolderPath + "\\测井曲线";
			String encoding = "GBK";
			File file = new File(newURLFile);
			if (file.isDirectory()) {
				File[] dirFile = file.listFiles();
				if (dirFile != null) {
					for (File f : dirFile) {
						InputStreamReader read = new InputStreamReader(new FileInputStream(f), encoding);// 考虑到编码格式
						BufferedReader bufferedReader = new BufferedReader(read);

						String wellName = f.getName(); // 获得井号
						
						System.out.println();
						System.out.println(wellName);
						
						int dotPos = wellName.indexOf('.');
						wellName = wellName.substring(0, dotPos);

						// System.out.println(wellName);
						// DWell well = null; // 根据井号获得对应井
						int itCnt = 0;	//#MNEM.UNIT
						int id = 0;
						for (int i = 0; i < wellList.size(); ++i) {
							if (wellList.get(i).getName().equals(wellName))
								id = i;
						}

						DWellLogs wellLogs = new DWellLogs();
						Menu menuArray[] = new Menu[99];
						String lineTxt = null;
						boolean flag = false;

						int testRow = -1;
						while ((lineTxt = bufferedReader.readLine()) != null) {
							lineTxt.replaceAll(" +"," ");
							if (flag == true) { // 遍历到数据部分
								DWellLogsAttribute wellLogsAttribute = new DWellLogsAttribute();

								String strNum[] = lineTxt.split(" ");
								int cnt = 0;
								
								
								for (int i = 0; i < strNum.length; ++i) {

									if (strNum[i].equals("") == false) {
										
										double num = 0;
										try {
											num = Double.valueOf(strNum[i]);
											if(testRow < 10){
												System.out.print(num+" ");
												}
										} catch (Exception e) {
											continue;
										}
										
										switch (menuArray[cnt]) {
										case DEPTH:
											wellLogsAttribute.getDEPTH().add(num);
											break;
										case AC:
											wellLogsAttribute.getAC().add(num);
											break;
										case CAL:
											wellLogsAttribute.getCAL().add(num);
											break;
										case GR:
											wellLogsAttribute.getGR().add(num);
											break;
										case COND:
											wellLogsAttribute.getCOND().add(num);
											break;
										case RLML:
											wellLogsAttribute.getRLML().add(num);
											break;

											//RNML,R04, R25 ,R4, SP , RFOC, RILD, RILM;
										case RNML:
											wellLogsAttribute.getRNML().add(num);
											break;
										case R04:
											wellLogsAttribute.getR04().add(num);
											break;
										case R25:
											wellLogsAttribute.getR25().add(num);
											break;
										case R4:
											wellLogsAttribute.getR4().add(num);
											break;
										case SP:
											wellLogsAttribute.getSP().add(num);
											break;
										case RFOC:
											wellLogsAttribute.getRFOC().add(num);
											break;
										case RILD:
											wellLogsAttribute.getRILD().add(num);
										case RILM:
											wellLogsAttribute.getRILM().add(num);
										default:
											break;
										}
										++cnt;
									}
								}
								testRow++;
								if(testRow <= 10)
								System.out.println();
								wellLogs.getmpWellLogs().put(wellLogsAttribute.getDEPTH(), wellLogsAttribute);
							}

							if (lineTxt.contains("~A")) {
								flag = true;
								String strName[] = lineTxt.split(" ");
								//记录文件中测井曲线的类型
								for (int i = 0; i < strName.length; ++i) {
									if (strName[i].equals("DEPTH") == true) {
										menuArray[itCnt++] = Menu.DEPTH;
									}
									else if(strName[i].equals("AC") == true){
										menuArray[itCnt++] = Menu.AC;
									}
									else if(strName[i].equals("CAL") == true){
										menuArray[itCnt++] = Menu.CAL;
									}
									else if(strName[i].equals("GR") == true){
										menuArray[itCnt++] = Menu.GR;
									}
									else if(strName[i].equals("COND") == true){
										menuArray[itCnt++] = Menu.COND;
									}
									else if(strName[i].equals("RLML") == true){
										menuArray[itCnt++] = Menu.RLML;
									}
									else if(strName[i].equals("RNML") == true){
										menuArray[itCnt++] = Menu.RNML;
									}
									else if(strName[i].equals("R04") == true){
										menuArray[itCnt++] = Menu.R04;
									}
									else if(strName[i].equals("R25") == true){
										menuArray[itCnt++] = Menu.R25;
									}
									else if(strName[i].equals("R4") == true){
										menuArray[itCnt++] = Menu.R4;
									}
									else if(strName[i].equals("SP") == true){
										menuArray[itCnt++] = Menu.SP;
									}
									else if(strName[i].equals("RFOC") == true){
										menuArray[itCnt++] = Menu.RFOC;
									}
									else if(strName[i].equals("RILD") == true){
										menuArray[itCnt++] = Menu.RILD;
									}
									else if(strName[i].equals("RILM") == true){
										menuArray[itCnt++] = Menu.RILM;
									}
								}
								
								for(int i=0; i<itCnt; ++i){
									System.out.print(menuArray[i]+" ");
								}
								System.out.println();
							}

						}
						read.close();
						wellList.get(id).setWellLogs(wellLogs);
					}
				}
			} else {
				System.out.println(file.getPath() + " 没有找到");
			}

			// 输出测井曲线
//			for (DWell tWell : wellList) {
//				if (tWell.getWellLogs() == null) {
//					continue;
//				}
//
//				for (Map.Entry<Double, DWellLogsAttribute> entry : tWell.getWellLogs().getmpWellLogs().entrySet()) {
//					DWellLogsAttribute tDWA = entry.getValue();
//					System.out.println(tDWA.getDEPTH() + " " + tDWA.getAC() + " " + tDWA.getCAL1() + " " + tDWA.getCAL2() + " " + tDWA.getCOND() + " "
//							+ tDWA.getRLML() + " " + tDWA.getRNML() + " " + tDWA.getR04() + " " + tDWA.getR25() + " " + tDWA.getR4() + " " + tDWA.getSP1()
//							+ " " + tDWA.getSP2());
//				}
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return wellList;
	}

	/**
	 * 
	 * 判断表格中数据是哪种类型并返回
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
