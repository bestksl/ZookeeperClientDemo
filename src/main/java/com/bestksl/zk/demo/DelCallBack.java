package com.bestksl.zk.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

import java.io.UnsupportedEncodingException;

@Slf4j
public class DelCallBack implements AsyncCallback.VoidCallback {


    public void processResult(int i, String s, Object o) {
        System.out.println("  --------  " + i + " ----------  ");
        System.out.println("  --------  " + s + " ----------  ");
        System.out.println("  --------  " + o.toString() + " ----------  ");
    }
}
