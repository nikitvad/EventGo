package com.ghteam.eventgo.ui.activity.createevent;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nikit on 03.12.2017.
 */

public class MapLiveData<K, V> extends MutableLiveData<Map<K, V>> {

    private List<Observer<K>> putObservers;
    private List<Observer<K>> removeObservers;


    public MapLiveData(Map<K, V> map) {
        super();
        setValue(map);
        putObservers = new ArrayList<>();
        removeObservers = new ArrayList<>();
    }

    public void put(K key, V value) {
        getValue().put(key, value);

        if (putObservers.size() > 0) {
            for (Observer<K> observer : putObservers) {
                observer.onChanged(key);
            }
        }
    }

    public V get(K key) {
        return getValue().get(key);
    }

    public void remove(K key) {
        getValue().remove(key);

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
