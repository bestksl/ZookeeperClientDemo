package com.bestksl.zk.disbutributesystem;

import org.apache.zookeeper.*;

import java.io.IOException;

public class TimeQueryServer {

    ZooKeeper zk = null;
    Watcher w = new Watcher() {
        public void process(WatchedEvent watchedEvent) {

        }
    };

    public void connectZK() throws IOException {
        zk = new ZooKeeper("HDP-01:2181,HDP-02:2181,HDP-03:2181", 2000, w);

    }

    public void registServerInfo(String hostname, String port) throws KeeperException, InterruptedException {
        if (null == zk.exists("/servers", null)) {
            zk.create("/servers", "servers".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        String node = zk.create("/servers/server", (hostname + ":" + port).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("success: " + node);
    }


    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        TimeQueryServer ts = new TimeQueryServer();
        // 构造客户端连接
        ts.connectZK();
        // 注册服务器信息
        ts.registServerInfo(args[0], args[1]);
        // 启动业务线程开始处理业务
        new TimeQueryService(Integer.parseInt(args[1])).start();
    }


}
