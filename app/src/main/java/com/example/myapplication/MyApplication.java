package com.example.myapplication;

import android.app.Application;

public class MyApplication extends Application
{
    private String mGlobalString;

    public String getGlobalString()
    {
        return mGlobalString;
    }

    public void setGlobalString(String globalString)
    {
        this.mGlobalString = globalString;
    }
}