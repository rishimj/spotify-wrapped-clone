package com.example.spotifywrapped;//package com.example.groovyexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotAPIActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "4560b5dacac741808ab90c6b7d585003";
    public static final String REDIRECT_URI = "com.example.spotifywrapped://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;
    public static final String SCOPES = "user-top-read, user-read-recently-played, user-library-modify,user-library-read,playlist-modify-public,playlist-modify-private,user-read-email,user-read-private,user-read-birthdate,playlist-read-private,playlist-read-collaborative";
    public static final String[] SCOPES_ARR = {"user-top-read, user-read-recently-played", "user-library-modify", "user-library-read" , "playlist-modify-public" , "playlist-modify-private" , "user-read-email", "user-read-private","user-read-birthdate","playlist-read-private","playlist-read-collaborative"};

    private static final int REQUEST_CODE = 1234;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static String mAccessToken, mAccessCode;
    private Call mCall;
    private String spotifyUsername;

    private TextView tokenTextView, codeTextView, profileTextView, tracksTextView, songText, artistText, song2, song3, song4, song5,
            topSongTextDescriptor, place2, place3, place4, place5,
            topArtistTextDescriptor, mostListenedArtist, artist2, artist3, artist4, artist5, aPlace2,aPlace3, aPlace4, aPlace5, asscGenres;



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();

        setContentView(R.layout.spotify_api_info);

//        authenticateSpotify();
        // Initialize the views
//        tokenTextView = (TextView) findViewById(R.id.token_text_view);
//        codeTextView = (TextView) findViewById(R.id.code_text_view);
        profileTextView = (TextView) findViewById(R.id.response_text_view);
//        tracksTextView = (TextView) findViewById(R.id.top_tracks_textview);

        // Initialize the buttons
//        Button tokenBtn = (Button) findViewById(R.id.token_btn);
//        Button codeBtn = (Button) findViewById(R.id.code_btn);
        Button profileBtn = (Button) findViewById(R.id.profile_btn);
        Button shortTermTracksBtn = (Button) findViewById(R.id.toptracks_button_shortterm);
        Button midTermTracksBtn = (Button) findViewById(R.id.toptracks_button_mediumterm);
        Button longTermTracksBtn = (Button) findViewById(R.id.toptracks_button_longterm);

        shortTermTracksBtn.setVisibility(View.GONE);
        midTermTracksBtn.setVisibility(View.GONE);
        longTermTracksBtn.setVisibility(View.GONE);


        songText = (TextView) findViewById(R.id.topName);
//        profileTextView.setText("you");

        artistText = (TextView) findViewById(R.id.topArtist);
        song2 = (TextView) findViewById(R.id.song2);
        song3 = (TextView) findViewById(R.id.song3);
        song4 = (TextView) findViewById(R.id.song4);
        song5 = (TextView) findViewById(R.id.song5);
        topSongTextDescriptor = (TextView) findViewById(R.id.number_one_song_descriptor);
        place2 = (TextView) findViewById(R.id.secondPlaceText);
        place3 = (TextView) findViewById(R.id.thirdPlaceText);
        place4 = (TextView) findViewById(R.id.fourthPlaceText);
        place5 = (TextView) findViewById(R.id.fifthPlaceText);

        mostListenedArtist = (TextView) findViewById(R.id.mostListenedArtist);
        asscGenres = (TextView) findViewById(R.id.genresAssociated);
        artist2 = (TextView) findViewById(R.id.artist2);
        artist3 = (TextView) findViewById(R.id.artist3);
        artist4 = (TextView) findViewById(R.id.artist4);
        artist5 = (TextView) findViewById(R.id.artist5);
        topArtistTextDescriptor = (TextView) findViewById(R.id.number_one_artist_descriptor);
        aPlace2 = (TextView) findViewById(R.id.secondPlaceArtistText);
        aPlace3 = (TextView) findViewById(R.id.thirdPlaceArtistText);
        aPlace4 = (TextView) findViewById(R.id.fourthPlaceArtistText);
        aPlace5 = (TextView) findViewById(R.id.fifthPlaceArtistText);





//        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
//                .setShowDialog(true)
//                .setScopes(SCOPES_ARR).build();
        // Set the click listeners for the buttons

//        tokenBtn.setOnClickListener((v) -> {
            getToken();
//        });

//        codeBtn.setOnClickListener((v) -> {
//            getCode();
//        });
//        setTextAsync(profileTextView, );



        profileBtn.setOnClickListener((v) -> {
            onGetUserProfileClicked();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            shortTermTracksBtn.setVisibility(View.VISIBLE);
            midTermTracksBtn.setVisibility(View.VISIBLE);
            longTermTracksBtn.setVisibility(View.VISIBLE);
//            try {
//                Thread.sleep(1050);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            onGetTopTracksClicked("medium_term");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            onGetTopArtistsClicked("medium_term", 5);
        });

        shortTermTracksBtn.setOnClickListener((v) -> {
            onGetTopTracksClicked("short_term");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            onGetTopArtistsClicked("short_term",5);
        });
        midTermTracksBtn.setOnClickListener((v) -> {
            onGetTopTracksClicked("medium_term");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            onGetTopArtistsClicked("medium_term",5);
        });
        longTermTracksBtn.setOnClickListener((v) -> {
            onGetTopTracksClicked("long_term");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            onGetTopArtistsClicked("long_term",5);
        });




    }

    /**
     * Get token from Spotify
     * This method will open the Spotify login activity and get the token
     * What is token?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(SpotAPIActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    /**
     * Get code from Spotify
     * This method will open the Spotify login activity and get the code
     * What is code?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(SpotAPIActivity.this, AUTH_CODE_REQUEST_CODE, request);
    }

    public void setThreeTimeButtonsEnabled() {

    }




    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
//            AuthenticationClient
            mAccessToken = response.getAccessToken();
//            setTextAsync(mAccessToken, tokenTextView);

        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
            mAccessCode = response.getCode();
            setTextAsync(mAccessCode, codeTextView);
        }
    }

    public void onGetTopTracksClicked(String timePeriod) {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // actually MAKE THE REQEUEST:
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range=" + timePeriod)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
//                Toast.makeText(SpotAPIActivity.this, "Failed to fetch data, watch Logcat for more details",
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());

                    JSONArray array = jsonObject.getJSONArray("items");
                    TextView[] songs = {song2, song3, song4, song5};
                    TextView[] placeNums = {place2, place3, place4, place5};
                    for (int i = 1; i < 5; i++) {
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
                        setTextAsync("#" + (i+1), placeNums[i - 1]);
                    }

                    JSONObject top = (JSONObject) array.get(0);
                    JSONArray artists = top.getJSONArray("artists");
                    JSONObject topArtist = (JSONObject) artists.get(0);


                    String name = top.getString("name");
                    String artist = "by " + topArtist.getString("name");

                    setTextAsync(name, songText);
                    setTextAsync(artist, artistText);
                    setTextAsync("Your top song is: ", topSongTextDescriptor);
//                    setTextAsync(jsonObject.toString(3), tracksTextView);
//                    setTextAsync("piss", tracksTextView);

//                    setTextAsync(message, tracksTextView);
//                    finish();
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
//                    Toast.makeText(SpotAPIActivity.this, "Failed to parse data, watch Logcat for more details",
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public void onGetTopArtistsClicked(String timePeriod, int numItems) {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // actually MAKE THE REQEUEST:
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=" + timePeriod)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
//                Toast.makeText(SpotAPIActivity.this, "Failed to fetch data, watch Logcat for more details",
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());


                    JSONArray array = jsonObject.getJSONArray("items");
                    TextView[] artistsArr = {artist2, artist3, artist4, artist5};
                    TextView[] placeNums = {aPlace2, aPlace3, aPlace4, aPlace5};
                    for (int i = 1; i < numItems; i++) {
                        if (i >= array.length()) {
                            break;
                        }
                        JSONObject obj = (JSONObject) array.get(i);

//                        String name = obj.getString("name");
//                        JSONArray artists = obj.getJSONArray("artists");
//                        JSONObject topArtist = (JSONObject) artists.get(0);
                        String artist = obj.getString("name");
//                        String message = name + " by " + artist;
                        setTextAsync(artist, artistsArr[i - 1]);
                        setTextAsync("#" + (i+1), placeNums[i - 1]);
                    }

                    JSONObject top = (JSONObject) array.get(0);
                    int numFollowers = top.getJSONObject("followers").getInt("total");
                    JSONArray associatedGenres = top.getJSONArray("genres");
                    JSONArray imageObjectArray = top.getJSONArray("images");
                    JSONObject firstImgObject = imageObjectArray.getJSONObject(0);
                    String primaryArtistURL = firstImgObject.getString("url");

                    String submessage = associatedGenres.getString(0);
                    if (associatedGenres.length() > 1) {
                        for (int i = 1; i < associatedGenres.length(); i++) {
                            if (i == associatedGenres.length() - 1) {
                                submessage += " and " + associatedGenres.getString(i) + ". ";
                            } else {
                                submessage += ", " + associatedGenres.getString(i);
                            }
                        }
                    }

                    String[] randomRec = {"You must really like ", "Your favorite genres seem to be ", "This artist is commonly associated with ", "Fans seem to enjoy "};
                    int stringPicker =  (int) (Math.random() * randomRec.length);


                    String message = randomRec[stringPicker] + submessage + "You're one of " + top.getString("name") + "'s " + numFollowers + " monthly listeners!";
//                    String message = top.getJSONArray("genres").getString(0);
                    setTextAsync(top.getString("name"), mostListenedArtist);
                    setTextAsync("Your number one artist was: ", topArtistTextDescriptor);
                    setTextAsync(message, asscGenres);
                    setImageAsync(primaryArtistURL, (ImageView) findViewById(R.id.topArtistPic));
//                    setTextAsync(jsonObject.toString(3), tracksTextView);

                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
//                    Toast.makeText(SpotAPIActivity.this, "Failed to parse data, watch Logcat for more details",
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    /**
     * Get user profile
     * This method will get the user profile using the token
     */
    public void onGetUserProfileClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
//                Toast.makeText(SpotAPIActivity.this, "Failed to fetch data, watch Logcat for more details",
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
//                    profileTextView.setText(jsonObject.getString("display_name") + "'s Spotify Wrapped");
                    setTextAsync(jsonObject.getString("display_name") + "'s Spotify Wrapped", profileTextView);
                    spotifyUsername = jsonObject.getString("display_name");
//                    setTextAsync("Your top song is: ", topSongTextDescriptor);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(SpotAPIActivity.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    private void setImageAsync(final String url, ImageView imgView) {
        runOnUiThread(() -> new DownloadImageTask(imgView).execute(url));
    }

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(true)
                .setScopes(new String[] { "user-read-email", "user-top-read", "user-library-modify" }) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    private void startAPIActivity() {
        Intent newintent = new Intent(SpotAPIActivity.this, MainActivity.class);
        startActivity(newintent);
    }
    private void authenticateSpotify() {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    public void logOutSpotify() {

    }

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }
}