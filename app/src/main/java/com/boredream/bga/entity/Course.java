package com.boredream.bga.entity;

/**
 * 课程
 */
public class Course extends BaseEntity {

    private User assistant;
    private Classroom classroom;
    private String date;
    private long startTime;

    /**
     * 0-萌新 1-进阶
     */
    private int grade;

    public User getAssistant() {
        return assistant;
    }

    public void setAssistant(User assistant) {
        this.assistant = assistant;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
