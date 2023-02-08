package com.example.project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

class Quiz {
    private String name;
    private final LinkedList<Question> questions = new LinkedList<>();

    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    public String toString() {
        return "nothing";
    }

    LinkedList<Question> loadFromFile(String args) {
        try {
            File file = new File(args);
            FillIn blank = new FillIn();
            Scanner input = new Scanner(file);
            while (input.hasNext()) {
                String str = input.nextLine();
                if (str.contains("{blank}")) {
                    Question question = new FillIn();
                    question.setDescription(str.replace("{blank}", blank.toString()));
                    question.setAnswer(input.nextLine());
                    addQuestion(question);
                }
                else if ( str.contains("{truefalse}")) {
                    Question truefalse = new TrueFalse();
                    String s = input.nextLine();
                    truefalse.setDescription(str.replace("{truefalse}", "(true or false?)"));
                    truefalse.setAnswer(s);
                    addQuestion(truefalse);
                }
                else {
                    Question question = new Test();
                    String[] list;
                    list = new String[4];
                    for (int k = 0; k < 4; k++) list[k] = input.nextLine();
                    question.setOptions(list);
                    question.setAnswer(list[0]);
                    question.setDescription(str);
                    addQuestion(question);
                }
            }
        } catch (FileNotFoundException exception) {}
        Collections.shuffle(questions);
        return questions;
    }

    void addQuestion(Question adderToLinkedList) {
        questions.add(adderToLinkedList);
    }
}
