package com.patrick.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 使用java操作和访问ZK
 */
public class AZookeeperTest {

    private static final String CONNECT_ADDR = "spark1:2181,spark2:2181,spark3:2181";
    private static final int SESSION_TIME = 2000;
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
            ZooKeeper zooKeeper = new ZooKeeper(CONNECT_ADDR, SESSION_TIME, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    Event.KeeperState keeperState = watchedEvent.getState();
                    Event.EventType eventType = watchedEvent.getType();
                    if(Event.KeeperState.SyncConnected == keeperState){
                        if(Event.EventType.None == eventType){
                            System.out.println("ZK Connnecting");

                            countDownLatch.countDown();//放行
                        }
                    }
                }
            });

            countDownLatch.await();
        System.out.println("ZK Connnected");
        //创建节点
        //String result = zooKeeper.create("/test2","node2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //临时节点，当然session有效，close后会不见，分布式锁，主要就是靠临时节点
        //即当有一个线程创建了这个临时节点 ，那其它要使用的线程就创建不了同样名字的临时节点，自然就实现了分布式锁的效果
        String result = zooKeeper.create("/test2","node2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("result="+result);

        zooKeeper.close();

    }
}
