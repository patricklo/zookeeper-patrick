package com.patrick.zookeeper.distributeLock;

public class OrderService implements Runnable {
    private C1OrderNumberGenerator orderNumberGenerator = new C1OrderNumberGenerator();
    private Lock lock = new ZookeeperDistributeLock();

    @Override
    public void run() {
        getOrderNumber();
    }

    public void getOrderNumber(){
        lock.getLock();
        String number = orderNumberGenerator.orderNumber();
        System.out.println(number);
        lock.unLock();
    }

    public static void main(String[] args){
        for(int i=0;i<100;i++){
            new Thread(new OrderService()).start();
        }
    }
}
