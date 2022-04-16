package com.azurealstn.psvm;

class Count {
    public int count;

    public Count() {
        this.count++;
        System.out.println(this.count);
    }

}
public class StaticEx {
    public static void main(String[] args) {
        Count count1 = new Count();
        Count count2 = new Count();
    }
}
