package com.example.first_project;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.first_project.Logic.GameManager;
import com.google.android.material.imageview.ShapeableImageView;


public class MainActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();
    private ShapeableImageView[] main_IMG_Lifes;
    private ShapeableImageView[] main_IMG_Cars;
    private ShapeableImageView[][] main_IMG_Matrix_Game;
    private AppCompatImageButton[] main_Button_Options;
    private Vibrator vb;
    private int timesDied = 0;
    private int[] obstacleIndexArr;

    private GameManager gameManager;
    private final Runnable updateDynamite = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, 1500);
            updateGameMatrixUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViewObjects();
        setButtonListeners();
        mHandler.postDelayed(updateDynamite, 1500);
    }

    private void initializeViewObjects() {
        main_IMG_Lifes = new ShapeableImageView[]{findViewById(R.id.life1),
                findViewById(R.id.life2), findViewById(R.id.life3)};
        main_Button_Options = new AppCompatImageButton[]{findViewById(R.id.left_arrow),
                findViewById(R.id.right_arrow)};
        GridLayout gridLayout = findViewById(R.id.game_gridLayout);
        gameManager = new GameManager(gridLayout.getRowCount(), gridLayout.getColumnCount());
        obstacleIndexArr = new int[gameManager.getRows()];
        for (int i = 0; i < gameManager.getRows(); i++) {
            obstacleIndexArr[i] = (-1);
        }
        main_IMG_Cars = new ShapeableImageView[]{findViewById(R.id.car),
                findViewById(R.id.car2), findViewById(R.id.car3)};
        main_IMG_Matrix_Game = new ShapeableImageView[gameManager.getRows()][gameManager.getCols()];
        for (int i = 0; i < gameManager.getRows(); i++) {
            for (int j = 0; j < gameManager.getCols(); j++) {
                main_IMG_Matrix_Game[i][j] = (ShapeableImageView) gridLayout.getChildAt(i * gameManager.getCols() + j);
            }
        }
        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void setButtonListeners() {
        main_Button_Options[0].setOnClickListener(v -> arrowClicked(gameManager.getCurrentCarIndex(), gameManager.getCurrentCarIndex() - 1));
        main_Button_Options[1].setOnClickListener(v -> arrowClicked(gameManager.getCurrentCarIndex(), gameManager.getCurrentCarIndex() + 1));
    }

    public void arrowClicked(int previousIndex, int futureIndex) {
        if (gameManager.canCarMove(futureIndex)) {
            gameManager.setCurrentCarIndex(futureIndex);
            setINVISIBLE(main_IMG_Cars[previousIndex]);
            setINVISIBLE(main_IMG_Matrix_Game[0][previousIndex]);
            setVISIBLE(main_IMG_Cars[futureIndex]);
            setINVISIBLE(main_IMG_Matrix_Game[0][futureIndex]);
        }
    }


    private void updateGameMatrixUI() {
        obstacleIndexArr[gameManager.getRows() - 1] = gameManager.getRandomColIndex();
        if (obstacleIndexArr[0] > -1) {//clears the previous obstacle from the first row only
            setINVISIBLE(main_IMG_Matrix_Game[0][obstacleIndexArr[0]]);
        }
        for (int i = 0; i < gameManager.getRows()-1 ; i++) {
            if (obstacleIndexArr[i + 1] > (-1)) {
                obstacleIndexArr[i] = obstacleIndexArr[i + 1];
                setVISIBLE(main_IMG_Matrix_Game[i][obstacleIndexArr[i]]);//moves all of the visible obstacles downwards
                setINVISIBLE(main_IMG_Matrix_Game[i + 1][obstacleIndexArr[i]]);// remove the visible marker from the previous obstacles
            }
        }
        updateLives(obstacleIndexArr[0]);
    }


    private void vibrate() {
        vb.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void updateLives(int index) {
        if (index > -1) {
            if (main_IMG_Matrix_Game[0][gameManager.getCurrentCarIndex()].isShown()) {
                setINVISIBLE(main_IMG_Cars[gameManager.getCurrentCarIndex()]);//
                main_IMG_Lifes[timesDied].setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "💥", Toast.LENGTH_SHORT).show();
                vibrate();
                timesDied++;
            }
            if (!main_IMG_Matrix_Game[0][gameManager.getCurrentCarIndex()].isShown()) {
                setVISIBLE(main_IMG_Cars[gameManager.getCurrentCarIndex()]);//
            }
            if (timesDied == 3) {//restart the lifes
                for (int i = 0; i < timesDied; i++) {
                    setVISIBLE(main_IMG_Lifes[i]);
                }
                timesDied = 0;// set the count to zero again
            }
        }
    }

    private void setVISIBLE(ShapeableImageView view) {
        view.setVisibility(View.VISIBLE);
    }

    private void setINVISIBLE(ShapeableImageView view) {
        view.setVisibility(View.INVISIBLE);
    }
}

