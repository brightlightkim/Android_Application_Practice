package com.example.familymap.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

public class ServerProxy {

    private static ServerProxy serverProxy;

    public static ServerProxy initialize()
    {
        if (serverProxy == null){
            serverProxy = new ServerProxy();
        }
        return serverProxy;
    }


    public LoginResult login(String serverHost, String serverPort, LoginRequest loginRequest)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.addRequestProperty("Accept", "application/json");

            connection.connect();

            String requestInfo = gson.toJson(loginRequest);
            OutputStream body = connection.getOutputStream();
            writeString(requestInfo, body);

            body.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = connection.getInputStream();
                String respData = readString(respBody);
                LoginResult loginResult = gson.fromJson(respData, LoginResult.class);
                return loginResult;
            }
            else {
                return new LoginResult(connection.getResponseMessage(), false);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new LoginResult("Error with Login", false);
        }
    }

    public RegisterResult register(String serverHost, String serverPort, RegisterRequest regReq)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Add an auth token to the request in the HTTP "Authorization" header
            // http.addRequestProperty("Authorization", "afj232hj2332");

            connection.addRequestProperty("Accept", "application/json");

            connection.connect();

            String requestInfo = gson.toJson(regReq);
            OutputStream body = connection.getOutputStream();
            writeString(requestInfo, body);

            body.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                String respData = readString(respBody);
                RegisterResult regResult = gson.fromJson(respData, RegisterResult.class);
                return regResult;
            }
            else {
                //indicate that something went wrong.
                System.out.println("ERROR: " + connection.getResponseMessage());
                return new RegisterResult(connection.getResponseMessage(), false);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new RegisterResult("Error with Registering User", false);
        }
    }

    public PersonsResult getPersonsResult(String serverHost, String serverPort, String authToken)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", authToken);
            connection.addRequestProperty("Accept", "application/json");

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = connection.getInputStream();
                String respData = readString(respBody);
                PersonsResult personsResult = gson.fromJson(respData, PersonsResult.class);
                return personsResult;
            }
            else {
                return new PersonsResult(connection.getResponseMessage(), false);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new PersonsResult("Error with retrieving all people", false);
        }
    }

    public EventsResult getEvents(String serverHost, String serverPort, String authToken)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", authToken);
            connection.addRequestProperty("Accept", "application/json");

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = connection.getInputStream();
                String respData = readString(respBody);
                EventsResult eventsResult = gson.fromJson(respData, EventsResult.class);
                return eventsResult;
            }
            else {
                return new EventsResult(connection.getResponseMessage(), false);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new EventsResult("Error with retrieving all events", false);
        }
    }

    private static String readString(InputStream input) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(input);
        char[] buffer = new char[1024];
        int len;
        while ((len = reader.read(buffer)) > 0) {
            builder.append(buffer, 0, len);
        }
        return builder.toString();
    }

    private static void writeString(String input, OutputStream output) throws IOException
    {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        writer.write(input);
        writer.flush();
    }

}

