package com.example.spotifywrapped;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.spotifywrapped.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spotifywrapped.databinding.ActivitySpotifyBinding;
import com.google.gson.Gson;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySpotifyBinding binding;

    TextView tokenTextView, codeTextView, profileTextView;

    TextView songText, albumText, artistText, song2, song3, song4, song5, song6, song7, song8, song9, song10;
    String userId;


    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    public User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);

        binding = ActivitySpotifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_spotify);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

         */


        getToken();


        /*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_spotify);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

         */


        /*
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

         */
        songText = (TextView) findViewById(R.id.topName);
        artistText = (TextView) findViewById(R.id.topArtist);
        albumText = (TextView) findViewById(R.id.topAlbum);
        song2 = (TextView) findViewById(R.id.song2);
        song3 = (TextView) findViewById(R.id.song3);
        song4 = (TextView) findViewById(R.id.song4);
        song5 = (TextView) findViewById(R.id.song5);
        song6 = (TextView) findViewById(R.id.song6);
        song7 = (TextView) findViewById(R.id.song7);
        song8 = (TextView) findViewById(R.id.song8);
        song9 = (TextView) findViewById(R.id.song9);
        song10 = (TextView) findViewById(R.id.song10);



        Button topItemsBtn = (Button) findViewById(R.id.button_top_items);

        topItemsBtn.setOnClickListener((v) -> {
            onGetTopItemsClicked();
        });

    }

    public void getToken() {
        final AuthorizationRequest tokenRequest = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(SpotifyActivity.this, SpotifyInfo.AUTH_TOKEN_REQUEST_CODE, tokenRequest);
    }

    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(SpotifyActivity.this, SpotifyInfo.AUTH_CODE_REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // check which request code is present
        if (SpotifyInfo.AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
        } else if (SpotifyInfo.AUTH_CODE_REQUEST_CODE == requestCode) {
            mAccessCode = response.getCode();
            setTextAsync(mAccessCode, codeTextView);
        }
    }

    public void onGetUserProfileClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(SpotifyActivity.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray array = jsonObject.getJSONArray("data");
                    String[] arr = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        arr[i] = array.getString(i);
                    }
                    /*
                    Gson gson = new Gson();
                    userInfo = gson.fromJson(jsonObject.toString(), User.class);
                    userId = userInfo.getId();

                     */
                    setTextAsync(arr[1], profileTextView);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(SpotifyActivity.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onGetTopItemsClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(SpotifyActivity.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());


                    JSONArray array = jsonObject.getJSONArray("items");
                    TextView[] songs = {song2, song3, song4, song5, song6, song7, song8, song9, song10};
                    for (int i = 1; i < 10; i++) {
                        if (i >= array.length()) {
                            break;
                        }
                        JSONObject obj = (JSONObject) array.get(i);

                        String name = obj.getString("name");
                        JSONArray artists = obj.getJSONArray("artists");
                        JSONObject topArtist = (JSONObject) artists.get(0);
                        String artist = topArtist.getString("name");
                        String message = name + " by " + artist;
                        setTextAsync(message, songs[i - 1]);
                    }

                    JSONObject top = (JSONObject) array.get(0);
                    JSONObject album = (JSONObject) top.get("album");
                    JSONArray artists = top.getJSONArray("artists");
                    JSONObject topArtist = (JSONObject) artists.get(0);


                    String name = top.getString("name");
                    String albumName = "Album: " + album.getString("name");
                    String artist = "by " + topArtist.getString("name");

                    setTextAsync(name, songText);
                    setTextAsync(artist, artistText);
                    setTextAsync(albumName, albumText);


                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                }
            }
        });
    }

    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(SpotifyInfo.CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-read-email", "user-top-read", "user-follow-read" })
                .setCampaign("your-campaign-token")
                .build();
    }

    private Uri getRedirectUri() {
        return Uri.parse(SpotifyInfo.REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }


    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_spotify);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

     */




}