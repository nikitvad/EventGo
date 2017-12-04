package com.ghteam.eventgo.ui.activity.createevent;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 03.12.2017.
 */

public class ListLiveData<T> extends LiveData<List<T>> {
    private List<T> mList;

    private List<Observer<T>> insertObservers;
    private List<Observer<T>> removeObservers;

    public ListLiveData(List<T> list) {
        mList = list;

        insertObservers = new ArrayList<>();
        removeObservers = new ArrayList<>();
    }

    public T getItem(int pos) {
        return mList.get(pos);
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
        mList.add(item);
        if (insertObservers.size() > 0) {
            for (Observer<T> observer : insertObservers) {
                observer.onChanged(item);
            }
        }
    }

    public void removeItem(T item) {
        int pos = mList.indexOf(item);
        mList.remove(pos);
        if (removeObservers.size() > 0) {
            for (Observer<T> observer : removeObservers) {
                observer.onChanged(item);
            }
        }
    }
}
