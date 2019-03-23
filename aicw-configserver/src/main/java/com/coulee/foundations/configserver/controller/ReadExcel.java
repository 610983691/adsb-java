package com.coulee.foundations.configserver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.coulee.aicw.foundations.utils.common.FilePathTools;
import com.coulee.foundations.ConfigServerConstants;
import com.coulee.foundations.configserver.entity.Location;

public class ReadExcel {

	private static final String FLY_DATA_NAME = "fly_data.xlsx";

	private static String getFlyDataFilePath() {
		String flyDataPath = null;
		if (FilePathTools.isDeployModel()) {
			File dbFolder = new File("data");
			if (!dbFolder.exists()) {
				dbFolder.mkdir();
			}
			File flydataFile = new File(dbFolder, FLY_DATA_NAME);
			if (!flydataFile.exists()) {
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				try {
					InputStream in = resolver.getResource(FLY_DATA_NAME).getInputStream();
					FileUtils.copyInputStreamToFile(in, flydataFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			flyDataPath = flydataFile.getAbsolutePath();
		} else {
			String resourcesPath = "src".concat(File.separator).concat("main").concat(File.separator)
					.concat("resources").concat(File.separator).concat(FLY_DATA_NAME);
			File dbFile = new File(resourcesPath);
			flyDataPath = dbFile.getAbsolutePath();
		}
		return flyDataPath;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private static boolean isExcel2003(String filePath) {

		return filePath.matches("^.+\\.(?i)(xls)$");

	}

	/**
	 * 
	 * @描述：是否是2007的excel，返回true是2007
	 * 
	 * @作者：zyj
	 * 
	 * @参数：@param filePath 文件完整路径
	 * 
	 * @参数：@return
	 * 
	 * @返回值：boolean
	 */

	private static boolean isExcel2007(String filePath) {

		return filePath.matches("^.+\\.(?i)(xlsx)$");

	}

	public static List<Location> read() throws IOException {
		Workbook wb = null;
		String filePath = getFlyDataFilePath();
		FileInputStream inputStream = new FileInputStream(filePath);
		if (isExcel2003(FLY_DATA_NAME)) {
			wb = new HSSFWorkbook(inputStream);
		} else {
			wb = new XSSFWorkbook(inputStream);
		}
		return read(wb);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param wb
	 * @return List<Map>
	 * @throws IOException
	 */
	private static List<Location> read(Workbook wb) throws IOException {
		List<Location> dataLst = new ArrayList<>();
		/** 得到第一个shell */
		Sheet sheet = wb.getSheetAt(0);
		/** 得到Excel的行数 */
		int totalRows = sheet.getPhysicalNumberOfRows();
		int totalCells = 0;
		/** 得到Excel的列数 */
		if (totalRows >= 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		Row firstRow = sheet.getRow(0);// 第一行一定要放維度信息
		Row secondRow = sheet.getRow(1);// 第二行放經度信息
		/** 循环Excel的列 */
		for (int c = 2; c < totalCells; c++) {// 前面兩列不要
			Cell latCell = firstRow.getCell(c);
			Cell lonCell = secondRow.getCell(c);
			String lat = readCell(latCell);
			String lon = readCell(lonCell);
			Location local = new Location(lat,lon);
			dataLst.add(local);
		}
		wb.close();
		return dataLst;
	}

	private static String readCell(Cell cell) {
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return "";
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		default:
			return "";
		}
	}
}
