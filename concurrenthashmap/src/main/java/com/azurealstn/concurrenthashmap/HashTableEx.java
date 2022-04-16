package com.azurealstn.concurrenthashmap;

import java.util.Hashtable;

public class HashTableEx {
    public static void main(String[] args) {
        Hashtable<String, String> hashtable = new Hashtable<>();

        hashtable.put("one", "hoho");
        hashtable.put(null, "haha"); //error 발생!
        hashtable.put("two", null); //error 발생!

        System.out.println(hashtable.get("one"));
        System.out.println(hashtable.get(null));
        System.out.println(hashtable.get("two"));
    }
}
