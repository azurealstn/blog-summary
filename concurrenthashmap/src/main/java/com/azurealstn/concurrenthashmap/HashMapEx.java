package com.azurealstn.concurrenthashmap;

import java.util.HashMap;

public class HashMapEx {
    public static void main(String[] args) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        hashMap.put(1, 2);
        hashMap.put(null, 3);
        hashMap.put(2, null);

        System.out.println(hashMap.get(1));
        System.out.println(hashMap.get(null));
        System.out.println(hashMap.get(2));
    }
}
