package com.patrick.zookeeper.distributeLock;

public interface Lock {
   public void getLock();
   public void unLock();
}
