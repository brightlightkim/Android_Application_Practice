package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(view -> Toast.makeText(
                MainActivity.this,
                R.string.correct_toast,
                Toast.LENGTH_SHORT)
                .show());

        falseButton.setOnClickListener(view -> Toast.makeText(
                MainActivity.this,
                R.string.incorrect_toast,
                Toast.LENGTH_SHORT)
                .show());

        int questionTextResId = questionBank[currentIndex].getTextResId();
        questionTextView.setText(questionTextResId);

    }
}