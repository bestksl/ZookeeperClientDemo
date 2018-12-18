package com.bestksl.zk.disbutributesystem;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Consumer {
    private ArrayList<String> onlineServers = new ArrayList<String>();
    ZooKeeper zk = null;

    public void connectZK() throws Exception {
        zk = new ZooKeeper("HDP-01:2181,HDP-02:2181,HDP-03:2181", 2000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected && watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                    try {
                        getOnlineServers();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void sendRequest() throws InterruptedException, IOException {
        Random r = new Random();

        while (true) {
            int serverNum = r.nextInt(onlineServers.size());
            String server = onlineServers.get(serverNum);
            String hostname = server.split(":")[0];
            int port = Integer.parseInt(server.split(":")[1]);
            System.out.println("service start");
            System.out.println("the server chosen is " + hostname + ":" + port);
            Socket s = new Socket(hostname, port);
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            os.write(("hello" + hostname).getBytes());
            os.flush();
            byte[] b = new byte[1024];
            System.out.println("response time: " + is.read(b));
            Thread.sleep(5000);
        }
    }

    //查询在线服务器列表
    public void getOnlineServers() throws KeeperException, InterruptedException {
        List<String> result = zk.getChildren("/servers", true);
        ArrayList<String> tempServerList = new ArrayList<String>();
        for (String child :
                result) {
            String servername = new String(zk.getData("/servers/" + child, false, null));
            tempServerList.add(servername);
        }
        onlineServers = tempServerList;
        System.out.println("there are " + onlineServers.size() + ": " + onlineServers);
    }

    public static void main(String[] args) throws Exception {
        Consumer c = new Consumer();
        c.connectZK();
        c.getOnlineServers();
        c.sendRequest();
    }

}
