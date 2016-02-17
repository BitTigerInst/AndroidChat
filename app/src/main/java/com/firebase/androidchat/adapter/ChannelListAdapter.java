package com.firebase.androidchat.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.androidchat.ChatApplication;
import com.firebase.androidchat.R;
import com.firebase.androidchat.activity.ChannelActivity;
import com.firebase.androidchat.activity.ChatActivity;
import com.firebase.androidchat.bean.Channel;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.AbstractCollection;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChannelListAdapter extends FirebaseListAdapter<Channel> {
    private ChannelActivity activity;

    public ChannelListAdapter(Query ref, ChannelActivity activity, int layout) {
        super(ref, Channel.class, layout, activity);
        this.activity = activity;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param channel An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(final View view, Channel channel) {
        // Map a Chat object to an entry in our listview
        final String channelName = channel.getName();
        TextView channelText = (TextView) view.findViewById(R.id.name);
        channelText.setText(channelName);
    }
}
