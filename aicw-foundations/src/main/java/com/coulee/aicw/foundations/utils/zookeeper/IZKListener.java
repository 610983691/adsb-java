package com.coulee.aicw.foundations.utils.zookeeper;

import org.apache.curator.framework.CuratorFramework;

/**
 * Description: 所有需要在ZK客户端链接成功后需要做的事件， 需要实现这个接口，由上面的ZookeeperFactoryBean统一调度<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * 
 * @author oblivion
 * @version 1.0
 */
public interface IZKListener {
	
	public void executor(CuratorFramework client);
}
