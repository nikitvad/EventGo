package com.ghteam.eventgo.data;

/**
 * Created by nikit on 19.11.2017.
 */

public class Repository {
    private static final Repository ourInstance = new Repository();

    public static Repository getInstance() {
        return ourInstance;
    }

    private Repository() {
    }


    public void createNewUser(){

    }
}
