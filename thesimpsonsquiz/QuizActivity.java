package com.darylhowedevs.thesimpsonsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 *  QuizActivity - Responisble for UI and is the manager of the quiz.
 */
public class QuizActivity extends AppCompatActivity {

    private String[] possibleAnswers;
    private Bitmap[] possibleCharacters;

    private QuestionQueue questionQueue = new QuestionQueue();

    private TextView questionText;
    private TextView instructionsText;

    private ImageView characterImage01;
    private ImageView characterImage02;
    private ImageView characterImage03;
    private ImageView characterImage04;

    private Button possibleAnswerButton01;
    private Button possibleAnswerButton02;
    private Button possibleAnswerButton03;
    private Button possibleAnswerButton04;

    private Vibrator vibe;

    private MediaPlayer correctSound;

    private int questionNumber = 0;
    private int randomPositions [] = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correctSound = MediaPlayer.create(this, R.raw.correctsound);

        instructionsText = findViewById(R.id.instructionsText);
        instructionsText.setText("How to play: \n Read the quote and pick which Simpsons character you think spoke, sang or shouted it in the series! \n Choose wisely!");

        questionText = findViewById(R.id.questionText);

        characterImage01 = findViewById(R.id.characterImage01);
        characterImage02 = findViewById(R.id.characterImage02);
        characterImage03 = findViewById(R.id.characterImage03);
        characterImage04 = findViewById(R.id.characterImage04);

        possibleAnswerButton01 = findViewById(R.id.possibleAnswerButton01);
        possibleAnswerButton02 = findViewById(R.id.possibleAnswerButton02);
        possibleAnswerButton03 = findViewById(R.id.possibleAnswerButton03);
        possibleAnswerButton04 = findViewById(R.id.possibleAnswerButton04);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        disableAndHidePossibleAnswers();
        instructionsToPossibleAnswersAnimation();
        activatePossibleAnswerButtons(13000);
        generateRandomPositionsToPossibleAnswers();
        addNewQuestionToQueue();
    }

    /**
     * A method to disable and hide the possible answers on screen.
     */
    private void disableAndHidePossibleAnswers(){
        characterImage01.setEnabled(false);
        characterImage02.setEnabled(false);
        characterImage03.setEnabled(false);
        characterImage04.setEnabled(false);

        characterImage01.setAlpha(0f);
        characterImage02.setAlpha(0f);
        characterImage03.setAlpha(0f);
        characterImage04.setAlpha(0f);

        possibleAnswerButton01.setClickable(false);
        possibleAnswerButton02.setClickable(false);
        possibleAnswerButton03.setClickable(false);
        possibleAnswerButton04.setClickable(false);

        possibleAnswerButton01.setAlpha(0f);
        possibleAnswerButton02.setAlpha(0f);
        possibleAnswerButton03.setAlpha(0f);
        possibleAnswerButton04.setAlpha(0f);
    }

    /**
     * A method to animate from the instructions to the first question.
     */
    private void instructionsToPossibleAnswersAnimation(){

        int fadeInDuration = 5000;
        int startDelay = 8000;

        instructionsText.animate().alpha(0).setStartDelay(5000).setDuration(5000);
        questionText.setAlpha(0);

        possibleAnswerButton01.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);
        possibleAnswerButton02.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);
        possibleAnswerButton03.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);
        possibleAnswerButton04.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);

        characterImage01.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);
        characterImage02.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);
        characterImage03.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);
        characterImage04.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);

        questionText.animate().alpha(1).setStartDelay(startDelay).setDuration(fadeInDuration);
    }

    /**
     * A UIHandler is used to update the button text, character image and character image when called.
     * After this is done a new questions will be added to the question queue.
     */
    Handler UIHandler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            questionText.setText("\"" + questionQueue.getQuestionByIndex(questionNumber).getQuote() + "\"");

            possibleAnswerButton01.setText(possibleAnswers[randomPositions [0]]);
            possibleAnswerButton02.setText(possibleAnswers[randomPositions [1]]);
            possibleAnswerButton03.setText(possibleAnswers[randomPositions [2]]);
            possibleAnswerButton04.setText(possibleAnswers[randomPositions [3]]);

            characterImage01.setTag(possibleAnswers[randomPositions [0]]);
            characterImage02.setTag(possibleAnswers[randomPositions [1]]);
            characterImage03.setTag(possibleAnswers[randomPositions [2]]);
            characterImage04.setTag(possibleAnswers[randomPositions [3]]);

            characterImage01.setImageBitmap(possibleCharacters[randomPositions [0]]);
            characterImage02.setImageBitmap(possibleCharacters[randomPositions [1]]);
            characterImage03.setImageBitmap(possibleCharacters[randomPositions [2]]);
            characterImage04.setImageBitmap(possibleCharacters[randomPositions [3]]);

            addNewQuestionToQueue();
        }
    };

    /**
     * A method to add a new question into the queue. As this involves retreiving JSON data
     * it is carried out on a seperate thread to the UI to avoid 'freezing' or non responive UI while
     * the data is being retrieved.
     */
    public void addNewQuestionToQueue(){

        // Ensures a max of 6 questions will be queued/buffered.
        // If there are less than 6 questions waiting in the queue to be asked..
        if(questionQueue.getQuestionSize() - questionNumber < 6){

            Runnable a = new Runnable() {

                @Override
                public void run() {

                    questionQueue.addQuestion();
                    possibleAnswers = questionQueue.getQuestionByIndex(questionNumber).getPossibleAnswers();
                    possibleCharacters = questionQueue.getQuestionByIndex(questionNumber).getPossibleCharacters();

                    UIHandler.sendEmptyMessage(0);
                }
            };

            Thread testThread = new Thread(a);
            testThread.start();
        }
    }

    /**
     * A method to animate the on screen widgits (buttons and character images) after a question
     * has been answered correctly. This animation also serves as a way to buy time for
     * the next 4 images to load in.
     */
    private void animateBetweenQuestions(){

        int fadeInDuration = 1500;
        int startDelay = 1500;

        disableAndHidePossibleAnswers();

        possibleAnswerButton01.animate().alpha(1f).setStartDelay(startDelay).setDuration(fadeInDuration);
        possibleAnswerButton02.animate().alpha(1f).setStartDelay(startDelay).setDuration(fadeInDuration);
        possibleAnswerButton03.animate().alpha(1f).setStartDelay(startDelay).setDuration(fadeInDuration);
        possibleAnswerButton04.animate().alpha(1f).setStartDelay(startDelay).setDuration(fadeInDuration);

        characterImage01.animate().alpha(1f).setStartDelay(startDelay).setDuration(fadeInDuration);
        characterImage02.animate().alpha(1f).setStartDelay(startDelay).setDuration(fadeInDuration);
        characterImage03.animate().alpha(1f).setStartDelay(startDelay).setDuration(fadeInDuration);
        characterImage04.animate().alpha(1f).setStartDelay(startDelay).setDuration(fadeInDuration);

        activatePossibleAnswerButtons(3000);
    }

    /**
     * A method to re-activate the on screen buttons after an amount of time.
     * @param countdownLength long the amount of time before the
     */
    private void activatePossibleAnswerButtons(long countdownLength){

        CountDownTimer timer = new CountDownTimer(countdownLength, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                possibleAnswerButton01.setClickable(true);
                possibleAnswerButton02.setClickable(true);
                possibleAnswerButton03.setClickable(true);
                possibleAnswerButton04.setClickable(true);

                characterImage01.setEnabled(true);
                characterImage02.setEnabled(true);
                characterImage03.setEnabled(true);
                characterImage04.setEnabled(true);
            }
        };

        timer.start();
    }


    /**
     * A method used to assign random positions to the possible answers.
     */
    public void generateRandomPositionsToPossibleAnswers(){

        ArrayList<Integer> positions = new ArrayList<>();

        positions.add(0);
        positions.add(1);
        positions.add(2);
        positions.add(3);

        Random random = new Random();

        int a = random.nextInt(positions.size());
        randomPositions[0] = positions.get(a);
        positions.remove(a);

        a = random.nextInt(positions.size());
        randomPositions[1] = positions.get(a);
        positions.remove(a);

        a = random.nextInt(positions.size());
        randomPositions[2] = positions.get(a);
        positions.remove(a);

        a = random.nextInt(positions.size());
        randomPositions[3] = positions.get(a);
        positions.remove(a);
    }

    /**
     * A method which is called when the player clicks a possible answer to check if they clicked
     * the correct answer.
     * @param view  the type of view pressed.
     */
    public void possibleAnswerPressed(View view) {

        String actualAnswer = questionQueue.getQuestionByIndex(questionNumber).getCharacter();

        if (view instanceof Button) {

            Button buttonPressed = (Button) view;
            String chosenAnswer = buttonPressed.getText().toString();

            if (chosenAnswer.equals(actualAnswer)) {
                correctAnswerChosen();
            } else {
                wrongAnswerChosen();
            }
        }
        else if (view instanceof ImageView) {

            ImageView characterPressed = (ImageView) view;
            String chosenAnswer = characterPressed.getTag().toString();

            if (chosenAnswer.equals(actualAnswer)) {
                correctAnswerChosen();
            } else {
                wrongAnswerChosen();
            }
        }
    }

    /**
     * A method which is called when the correct answer has been chosen.
     */
    public void correctAnswerChosen() {
        correctSound.start();
        questionNumber++;
        generateRandomPositionsToPossibleAnswers();
        animateBetweenQuestions();
        UIHandler.sendEmptyMessage(0);
    }

    /**
     * A method which is called when the incorrect answer has been chosen.
     */
    public void wrongAnswerChosen() {
        vibe.vibrate(100);
    }

}

