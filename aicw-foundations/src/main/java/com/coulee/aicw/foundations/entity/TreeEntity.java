package com.coulee.aicw.foundations.entity;

import java.util.List;
import java.util.Map;

/**
 * Description: 树数据结构对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class TreeEntity extends BaseEntity {

	private static final long serialVersionUID = -8471368464705422776L;

	/**
	 * 树节点ID
	 */
	protected String nodeId;
	
	/**
	 * 树节点名
	 */
	protected String text;
	
	/**
	 *  树节点初始状态
	 *  state.checked	Boolean，默认值false	指示一个节点是否处于checked状态，用一个checkbox图标表示。
	 *	state.disabled	Boolean，默认值false	指示一个节点是否处于disabled状态。（不是selectable，expandable或checkable）
	 *	state.expanded	Boolean，默认值false	指示一个节点是否处于展开状态。
	 *	state.selected	Boolean，默认值false	指示一个节点是否可以被选择。
	 */
	protected Map<String, Boolean> state;
	
	/**
	 * 树子功能节点
	 */
	protected List<?> nodes;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Map<String, Boolean> getState() {
		return state;
	}

	public void setState(Map<String, Boolean> state) {
		this.state = state;
	}

	public List<?> getNodes() {
		return nodes;
	}

	public void setNodes(List<?> nodes) {
		this.nodes = nodes;
	}
	
}
