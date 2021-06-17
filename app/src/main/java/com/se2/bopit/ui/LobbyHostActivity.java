package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.RequiresApi;
import com.se2.bopit.R;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.data.DataProviderContext;
import com.se2.bopit.domain.data.NearbyDataProvider;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.ui.helpers.CustomToast;
import com.se2.bopit.ui.helpers.UserAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class LobbyHostActivity extends BaseActivity {

    private DataProviderContext dataProvider;
    private ArrayList<User> userItems = new ArrayList<>();
    private String TAG = "LobbyHostActivity";


    private Context context;
    static ScheduledFuture<?> countdownFuture;

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_host);
        ListView lobbyUserList;

        lobbyUserList = findViewById(R.id.userList);
        userAdapter = new UserAdapter(this, R.layout.custom_listview_layout, userItems);

        lobbyUserList.setAdapter(userAdapter);
        context = this;

        //Set dataprovider, currently one one available
        Intent intent = getIntent();
        dataProvider = DataProviderContext.create(new NearbyDataProvider(this, networkListener, intent.getStringExtra("username")));
        dataProvider.startAdvertising();
    }

    public void onStartClick(View view) {
        dataProvider.startGameCountdown();
    }

    public void onReadyClick(View view) {
        dataProvider.sendReadyMessage();
    }

    private final NetworkLobbyListener networkListener = new NetworkLobbyListener() {

        @Override
        public void onError(String error) {
            Log.e(TAG, error);
        }

        @Override
        public void onStatusChange(String status) {
            Log.d("Network", status);
        }

        @Override
        public void onEndpointDiscovered(String id, String name) {
            //Listener for Endpoint Discovered
            Log.d(TAG, "endpoint discovered: " + id + " " + name);
        }

        @Override
        public void onEndpointConnected(String id, List<String> names) {
            //Listener for Endpoint Connection
            Log.d(TAG, "endpoint connected: " + id + " " + names);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onUserLobbyChange(ArrayList<User> users) {
            if (users != null) {
                userItems = users;
                userAdapter.clear();
                userAdapter.addAll(userItems);
                userAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onGameCountdownStart() {
            AtomicInteger countdown = new AtomicInteger(3);

            Runnable countdownRunnable = () -> {
                runOnUiThread(
                        () -> {
                            CustomToast.showToast(String.valueOf(countdown.get()), context);
                            countdown.getAndDecrement();
                            if (countdown.get() <= 0)
                                countdownFuture.cancel(false);
                        });
            };
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            countdownFuture = executor.scheduleAtFixedRate(countdownRunnable, 0, 1, TimeUnit.SECONDS);
        }

        @Override
        public void onReadyMessageReceived() {
            //Ignore
        }

        @Override
        public void OnReadyAnswerReceived(boolean answer, String username) {
            for (User usr : userItems) {
                if (usr.getName().equals(username)) {
                    if (answer) {
                        usr.setReady(true);

                    } else {
                        usr.setReady(false);
                    }
                }
            }
            userAdapter.clear();
            userAdapter.addAll(userItems);
            userAdapter.notifyDataSetChanged();
        }

        @Override
        public void onGameStart() {
            Log.d("LobbyHostActivity", "onGameStart");

            startGame();
        }

        @Override
        public void onGameInformationReceived(String data) {
            Log.i("Network data", data);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dataProvider.disconnect();
    }

    void startGame() {
        Log.d("LobbyHostActivity", "startGame");
        startActivity(new Intent(this, GameActivity.class)
                .putExtra(GameActivity.GAME_MODE, GameMode.MULTI_PLAYER_SERVER));
    }
}