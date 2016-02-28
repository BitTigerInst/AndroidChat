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
    public void populateView(View view, final ToDoItem toDoItem){


        TextView descriptionTextView = (TextView)view.findViewById(R.id.todoItemDescriptionTextView);
        TextView dateTextView = (TextView)view.findViewById(R.id.todoItemDateTextView);
        CheckBox doneCheckBox = (CheckBox)view.findViewById(R.id.todoItemCheckBox);

        descriptionTextView.setText(toDoItem.getDescription());
        dateTextView.setText(toDoItem.getTimestamp().toString());
        doneCheckBox.setChecked(toDoItem.isCompleted());
        doneCheckBox.setTag(toDoItem);
        doneCheckBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox box = (CheckBox) v;
                Firebase todoFirebase = getFirebaseRef();
                if(box.isChecked()){
                    toDoItem.setCompleted(true);
                }else {
                    toDoItem.setCompleted(false);
                }
                todoFirebase.child(toDoItem.getKey()).setValue(toDoItem);
            }
        });
    }

    public void addToDo(ToDoItem toDoItem){
        Firebase todoFirebaseRef = getFirebaseRef().push();
        todoFirebaseRef.setValue(toDoItem);
        String key = todoFirebaseRef.getKey();
        toDoItem.setKey(key);
        todoFirebaseRef.setValue(toDoItem);
    }

}
