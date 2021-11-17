package com.example.familymap;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class QuizViewModel extends ViewModel {
    private final String TAG = "QuizViewModel";

    public QuizViewModel() {
        super();
        Log.d(TAG, "ViewModel instance created");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "ViewModel instance about to be destroyed");
    }

}
