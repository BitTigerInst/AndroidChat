package com.firebase.androidchat.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.androidchat.R;
import com.firebase.androidchat.bean.ToDoItem;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

/**
 * Created by Johnwan on 02/27/2016.
 */
public class ToDoItemListAdapter extends FirebaseListAdapter<ToDoItem> {

    public ToDoItemListAdapter(Query ref, int layout, Activity activity){
        super(ref,ToDoItem.class,layout,activity);
    }

    @Override
    public void populateView(View view, ToDoItem toDoItem){


        TextView descriptionTextView = (TextView)view.findViewById(R.id.todoItemDescriptionTextView);
        TextView dateTextView = (TextView)view.findViewById(R.id.todoItemDateTextView);
        CheckBox doneCheckBox = (CheckBox)view.findViewById(R.id.todoItemCheckBox);

        Firebase todoFirebase = getFirebaseRef();


        descriptionTextView.setText(toDoItem.getDescription());
        dateTextView.setText(toDoItem.getTimestamp().toString());
        doneCheckBox.setChecked(toDoItem.isCompleted());
    }

    public void addToDo(ToDoItem toDoItem){
        Firebase todoFirebaseRef = getFirebaseRef().push();
        todoFirebaseRef.setValue(toDoItem);
        String key = todoFirebaseRef.getKey();
        toDoItem.setKey(key);
        todoFirebaseRef.setValue(toDoItem);
    }

}
