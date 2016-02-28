package com.firebase.androidchat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.androidchat.R;
import com.firebase.androidchat.bean.ToDoItem;

public class ToDoListActivity extends AppCompatActivity implements ToDoItemFragment.OnFragmentInteractionListener {


    ToDoItemFragment mToDoItemFragment;
    private String channelUrl;

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode==0&&resultCode==RESULT_OK&&data!=null){
            channelUrl = data.getStringExtra(ChatActivity.EXTRA_NEW_NAME);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mToDoItemFragment = new ToDoItemFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_todo:
                createDialog(getLayoutInflater(),ToDoListActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createDialog(LayoutInflater inflater,final Activity activity){
        // 1. Instantiate an AlertDialog.Builder with its constructor

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Add Your Next ToDo");
        final View dialogView = inflater.inflate(R.layout.dialog_add_todo, null, false);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.addTodo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText todoEditText = (EditText)dialogView.findViewById(R.id.todoEditText);
                //mSynchronizedToDoItemArray.addTodo(new ToDoItem(todoEditText.getText().toString()));
                mToDoItemFragment.addItem(new ToDoItem(todoEditText.getText().toString()));
                Toast.makeText(activity, "Todo Add", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancleTodoAdd, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onFragmentInteraction(String id){
        // TODO
    }
}
