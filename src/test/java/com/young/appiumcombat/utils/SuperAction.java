package com.young.appiumcombat.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.testng.Assert;
/**
 * 
 * @author xy-incito-wy
 * @Description 把appium操作的变成关键字操作
 *
 */
public class SuperAction {
	public static Logger logger = Logger.getLogger(SuperAction.class);
	static String pageFileDir = "res/pages/";
	public static Alert a = null;
	/**
	 * 
	 * @param locateWay 定位方式
	 * @param locateValue 定位的方式的具体值
	 * @return 定位的方式（By）
	 */
	public static By getLocateWay(String locateWay,String locateValue){
		 By elementLocator=null;
		 	if(locateWay.equalsIgnoreCase("xpath")){
		 		elementLocator=By.xpath(locateValue);
		 	}
		 	else if(locateWay.equalsIgnoreCase("className")){
		 		elementLocator=By.className(locateValue);
		 	}
		 	else if(locateWay.equalsIgnoreCase("id")){
		 		elementLocator=By.id(locateValue);
		 	}
		 	else	if(locateWay.equalsIgnoreCase("linktext")){
		 		elementLocator=By.linkText(locateValue);
		 	}
		 	else	if(locateWay.equalsIgnoreCase("name")){
		 		elementLocator=By.name(locateValue);
		 	}
		 	else	if(locateWay.equalsIgnoreCase("css")){
		 		elementLocator=By.cssSelector(locateValue);
		 	}
		 	else	if(locateWay.equalsIgnoreCase("tagname")){
		 		elementLocator=By.tagName(locateValue);
		 	}
		 	else{
		 		Assert.fail("你选择的定位方式：["+locateWay+"] 不被支持!");
		 	}
		 	return elementLocator;
		 }
	

	/**
	 * 
	 * @param sheet - 测试用例表中的sheet
	 * @param rowIndex - 测试用例表中的行index
	 * @param locateColumnIndex - 测试用例表中的定位列的index
	 * @return 从page表中 返回定位方式和定位值
	 * @Description 根据testcase中的元素定位列，去取得page页中的 定位方式和定位值
	 */
	public static String[] getPageElementLocator(Sheet sheet,int rowIndex,int locateColumnIndex,String pageName){

			XSSFWorkbook pageBook = null;
			//定位方式
			String elementLocatorWay = null;
			//定位值
			String elementLocatorValue = null;
			//sheet表
			Sheet pageSheet = null;
			//page excel路径
			String pageFilePath = pageFileDir+pageName+".xlsx";
			//获取定位列的值
			String locator = sheet.getRow(rowIndex).getCell(locateColumnIndex).getStringCellValue();
			//用.分割开元素定位值
			String locatorSplit[] = locator.split("\\.");
		try {
			pageBook = new XSSFWorkbook(new FileInputStream(new File(pageFilePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		  pageSheet =  pageBook.getSheetAt(0); //取得第一个sheet
		 int pageRowNum =  pageSheet.getPhysicalNumberOfRows();//获得这个sheet的实际有效行数
		 for (int j = 0; j < pageRowNum; j++) {
			 //如果获取到的别名和指定的别名相同，就存储当前行的定位值和定位方式
			if(pageSheet.getRow(j).getCell(0).getStringCellValue().equalsIgnoreCase(locatorSplit[1])){
				 elementLocatorWay = pageSheet.getRow(j).getCell(1).getStringCellValue();
				 elementLocatorValue = pageSheet.getRow(j).getCell(2).getStringCellValue();
				break;
			}
		}
		return new String[]{elementLocatorWay,elementLocatorValue};
	
	}
	/**
	 * @param founction
	 *            excel文件的名字
	 * @param caseName
	 *            excel中sheet的名字
	 * @param appiumUtil
	 *            引用appiumUtil
	 * @Description 读取excel中每个sheet的操作步骤，进而生成测试用例
	 * */
	public static void parseExcel(String founction, String caseName, AppiumUtil appiumUtil,String platformName) {
		FileInputStream filePath = null;
		XSSFWorkbook workbook = null;
		String locateSplit[]  = null;//页面sheet中的定位方式和定位值拆解
		String locator = null;//用例页面的定位列
		String file = "res/testcases/android/" + founction + ".xlsx";
		try {
			filePath = new FileInputStream(file);// 读取功能模块
		} catch (FileNotFoundException e) {
			logger.error("文件：" + file + "不存在");
			Assert.fail("文件：" + file + "不存在");
		}
		try {
			workbook = new XSSFWorkbook(filePath);
		} catch (IOException e) {
			logger.error("IO异常");
			Assert.fail("IO异常");
		}
		/**取得指定的case名字*/
		Sheet sheet = workbook.getSheet(caseName);
		/**获得的实际行数*/
		int rows = sheet.getPhysicalNumberOfRows(); 
		/** excel中的测试数据*/
		String testData = null;
		//获取首行的单元格数
		int cellsNumInOneRow = sheet.getRow(0).getPhysicalNumberOfCells();
		//声明一个数组存储列值的角标
		String column[] = new String[cellsNumInOneRow];
		//声明一个迭代器
		Iterator<Cell> cell = sheet.getRow(0).iterator();
		int ii =0;
		while(cell.hasNext()){
			column[ii]= String.valueOf(cell.next()); 
			ii++;
		}
		//定义动作列的角标
		int actionColumnIndex =0;
		//定义元素定位列的角标
		int locateColumnIndex = 0;
		//定义测试数据列的角标
		int testDataColumnIndex = 0;
		//动态获取这几个关键列所在位置
		 for (int i = 0; i < column.length; i++) {
			 if(column[i].equals("动作")){
				 actionColumnIndex = i;
			 }
			 if(column[i].equals("元素定位")){
				 locateColumnIndex = i;
			 }
			 if(column[i].equals("测试数据")){
				 testDataColumnIndex = i;
			 }
			
		}

			// 循环每行的操作，根据switch来判断每行的操作是什么，然后转换成具体的代码，从第二行开始循环，因为第一行是列的说明数据。	
		for (int i = 1; i < rows; i++) {
			logger.info("正在解析excel:["+founction+".xlsx]中的sheet(用例)：["+caseName+"]的第"+i+"行步骤...");
			String action = sheet.getRow(i).getCell(actionColumnIndex).getStringCellValue();
			Row row = sheet.getRow(i);
			if (row != null) {
				switch (action) {
				case "输入":
					//先设置Cell的类型，然后就可以把纯数字作为String类型读进来了
					sheet.getRow(i).getCell(testDataColumnIndex).setCellType(Cell.CELL_TYPE_STRING);
					testData = sheet.getRow(i).getCell(testDataColumnIndex).getStringCellValue(); //测试数据
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的元素定位
					//locator.split("\\.")[0]分离出页面名称，比如HomePage.我的单据，提取出HomePage
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]); //找到定位方式、定位值
					appiumUtil.typeContent(getLocateWay(locateSplit[0], locateSplit[1]), testData);
					break;
					
				case "点击":
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的定位
					//locator.split("\\.")[0]分离出页面名称，比如HomePage.我的单据，提取出HomePage
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]);
					appiumUtil.click(getLocateWay(locateSplit[0], locateSplit[1]));
					break;
					
				case "暂停":
					//先设置Cell的类型，然后就可以把纯数字作为String类型读进来了
					sheet.getRow(i).getCell(testDataColumnIndex).setCellType(Cell.CELL_TYPE_STRING);
					testData = sheet.getRow(i).getCell(testDataColumnIndex).getStringCellValue();
					appiumUtil.pause(Integer.parseInt(testData));
					break;
					
				case "等待元素":
					//先设置Cell的类型，然后就可以把纯数字作为String类型读进来了
					sheet.getRow(i).getCell(testDataColumnIndex).setCellType(Cell.CELL_TYPE_STRING);
					testData = sheet.getRow(i).getCell(testDataColumnIndex).getStringCellValue();
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的定位
					//locator.split("\\.")[0]分离出页面名称，比如HomePage.我的单据，提取出HomePage
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]);
					appiumUtil.waitForElementToLoad(Integer.parseInt(testData), getLocateWay(locateSplit[0], locateSplit[1]));
					break;
		
					
				case "清除":
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的定位
					//locator.split("\\.")[0]分离出页面名称，比如HomePage.我的单据，提取出HomePage
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]);
					appiumUtil.clear(getLocateWay(locateSplit[0], locateSplit[1]));
					break;			
					
				case "检查文本 - 属性":
					testData = sheet.getRow(i).getCell(testDataColumnIndex).getStringCellValue();
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的定位
					//locator.split("\\.")[0]分离出页面名称，比如HomePage.我的单据，提取出HomePage
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]);
					String[] Datas = testData.split(",");
					appiumUtil.isTextCorrect(appiumUtil.getAttributeText(getLocateWay(locateSplit[0], locateSplit[1]), Datas[0]),Datas[1]);
					break;
					
					
					
				case "检查文本":
					testData = sheet.getRow(i).getCell(testDataColumnIndex).getStringCellValue();
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的定位
					//locator.split("\\.")[0]分离出页面名称，比如HomePage.我的单据，提取出HomePage
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]);
					appiumUtil.isTextCorrect(appiumUtil.getText(getLocateWay(locateSplit[0], locateSplit[1])), testData);
					break;
					

					
				case "执行JS点击":
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的定位
					//locator.split("\\.")[0]分离出页面名称，比如HomePage.我的单据，提取出HomePage
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]);
					appiumUtil.executeJS("arguments[0].click();", appiumUtil.findElement(getLocateWay(locateSplit[0], locateSplit[1])));
					break;	
				
				case "切换到context":
					testData = sheet.getRow(i).getCell(testDataColumnIndex).getStringCellValue();
					appiumUtil.switchWebview(testData);
					break;
					
				case "是否包含指定文本":
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的定位
					testData = sheet.getRow(i).getCell(testDataColumnIndex).getStringCellValue();
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]);
					appiumUtil.isContains(appiumUtil.getText(getLocateWay(locateSplit[0], locateSplit[1])), testData);
					break;
					
				case "处理欢迎和定位界面":
					locator = sheet.getRow(i).getCell(locateColumnIndex).getStringCellValue();//获取步骤中的定位			
					locateSplit = getPageElementLocator(sheet, i, locateColumnIndex,locator.split("\\.")[0]);
					boolean flag = appiumUtil.doesElementsExist(getLocateWay(locateSplit[0], locateSplit[1]));
					if(flag){
						appiumUtil.click(getLocateWay(locateSplit[0], locateSplit[1]));
					}else{
						return;
					}
					break;
					
					default:
						logger.error("你输入的操作：["+action+"]不被支持，请自行添加");
						Assert.fail("你输入的操作：["+action+"]不被支持，请自行添加");
					
				}
			}
		}
	}

}
