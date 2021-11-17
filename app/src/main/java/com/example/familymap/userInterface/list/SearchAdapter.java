package com.example.familymap.userInterface.list;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.familymap.R;
import com.example.familymap.data.DataCache;
import com.example.familymap.userInterface.activities.EventActivity;
import com.example.familymap.userInterface.activities.PersonActivity;

import java.util.List;

import Model.Event;
import Model.Person;

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

    private List<Object> mObjects;
    private Context context;
    private LayoutInflater inflater;

    public SearchAdapter(List<Object> objects, Context context)
    {
        this.context = context;
        this.mObjects = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.list_item_event, parent, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position)
    {
        final Object currObject = mObjects.get(position);
        if (currObject instanceof Person){
            holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    personsClicked((Person) currObject);
                }
            });
            holder.bindPerson(currObject);
        }
        else{
            holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    eventClicked((Event) currObject);
                }
            });
            holder.bindEvent(currObject);
        }
    }

    @Override
    public int getItemCount()
    {
        return mObjects.size();
    }

    private void eventClicked(Event event)
    {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra("Event", "Event");
        DataCache.initialize().setSelectedEvent(event);
        context.startActivity(intent);
    }

    private void personsClicked(Person person)
    {
        Intent intent = new Intent(context, PersonActivity.class);
        DataCache.initialize().setSelectedPerson(person);
        context.startActivity(intent);
    }
}
