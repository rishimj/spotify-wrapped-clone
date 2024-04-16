package com.example.spotifywrapped;
import static com.example.spotifywrapped.MainActivity.auth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import android.widget.Button;
import android.widget.TextView;

import android.widget.*;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {
    Switch darkModeSwitch;
    TextView settingText;
    Spinner notificationSpinner;
    Button backToM;
    Button deleteAccountButton;
    Button resetPasswordButton;
    Button sign_out_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Initialization
        darkModeSwitch = findViewById(R.id.switchDarkMode);
        settingText = findViewById(R.id.settingsbegin);
        backToM = findViewById(R.id.backToMainActivityButton);
        deleteAccountButton = findViewById(R.id.delete_account_button);
        resetPasswordButton = findViewById(R.id.reset_password_button);
        sign_out_button = findViewById(R.id.sign_out_button);

        // Update switch state based on the current mode
        updateSwitchState();
        checkResetPasswordButton();
        checkDeleteAccountButton();

        backToM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain(MainActivity.class);
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

                            GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);
                            googleSignInClient.signOut();

                        }
                    }
                });
                Toast.makeText(SettingsActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signOut();
//                logoutFromSpotify();
                Intent intent  = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
        notificationSpinner = findViewById(R.id.notificationSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.notification_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationSpinner.setAdapter(adapter);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.settings, new SettingsFragment())
//                    .commit();
//        }
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    private void updateSwitchState() {
        // This method uses setChecked without issue
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeSwitch.setChecked(true);
            darkModeSwitch.setTextColor(Color.WHITE);
            settingText.setTextColor(Color.WHITE);

        } else {
            darkModeSwitch.setChecked(false);
            darkModeSwitch.setTextColor(Color.BLACK);
            settingText.setTextColor(Color.BLACK);
        }
    }
    public void showNotification(String channelID, int priority) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for App notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Sample Notification")
                .setContentText("This is a sample notification")
                .setPriority(priority);

        notificationManager.notify(1, builder.build());
    }
    public void backToMain(Class<MainActivity> view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    public void checkResetPasswordButton() {
        String emailAddress = MainActivity.user.getEmail();

        assert emailAddress != null;
        MainActivity.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Password Reset", "Email sent.");
                            Toast.makeText(SettingsActivity.this, "Email sent to reset password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void checkDeleteAccountButton() {
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.user.delete()
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
//                finish();
            }
        });
    }

}