package com.example.familymap.net.tasks;

import android.os.AsyncTask;

import com.example.familymap.data.DataCache;
import com.example.familymap.net.ServerProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.Event;
import Model.Person;
import Result.EventResult;
import Result.EventsResult;
import Result.PersonResult;
import Result.PersonsResult;

/** DataTask
 * DataTask extends the AsyncTask and reaches the server to extract all information regarding user
 * after successful login or register
 */
public class DataTask extends AsyncTask<String, Boolean, Boolean> {

    private String serverHost;
    private String ipAddress;
    private DataContext context;
    private DataCache dataCache = DataCache.initialize();

    ///////// Interface //////////
    public interface DataContext {
        void onExecuteCompleteData(String message);
    }

    // ========================== Constructor ========================================
    public DataTask(String server, String ip, DataTask.DataContext c)
    {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected Boolean doInBackground(String... authToken)
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        PersonsResult allPersonResults = serverProxy.getPersonsResult(serverHost, ipAddress, authToken[0]);
        EventsResult allEventResults = serverProxy.getEvents(serverHost, ipAddress, authToken[0]);

        Boolean bool = sendDataToModel(allPersonResults, allEventResults);
        return bool;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(Boolean bool) {
        if (bool){
            Person user = dataCache.getUser();
            String message = "Welcome, " + user.getFirstName() + " " + user.getLastName();
            context.onExecuteCompleteData(message);
            dataCache.initializeAllData();
        }
        else {
            context.onExecuteCompleteData("Error occurred with user data");
        }
    }

    //_______________________________ Data Initialization __________________________________________
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

            Person initialPerson = new Person(personArray.get(0).getPersonID(), personArray.get(0).getAssociatedUsername(),
                    personArray.get(0).getFirstName(), personArray.get(0).getLastName(), personArray.get(0).getGender(),
                    personArray.get(0).getFatherID(), personArray.get(0).getMotherID(), personArray.get(0).getSpouseID());
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