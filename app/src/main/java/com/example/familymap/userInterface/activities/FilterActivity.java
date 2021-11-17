package com.example.familymap.userInterface.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymap.R;
import com.example.familymap.data.DataCache;
import com.example.familymap.userInterface.list.FilterRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private RecyclerView mFilterRecycler;
    private FilterRecycleAdapter mFilterAdapter;

    private DataCache model = DataCache.initialize();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFilterRecycler = findViewById(R.id.filter_recycler);
        mFilterRecycler.setLayoutManager(new LinearLayoutManager(this));

        updateUI();
    }

    //--****************-- Initializing the Filter Adapter --***************--
    private void updateUI()
    {
        List<String> defaultFilter = new ArrayList<>();
        defaultFilter.add("Father's Side");
        defaultFilter.add("Mother's Side");
        defaultFilter.add("Male Events");
        defaultFilter.add("Female Events");

        List<String> eventTypes = model.getEventTypes();
        defaultFilter.addAll(eventTypes);
        mFilterAdapter = new FilterRecycleAdapter(defaultFilter, this);
        mFilterRecycler.setAdapter(mFilterAdapter);
    }

    //--****************-- Overriding the up Button and creating the Options Menu --***************--
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

}
