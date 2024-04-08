package com.example.spotifywrapped;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.spotifywrapped.databinding.FragmentFirst2Binding;
import com.google.gson.Gson;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class First2Fragment extends Fragment {

    private FragmentFirst2Binding binding;

    TextView topSong;
    TextView topArtistName;
    TextView topSongPopularity;
    String topData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topData =  getArguments().getString("topItemsString");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirst2Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = gson.fromJson(topData, JSONObject.class);
            String results = "";
            JSONArray array = jsonObject.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                JSONObject album = (JSONObject) obj.get("album");
                String albumName = album.getString("name");
                String name = obj.getString("name");
                String popularity = obj.getString("popularity");
                String message = "Your #" + (i + 1) + " track is " + name + " on the album " + albumName + ". It has " + popularity + "/100 popularity.\n";
                results += message;
            }
            JSONObject obj = (JSONObject) array.get(0);
            JSONObject album = (JSONObject) obj.get("album");
            topSong.setText(obj.getString("name"));
            topArtistName.setText(album.getString("name"));
            topSongPopularity.setText(obj.getString("popularity"));


        } catch (JSONException e) {
            Log.d("JSON", "Failed to parse data: " + e);
            Toast.makeText(getContext(), "Failed to parse data, watch Logcat for more details",
                    Toast.LENGTH_SHORT).show();
        }

         */


    }

    public void getToken() {
        final AuthorizationRequest tokenRequest = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(getActivity(), SpotifyInfo.AUTH_TOKEN_REQUEST_CODE, tokenRequest);
    }

    private Uri getRedirectUri() {
        return Uri.parse(SpotifyInfo.REDIRECT_URI);
    }


    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(SpotifyInfo.CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-read-email", "user-top-read" })
                .setCampaign("your-campaign-token")
                .build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}