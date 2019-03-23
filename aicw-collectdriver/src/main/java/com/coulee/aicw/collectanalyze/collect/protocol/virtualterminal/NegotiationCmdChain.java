package com.coulee.aicw.collectanalyze.collect.protocol.virtualterminal;

import java.util.ArrayList;

/**
 * 
 * @author liaowufeng
 * @version 1.0 定义 telnet 操作分析时的链表, 组合成一个命令的List
 * 
 */
public class NegotiationCmdChain extends ArrayList<NegotiationCmd> {

	private static final long serialVersionUID = 5346804827304689096L;

	/**
	 * 组成命令数组
	 * 
	 * @return 返回命令数组
	 */
	public byte[] getBytes() {
		byte[] rv = new byte[this.size() * NegotiationCmd.CMD_BYTE_COUNT];
		for (int i = 0; i < this.size(); i++) {
			NegotiationCmd cmd = get(i);
			rv[i * NegotiationCmd.CMD_BYTE_COUNT] = NegotiationCmd.IAC;
			rv[i * NegotiationCmd.CMD_BYTE_COUNT + 1] = cmd.option;
			rv[i * NegotiationCmd.CMD_BYTE_COUNT + 2] = cmd.optionValue;
		}
		return rv;
	}

}
