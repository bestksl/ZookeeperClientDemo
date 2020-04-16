package com.bestksl.zk.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class TestWatcher implements Watcher {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeper zk;

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            log.info("get watcher {} ", watchedEvent.getType());
            countDownLatch.countDown();
        }
    }

    public void init() throws IOException, KeeperException, InterruptedException {
        zk = new ZooKeeper("zk01:2181,zk02:2181,zk03:2181", 3000, this);
    }

    public void testGet() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        byte[] b = zk.getData("/idea", true, null);
        countDownLatch.await();
        log.info("continue ");
        String result = new String(b, "UTF-8");
        System.out.println(result);
    }

    public void testDel() throws KeeperException, InterruptedException, UnsupportedEncodingException {

        zk.delete("/ideb", 0, new DelCallBack(), "new Object()");
       // countDownLatch.await();
        log.info("continue ");
    }


    public void clean() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        TestWatcher testWatcher = new TestWatcher();
        testWatcher.init();
        testWatcher.testDel();
        testWatcher.clean();


    }
}
