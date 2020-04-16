package com.bestksl.zk.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Executors;

@Slf4j
public class ZookeeperClientDemo implements Watcher {
    private ZooKeeper zk;

    @Before
    public void init() throws IOException, KeeperException, InterruptedException {
        zk = new ZooKeeper("zk01:2181,zk02:2181,zk03:2181", 3000, new ZookeeperClientDemo());
    }

    @Test
    public void testCreate() throws InterruptedException, KeeperException {
        //构造一个连接zk对象

        String path = zk.create("/idssea", "hello ksl bbb".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);

    }

    @Test
    public void testUpdate() throws KeeperException, InterruptedException {
        // -1 代表 修改所有版本 (过于底层 不需要了解)
        Stat status = zk.setData("/idea", "ksl最厉害".getBytes(), 4);

        log.info("ver {}", status.getVersion());
    }

    @Test
    public void testGet() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        byte[] b = zk.getData("/idea", false, null);
        String result = new String(b, "UTF-8");
        System.out.println(result);
    }

    @Test
    public void testGetListChildren() throws KeeperException, InterruptedException {
        List<String> l = zk.getChildren("/idea", true);
        for (String ele :
                l) {
            System.out.println(ele);
        }
    }

    @Test
    public void testRm() throws KeeperException, InterruptedException {
        zk.delete("/idssea", 0);
    }

    @After
    public void clean() throws InterruptedException {
        zk.close();
    }

    public void process(WatchedEvent watchedEvent) {

    }
}
