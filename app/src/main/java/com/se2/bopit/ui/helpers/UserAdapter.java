package com.se2.bopit.ui.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.se2.bopit.R;
import com.se2.bopit.domain.models.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private final Context mContext;
    private final int mResource;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param list     The objects to represent in the ListView.
     */

    public UserAdapter(Context context, int resource, List<User> list) {
        super(context, resource, list);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String username = getItem(position).getName();
        boolean ready = getItem(position).isReady();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View newView = inflater.inflate(mResource, parent, false);

        TextView usernameTV = newView.findViewById(R.id.userNameTV);
        ImageView readyIV = newView.findViewById(R.id.readySign);

        usernameTV.setText(username);

        if (ready) {
            readyIV.setVisibility(View.VISIBLE);
        } else {
            readyIV.setVisibility(View.INVISIBLE);
        }
        return newView;

    }
}
