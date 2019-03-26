package com.coulee.foundations.configserver.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.coulee.aicw.foundations.utils.common.FilePathTools;
import com.coulee.foundations.configserver.entity.Location;

public class ReadTxt {

	private static final String FLY_DATA_LAT_NAME = "latData.txt";
	private static final String FLY_DATA_LON_NAME = "lonData.txt";
	
	private static final String INIT_FLY_DATA_LAT_NAME = "initlatData.txt";
	private static final String INIT_FLY_DATA_LON_NAME = "initlonData.txt";

	
	private static String readStream(InputStream in) {
		try {
			// <1>创建字节数组输出流，用来输出读取到的内容
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// <2>创建缓存大小
			byte[] buffer = new byte[1024]; // 1KB
			// 每次读取到内容的长度
			int len = -1;
			// <3>开始读取输入流中的内容
			while ((len = in.read(buffer)) != -1) { // 当等于-1说明没有数据可以读取了
				baos.write(buffer, 0, len); // 把读取到的内容写到输出流中
			}
			// <4> 把字节数组转换为字符串
			String content = baos.toString();
			// <5>关闭输入流和输出流
			in.close();
			baos.close();
			// <6>返回字符串结果
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static  List<List<Location>> readresult() throws IOException{
		List<List<Location>> result = new ArrayList<>();
		FileInputStream latInputStream = new FileInputStream(getFlyDataFilePath(FLY_DATA_LAT_NAME));
		FileInputStream lonInputStream = new FileInputStream(getFlyDataFilePath(FLY_DATA_LON_NAME));
		String latdata= readStream(latInputStream);
		latdata=latdata.replaceAll("\r\n", "");
		String londata = readStream(lonInputStream);
		londata=londata.replaceAll("\r\n", "");
		String[] latRows = latdata.split(";");
		String[] lonRows = londata.split(";");
		if(latRows.length!=lonRows.length) {//行数不同，表示飞机经纬度不一致可能有错位，因此直接丢弃掉这次读取。
			return null;
		}
		int planeCounts = latRows.length;//飞机架数
		for (int i=0;i<planeCounts;i++) {//遍历
			List<Location> onePlane = new ArrayList<>();
			String[] onePlaneLons = lonRows[i].split(",");//一架飞机的所有经度
			String[] onePlaneLats = latRows[i].split(",");//一架飞机的所有纬度
			if(onePlaneLons.length!=onePlaneLats.length) {//这架飞机的经纬度数量不一致表明有丢失数据，因此丢弃这架飞机的数据
				continue;
			}
			int columns =onePlaneLons.length;//列数代表采样的经纬度点的数量
			for (int j = 0; j < columns; j++) {
				Location local = new Location(onePlaneLats[j],onePlaneLons[j]);
				onePlane.add(local);//飞机的一个点添加
			}
			//遍历完一行代表这个飞机的数据获取完成，因此要把这个飞机添加到返回列表中
			result.add(onePlane);
		}
		return result;
	}
	
	private static String getFlyDataFilePath(String filename) {
		String dbFilePath = null;
		if (FilePathTools.isDeployModel()) {
			File dbFolder = new File("data");
			if (!dbFolder.exists()) {
				dbFolder.mkdir();
			}
			File dbFile = new File(dbFolder, filename);
			if (!dbFile.exists()) {
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				try {
					InputStream in = resolver.getResource(filename).getInputStream();
					FileUtils.copyInputStreamToFile(in, dbFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			dbFilePath = dbFile.getAbsolutePath();
		} else {
			String resourcesPath = "src".concat(File.separator).concat("main").concat(File.separator).concat("resources")
					.concat(File.separator).concat(filename);
			File dbFile = new File(resourcesPath);
			dbFilePath = dbFile.getAbsolutePath();
		}
		return dbFilePath;
	}
	
	

	public static List<Location> readInitLocations() throws IOException{
		List<Location> result = new ArrayList<>();
		FileInputStream latInputStream = new FileInputStream(getFlyDataFilePath(INIT_FLY_DATA_LAT_NAME));
		FileInputStream lonInputStream = new FileInputStream(getFlyDataFilePath(INIT_FLY_DATA_LON_NAME));
		String latdata= readStream(latInputStream);
		latdata=latdata.replaceAll("\r\n", "");
		String londata = readStream(lonInputStream);
		londata=londata.replaceAll("\r\n", "");
		String[] latRows = latdata.split(";");
		String[] lonRows = londata.split(";");
		if(latRows.length!=lonRows.length) {//行数不同，表示飞机经纬度不一致可能有错位，因此直接丢弃掉这次读取。
			return null;
		}
		int planeCounts = latRows.length;//飞机架数
		for (int i=0;i<planeCounts;i++) {//遍历
			String[] onePlaneLons = lonRows[i].split(",");//一架飞机的所有经度
			String[] onePlaneLats = latRows[i].split(",");//一架飞机的所有纬度
			if(onePlaneLons.length!=onePlaneLats.length) {//这架飞机的经纬度数量不一致表明有丢失数据，因此丢弃这架飞机的数据
				continue;
			}
			int columns =onePlaneLons.length;//列数代表采样的经纬度点的数量
			for (int j = 0; j < columns; j++) {
				Location local = new Location(onePlaneLats[j],onePlaneLons[j]);
				result.add(local);//飞机的一个点添加
			}
		}
		return result;
	}
	
	
	public static List<Location> readInitHeatMapLocations() throws IOException{
		List<Location> result = new ArrayList<>();
		FileInputStream latInputStream = new FileInputStream(getFlyDataFilePath(INIT_FLY_DATA_LAT_NAME));
		FileInputStream lonInputStream = new FileInputStream(getFlyDataFilePath(INIT_FLY_DATA_LON_NAME));
		String latdata= readStream(latInputStream);
		latdata=latdata.replaceAll("\r\n", "");
		String londata = readStream(lonInputStream);
		londata=londata.replaceAll("\r\n", "");
		String[] latRows = latdata.split(";");
		String[] lonRows = londata.split(";");
		if(latRows.length!=lonRows.length) {//行数不同，表示飞机经纬度不一致可能有错位，因此直接丢弃掉这次读取。
			return null;
		}
		int planeCounts = latRows.length;//飞机架数
		for (int i=0;i<planeCounts;i++) {//遍历
			String[] onePlaneLons = lonRows[i].split(",");//一架飞机的所有经度
			String[] onePlaneLats = latRows[i].split(",");//一架飞机的所有纬度
			if(onePlaneLons.length!=onePlaneLats.length) {//这架飞机的经纬度数量不一致表明有丢失数据，因此丢弃这架飞机的数据
				continue;
			}
			int columns =onePlaneLons.length;//列数代表采样的经纬度点的数量
			for (int j = 0; j < columns; j++) {
				Location local = new Location(onePlaneLats[j],onePlaneLons[j],1);
				result.add(local);//飞机的一个点添加
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
