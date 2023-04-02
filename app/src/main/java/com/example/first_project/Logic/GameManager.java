package com.example.first_project.Logic;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class GameManager {
    private int rows,cols;
    private final int lives = 3;

    public GameManager(int rows,int cols) {
        this.rows = rows;
        this.cols = cols;
    }
    private boolean canCarMove(int index){
        if ( 0 <= index && cols > index){
            return true;
        }
        return false;
    }

    public boolean moveCar(int futureIndex, Vibrator vib){
        if(canCarMove(futureIndex)){
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vib.vibrate(500);
        }
        return false;
    }
}