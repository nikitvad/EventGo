package com.ghteam.eventgo.ui.activity.createevent;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 03.12.2017.
 */

public class ListLiveData<T> extends MutableLiveData<List<T>> {
//    private List<T> mList;

    private List<Observer<T>> insertObservers;
    private List<Observer<T>> removeObservers;


    public ListLiveData() {
        super();
        setValue(new ArrayList<T>());
        insertObservers = new ArrayList<>();
        removeObservers = new ArrayList<>();
    }

    public ListLiveData(List<T> list) {
        super();
        setValue(list);
        insertObservers = new ArrayList<>();
        removeObservers = new ArrayList<>();

    }


    public T getItem(int pos) {
        return getValue().get(pos);
    }

    public void addInsertObserver(Observer<T> observer) {
        insertObservers.add(observer);
    }

    public void deleteInsertObserver(Observer<T> observer) {
        insertObservers.remove(observer);
    }

    public void deleteInsertObservers() {
        insertObservers.clear();
    }

    public void addRemoveObserver(Observer<T> observer) {
        removeObservers.add(observer);
    }

    public void deleteRemoveObserver(Observer<T> observer) {
        removeObservers.remove(observer);
    }

    public void deleteRemoveObservers() {
        removeObservers.clear();
    }

    public void addItem(T item) {
        getValue().add(item);
        if (insertObservers.size() > 0) {
            for (Observer<T> observer : insertObservers) {
                observer.onChanged(item);
            }
        }
    }

    public void removeItem(T item) {
        int pos = getValue().indexOf(item);
        getValue().remove(pos);
        if (removeObservers.size() > 0) {
            for (Observer<T> observer : removeObservers) {
                observer.onChanged(item);
            }
        }
    }
}
