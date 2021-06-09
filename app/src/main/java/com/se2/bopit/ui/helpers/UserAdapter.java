package com.se2.bopit.ui.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.se2.bopit.R;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.ui.LobbyHostActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private static final String TAG = "UserAdapter";
    private Context mContext;
    private int mResource;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param list  The objects to represent in the ListView.
     */

    public UserAdapter(Context context, int resource, ArrayList<User> list) {
        super(context, resource, list);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String id = getItem(position).getId();
        String username = getItem(position).getName();
        boolean ready = getItem(position).isReady();

        User user = new User(id,username,ready);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView usernameTV = (TextView) convertView.findViewById(R.id.userNameTV);
        ImageView readyIV = (ImageView) convertView.findViewById(R.id.readySign);
        Button kickButton = (Button) convertView.findViewById(R.id.kickButton);

        usernameTV.setText(username);

        if(ready){
            readyIV.setVisibility(View.VISIBLE);
        }
        else{
            readyIV.setVisibility(View.INVISIBLE);
        }
        return convertView;

    }
}
