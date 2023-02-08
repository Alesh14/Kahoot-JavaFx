package com.example.project3;

class Test extends Question {
    private String[] options;

    public void setOptions(String[] array) {
        options = array;
        isTrue = false;
    }

    public String getOptionsAt(int indexforOptions) {
        return options[indexforOptions];
    }
}
