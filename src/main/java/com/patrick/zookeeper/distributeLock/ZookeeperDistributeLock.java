package com.patrick.zookeeper.distributeLock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

public  class ZookeeperDistributeLock extends ZookeeperAbstractLock {
    private CountDownLatch countDownLatch = null;

    @Override
    protected boolean tryLock() {
        try {
            zkClient.createEphemeral(PATH);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    protected void waitLock() {
        IZkDataListener dataListener = new IZkDataListener(){
            @Override
            public void handleDataChange(String path, Object data) throws Exception {

            }
            @Override
            public void handleDataDeleted(String path) throws Exception {
                //notify lock
                if(countDownLatch!=null) {
                    countDownLatch.countDown();
                    /***
                     * 可能会有同时出现"删除节点"的log，因为dataDelete是通知到所有客户端/线程，当有data del的通知时，有可能同时有多个线程
                     */
                    System.out.println("删除节点");
                }
            }
        };
        //注册到zkclient进行监听
        zkClient.subscribeDataChanges(PATH, dataListener);
        if(zkClient.exists(PATH)){
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //删除监听
        zkClient.unsubscribeDataChanges(PATH,dataListener);
    }
}
