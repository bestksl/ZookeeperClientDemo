package com.bestksl.zk.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ZookeeperWatchDemo {

    ZooKeeper zk = null;
    Watcher w = null;

    @Before
    public void init() throws IOException {
        zk = new ZooKeeper("HDP-01:2181,HDP-02:2181,HDP-03:2181", 2000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {

                if (watchedEvent.getState() == Event.KeeperState.SyncConnected && watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                    System.out.println("换了");
                    System.out.println(watchedEvent.toString());
                    System.out.println("path:  " + watchedEvent.getPath() + "type:  " + watchedEvent.getType());
                    try {
                        zk.getData("/idea", true, null);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        });
    }

    @Test
    public void testGetWatch() throws Exception {
        byte[] data = zk.getData("/idea", true, null);
        Thread.sleep(Long.MAX_VALUE);

    }
}
