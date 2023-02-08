package com.example.project3;

abstract class Question {
    boolean isTrue = true;
    private String description = "Description";
    private String answer = "Answer";

    public abstract void setOptions(String[] a);

    public abstract String getOptionsAt(int n);

    public void setAnswer(String stringBalga) {
        answer = stringBalga;
    }

    public void setDescription(String s) {
        description = s;
    }

    String getAnswer() {
        return answer;
    }

    String getDescription() {
        return description;
    }
}
