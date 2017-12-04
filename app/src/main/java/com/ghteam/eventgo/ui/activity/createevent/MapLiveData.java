package com.ghteam.eventgo.ui.activity.createevent;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nikit on 03.12.2017.
 */

public class MapLiveData<K, V> extends LiveData<Map<K, V>> {
    private Map<K, V> mMap;

    private List<Observer<K>> putObservers;
    private List<Observer<K>> removeObservers;


    public MapLiveData(Map<K, V> map) {
        this.mMap = map;

        putObservers = new ArrayList<>();
        removeObservers = new ArrayList<>();
    }

    public void put(K key, V value) {
        mMap.put(key, value);

        if (putObservers.size() > 0) {
            for (Observer<K> observer : putObservers) {
                observer.onChanged(key);
            }
        }
    }

    public V get(K key){
        return mMap.get(key);
    }

    public void remove(K key) {
        mMap.remove(key);

        if (removeObservers.size() > 0) {
            for (Observer<K> observer : removeObservers) {
                observer.onChanged(key);
            }
        }
    }

    public void addPutObserver(Observer<K> observer) {
        putObservers.add(observer);
    }

    public void deletePutObservers() {
        putObservers.clear();
    }

    public void addRemoveObserver(Observer<K> observer) {
        removeObservers.add(observer);
    }

    public void deleteRemoveObservers() {
        removeObservers.clear();
    }

}
