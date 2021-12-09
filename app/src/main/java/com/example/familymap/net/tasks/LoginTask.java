package com.example.familymap.net.tasks;


import android.os.AsyncTask;

import com.example.familymap.data.DataCache;
import com.example.familymap.net.ServerProxy;

import Request.LoginRequest;
import Result.LoginResult;

/** LoginTask
 * The LoginTask extends the AsyncTask and is used to check if the login or register request is valid
 * and then uses a DataTask to extract data
 */
public class LoginTask extends AsyncTask<LoginRequest, LoginResult, LoginResult> implements DataTask.DataContext {
    private String serverHost;
    private String ipAddress;
    private LoginContext context;

    ///////// Interface //////////
    public interface LoginContext {
        void onExecuteComplete(String message);
    }

    // ========================== Constructor ========================================
    public LoginTask(String server, String ip, LoginContext c)
    {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected LoginResult doInBackground(LoginRequest... logReqs)
    { //for the Handler job
        ServerProxy serverProxy = ServerProxy.initialize();
        LoginResult loginResult = serverProxy.login(serverHost, ipAddress, logReqs[0]);
        return loginResult;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(LoginResult loginResult)
    { //for the run
        if (loginResult.getMessage() == null){
            DataCache dataCache = DataCache.initialize();

            dataCache.setServerHost(serverHost);
            dataCache.setIpAddress(ipAddress);
            dataCache.setAuthToken(loginResult.getAuthtoken());

            DataTask dataTask = new DataTask(serverHost, ipAddress, this);
            dataTask.execute(loginResult.getAuthtoken());
        }
        else {
            context.onExecuteComplete(loginResult.getMessage());
        }
    }

    //--****************-- Receive Completion from DataTask --***************--
    @Override
    public void onExecuteCompleteData(String message)
    {
        context.onExecuteComplete(message);
    }


}






