package com.example.educationapplication;

public class DataClass {
    private String subtitle;
    private String questionList;
    private String correct;
    private String options;
    private String question;
    private String key;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getsubtitle() {
        return subtitle;
    }
    public String questionList() {
        return questionList;
    }
    public String getCorrect() {
        return correct;
    }

    public String getOptions() {
        return options;
    }

    public String getQuestion() {
        return question;
    }

    public DataClass(String subtitle, String questionList,String correct, String options, String question) {
        this.subtitle = subtitle;
        this.questionList = questionList;
        this.correct = correct;
        this.options = options;
        this.question = question;

    }

}