package com.coulee.aicw.util;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.hssf.util.HSSFColor;

public class HSSFTools {
	/**
	 * 
	 * @param hwb
	 * @param fontName
	 * @param fontHeight
	 * @param fontColor
	 * @param isBold
	 * @param isItalic
	 * @param isUnderline
	 * @return
	 */
	public static HSSFFont setHSSFont(HSSFWorkbook hwb, String fontName, short fontHeight, 
			Object fontColor, boolean isBold, boolean isItalic, boolean isUnderline) {
		HSSFFont hfont = hwb.createFont();

		// 设置字体样式
		if (!"".equals(fontName)) {
			hfont.setFontName(fontName);
		} else {
			hfont.setFontName("宋体");
		}

		// 设置字体高度
		hfont.setFontHeightInPoints(fontHeight); // 参数格式： (short)12

		// 设置字体颜色
		if (fontColor instanceof String) {
			if ((fontColor + "").indexOf("蓝") > -1) {
				hfont.setColor(HSSFColor.BLUE.index);
			} else if ((fontColor + "").indexOf("红") > -1) {
				hfont.setColor(HSSFColor.RED.index);
			} else if ((fontColor + "").indexOf("绿") > -1) {
				hfont.setColor(HSSFColor.GREEN.index);
			} else {
				hfont.setColor(HSSFColor.BLACK.index);
			}
		} else if (fontColor instanceof Short) {
			hfont.setColor(Short.valueOf(fontColor + ""));
		} else {
			hfont.setColor(HSSFColor.BLACK.index);
		}

		// 设置粗体
		if (isBold) {
			hfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}

		// 设置斜体
		hfont.setItalic(isItalic);

		// 设置下划线
		if (isUnderline) {
			hfont.setUnderline(HSSFFont.U_SINGLE);
		}

		return hfont;
	}
	/**
	 * 
	 * @param patr
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param c1
	 * @param r1
	 * @param c2
	 * @param r2
	 * @param comment
	 * @param author
	 * @return
	 */
	public static HSSFComment setHSSFComment(HSSFPatriarch patr, short x1, short y1, short x2, short y2, 
			short c1, short r1, short c2, short r2, String comment, String author){
		HSSFComment hssfComment = patr.createComment(new HSSFClientAnchor(x1, y1, x2, y2, c1, r1, c2, r2));
		hssfComment.setString(new HSSFRichTextString(comment));
		hssfComment.setAuthor(author);
		return hssfComment;
	}
	/**
	 * 设置下拉框
	 * @param i
	 * @param hwb
	 * @param sheet
	 * @param dictionarySheet
	 * @param dictionarySheetName
	 * @param dictionaryArray
	 */
	public static void createSelect(int i, HSSFWorkbook hwb, HSSFSheet sheet, HSSFSheet dictionarySheet, 
			String dictionarySheetName, String[] dictionaryArray){
		for (int j=0; j<dictionaryArray.length; j++) {
			HSSFRow r = dictionarySheet.getRow(j);
			if (r == null) {
				r = dictionarySheet.createRow(j);
			}
			HSSFCell c = dictionarySheet.getRow(j).getCell(i);
			if (c == null) {
				c = r.createCell(i);
			}
			c.setCellValue(dictionaryArray[j]);
		}
		HSSFName range = hwb.createName();
		String s = null;
		if (i < 26) {
			s = dictionarySheetName + "!$" + (char) (i + 65) + "$1:"
					+ "$" + (char) (i + 65) + "$" + dictionaryArray.length;
		} else if (i >= 26 && i < 52) {
			s = dictionarySheetName + "!$A" + (char) (i + 65 - 26)
					+ "$1:" + "$A" + (char) (i + 65 - 26) + "$"
					+ dictionaryArray.length;
		} else if (i >= 52 && i < 78) {
			s = dictionarySheetName + "!$B" + (char) (i + 65 - 52)
					+ "$1:" + "$B" + (char) (i + 65 - 52) + "$"
					+ dictionaryArray.length;
		}
		range.setRefersToFormula(s);
		range.setNameName(dictionarySheetName + i);
		
		// 生成下拉列表
		// (起始行序号，终止行序号，起始列序号，终止列序号)
		CellRangeAddressList regions = new CellRangeAddressList(1, 65535, i, i);

		// 生成下拉框内容
		DVConstraint constraint = DVConstraint.createFormulaListConstraint(dictionarySheetName + i);
		// 绑定下拉框和作用区域
		HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
		// 对sheet页生效
		sheet.addValidationData(data_validation);
	}
}
