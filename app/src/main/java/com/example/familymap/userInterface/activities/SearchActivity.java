package com.example.familymap.userInterface.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymap.R;
import com.example.familymap.data.DataCache;
import com.example.familymap.userInterface.list.SearchAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchBar;
    private Button mSearchButton;
    private String searchInput;
    private RecyclerView mSearchRecycler;
    private RecyclerView.Adapter mSearchAdapter;

    private DataCache model = DataCache.initialize();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchBar = findViewById(R.id.search_text);
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                searchInput = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {}
        });

        mSearchButton = findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (searchInput != null){
                    updateUI();
                }
            }
        });

        mSearchRecycler = findViewById(R.id.list_search_recycler);
        mSearchRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    //--****************-- Overriding the up Button --***************--
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

    //--****************-- Initializing the Search Adapter --***************--
    private void updateUI()
    {
        List<Object> objectList = new ArrayList<>();

        Map<String, Person> availablePeople = model.getPeople();
        getPersonsList(availablePeople, objectList);

        Map<String, Event> availableEvents = model.getDisplayedEvents();
        getEventsList(availableEvents, objectList);

        if (objectList.size() != 0) {
            mSearchAdapter = new SearchAdapter(objectList, this);
            mSearchRecycler.setAdapter(mSearchAdapter);
        }
    }

    //--****************-- Get the Person List that contains the Search Input --***************--
    private void getPersonsList(Map<String, Person> allPeople, List<Object> objectList)
    {
        for (Person person: allPeople.values()) {
            if (person.getFirstName().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(person);
            }
            else if (person.getLastName().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(person);
            }
        }
    }

    //--****************-- Get the Event List that contains the Search Input --***************--
    private void getEventsList(Map<String, Event> availableEvents, List<Object> objectList)
    {
        for (Event event: availableEvents.values()) {
            if (event.getEventType().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
            else if (event.getCountry().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
            else if (event.getCity().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
        }
    }
}
