package com.coulee.aicw.collectanalyze.collect.protocol.virtualterminal;

/**
 * 
 * @author liaowufeng
 * @version 1.0 定义telnet 协议分析时的操作属性
 * 
 */
public class NegotiationCmd {
	
	/**
	 * 命令字节数
	 */
	public static final int CMD_BYTE_COUNT = 3;
	
	/**
	 * IAC 定义
	 */
	public static final byte IAC = (byte) 255;
	
	/**
	 * telnet 操作
	 */
	public byte option;
	
	/**
	 * telnet 操作值
	 */
	public byte optionValue;

}
