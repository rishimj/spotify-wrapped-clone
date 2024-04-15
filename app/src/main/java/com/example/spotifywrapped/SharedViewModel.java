package com.example.spotifywrapped;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String[]> topTracks = new MutableLiveData<>();
    private MutableLiveData<String> userName = new MutableLiveData<>();

    public void setTopTracks(String[] tracks) {
        topTracks.setValue(tracks);
    }

    public void setUserName(String name) {
        userName.setValue(name);
    }

    public LiveData<String[]> getTopTracks() {
        return topTracks;
    }

    public LiveData<String> getUserName() {
        return userName;
    }
}