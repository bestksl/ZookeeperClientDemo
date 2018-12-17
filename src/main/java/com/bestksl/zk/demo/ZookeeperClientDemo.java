package com.bestksl.zk.demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ZookeeperClientDemo {
    private ZooKeeper zk;

    @Before
    public void init() throws IOException, KeeperException, InterruptedException {
        zk = new ZooKeeper("HDP-01:2181,HDP-02:2181,HDP-03:2181", 2000, null);
    }

    @Test
    public void testCreate() throws InterruptedException, KeeperException {
        //构造一个连接zk对象

        String path = zk.create("/idea/bbb", "hello ksl bbb".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);

    }

    @Test
    public void testUpdate() throws KeeperException, InterruptedException {
        // -1 代表 修改所有版本 (过于底层 不需要了解)
        zk.setData("/idea", "ksl最厉害".getBytes(), -1);
    }

    @Test
    public void testGet() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        byte[] b = zk.getData("/idea", false, null);
        String result = new String(b, "UTF-8");
        System.out.println(result);
    }

    @Test
    public void testGetListChildren() throws KeeperException, InterruptedException {
        List<String> l = zk.getChildren("/idea", false);
        for (String ele :
                l) {
            System.out.println(ele);
        }
    }

    @Test
    public void testRm() throws KeeperException, InterruptedException {
        zk.delete("/idea/aaa", -1);
    }

    @After
    public void clean() throws InterruptedException {
        zk.close();
    }
}
