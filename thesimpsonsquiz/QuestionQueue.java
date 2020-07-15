package com.darylhowedevs.thesimpsonsquiz;

import java.util.ArrayList;

public class QuestionQueue {

    boolean isAbleToLoad = true;

    private ArrayList<SimpsonsAPI> questionList = new ArrayList<>();

    public QuestionQueue(){
    }

    public void addQuestion(){

        if(isAbleToLoad){
            isAbleToLoad = false;
            SimpsonsAPI question = new SimpsonsAPI();
            question.extractData();
            question.getQuote();
            question.generatePossibleAnswers();
            questionList.add(question);
            isAbleToLoad = true;
        }
    }

    public SimpsonsAPI getQuestionByIndex(int index){
        return questionList.get(index);
    }

    public void removeQuestionByIndex(int index){
        questionList.remove(index);
    }

    public int getQuestionSize(){
        return questionList.size();
    }

}
