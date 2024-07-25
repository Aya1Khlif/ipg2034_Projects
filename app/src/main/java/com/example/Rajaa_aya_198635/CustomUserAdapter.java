package com.example.test6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomUserAdapter extends ArrayAdapter<userModel> {

    private Context context;
    private List<userModel> users;

    public CustomUserAdapter(Context context, List<userModel> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_list_item_user, parent, false);
        }

        userModel user = users.get(position);

        TextView usernameTextView = convertView.findViewById(R.id.username);
        TextView passwordTextView = convertView.findViewById(R.id.password);
        TextView roleTextView = convertView.findViewById(R.id.role);

        usernameTextView.setText("Username: " + user.getUsername());
        passwordTextView.setText("Password: " + user.getPassword());
        roleTextView.setText("Role: " + user.getRole());

        return convertView;
    }
}
