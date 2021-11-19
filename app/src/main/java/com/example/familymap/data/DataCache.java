package com.example.familymap.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.Person;
import Model.Event;

public class DataCache {
    Map<String, Person> people; //Person ID
    Map<String, Event> events; //Event ID

    Map<String, Event> displayedEvents;
    Map<String, List<Event>> allPersonEvents; // by person ID

    Set<String> paternalAncestors;
    Set<String> maternalAncestors;
    Map<String, Person> children;

    private Settings settings;
    private Filter filter;

    private List<String> eventTypes;
    private Map <String, MapColor> eventColor;

    private Person user;

    private String serverHost;
    private String ipAddress;
    private String authToken;

    private Person selectedPerson;
    private Event selectedEvent;

    private static DataCache instance = new DataCache();

    public static DataCache initialize() {
        return instance;
    }

    private DataCache() {
    }
    //Getters amd Setters

    public static void setInstance(DataCache instance) {
        DataCache.instance = instance;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public Map<String, List<Event>> getAllPersonEvents() {
        return allPersonEvents;
    }

    public void setAllPersonEvents(Map<String, List<Event>> allPersonEvents) {
        this.allPersonEvents = allPersonEvents;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Map<String, MapColor> getEventColor() {
        return eventColor;
    }

    public void setEventColor(Map<String, MapColor> eventColor) {
        this.eventColor = eventColor;
    }

    public static DataCache getInstance() {
        return instance;
    }

    public void setDisplayedEvents(Map<String, Event> displayedEvents) {
        this.displayedEvents = displayedEvents;
    }

    public Set<String> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors(Set<String> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public Set<String> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors(Set<String> maternalAncestors) {
        this.maternalAncestors = maternalAncestors;
    }

    public Map<String, Person> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Person> children) {
        this.children = children;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    //____________________________________ Data Manipulation Methods _________________________________
    //--****************-- Is Person Included in the Filter --***************--
    public boolean isPersonDisplayed(Person currPerson)
    {
        if (!filter.isMales() && currPerson.getGender().toLowerCase().equals("m")){
            return false;
        }
        else if (!filter.isFemales() && currPerson.getGender().toLowerCase().equals("f")){
            return false;
        }
        else if (!filter.isFathersSide() && paternalAncestors.contains(currPerson.getPersonID())) {
            return false;
        }
        else return filter.isMothersSide() || !maternalAncestors.contains(currPerson.getPersonID());
    }

    //--****************-- Sort Events By Year --***************--
    public List<Event> sortEventsByYear(List<Event> eventsArrayList)
    {
        List<Event> sortedEventsList = new ArrayList<>();
        List<Event> currArrayList = new ArrayList<>(eventsArrayList);

        while(currArrayList.size() > 0){
            Event currEvent = currArrayList.get(0);
            int index = 0;
            for (int i = 0; i < currArrayList.size(); i++){
                if (currArrayList.get(i).getYear() < currEvent.getYear()){
                    currEvent = currArrayList.get(i);
                    index = i;
                }
            }
            sortedEventsList.add(currEvent);
            currArrayList.remove(index);
        }
        return sortedEventsList;
    }

    //--****************-- Find all Relatives of a Person --***************--
    public List<Person> findRelatives(String personID)
    {
        Person currPerson = getPeople().get(personID);
        List<Person> personList = new ArrayList<>();

        if(getPeople().get(currPerson.getSpouseID()) != null){
            personList.add(getPeople().get(currPerson.getSpouseID()));
        }
        if(getPeople().get(currPerson.getMotherID()) != null){
            personList.add(getPeople().get(currPerson.getMotherID()));
        }
        if(getPeople().get(currPerson.getFatherID()) != null){
            personList.add(getPeople().get(currPerson.getFatherID()));
        }
        if(getChildren().get(currPerson.getPersonID()) != null){
            personList.add(getChildren().get(currPerson.getPersonID()));
        }

        return personList;
    }

    //--****************-- Get all Events that are Displayed --***************--
    public Map<String, Event> getDisplayedEvents()
    {
        displayedEvents = new HashMap<>();

        for (Event currEvent: events.values()) {
            Person eventPerson = getPeople().get(currEvent.getPersonID());
            if (!isPersonDisplayed(eventPerson)){
            }
            else if (!filter.containsEventType(currEvent.getEventType())){
            }
            else {
                displayedEvents.put(currEvent.getEventID(), currEvent);
            }
        }
        return displayedEvents;
    }

    //____________________________________ Initialize rest of Data _________________________________
    public void initializeAllData()
    {
        initializeEventTypes();
        initializePaternalTree();
        initializeMaternalTree();
        initializeAllPersonEvents();
        initializeAllChildren();
        if (settings == null){
            settings = new Settings();
        }
        if (filter == null) {
            filter = new Filter();
        }
    }

    //--****************-- Event Types --***************--
    private void initializeEventTypes()
    {
        ArrayList<Event> eventsArray = new ArrayList<>();
        try {
            for (Event currEvent : events.values()) {
                eventsArray.add(currEvent);
            }
        }
        catch (NullPointerException exception){
            exception.printStackTrace();
            throw new NullPointerException("events values are null");
        }
        eventColor = new HashMap<>();
        eventTypes = new ArrayList<>();
        for (int i = 0; i < eventsArray.size(); i++){
            if (!eventColor.containsKey(eventsArray.get(i).getEventType().toLowerCase())){
                eventColor.put(eventsArray.get(i).getEventType().toLowerCase(),
                        new MapColor(eventsArray.get(i).getEventType().toLowerCase()));

                eventTypes.add(eventsArray.get(i).getEventType().toLowerCase());
            }
        }
        instance.setEventTypes(eventTypes);
    }

    //--****************-- Paternal and Maternal Tree Start --***************--
    private void initializePaternalTree()
    {
        paternalAncestors = new HashSet<>();
        ancestorHelper(user.getFatherID(), paternalAncestors);
    }

    private void initializeMaternalTree()
    {
        maternalAncestors = new HashSet<>();
        ancestorHelper(user.getMotherID(), maternalAncestors);
    }

    //--****************-- Ancestor Recursive Helper --***************--
    private void ancestorHelper(String currPersonID, Set<String> personSet)
    {
        if (currPersonID == null){
            return;
        }
        personSet.add(currPersonID);
        Person currPerson = people.get(currPersonID);

        if (currPerson.getFatherID() != null) {
            ancestorHelper(currPerson.getFatherID(), personSet);
        }

        if (currPerson.getMotherID() != null) {
            ancestorHelper(currPerson.getMotherID(), personSet);
        }
    }

    //--****************-- All Events per Person --***************--
    private void initializeAllPersonEvents()
    {
        allPersonEvents = new HashMap<>();
        for (Person person: people.values()) {
            ArrayList<Event> eventList = new ArrayList<Event>();

            for (Event event: events.values()) {
                if (person.getPersonID().equals(event.getPersonID())){
                    eventList.add(event);
                }
            }
            allPersonEvents.put(person.getPersonID(),eventList);
        }
    }

    //--****************-- All Children of each Person --***************--
    private void initializeAllChildren()
    {
        children = new HashMap<>();
        for (Person person: people.values()) {

            if (person.getFatherID() != null){
                children.put(person.getFatherID(), person);
            }
            if (person.getMotherID() != null){
                children.put(person.getMotherID(), person);
            }
        }
    }
}
