package com.example.familymap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.Person;
import Model.Event;

public class DataCache {
    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {
    }

    Map<String, Person> people; //Person ID
    Map<String, Event> events; //Event ID
    Map<String, List<Event>> personEvents; // by person ID
    Set<String> paternalAncestors;
    Set<String> maternalAncestors;

    //Settings settings;

    Person getPersonById(String personID){
        return null;
    }

    Event getEventById(String eventID){
        return null;
    }

    List<Event> getPersonEvents(String personID){
        return null;
    }
}
