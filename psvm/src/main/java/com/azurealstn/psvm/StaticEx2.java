package com.azurealstn.psvm;

class Count2 {
    public static int num = 0;

    public Count2() {
        num++;
        System.out.println(num);
    }
}
public class StaticEx2 {
    public static void main(String[] args) {
        Count2 count1 = new Count2();
        Count2 count2 = new Count2();
    }
}
