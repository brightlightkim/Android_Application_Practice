package com.example.familymap.userInterface.list;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.familymap.R;
import com.example.familymap.data.DataCache;
import com.example.familymap.data.Filter;

public class FilterHolder extends RecyclerView.ViewHolder {

    private TextView mEventType;
    private TextView mEventDescription;
    private Switch mEventSwitch;
    private Filter filter = DataCache.initialize().getFilter();

    public FilterHolder(View itemView)
    {
        super(itemView);

        mEventType = itemView.findViewById(R.id.filter_setting);
        mEventDescription = itemView.findViewById(R.id.filter_description);
        mEventSwitch = itemView.findViewById(R.id.filter_switch);
    }

    public Switch getSwitch()
    {
        return mEventSwitch;
    }

    public void bind(String eventType)
    {
        String eventTypeText = eventType + " Events";
        String eventDescription = "filter by " + eventType + " events";

        mEventSwitch.setChecked(filter.containsEventType(eventType));
        mEventType.setText(eventTypeText);
        mEventDescription.setText(eventDescription.toUpperCase());
    }

    public void bindDefaults(String defaultText, int index)
    {
        String defaultDescription;
        boolean isChecked;

        if(index == 0){
            defaultDescription = "filter by father's side of family";
            isChecked = filter.isFathersSide();
        }
        else if (index == 1){
            defaultDescription = "filter by mother's side of family";
            isChecked = filter.isMothersSide();
        }
        else if (index == 2) {
            defaultDescription = "filter events based on gender";
            isChecked = filter.isMales();
        }
        else {
            defaultDescription = "filter events based on gender";
            isChecked = filter.isFemales();
        }

        mEventType.setText(defaultText);
        mEventDescription.setText(defaultDescription);
        mEventSwitch.setChecked(isChecked);
    }

}
