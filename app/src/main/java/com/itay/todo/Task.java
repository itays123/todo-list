package com.itay.todo;

public class Task {
    private String title;
    private int priority;
    private int daysToComplete;
    private int date;

    public Task() {
        //empty constructor needed
    }

    public Task(String title, int priority, int daysToComplete, int date) {
        this.title = title;
        this.priority = priority;
        this.daysToComplete = daysToComplete;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDaysToComplete() {
        return daysToComplete;
    }

    public void setDaysToComplete(int daysToComplete) {
        this.daysToComplete = daysToComplete;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int drawableTaskPicture(){
        if (this.priority > 10000){
            return R.drawable.ic_mood;
        }else if (this.priority > 100) {
            return R.drawable.star_empty;
        } else if (this.priority > 10) {
            return  R.drawable.star_half;
        } else {
            return R.drawable.star_full;
        }
    }

    public String taskTimeStamp(){
        if (this.priority > 10000){
            return "Completed";
        } else if (this.daysToComplete < 1){
            return "Today";
        } else if (this.daysToComplete < 2){
            return "Tomorrow";
        } else if (this.daysToComplete < 7){
            return "This week";
        } else {
            return this.date + "";
        }
    }

    public static int taskPriority(int daysToComplete, int category, int timeToExecute){
        if (timeToExecute != 0){
            int timeRounded = timeToExecute/5;
            return daysToComplete * category / timeRounded;
        } else return 10001;
    }

}
