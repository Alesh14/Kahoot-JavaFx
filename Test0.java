package com.example.project3;

import java.util.LinkedList;

public class Test0 {
    public static void main(String[] args) {
        WhiteList<String> list = new WhiteList<>();
        list.add("Dana");
        list.add("Alisher");
        list.add("Love");
        for ( int i = 0; i < list.size(); ++i ) {
            System.out.println(list.get(i));
        }
    }
}

