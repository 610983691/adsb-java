package com.coulee.aicw.foundations.utils.excel;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.coulee.aicw.foundations.utils.common.BaseTools;

/**
 * 扩展导入exl，增加不同版本的解析功能
 * 2003,2007两个不同版本
 * @author zyj
 *
 */
public class ImportExcel {
	/** 总行数 */

	private int totalRows = 0;

	/** 总列数 */

	private int totalCells = 0;

	/** 错误信息 */

	private String errorInfo;

	/** 解析文件的sheet号 */
	private int sheetNo;
	

	/**
	 * 解析exl的列头信息，用来做返回行项map的key
	 */
	private int headRow;
	/** 构造方法 */

	public ImportExcel()
	{

	}

	/**
	 * 
	 * @描述：得到总行数
	 * 
	 * @作者：zyj
	 * 
	 * @时间：
	 * 
	 * @参数：@return
	 * 
	 * @返回值：int
	 */

	public int getTotalRows()
	{

		return totalRows;

	}

	/**
	 * 
	 * @描述：得到总列数
	 * 
	 * @作者：zyj
	 * 
	 * @时间：
	 * 
	 * @参数：@return
	 * 
	 * @返回值：int
	 */

	public int getTotalCells()
	{

		return totalCells;

	}

	/**
	 * 
	 * @描述：得到错误信息
	 * 
	 * @作者：zyj
	 * 
	 * @时间：
	 * 
	 * @参数：@return
	 * 
	 * @返回值：String
	 */

	public String getErrorInfo()
	{

		return errorInfo;

	}

	/**
	 * 
	 * @描述：是否是2003的excel，返回true是2003
	 * 
	 * @作者：zyj
	 * 
	 * @参数：@param filePath 文件完整路径
	 * 
	 * @参数：@return
	 * 
	 * @返回值：boolean
	 */

	public static boolean isExcel2003(String filePath)
	{

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

	public static boolean isExcel2007(String filePath)
	{

		return filePath.matches("^.+\\.(?i)(xlsx)$");

	}

	/**
	 * 
	 * @描述：验证excel文件
	 * 
	 * @作者：zyj
	 * 
	 * @参数：@param filePath 文件完整路径
	 * 
	 * @参数：@return
	 * 
	 * @返回值：boolean
	 */

	public boolean validateExcel(String filePath)
	{

		/** 检查文件名是否为空或者是否是Excel格式的文件 */

		if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath)))
		{

			errorInfo = "文件名不是excel格式";

			return false;

		}

		/** 检查文件是否存在 */

		File file = new File(filePath);

		if (file == null || !file.exists())
		{

			errorInfo = "文件不存在";

			return false;

		}

		return true;

	}


	/**
	 * 根据文件名读取excel文件
	 * @param filePath
	 * @param sheetNo exl要解析的sheet页号 第一页 0
	 * @param headRow 列属性信息头的行号，第一行 0
	 * @return
	 */
	public List<Map> read(String filePath,int sheetNo,int headRow)
	{

		List<Map> dataLst = new ArrayList<Map>();

		InputStream is = null;
		this.sheetNo = sheetNo;
		this.headRow = headRow;
		try
		{

			/** 验证文件是否合法 */

			if (!validateExcel(filePath))
			{

				System.out.println(errorInfo);

				return null;

			}

			/** 判断文件的类型，是2003还是2007 */

			boolean isExcel2003 = true;

			if (isExcel2007(filePath))
			{

				isExcel2003 = false;

			}

			/** 调用本类提供的根据流读取的方法 */

			File file = new File(filePath);

			is = new FileInputStream(file);

			dataLst = read(is, isExcel2003);

			is.close();

		} catch (Exception ex)
		{

			ex.printStackTrace();

		} finally
		{

			if (is != null)
			{

				try
				{

					is.close();

				} catch (Exception e)
				{

					is = null;

					e.printStackTrace();

				}

			}

		}

		/** 返回最后读取的结果 */

		return dataLst;

	}

	/**
	 * 
	 * @描述：根据流读取Excel文件
	 * 
	 * @作者：zyj
	 * 
	 * @参数：@param inputStream
	 * 
	 * @参数：@param isExcel2003
	 * 
	 * @参数：@return
	 * 
	 * @返回值：List
	 */

	public List<Map> read(InputStream inputStream, boolean isExcel2003)
	{

		List<Map> dataLst = null;

		try
		{

			/** 根据版本选择创建Workbook的方式 */

			Workbook wb = null;

			if (isExcel2003)
			{
				wb = new HSSFWorkbook(inputStream);
			} else
			{
				wb = new XSSFWorkbook(inputStream);
			}
			dataLst = read(wb);

		} catch (Exception e)
		{

			e.printStackTrace();

		}

		return dataLst;

	}

	/**
	 * 读取文件内容
	 * @param wb
	 * @return List<Map>
	 */
	private List<Map> read(Workbook wb)
	{

		List<Map> dataLst = new ArrayList<Map>();

		/** 得到第一个shell */

		Sheet sheet = wb.getSheetAt(this.sheetNo);

		/** 得到Excel的行数 */

		this.totalRows = sheet.getPhysicalNumberOfRows();

		/** 得到Excel的列数 */

		if (this.totalRows >= 1 && sheet.getRow(0) != null)
		{

			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();

		}
		// 导入表的第一行作为头信息
		Map headColMap = new HashMap();
		Row headRow = sheet.getRow(this.headRow);
		/** 循环Excel的列 */

		for (int c = 0; c < this.getTotalCells(); c++)
		{
			Cell cell = headRow.getCell(c);
			String key = cell.getRichStringCellValue().getString();
			headColMap.put(c, key);
		}

		/** 循环Excel的行第一行除外其余行项信息 */
		int startRow = this.headRow + 1;
		for (int r = startRow; r < this.totalRows; r++)
		{

			Row row = sheet.getRow(r);

			if (row == null)
			{

				continue;

			}

			Map itemMap = new HashMap();
			/** 循环Excel的列 */
			String cellValue = null;
			for (int c = 0; c < this.getTotalCells(); c++)
			{

				Cell cell = row.getCell(c);
				
				cellValue = BaseTools.getDefStr(getCellValue(cell));
				
				itemMap.put(headColMap.get(c), cellValue);
			}

			/** 保存第r行的第c列 */

			dataLst.add(itemMap);

		}

		return dataLst;

	}
	

	/**
	 * 获取单元格内容
	 * @param cell
	 * @return
	 */
	private Object getCellValue(Cell cell)
	{
		Object result = null;

		if (cell != null)
		{

			switch (cell.getCellType())
			{

			case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型

				if (HSSFDateUtil.isCellDateFormatted(cell))
				{// 处理日期格式、时间格式

					SimpleDateFormat sdf = null;

					if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm"))
					{

						sdf = new SimpleDateFormat("HH:mm");

					} else
					{ // 日期

						sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					}
					Date date = cell.getDateCellValue();

//					System.out.println("处理日期格式:" + date);

					result = sdf.format(date);
				} else if (cell.getCellStyle().getDataFormat() == 58)
				{
					// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					double value = cell.getNumericCellValue();

//					System.out.println("处理自定义时间格式:" + value);

					Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);

					result = sdf.format(date);

				} else
				{
					double value = cell.getNumericCellValue();

//					System.out.println("获取数值:" + value);

					CellStyle style = cell.getCellStyle();

					DecimalFormat format = new DecimalFormat();
					String temp = style.getDataFormatString();

//					System.out.println("处理样式:" + style);
//					System.out.println("处理格式:" + format);
//					System.out.println("处理字符串:" + temp);

					// 单元格设置成常规
					if (temp.equals("General"))
					{
						format.applyPattern("#");
					}
					result = format.format(value);
				}
				break;
			case HSSFCell.CELL_TYPE_STRING:// String类型
				result = cell.getRichStringCellValue().toString();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				result = "";
			default:
				result = "";
				break;
			}

		}
		return result;
	}
}
