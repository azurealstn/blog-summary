package com.azurealstn.concurrenthashmap;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapEx {
    public static void main(String[] args) {
        ConcurrentHashMap<Long, String> concurrentHashMap = new ConcurrentHashMap<>();

        concurrentHashMap.put(1L, "HI");
        concurrentHashMap.put(null, "null!!");
        concurrentHashMap.put(2L, null);

        System.out.println(concurrentHashMap.get(1L));
        System.out.println(concurrentHashMap.get(null));
        System.out.println(concurrentHashMap.get(2L));
    }
}
