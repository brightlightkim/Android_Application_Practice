package com.example.familymap.userInterface.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymap.BuildConfig;
import com.example.familymap.Question;
import com.example.familymap.QuizViewModel;
import com.example.familymap.R;

import java.time.Duration;
import java.time.OffsetTime;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;

    private Question[] questionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)};

    private int currentIndex = 0;

    public MainActivity() {
        if(BuildConfig.DEBUG)
            StrictMode.enableDefaults();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);
        // background task
        // go through the slides >> data cache
        // get back to main thread >>

        //
        QuizViewModel quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        Log.d(TAG, "Got a QuizViewModel: " + quizViewModel);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(view -> checkAnswer(true));

        falseButton.setOnClickListener(view -> checkAnswer(false));

        nextButton.setOnClickListener(view -> {
            currentIndex = (currentIndex + 1) % questionBank.length;
            updateQuestion();
        });

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int questionTextResId = questionBank[currentIndex].getTextResId();
        questionTextView.setText(questionTextResId);
    }

    private void checkAnswer(boolean userAnswer) {
        boolean correctAnswer = questionBank[currentIndex].getAnswer();

        int messageResId = ((userAnswer == correctAnswer) ?
                R.string.correct_toast : R.string.incorrect_toast);

        Toast.makeText(this.getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
    }

}