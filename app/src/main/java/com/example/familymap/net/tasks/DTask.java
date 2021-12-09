package com.example.familymap.net.tasks;

import com.example.familymap.data.DataCache;
import com.example.familymap.net.ServerProxy;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import Model.Event;
import Model.Person;
import Result.EventResult;
import Result.EventsResult;
import Result.PersonResult;
import Result.PersonsResult;

public class DTask implements Runnable{
    private final Handler messageHandler;

    private String serverHost;
    private String ipAddress;
    private DataTask.DataContext context;
    private DataCache dataCache = DataCache.initialize();
    private String[] authToken;

    ///////// Interface //////////
    public interface DataContext {
        void onExecuteCompleteData(String message);
    }

    public DTask(Handler messageHandler, String server, String ip, DataTask.DataContext c, String... authToken) {
        this.messageHandler = messageHandler;
        serverHost = server;
        ipAddress = ip;
        context = c;
        this.authToken = authToken;
    }

    @Override
    public void run() {
        ServerProxy serverProxy = ServerProxy.initialize();
        PersonsResult allPersonResults = serverProxy.getPersonsResult(serverHost, ipAddress, authToken[0]);
        EventsResult allEventResults = serverProxy.getEvents(serverHost, ipAddress, authToken[0]);

        Boolean bool = sendDataToModel(allPersonResults, allEventResults);

    }

    private void sendMessage(int progress, Long totalSize) {
        //TODO: send the data that will be shown on the screen.
    }

    private boolean sendDataToModel(PersonsResult personsResult, EventsResult eventsResult)
    {
        return (initializeAllEvents(eventsResult) && initializeAllPeople(personsResult));
    }

    //--****************-- Initializing People --***************--
    private boolean initializeAllPeople(PersonsResult personsResult)
    {
        if (personsResult.getMessage() == null){
            Map<String, Person> personsMap = new HashMap<>();
            ArrayList<PersonResult> personArray = personsResult.getData();
            int desiredNum = 0;
            for (int i = 0; i < personArray.size(); i++){
                if (personArray.get(i).getSpouseID() == null){
                    desiredNum = i;
                    break;
                }
            }
            PersonResult user = personArray.get(desiredNum);
            Person initialPerson = new Person(user.getPersonID(), user.getAssociatedUsername(),
                    user.getFirstName(), user.getLastName(), user.getGender(),
                    user.getFatherID(), user.getMotherID(), user.getSpouseID());
            dataCache.setUser(initialPerson);

            for(int i = 0; i < personArray.size(); i++){
                String personID = personArray.get(i).getPersonID();
                Person person = new Person(personArray.get(i).getPersonID(), personArray.get(i).getAssociatedUsername(),
                        personArray.get(i).getFirstName(), personArray.get(i).getLastName(), personArray.get(i).getGender(),
                        personArray.get(i).getFatherID(), personArray.get(i).getMotherID(), personArray.get(i).getSpouseID());
                personsMap.put(personID, person);
            }

            dataCache.setPeople(personsMap);
            return true;
        }
        return false;
    }

    //--****************-- Initializing Events --***************--
    private boolean initializeAllEvents(EventsResult eventsResult)
    {
        if (eventsResult.getMessage() == null){
            Map<String, Event> eventsMap = new HashMap<>();
            ArrayList<EventResult> eventsArray = eventsResult.getData();

            for(int i = 0; i < eventsArray.size(); i++){
                String eventID = eventsArray.get(i).getEventID();
                Event event = new Event(eventsArray.get(i).getEventID(), eventsArray.get(i).getAssociatedUsername(),
                        eventsArray.get(i).getPersonID(), eventsArray.get(i).getLatitude(), eventsArray.get(i).getLongitude(),
                        eventsArray.get(i).getCountry(), eventsArray.get(i).getCity(), eventsArray.get(i).getEventType(),
                        eventsArray.get(i).getYear());
                eventsMap.put(eventID, event);
            }

            dataCache.setEvents(eventsMap);
            return true;
        }
        return false;
    }

}
