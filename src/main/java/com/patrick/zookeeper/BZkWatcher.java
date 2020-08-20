package com.patrick.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;

public class BZkWatcher implements Watcher {
    private static final String CONNECT_ADDR = "spark1:2181,spark2:2181,spark3:2181";
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) {

    }
   //ZK节点发生变更： 删除，新增，修改
    //都会用process方法
    @Override
    public void process(WatchedEvent watchedEvent) {
        Event.EventType eventType = watchedEvent.getType();
        Event.KeeperState keeperState = watchedEvent.getState();
        String path =watchedEvent.getPath();
        if(Event.KeeperState.SyncConnected == keeperState){
            if(Event.EventType.None == eventType){
                //建立ZK连接connection
                System.out.println("create node");
            }else if(Event.EventType.NodeCreated == eventType){
                System.out.println("NodeCreated:"+path);
            }else if(Event.EventType.NodeDataChanged == eventType){
                System.out.println("NodeDataChanged");
            }else if(Event.EventType.NodeDeleted == eventType){
                System.out.println("NodeDeleted");
            }
        }
    }

    public void createConnection(String addr, int sessionTimeout) throws IOException {
        zooKeeper = new ZooKeeper(addr, sessionTimeout, this);
    }

    //create node
    public void createNode(String path, String data) throws KeeperException, InterruptedException {
        //true:有事件通知：
        zooKeeper.exists(path,true);
        zooKeeper.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

    }

    public void updateNode(String path, String data) throws KeeperException, InterruptedException {

        zooKeeper.exists(path,true);
        zooKeeper.setData(path,data.getBytes(),-1);
    }

    public void deleteNode(String path) throws KeeperException, InterruptedException {

        zooKeeper.exists(path,true);
        zooKeeper.delete(path,-1);
    }

    public void closeConn() throws InterruptedException {
        if(zooKeeper!=null) {
            zooKeeper.close();
        }
    }
}
