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

	private static final String FLY_DATA_LAT_NAME = "fly_data_lat.xlsx";
	private static final String FLY_DATA_LON_NAME = "fly_data_lon.xlsx";

	private static String getFlyDataFilePath(String filename) {
		String flyDataPath = null;
		if (FilePathTools.isDeployModel()) {
			File dbFolder = new File("data");
			if (!dbFolder.exists()) {
				dbFolder.mkdir();
			}
			File flydataFile = new File(dbFolder, filename);
			if (!flydataFile.exists()) {
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				try {
					InputStream in = resolver.getResource(filename).getInputStream();
					FileUtils.copyInputStreamToFile(in, flydataFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			flyDataPath = flydataFile.getAbsolutePath();
		} else {
			String resourcesPath = "src".concat(File.separator).concat("main").concat(File.separator)
					.concat("resources").concat(File.separator).concat(filename);
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

	public static List<List<Location>> read() throws IOException {
		Workbook wbLat = null;
		Workbook wbLon = null;
		String latDataFileName = getFlyDataFilePath(FLY_DATA_LAT_NAME);
		String lonDataFileName = getFlyDataFilePath(FLY_DATA_LON_NAME);
		
		FileInputStream latInputStream = new FileInputStream(latDataFileName);
		FileInputStream lonInputStream = new FileInputStream(lonDataFileName);
		if (isExcel2003(FLY_DATA_LAT_NAME)) {
			wbLat = new HSSFWorkbook(latInputStream);
		} else {
			wbLat = new XSSFWorkbook(latInputStream);
		}
		if (isExcel2003(FLY_DATA_LON_NAME)) {
			wbLon = new HSSFWorkbook(lonInputStream);
		} else {
			wbLon = new XSSFWorkbook(lonInputStream);
		}
		return read(wbLon,wbLat);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param wabLon
	 * @return List<Map>
	 * @throws IOException
	 */
	private static List<List<Location>> read(Workbook wabLon,Workbook wabLat) throws IOException {
		List<List<Location>> result = new ArrayList<>();
		/** 得到第一个shell */
		Sheet lonSheet = wabLon.getSheetAt(0);
		/** 得到Excel的行数 */
		int totalLonRows = lonSheet.getPhysicalNumberOfRows();
		int totalLonCells = 0;
		/** 得到Excel的列数 */
		if (totalLonRows >= 1 && lonSheet.getRow(0) != null) {
			totalLonCells = lonSheet.getRow(0).getPhysicalNumberOfCells();
		}
		
		/* 得到第一个shell */
		Sheet latSheet = wabLat.getSheetAt(0);
		/** 得到Excel的行数 */
		int totalLatRows = latSheet.getPhysicalNumberOfRows();
		int totalLatCells = 0;
		/** 得到Excel的列数 */
		if (totalLatRows >= 1 && latSheet.getRow(0) != null) {
			totalLatCells = latSheet.getRow(0).getPhysicalNumberOfCells();
		}
		
		//行数不等或者列数不等，说明有错位，就不读取，直接返回。
		if(totalLatRows!=totalLonRows||totalLatCells!=totalLonCells){
			return null;
		}
		
		for (int i = 0; i < totalLatRows; i++) {//一行一行的读
			List<Location> onePlane = new ArrayList<>();
			Row lonRow = lonSheet.getRow(i);//经度行
			Row latRow = latSheet.getRow(i);//维度行
			for (int j = 2; j < totalLonCells; j++) {//前面两列不要
				Cell latCell = latRow.getCell(j);
				Cell lonCell = lonRow.getCell(j);
				String lat = readCell(latCell);
				String lon = readCell(lonCell);
				Location local = new Location(lat,lon);
				onePlane.add(local);
			}
			result.add(onePlane);//读完一行表示一个飞机的信息读取完成
		}
		return result;
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
