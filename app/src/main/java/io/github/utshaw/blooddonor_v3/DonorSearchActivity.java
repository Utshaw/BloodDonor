package io.github.utshaw.blooddonor_v3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utshaw on 9/28/2017.
 */

public class DonorSearchActivity extends AppCompatActivity {

    private ListView mListView ;
    private UserAdapter mUserAdapter;
    private ProgressBar mProgressBar;
    private TextView emptyStateView;

    FirebaseDatabase mFirebaseDatabase ;
    DatabaseReference mDatabaseReference;

    private ChildEventListener mChildEventListener;

    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);




        emptyStateView = (TextView) findViewById(R.id.empty_view);
        Bundle extras = getIntent().getExtras();
        final String bloodGroup = extras.getString("bloodGroup");
        setTitle(bloodGroup + " Donors");

        mFirebaseDatabase =  FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(getResources().getString(R.string.users)).child(bloodGroup);


        List<User> userList = new ArrayList<User>();
        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mUserAdapter = new UserAdapter(this,R.layout.list_item,userList);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(mUserAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User currentUser = mUserAdapter.getItem(position);
                Intent intent = new Intent(DonorSearchActivity.this,UserDetails.class);
                intent.putExtra("userObject",currentUser);
                startActivity(intent);
            }
        });
        mListView.setEmptyView(emptyStateView);

        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        attachDatabaseReadListener();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mUserAdapter.getCount() == 0){
                    emptyStateView.setText("No donor for " + bloodGroup + " blood group found");
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    detachDatabaseReadListener();
                }
            }
        }, 3000);

    }


    private void attachDatabaseReadListener() {
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    mUserAdapter.add(user);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    emptyStateView.setText("Error reading data from server");
                }
            };
            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        mUserAdapter.clear();
    }

    private void detachDatabaseReadListener() {
        if(mChildEventListener != null){
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
