package com.firebase.androidchat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.androidchat.ChatApplication;
import com.firebase.androidchat.R;
import com.firebase.androidchat.bean.User;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by longjiao on 2/22/16.
 */
public class CheckboxAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> checkboxItems;
    private Firebase mFirebaseUser;
    private boolean isAdmin;
    public CheckboxAdapter(Context context, List<User> resource, String channelName, boolean isAdmin) {
        super(context, R.layout.select_dialog_checkbox_item, resource);

        this.context = context;
        this.checkboxItems = resource;
        this.mFirebaseUser = new Firebase(ChatApplication.FIREBASE_URL).child("channel").child(channelName).child("user");
        this.isAdmin = isAdmin;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.select_dialog_checkbox_item, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.username);
        textView.setText(checkboxItems.get(position).getName());

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);
        if(isAdmin) {
            cb.setChecked(checkboxItems.get(position).getState() != 0);
            cb.setTag(checkboxItems.get(position));
            cb.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox box = (CheckBox) v;
                    User user = (User) v.getTag();
                    if (box.isChecked()) {
                        user.setState(1);
                    } else {
                        user.setState(0);
                    }
                    mFirebaseUser.child(user.getName().replace(".", ",")).setValue(user);
                }
            });
        }else
            cb.setVisibility(View.GONE);
        return convertView;
    }
}
