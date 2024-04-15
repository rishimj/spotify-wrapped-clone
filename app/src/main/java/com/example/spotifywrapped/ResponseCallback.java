package com.example.spotifywrapped;

public interface ResponseCallback {
    void onResponse(String response);
    void onError(Throwable throwable);
}
