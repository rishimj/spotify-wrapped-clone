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

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifywrapped.SpotAPIActivity.*;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final int TOP_TRACKS_REQUEST_CODE = 2;
    private SharedViewModel sharedViewModel;

    FirebaseAuth auth;
    Button sign_out_button;
    Button settingsButton;
    Button backgroundModeButton;
    Button spotifyLoginButton;
    TextView welcome_user_text;
    TextView spotify_login_text;

    Button deleteAccountButton;

    Button resetPasswordButton;
    FirebaseUser user;
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
        backgroundModeButton = findViewById(R.id.background_mode_button);
        spotifyLoginButton = findViewById(R.id.spotifyAPI_login);
        spotify_login_text = findViewById(R.id.spotify_user_logged);
        deleteAccountButton = (Button) findViewById(R.id.delete_account_button);
        resetPasswordButton = (Button) findViewById(R.id.reset_password_button);

        user = auth.getCurrentUser();

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getTopTracks().observe(this, this::updateTopTracksTextView);
        sharedViewModel.getUserName().observe(this, this::updateUserNameTextView);

        checkDeleteAccountButton();

        checkResetPasswordButton();
        if (user == null) {
            //login NOT DONE
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            //if user already logged in
//            retrieveAndUseSpotifyCredentials(user.getUid());
            welcome_user_text.setText(String.format("Welcome back %s", user.getEmail()));

            if (SpotAPIActivity.mAccessCode == null) {
                spotify_login_text.setText("Not logged in yet!");
            } else {
                spotify_login_text.setText("Access code: " + SpotAPIActivity.mAccessCode.toString());
            }
//            spotify_login_text.setText(SpotAPIActivity.mAccessCode.toString());
        }



        spotifyLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String clientID = SpotAPIActivity.CLIENT_ID;
//                String redirectURI = SpotAPIActivity.REDIRECT_URI;
//                String scopes = SpotAPIActivity.SCOPES;
                Intent intent = new Intent(getApplicationContext(), SpotAPIActivity.class);
                startActivity(intent);
//                finish();
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

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            isNightModeOn = false;
            backgroundModeButton.setText("Enable Dark Mode");
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            isNightModeOn = true;
            backgroundModeButton.setText("Disable Dark Mode");
        } else {
            isNightModeOn = false;
            backgroundModeButton.setText("Enable Dark Mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        backgroundModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNightModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    backgroundModeButton.setText("Enable Dark Mode");
                    isNightModeOn = false;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    backgroundModeButton.setText("Disable Dark Mode");
                    isNightModeOn = true;
                }
            }
        });



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

    public void checkResetPasswordButton() {

        String emailAddress = user.getEmail();

        assert emailAddress != null;
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Password Reset", "Email sent.");
                            Toast.makeText(MainActivity.this, "Email sent to reset password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void checkDeleteAccountButton() {
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("User Account Delete", "User account deleted.");

                                }
                            }
                        });
                Intent intent  = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
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