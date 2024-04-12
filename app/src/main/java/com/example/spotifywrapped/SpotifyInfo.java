package com.example.spotifywrapped;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyInfo {
    public static String CLIENT_ID = "4560b5dacac741808ab90c6b7d585003";
    public static int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;
    public static String REDIRECT_URI = "com.example.spotifywrapped://auth";
    public static String[] SCOPES = {"user-top-read", "user-read-recently-played", "user-library-modify","user-library-read","playlist-modify-public","playlist-modify-private","user-read-email","user-read-private","user-read-birthdate","playlist-read-private","playlist-read-collaborative"};

}