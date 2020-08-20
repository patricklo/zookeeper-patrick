package com.patrick.zookeeper.distributeLock;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 分布式锁的实现方式：
 * 1.zookeeper: 在zk上创建临时节点（有效期），如果能生成节点，则获取锁，生成订单号。
 *             如果创建失败，则等待，当节点关闭通知时，即代表锁释放，可以再尝试创建节点，获取锁
 * 2.redis
 * 3.db
 */
public class CZookeeperDistributedLock {

    public static void main(String[] args) throws Exception {

    }
}
