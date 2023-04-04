package com.example.first_project.Logic;


import java.util.Random;

public class GameManager {
    private final int rows;
    private final int cols ;

    private int timesDied = 0;

    private int currentCarIndex = 1;

    public GameManager(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public boolean canCarMove(int index) {
        return (0 <= index && cols > index);
    }

    public int getRandomColIndex() {
        Random r = new Random();
        return r.nextInt(cols);
    }

    public int getCurrentCarIndex() {
        return currentCarIndex;
    }

    public void setCurrentCarIndex(int currentCarIndex) {
        this.currentCarIndex = currentCarIndex;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTimesDied() {
        return timesDied;
    }

    public void setTimesDied(int timesDied) {
        this.timesDied = timesDied;
    }
}