package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.javafaker.Faker;
import com.se2.bopit.R;
import com.se2.bopit.data.NearbyDataProvider;
import com.se2.bopit.domain.interfaces.NetworkListener;

import java.util.ArrayList;


public class LobbyJoinActivity extends AppCompatActivity {

    private NearbyDataProvider dp;
    private ListView openEndpointsList;
    private final ArrayList<String> endpointItems = new ArrayList<>();
    private final ArrayList<String> userItems = new ArrayList<>();
    private String endpointId;
    private ListView lobbyUserList;
    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_NAME = "name";

    private ArrayAdapter<String> endPointAdapter;
    private ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_join);

        openEndpointsList = findViewById(R.id.openEndpointLists);
        lobbyUserList = findViewById(R.id.userList);
        endPointAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                endpointItems);
        userAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                userItems);
        openEndpointsList.setAdapter(endPointAdapter);
        lobbyUserList.setAdapter(userAdapter);
        openEndpointsList.setOnItemClickListener((arg0, arg1, position, arg3) -> dp.connectToEndpoint(endpointId));

        Intent intent = getIntent();
        dp = new NearbyDataProvider(this, networkListener,  intent.getStringExtra("username"));
        dp.startDiscovery();
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
        public void onEndpointDiscovered(String id, String name) {
            endpointId = id;
            endpointItems.add(name);
            endPointAdapter.notifyDataSetChanged();
        }

        @Override
        public void onEndpointConnected(String id, ArrayList<String> names) {

        }

        @Override
        public void onUserLobbyChange(ArrayList<String> users) {
            if (users != null) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dp.disconnect();
    }
}