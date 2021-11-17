package com.example.familymap.userInterface.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.R;
import com.example.familymap.data.DataCache;

import java.util.List;

import Model.Event;
import Model.Person;

public class PersonActivityListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> mHeaders;
    private List<Event> mEvents;
    private List<Person> mPersons;
    private Person mCurrPerson;

    private TextView mFirstLine;
    private TextView mSecondLine;
    private ImageView mListIcon;

    private DataCache model = DataCache.initialize();

    public PersonActivityListAdapter(Context context, List<String> listDataHeader,
                                     List<Event> eventsList, List<Person> personsList,
                                     Person person) {
        this.context = context;
        this.mHeaders = listDataHeader;
        this.mEvents = eventsList;
        this.mPersons = personsList;
        this.mCurrPerson = person;
    }

    @Override
    public int getGroupCount()
    {
        return mHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (groupPosition == 0){
            return mEvents.size();
        }
        else{
            return mPersons.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        if (groupPosition == 0){
            return mEvents;
        }
        else{
            return mPersons;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if(groupPosition == 0){
            return mEvents.get(childPosition);
        }
        else{
            return mPersons.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = mHeaders.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_header_event, null);
        }

        TextView header = convertView.findViewById(R.id.event_header);
        header.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_event, null);
        }

        mFirstLine = convertView.findViewById(R.id.event_list_info);
        mSecondLine = convertView.findViewById(R.id.event_list_person);
        mListIcon = convertView.findViewById(R.id.list_item_icon);

        if (groupPosition == 0) {
            Event currEvent = (Event) getChild(groupPosition, childPosition);

            mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.map_pointer_icon));
            update(currEvent, null);

        }
        else{
            Person currPerson = (Person) getChild(groupPosition, childPosition);

            if (currPerson.getGender().toLowerCase().equals("m")){
                mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.icons8_male_52));
            }
            else {
                mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.icons8_female_52));
            }

            update(null, currPerson);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }


    private void update(Event events, Person persons)
    {
        if (persons == null) {
            String eventInfo = events.getEventType() + ", " + events.getCity() + ", " + events.getCountry() + " " + events.getYear();
            mFirstLine.setText(eventInfo);
            Person currPerson = model.getPeople().get(events.getPersonID());
            String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
            mSecondLine.setText(personInfo);
        }
        else {
            String personInfo = persons.getFirstName() + " " + persons.getLastName();
            mFirstLine.setText(personInfo);
            mSecondLine.setText(getRelationship(persons));

        }
    }

    private String getRelationship(Person persons)
    {
        if (mCurrPerson.getSpouseID().equals(persons.getPersonID())) {
            return "Spouse";
        }

        if (persons.getFatherID() != null && persons.getMotherID() != null) {
            if (persons.getFatherID().equals(mCurrPerson.getPersonID()) ||
                    persons.getMotherID().equals(mCurrPerson.getPersonID())) {
                return "Child";
            }
        }

        if (mCurrPerson.getMotherID() != null && mCurrPerson.getMotherID() != null) {
            if (mCurrPerson.getFatherID().equals(persons.getPersonID())) {
                return "Father";
            }
            else if (mCurrPerson.getMotherID().equals(persons.getPersonID())) {
                return "Mother";
            }
        }
        return "Error";
    }

}