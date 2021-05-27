package com.example.usersmanagement.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.usersmanagement.Model.User;
import com.example.usersmanagement.Model.UserParcel;
import com.example.usersmanagement.R;
import com.example.usersmanagement.View.MainActivity;
import com.example.usersmanagement.View.ProfileFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context =  context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user= userList.get(position);
        holder.firstName.setText(user.getFirstname());
        holder.lastName.setText(user.getLastname());
        holder.userName.setText(user.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context == null) return;
                MainActivity mainActivity = (MainActivity) context;
                Bundle bundle = new Bundle();
                ProfileFragment fragment = new ProfileFragment();
                User.Address address = user.getAddress();
                User.Geo geo = user.getAddress().getGeo();
                bundle.putParcelable("User", new UserParcel(user.getId(), user.getName(), user.getFirstname(), user.getLastname(), user.getUsername(), user.getPhone(), user.getEmail(), user.getWebsite(), user.getCompany().getName(), buildAddress(address), geo.getLat(), geo.getLng()));
                FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_main, fragment).addToBackStack("ProfilePage").commit();
            }
        });

    }

    private String buildAddress(User.Address address) {
        StringBuilder sb = new StringBuilder();
        if(address != null) {
            if(!TextUtils.isEmpty(address.getStreet())) {
                sb.append(address.getStreet() + ", ");
            }
            if(!TextUtils.isEmpty(address.getSuite())) {
                sb.append(address.getSuite() + ", ");
            }
            if(!TextUtils.isEmpty(address.getCity())) {
                sb.append(address.getCity() + ", ");
            }
            if(!TextUtils.isEmpty(address.getZipcode())) {
                sb.append(address.getZipcode());
            }
        }
        return sb.toString();
    }

    public void setItems(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView firstName, lastName, userName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.txt_first_name);
            lastName = itemView.findViewById(R.id.txt_last_name);
            userName = itemView.findViewById(R.id.txt_user_name);
        }
    }
}
