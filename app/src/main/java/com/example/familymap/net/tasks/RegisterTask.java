package com.example.familymap.net.tasks;

import android.os.AsyncTask;

import com.example.familymap.net.ServerProxy;

import Request.RegisterRequest;
import Result.RegisterResult;

public class RegisterTask extends AsyncTask<RegisterRequest, RegisterResult, RegisterResult> implements DataTask.DataContext{

    private String serverHost;
    private String ipAddress;
    private RegisterContext context;

    ////////// Interface ///////////
    public interface RegisterContext {
        void onExecuteComplete(String message);
    }

    // ========================== Constructor ========================================
    public RegisterTask(String server, String ip, RegisterContext c)
    {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected RegisterResult doInBackground(RegisterRequest... registerRequests)
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterResult regResult = serverProxy.register(serverHost, ipAddress, registerRequests[0]);
        return regResult;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(RegisterResult registerResult)
    {
        if (registerResult.getMessage() == null){
            DataTask dataTask = new DataTask(serverHost, ipAddress, this);
            dataTask.execute(registerResult.getAuthtoken());
        }
        else {
            context.onExecuteComplete(registerResult.getMessage());
        }
    }


    //--****************-- Completion from DataTask --***************--
    @Override
    public void onExecuteCompleteData(String message)
    {
        context.onExecuteComplete(message);
    }
}