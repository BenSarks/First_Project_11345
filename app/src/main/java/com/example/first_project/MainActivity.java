package com.example.first_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.first_project.Logic.GameManager;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private ShapeableImageView[] main_IMG_Lifes;
    private ShapeableImageView[] main_IMG_Cars;
    private ShapeableImageView[][] main_IMG_Matrix_Game;
    private ShapeableImageView main_IMG_boom;
    private AppCompatImageButton[] main_Button_Options;
    private GridLayout gridLayout;
    private int rows = 0;
    private int cols = 0;
    private int timesDied=0;
    private int currentIndex=1;
    private int[] obstacleIndexArr;
    private GameManager gameManager;
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViewObjects();
        gameManager = new GameManager(rows,cols);
        setButtonListeners();
        mHandler.postDelayed(updateDynamite,1000);
    }

private void initializeViewObjects(){
    main_IMG_Lifes = new ShapeableImageView[]{findViewById(R.id.life1),
            findViewById(R.id.life2),findViewById(R.id.life3)};
    main_Button_Options = new AppCompatImageButton[]{findViewById(R.id.left_arrow),
            findViewById(R.id.right_arrow)};
    gridLayout = findViewById(R.id.game_gridLayout);
    main_IMG_boom = findViewById(R.id.boom);
    rows = gridLayout.getRowCount();
    obstacleIndexArr = new int[rows];
    cols = gridLayout.getColumnCount();
    for (int i = 0; i < rows ; i++) {
        obstacleIndexArr[i]=-1;
    }
    main_IMG_Cars = new ShapeableImageView[]{findViewById(R.id.car),
            findViewById(R.id.car2),findViewById(R.id.car3)};
    main_IMG_Matrix_Game = new ShapeableImageView[rows][cols];
    for (int i = 0; i <rows ; i++) {
        for (int j = 0; j <cols ; j++) {
            main_IMG_Matrix_Game[i][j] = (ShapeableImageView) gridLayout.getChildAt( i*cols+j);
        }
    }
    }
private void setButtonListeners(){
        main_Button_Options[0].setOnClickListener(v -> arrowClicked(currentIndex,currentIndex-1));
        main_Button_Options[1].setOnClickListener(v -> arrowClicked(currentIndex,currentIndex+1));
}
public void arrowClicked(int previousIndex, int futureIndex){
    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    if(gameManager.moveCar(futureIndex,vib)){
        currentIndex = futureIndex;
        main_IMG_Cars[previousIndex].setVisibility(View.INVISIBLE);
        main_IMG_Cars[futureIndex].setVisibility(View.VISIBLE);
        }
    }

    private final Runnable updateDynamite = new Runnable() {
        @Override
        public void run() {
            updateGameMatrixUI();
            mHandler.postDelayed(this,1000);
        }
    };

    private void updateGameMatrixUI() {
        for (int i = 1; i  < rows  ; i++) {
            if (obstacleIndexArr[i]>(-1)){
                obstacleIndexArr[i-1]=obstacleIndexArr[i];
                main_IMG_Matrix_Game[i][obstacleIndexArr[i]].setVisibility(View.INVISIBLE);
                main_IMG_Matrix_Game[i-1][obstacleIndexArr[i]].setVisibility(View.VISIBLE);
            }
        }
        Random r = new Random();
        int index = r.nextInt(cols);
        obstacleIndexArr[4] = index;
        main_IMG_Matrix_Game[4][index].setVisibility(View.VISIBLE);
        updateLives(obstacleIndexArr[0]);
    }

    private void updateLives(int index) {
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (index > -1) {
            if (timesDied == 2) {
                for (int i = 0; i < timesDied+1; i++) {
                    main_IMG_Lifes[i].setVisibility(View.VISIBLE);
                }
                timesDied = 0;
            }
            if (main_IMG_Cars[index].isShown()) {
                main_IMG_Lifes[timesDied].setVisibility(View.INVISIBLE);
                main_IMG_boom.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "🏎️", Toast.LENGTH_SHORT).show();
                    vb.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                timesDied++;
            }
            if (!main_IMG_Cars[index].isShown()){
                main_IMG_boom.setVisibility(View.INVISIBLE);
            }
            main_IMG_Matrix_Game[0][index].setVisibility(View.INVISIBLE);
        }
    }

}

