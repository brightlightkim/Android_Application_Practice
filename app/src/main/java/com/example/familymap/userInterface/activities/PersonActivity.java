package com.example.familymap.userInterface.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familymap.R;
import com.example.familymap.data.DataCache;
import com.example.familymap.userInterface.list.PersonActivityListAdapter;

import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {

    private Person currPerson;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;

    private ExpandableListView mListView;
    private ExpandableListAdapter mListAdapter;

    private DataCache model = DataCache.initialize();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FamilyMap: Person Details");
        currPerson = model.getSelectedPerson();

        mFirstName = findViewById(R.id.person_first_name);
        mLastName = findViewById(R.id.person_last_name);
        mGender = findViewById(R.id.person_gender);

        mFirstName.setText(currPerson.getFirstName());
        mLastName.setText(currPerson.getLastName());
        mGender.setText(currPerson.getGender().toUpperCase());

        mListView = findViewById(R.id.expandable_list_person_activity);

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                if (groupPosition == 0){
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("Event", "Event");
                    model.setSelectedEvent((Event) mListAdapter.getChild(groupPosition, childPosition));
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    model.setSelectedPerson((Person) mListAdapter.getChild(groupPosition, childPosition));
                    startActivity(intent);
                }
                return false;
            }
        });

        updateUI();
    }

    //--****************-- Initialize the PersonActivity Adapter --***************--
    private void updateUI()
    {
        List<Person> relatives = new ArrayList<>(model.findRelatives(currPerson.getPersonID()));

        List<Event> eventsArrayList = new ArrayList<>(model.getAllPersonEvents().get(currPerson.getPersonID()));
        eventsArrayList = model.sortEventsByYear(eventsArrayList);

        List<String> headers = new ArrayList<>();
        headers.add("Events");
        headers.add("Relatives");

        eventsArrayList = filterEvents(eventsArrayList);
        relatives = filterPersons(relatives);

        mListAdapter = new PersonActivityListAdapter(this, headers, eventsArrayList, relatives, currPerson);
        mListView.setAdapter(mListAdapter);
    }

    //--****************-- Overriding the up Button --***************--
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    //--****************-- Filter Event based on Filters --***************--
    private List<Event> filterEvents(List<Event> eventsList)
    {
        List<Event> testEventList = new ArrayList<>();
        for (Event currEvent: eventsList) {
            if (model.getDisplayedEvents().containsValue(currEvent)){
                testEventList.add(currEvent);
            }
        }
        return testEventList;
    }

    //--****************-- Filter People based on Filters --***************--
    private List<Person> filterPersons(List<Person> personsList)
    {
        List<Person> filteredPersonsList = new ArrayList<>();

        for (Person person: personsList) {
            if (model.isPersonDisplayed(person)){
                filteredPersonsList.add(person);
            }
        }
        return filteredPersonsList;
    }
}
