package com.simplemapapp.sergej.problemsolve;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    GridLayout gridLayout;
    TextView estimatedTime;
    TextView problemText;
    TextView problemCounter;
    TextView answerCheckText;
    Button restartButton;
    ArrayList<Integer> answerList;
    ArrayList<TextView> answerViewList;
    Boolean gameIsRunning = false;
    final int MAX = 21;
    final int MIN = 1;
    int rightAnswers = 0;
    int currentProblem = 0;
    int rightIndex;
    Animation fadeOut;
    Animation fadeIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        gridLayout = findViewById(R.id.gridLayout);
        estimatedTime = findViewById(R.id.estimatedTime);
        problemText = findViewById(R.id.problemText);
        problemCounter = findViewById(R.id.problemCount);
        answerCheckText = findViewById(R.id.answerCheckText);
        restartButton = findViewById(R.id.restartButton);
        answerViewList = new ArrayList<>();
        answerViewList.add((TextView) findViewById(R.id.answer1));
        answerViewList.add((TextView) findViewById(R.id.answer2));
        answerViewList.add((TextView) findViewById(R.id.answer3));
        answerViewList.add((TextView) findViewById(R.id.answer4));
        for (int i = 0; i < answerViewList.size(); i++)
            answerViewList.get(i).setTag(i);
    }

    public void answerPressed(View view) {
        if(gameIsRunning) {
            int tag = (int) view.getTag();
            if (tag == rightIndex) {
                answerCheckText.setText("Right!");
                rightAnswers++;
            } else {
                answerCheckText.setText("Wrong!");
            }
            if (answerCheckText.getVisibility() == View.INVISIBLE) {
                answerCheckText.setVisibility(View.VISIBLE);
                answerCheckText.startAnimation(fadeIn);
            }
            if (answerCheckText.getVisibility() == View.VISIBLE) {
                answerCheckText.startAnimation(fadeOut);
                answerCheckText.setVisibility(View.INVISIBLE);
            }
            rightIndex = newQuestion();
            currentProblem++;
            updateScore();
        }
    }

    public void getStarted(View view) {
        gridLayout.setVisibility(View.VISIBLE);
        estimatedTime.setVisibility(View.VISIBLE);
        problemText.setVisibility(View.VISIBLE);
        problemCounter.setVisibility(View.VISIBLE);
        answerCheckText.setVisibility(View.INVISIBLE);
        rightIndex = newQuestion();
        startNewGame();
        view.setVisibility(View.GONE);
        gameIsRunning = true;
    }

    public void restartGame(View view) {
        startNewGame();
    }
    public void startNewGame() {
        gameIsRunning = true;
        rightAnswers = 0;
        currentProblem = 0;
        updateScore();
        answerCheckText.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.GONE);
        estimatedTime.setText("35s");
        new CountDownTimer(35 * 1000,1000) {

            @Override
            public void onTick(long l) {
                int untilEnd = (int) l / 1000;
                estimatedTime.setText(untilEnd+"s");
            }

            @Override
            public void onFinish() {
                estimatedTime.setText(0+"s");
                answerCheckText.setText("Time is over!");
                if (answerCheckText.getVisibility() == View.INVISIBLE) {
                    answerCheckText.setVisibility(View.VISIBLE);
                    answerCheckText.startAnimation(fadeIn);
                }
                restartButton.setVisibility(View.VISIBLE);
                gameIsRunning = false;
            }
        }.start();
    }
    public int newQuestion() {
        Random rnd = new Random();
        ArrayList<Boolean> probableAnswerDisplayed = new ArrayList<Boolean>();
        probableAnswerDisplayed.add(false);
        probableAnswerDisplayed.add(false);
        probableAnswerDisplayed.add(false);
        probableAnswerDisplayed.add(false);
        int number1 = rnd.nextInt((MAX - MIN) + 1) + MIN;
        int number2 = rnd.nextInt((MAX - MIN) + 1) + MIN;
        int rightIndex = 0;
        answerList = new ArrayList<>();
        answerList.add(number1 + number2);
        for (int i = 1; i < 4; i++) {
            do {
                int randomInt = number1 + rnd.nextInt(((10) - MIN) + 1) + MIN + number2 - rnd.nextInt(((10) - MIN) + 1) + MIN;
                if (randomInt != number1 + number2)
                    answerList.add(randomInt);
            } while (answerList.size() != i+1);

        }
        for (int i = 0; i < answerList.size(); i++) {
            int probableAnswerIndex;
            do {
                probableAnswerIndex = rnd.nextInt((3 - 0) + 1) + 0;
                if (!probableAnswerDisplayed.get(probableAnswerIndex)) {
                    answerViewList.get(i).setText(answerList.get(probableAnswerIndex).toString());
                    if (probableAnswerIndex == 0)
                        rightIndex = i;
                }
            } while (probableAnswerDisplayed.get(probableAnswerIndex));
            probableAnswerDisplayed.set(probableAnswerIndex,true);
        }
        updateScore();
        problemText.setText(number1 + " + " + number2);
        return rightIndex;
    }
    public void updateScore() {
        problemCounter.setText(rightAnswers+"/"+currentProblem);
    }
}
