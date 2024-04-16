package com.example.spotifywrapped;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spotifywrapped.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifywrapped.SpotAPIActivity.*;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final int TOP_TRACKS_REQUEST_CODE = 2;
    private SharedViewModel sharedViewModel;

    static FirebaseAuth auth;
    Button sign_out_button;
    Button settingsButton;
    Button spotifyLoginButton;
    TextView welcome_user_text;
    TextView spotify_login_text;


    Button readDataButton;
    static FirebaseUser user;
    boolean isNightModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        sign_out_button = findViewById(R.id.sign_out_button);
        settingsButton = findViewById(R.id.settings);
        welcome_user_text = findViewById(R.id.user_details);
        spotifyLoginButton = findViewById(R.id.spotifyAPI_login);
//        spotify_login_text = findViewById(R.id.spotify_user_logged);

        readDataButton = findViewById(R.id.get_database_data_button);

        user = auth.getCurrentUser();

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getTopTracks().observe(this, this::updateTopTracksTextView);
        sharedViewModel.getUserName().observe(this, this::updateUserNameTextView);


        if (user == null) {
            //login NOT DONE
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            welcome_user_text.setText(String.format("Welcome back, %s", user.getEmail()));
        }



        spotifyLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String clientID = SpotAPIActivity.CLIENT_ID;
//                String redirectURI = SpotAPIActivity.REDIRECT_URI;
//                String scopes = SpotAPIActivity.SCOPES;
                Intent intent = new Intent(getApplicationContext(), SpotAPIActivity.class);
                startActivity(intent);
                finish();
//                spotify_login_text.setText("Access code: " + SpotAPIActivity.mAccessCode.toString());

//                AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(clientID, AuthorizationResponse.Type.TOKEN, redirectURI);
//                builder.setScopes(new String[]{scopes});
//                AuthorizationRequest request = builder.build();
//                AuthorizationClient.openLoginActivity(MainActivity.this, SpotAPIActivity.AUTH_TOKEN_REQUEST_CODE, request);
            }
        });

        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() == null) {

                            GoogleSignInOptions gso = new GoogleSignInOptions.
                                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                    build();

                            GoogleSignInClient googleSignInClient=GoogleSignIn.getClient(getApplicationContext(),gso);
                            googleSignInClient.signOut();

                        }
                    }
                });
                Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signOut();
//                logoutFromSpotify();
                Intent intent  = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                finish();

            }
        });



//
        createDatabase();

        readDataButton();


    //ADD action bar


        //setSupportActionBar(binding.toolbar);

        //Intent googleIntent = new Intent(MainActivity.this, GoogleSignInActivity.class);
        //MainActivity.this.startActivity(googleIntent);



        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });*/




    }



    public void readDataButton() {

        readDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserPref("rishimj");
            }
        });

    }




    FirebaseDatabase database;
    DatabaseReference databaseReference;
    public void addDatabase() {
        // Write a message to the database
        database = FirebaseDatabase.getInstance("ttps://spotify-wrapped-21aab-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference();
        //myRef.setValue("Hello, World!");

        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", user.getDisplayName());
        map.put("User Number", "one");

        database.getReference().child("users").push().child("user details").updateChildren(map);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, "data changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "data not changed", Toast.LENGTH_SHORT).show();

            }
        });
    }
    DataSnapshot userData;
    public void createDatabase() {
        database = FirebaseDatabase.getInstance("ttps://spotify-wrapped-21aab-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference();

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });
    }

    public void addUser(String username, int pref) {
        DatabaseReference userRef = databaseReference.child("users").child(username);
        userRef.setValue(username);
        userRef.child("pref").setValue(pref);

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });


    }



    public void getUserPref(String username) {
        /*
        databaseReference.child("users").child(username).child("pref").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });*/


        int pref = userData.child(username).child("pref").getValue(Integer.class);
        Toast.makeText(MainActivity.this, String.format("User Preference %d", pref), Toast.LENGTH_SHORT).show();

    }
//    private void retrieveAndUseSpotifyCredentials(String userID) {
//        credentialsRef =
//    }

    private void updateTopTracksTextView(String[] topTracks) {
        StringBuilder sb = new StringBuilder();
        for (String track : topTracks) {
            sb.append(track).append("\n");
        }
//        tracks.setText(userName);
    }

    private void updateUserNameTextView(String userName) {
        if (userName != null) {
            spotify_login_text.setText(userName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}