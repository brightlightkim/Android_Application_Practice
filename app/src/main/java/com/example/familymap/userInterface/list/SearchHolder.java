package com.example.familymap.userInterface.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.familymap.R;
import com.example.familymap.data.DataCache;

import Model.Event;
import Model.Person;

public class SearchHolder extends RecyclerView.ViewHolder {

    private View convertView;

    private LinearLayout mLinearLayout;
    private TextView mFirstLine;
    private TextView mDescription;
    private ImageView mIcon;

    public SearchHolder(View itemView)
    {
        super(itemView);
        mFirstLine = itemView.findViewById(R.id.event_list_info);
        mDescription = itemView.findViewById(R.id.event_list_person);
        mIcon = itemView.findViewById(R.id.list_item_icon);
        mLinearLayout = itemView.findViewById(R.id.linear_layout_click_area);
        mLinearLayout.setClickable(true);
        convertView = itemView;
    }

    public LinearLayout getLinearLayout()
    {
        return mLinearLayout;
    }

    public void bindEvent(Object currObject) {

        final Event event = (Event) currObject;
        String eventInfo = event.getEventType() + ", " + event.getCity() + ", "
                + event.getCountry() + " " + event.getYear();
        mFirstLine.setText(eventInfo);

        DataCache model = DataCache.initialize();
        Person currPerson = model.getPeople().get(event.getPersonID());
        String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
        mDescription.setText(personInfo);
        mIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.map_pointer_icon));

    }

    public void bindPerson(Object currObject)
    {
        Person currPerson = (Person) currObject;
        String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
        mFirstLine.setText(personInfo);
        mDescription.setVisibility(View.INVISIBLE);
        if (currPerson.getGender().toLowerCase().equals("m")) {
            mIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.icons8_male_52));
        } else {
            mIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.icons8_female_52));
        }
    }

}
