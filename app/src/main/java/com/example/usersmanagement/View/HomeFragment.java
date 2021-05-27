package com.example.usersmanagement.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usersmanagement.Adapter.UserAdapter;
import com.example.usersmanagement.Model.User;
import com.example.usersmanagement.Network.Retrofit;
import com.example.usersmanagement.R;
import com.example.usersmanagement.Repository.UserRespository;
import com.example.usersmanagement.ViewModel.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private View view;
    private UserViewModel userViewModel;
    private EditText editSearch;
    private RecyclerView recyclerView;
    private List<User> userList;
    private UserRespository userRespository;
    private UserAdapter userAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        if(getActivity() != null) {
            getActivity().setTitle(getResources().getString(R.string.app_name));
        }
        initialiseUI();
        return view;
    }

    private void initialiseUI() {
        editSearch = view.findViewById(R.id.edit_search);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userRespository = new UserRespository(getActivity().getApplication());
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), userList);

        userViewModel =new ViewModelProvider(this).get(UserViewModel.class);
        networkRequest();
        userViewModel.getAllUsers().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userList = users;
                recyclerView.setAdapter(userAdapter);
                Collections.sort(users, new User.SortByName());
                userAdapter.setItems(users);

                Log.d("main", "onChanged: "+users);
            }
        });
        setUpSearchView();
    }

    private void setUpSearchView() {
        if(editSearch == null || userViewModel == null || userAdapter == null) return;
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    List<User> list = userViewModel.query(s.toString());
                    if(list == null || list.size() == 0) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                    }
                    userAdapter.setItems(list);
                    userAdapter.notifyDataSetChanged();
                } else {
                    userAdapter.setItems(userList);
                    userAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isInternetConnected() {
        if(getActivity() == null) {
            return false;
        }
        ConnectivityManager conMgr =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            return false;
        }
        return true;
    }

    private void networkRequest() {
        if(!isInternetConnected()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit();
        Call<List<User>> call = retrofit.api.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    userViewModel.insert(response.body());
                    Log.d("main", "onResponse: "+response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getActivity(), "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
