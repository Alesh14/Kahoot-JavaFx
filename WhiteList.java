package com.example.project3;

class WhiteList<T> {
    private int size = 0;
    private Object[] list;


    WhiteList() {
        list = new Object[size];
    }

    WhiteList(int size) {
        this.size = size;
        list = new Object[size];
    }

    public int size() {
        return size;
    }

    public void add(T a) {
        size++;
        Object[] newObject = new Object[size];
        for (int i = 0; i < list.length; ++i) {
            newObject[i] = list[i];
        }
        newObject[size - 1] = a;
        list = newObject;
    }

    public void set(int n, T a) {
        list[n] = a;
    }

    public void remove(T a) {
        int idx = 0;
        size--;
        Object[] objects = new Object[size];
        for (Object o : list) {
            if (o != a) {
                objects[idx++] = o;
            }
        }
        list = objects;
    }

    public Object get(int index) {
        return list[index];
    }

    @Override
    public String toString() {
        String str = "[";
        for (int i = 0; i < list.length - 1; ++i) {
            str += list[i] + ",";
        }
        str += list[list.length - 1] + "]";
        return str;
    }
}