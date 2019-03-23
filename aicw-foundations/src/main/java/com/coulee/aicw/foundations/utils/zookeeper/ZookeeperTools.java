package com.coulee.aicw.foundations.utils.zookeeper;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 在Spring Context加载过程中
 * 		创建Zookeeper连接对像并设置触发监听，
 * 		通过Curator封装<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ZookeeperTools {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CuratorFramework zkClient;
    private List<IZKListener> listeners;
    /**
     * zookeeper服务端地址.(例如：127.0.0.1:2181,127.0.0.2:2181)
     */
    private String zkConnectionString;
    /** 
	  * Description: 
	  * 		设置Zookeeper启动后需要调用的监听，或者需要做的初始化工作.
	  * @param：
	  * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
    public void setListeners(List<IZKListener> listeners) {
    	this.listeners = listeners;
    }
    /** 
	  * Description: 
	  * 		设置ZK连接串.
	  * @param：
	  * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
	public void setZkConnectionString(String zkConnectionString) {
		this.zkConnectionString = zkConnectionString;
	}
	/** 
	  * Description: 
	  * 	初始化一个zk连接
	  * @param：
	  * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
	public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        zkClient = createWithOptions(zkConnectionString, retryPolicy, 2000, 10000);
        registerListeners(zkClient);
		zkClient.start();
	}
	/** 
	  * Description: 
	  * 	查询一个节点的值.
	  * @param：
	  * 	path	指定要查询的节点路径.
	  * @return
	  * 	返回节点的值；
	  * 	其中null之表示从未设置过节点值；
	  * 	空值表示设置过空字符串作为节点值.		
	 * @throws Exception 
	 * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
	public String getData(String path) throws Exception {
		byte[] data = this.zkClient.getData().forPath(path);
		if (null == data) {
			return null;
		} else {
			return new String(data);
		}
	}
	/** 
	  * Description: 
	  * 	查询一个节点的值，同时设置观察者.
	  * @param：
	  * 	path	指定要查询的节点路径.
	  * 	wather	监听节点的观察者.
	  * @return
	  * 	返回节点的值；
	  * 	其中null之表示从未设置过节点值；
	  * 	空值表示设置过空字符串作为节点值.		
	 * @throws Exception 
	 * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
	public String getData(String path, CuratorWatcher watcher) throws Exception {
		byte[] data = this.zkClient.getData()
						.usingWatcher(watcher).forPath(path);
		if (null == data) {
			return null;
		} else {
			return new String(data);
		}
	}
	/** 
	  * Description: 
	  * 	查询一个节点的子节点.
	  * @param：
	  * 	path	指定要查询的节点路径.
	  * @return
	  * 	返回节点的值；
	 * @throws Exception 
	 * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
	public List<String> getChildren(String path) throws Exception {
		return this.zkClient.getChildren().forPath(path);
	}
	/** 
	  * Description: 
	  * 	查询一个节点的子节点，同时设置观察者.
	  * @param：
	  * 	path	指定要查询的节点路径.
	  * 	wather	监听节点的观察者.
	  * @return
	  * 	返回子节点
	 * @throws Exception 
	 * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
	public List<String> getChildren(String path, CuratorWatcher watcher) throws Exception {
		return this.zkClient.getChildren()
					.usingWatcher(watcher).forPath(path);
	}
	/** 
	  * Description: 
	  * 	检查一个节点是否存在.
	  * @param：
	  * 	path	指定要检查的节点路径.
	  * @return
	  * 	true	存在
	  * 	false	不存在
	 * @throws Exception 
	 * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
	public boolean checkExists(String path) throws Exception {
		Stat stat = this.zkClient.checkExists().forPath(path);
		if (null == stat) {
			return false;
		} else {
			return true;
		}
	}
	/** 
	  * Description: 
	  * 	设置一个节点的值.
	  * 	如果节点不存在就创建节点和其父节点.
	  * @param：
	  * 	path	指定要查询的节点路径.
	  * 	data	要设置的节点值，如为null则仅创建节点，不设置值.
	  * 	mode	仅当创建节点路径则此设置生效.
	  * @return
	  * 	返回节点的值；
	  * 	其中null之表示从未设置过节点值；
	  * 	空值表示设置过空字符串作为节点值.		
	 * @throws Exception 
	 * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
	public boolean setData(String path, String data, CreateMode mode) {
		if (null == path) {
			return false;
		}
		if (null == data) {
			try {
				this.zkClient.create().creatingParentsIfNeeded().withMode(mode).forPath(path);
				return true;
			} catch (UnsupportedEncodingException e) {
				return false;
			} catch (NodeExistsException e) {
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			try {
				this.zkClient.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data.getBytes());
				return true;
			} catch (UnsupportedEncodingException e) {
				return false;
			} catch (NodeExistsException e) {
				try {
					Stat stat = zkClient.setData().forPath(path, data.getBytes());
					if (null !=stat) {
						return true;
					} else {
						return false;
					}
				} catch (Exception e1) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
	}
	/** 
	  * Description: 
	  * 		通过自定义参数创建zk连接.
	  * @param：
	  * @return： 返回CuratorFramework即Curator封装过的zkClient.(需要start后才能使用)
	  * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
    private CuratorFramework createWithOptions(String connectionString, 
    		RetryPolicy retryPolicy, int connectionTimeoutMs, 
    		int sessionTimeoutMs){
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }    
    /** 
	  * Description: 
	  * 		注册链接需要监听的监听者对像. 
	  * @param：
	  * @Author：oblivion
	  * @Create Date：2014-12-02 
	  */
    private void registerListeners(CuratorFramework client){
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.info("CuratorFramework state changed: {}", newState);
                if(newState == ConnectionState.CONNECTED || newState == ConnectionState.RECONNECTED){
                	if (listeners != null && !listeners.isEmpty()) {
                        for(IZKListener listener : listeners){
                            listener.executor(client);
                            logger.info("Listener {} executed!", listener.getClass().getName());
                        }
                	}
                }
                if(newState == ConnectionState.LOST || newState == ConnectionState.SUSPENDED){
                	logger.info("CuratorFramework state changed: LOST or SUSPENDED");
                }
            }
        });
        client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
            @Override
            public void unhandledError(String message, Throwable e) {
                logger.info("CuratorFramework unhandledError: {}", message);
            }
        });
    }
    
    /**
     * Definition:级联删除某节点
     * @param path
     * @return
     * @Author: oblivion
     * @Created date: 2016-11-28
     */
    public boolean delete(String path) {
    	try {
			this.zkClient.delete().deletingChildrenIfNeeded().forPath(path);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    /**
     * Description: 测试zk连接是否正常<br> 
     * Created date: 2018年1月19日
     * @return
     * @author oblivion
     */
	public boolean testConnection() {
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 1);
			zkClient = createWithOptions(zkConnectionString, retryPolicy, 1000, 1000);
			zkClient.start();
			return this.checkExists("/");
		} catch (Exception e) {
			return false;
		} finally {
			zkClient.close();
		}
	}
}
