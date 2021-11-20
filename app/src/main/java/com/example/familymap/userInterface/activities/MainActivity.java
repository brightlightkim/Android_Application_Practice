package com.example.familymap.userInterface.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.familymap.R;

import com.example.familymap.userInterface.activities.fragments.*;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener{

    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("Re-sync"))){

            Fragment mapFragment = new MyMapFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container, mapFragment).commit();
        }
        else if (fragment == null) {
            fragment = new LoginFragment(this);
            ((LoginFragment) fragment).setLoginListener(this);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }


    public void loginComplete()
    {
        Fragment mapFragment = new MyMapFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


}
