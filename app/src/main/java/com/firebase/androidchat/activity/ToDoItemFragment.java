package com.firebase.androidchat.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.firebase.androidchat.ChatApplication;
import com.firebase.androidchat.R;
import com.firebase.androidchat.adapter.ToDoItemListAdapter;
import com.firebase.androidchat.bean.ToDoItem;
import com.firebase.client.Firebase;



public class ToDoItemFragment extends ListFragment {



    private OnFragmentInteractionListener mListener;
    private ToDoItemListAdapter mToDoItemListAdapter;
    private Firebase todoItemFirebaseRef;


    public ToDoItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setuptodoItemFirebase();
        mToDoItemListAdapter = new ToDoItemListAdapter(todoItemFirebaseRef, R.layout.fragment_to_do_list,getActivity());
        setListAdapter(mToDoItemListAdapter);
    }

    private void setuptodoItemFirebase() {
        SharedPreferences prefs = getActivity().getApplication().getSharedPreferences("ChatPrefs", 0);
        String mChannelName = prefs.getString("channel", null);
        todoItemFirebaseRef = new Firebase(ChatApplication.FIREBASE_URL).child("channel").child(mChannelName.replace(".", ",")).child("todo");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    protected void addItem(ToDoItem toDoItem){
        mToDoItemListAdapter.addToDo(toDoItem);
    }

}
