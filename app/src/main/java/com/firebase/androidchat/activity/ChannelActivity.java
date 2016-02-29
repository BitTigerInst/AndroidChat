package com.firebase.androidchat.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.firebase.androidchat.ChatApplication;
import com.firebase.androidchat.R;
import com.firebase.androidchat.adapter.ChannelListAdapter;
import com.firebase.androidchat.bean.Channel;
import com.firebase.androidchat.util.Validator;
import com.firebase.client.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ChannelActivity extends AppCompatActivity {

    public static final String LOG_TAG = ChannelActivity.class.getSimpleName();

    // TODO: change this to your own Firebase URL
    private final static String DEFAULT_CHANNEL = "MonkeyBOOM";
    private static Validator mValidator = Validator.getInstance();
    private String mUsername;
    private Firebase mFirebase;
    private ArrayList<String> channelList;
    private Firebase mFirebaseUser;
    private ValueEventListener mConnectedListener;
    private ChannelListAdapter mChannelListAdapter;

    public void setmFirebaseUser(Firebase mFirebaseUser) {
        this.mFirebaseUser = mFirebaseUser;
    }

    public void setmChannelListAdapter(final ChannelListAdapter mChannelListAdapter) {
        this.mChannelListAdapter = mChannelListAdapter;
        final ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(mChannelListAdapter);
        mChannelListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChannelListAdapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        // Make sure we have a mUsername
        setupUsername();

        setTitle("Chatting as " + mUsername);

        // Setup our Firebase mFirebaseUser
        mFirebase = new Firebase(ChatApplication.FIREBASE_URL);
        mFirebaseUser = mFirebase.child("user").child(mUsername.replace(".",",")).child("channel");
        getChannelList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.channel_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_channel:
                createChannel();
                return true;
            case R.id.logout:
                backToLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createChannel() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.username_alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText channelName = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        final EditText userPassword = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserPassword);
        userPassword.setVisibility(View.GONE);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
//                                if (mValidator.checkUrl(channelName.getText().toString())) {
                                    setChannel(channelName.getText().toString());
                                    loginToChannel(channelName.getText().toString());
//                                } else {
//                                    Toast.makeText(ChannelActivity.this,"Channel name should onln contain 0-9 a-Z and '_' ",Toast.LENGTH_SHORT).show();
//                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void loginToChannel(String channelName) {
        Intent intent = new Intent(getApplication(), ChatActivity.class);
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        prefs.edit().putString("channel", channelName).apply();
        startActivity(intent);
    }

    private void backToLogin() {
        SharedPreferences prefs = getSharedPreferences("ChatPrefs", 0);
        prefs.edit().clear().commit();
        Intent intent = new Intent(getApplication(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = (ListView) findViewById(R.id.listview);
        // Tell our list adapter that we only want 50 messages at a time
        mChannelListAdapter = new ChannelListAdapter(mFirebaseUser.limit(50), this, R.layout.channel_list);
        listView.setAdapter(mChannelListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView channelText = (TextView) view.findViewById(R.id.name);
                loginToChannel(channelText.getText().toString());
            }
        });
        mChannelListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChannelListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebase.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Log.d(LOG_TAG, "Connected to Firebase");
//                    Toast.makeText(ChannelActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(ChannelActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseUser.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChannelListAdapter.cleanup();
    }

    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);
        if (mUsername == null) {
            userLoginAlertDialog();
        }
    }

    private void userLoginAlertDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.username_alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userName = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        if(mUsername != null)
            userName.setText(mUsername);

        final EditText userPassword = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {

                            }
                        })
                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                backToLogin();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString();
                String password = userPassword.getText().toString();
                mFirebase.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getBaseContext(), "User not exist or password wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void setChannel(String input) {
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            if(channelList.indexOf(input) == -1) {
                Channel channel = new Channel(input);
                // Create a new, auto-generated child of that chat location, and save our chat data there
                mFirebaseUser.push().setValue(channel);
            }
        }
    }

    private void getChannelList(){
        channelList = new ArrayList<>();
        mFirebaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String,Channel>> t = new GenericTypeIndicator<HashMap<String, Channel>>() {
                };
                HashMap<String,Channel> map = snapshot.getValue(t);
                if(map == null){
                    Channel channel = new Channel(DEFAULT_CHANNEL);
                    mFirebaseUser.push().setValue(channel);
                    return;
                }
                for (Channel c: map.values()){
                    channelList.add(c.getName());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
