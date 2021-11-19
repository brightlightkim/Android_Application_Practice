package com.example.familymap.userInterface.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.example.familymap.R;

import com.example.familymap.userInterface.activities.fragments.*;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("Re-sync"))){

            Fragment mapFragment = new MyMapFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container, mapFragment).commit();
        }
        else if (fragment == null) {
            fragment = new LoginFragment(this);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }


    public void loginComplete()
    {
        Fragment mapFragment = new MyMapFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


}
