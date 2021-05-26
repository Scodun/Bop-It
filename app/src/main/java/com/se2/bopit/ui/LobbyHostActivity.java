package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.se2.bopit.R;
import com.se2.bopit.data.NearbyDataProvider;
import com.se2.bopit.domain.interfaces.NetworkListener;

import java.util.ArrayList;

public class LobbyHostActivity extends AppCompatActivity {

    private NearbyDataProvider dp;
    private ArrayList<String> userItems = new ArrayList<>();
    private ListView lobbyUserList;

    private ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_host);

        lobbyUserList = findViewById(R.id.userList);
        userAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                userItems);
        lobbyUserList.setAdapter(userAdapter);

        //TODO: replace username
        dp = new NearbyDataProvider(this, networkListener,"usernameHoster");
        dp.startAdvertising();
    }

    public void onStartClick(View view){

    }

    private final NetworkListener networkListener = new NetworkListener() {

        @Override
        public void onError(String error) {
            Log.e("Network", error);
        }

        @Override
        public void onStatusChange(String status) {
            Log.d("Network", status);

        }

        @Override
        public void onEndpointDiscovered(String id, String name){
            //Listener for Endpoint Discovered
        }

        @Override
        public void onEndpointConnected(String id, ArrayList<String> names){
            //Listener for Endpoint Connection
        }

        @Override
        public void onUserLobbyChange(ArrayList<String> users) {
            if(users!=null) {
                userAdapter.clear();
                userAdapter.addAll(users);
                userAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onGameInformationReceived(String data) {
            Log.i("Network data", data);
        }
    };
}