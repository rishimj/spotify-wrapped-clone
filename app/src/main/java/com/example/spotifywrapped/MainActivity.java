package com.example.spotifywrapped;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spotifywrapped.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    FirebaseAuth auth;
    Button sign_out_button;
    Button settingsButton;
    Button backgroundModeButton;
    Button deleteAccountButton;

    Button resetPasswordButton;

    Button readDataButton;
    TextView textView;
    TextView tokenTextView, codeTextView, profileTextView;
    FirebaseUser user;
    boolean isNightModeOn;
    String userId;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    public User userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         */

        auth = FirebaseAuth.getInstance();
        sign_out_button = (Button) findViewById(R.id.sign_out_button);
        settingsButton = (Button) findViewById(R.id.settings);
        textView = (TextView) findViewById(R.id.user_details);
        backgroundModeButton = (Button) findViewById(R.id.background_mode_button);
        deleteAccountButton = (Button) findViewById(R.id.delete_account_button);
        resetPasswordButton = (Button) findViewById(R.id.reset_password_button);
        readDataButton = (Button) findViewById(R.id.get_database_data_button);

        user = auth.getCurrentUser();

        Button loginButton = (Button) findViewById(R.id.button_to_spotify);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), SpotifyActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //getToken();

        //getCode();




        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(String.format("Welcome back %s", user.getEmail()));
        }



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

        checkDeleteAccountButton();

        checkResetPasswordButton();

        createDatabase();

        readDataButton();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        addUser("rishimj", 1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //getUserPref("rishimj2");
        //addDatabase();



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

    public void readDataButton() {

        readDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserPref("rishimj");
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

    private Uri getRedirectUri() {
        return Uri.parse(SpotifyInfo.REDIRECT_URI);
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