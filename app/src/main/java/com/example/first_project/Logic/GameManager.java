package com.example.first_project.Logic;



import java.util.Random;

public class GameManager {
    private final int rows;
    private final int cols;

    public GameManager(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public boolean canCarMove(int index) {
        return (0 <= index && cols > index);
    }

    public int getRandomColIndex(){
        Random r = new Random();
         return r.nextInt(cols);
        }

}