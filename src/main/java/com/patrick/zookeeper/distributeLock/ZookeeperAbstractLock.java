package com.patrick.zookeeper.distributeLock;

import org.I0Itec.zkclient.ZkClient;

public abstract class ZookeeperAbstractLock implements Lock {

    private static final String CONNECT_ADDR = "spark1:2181,spark2:2181,spark3:2181";

    protected ZkClient zkClient = new ZkClient(CONNECT_ADDR);
    protected static final String PATH = "/lock";

    @Override
    public void getLock() {
        //tryLock ：创建zk 临时节点
        if(tryLock()){
            System.out.println("get Lock");
        }else{
            System.out.println("fail to get lock, wait...");
            //线程挂起，等待
            waitLock();
            getLock();
        }
    }

    protected abstract  boolean tryLock();
    protected abstract void waitLock();
    @Override
    public void unLock() {
        if(zkClient!=null){
            zkClient.close();
        }
    }
}
