package com.example.usersmanagement.ViewModel;

import android.app.Application;

import com.example.usersmanagement.Model.User;
import com.example.usersmanagement.Repository.UserRespository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {

    private UserRespository userRespository;
    private LiveData<List<User>> getAllUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRespository = new UserRespository(application);
        getAllUsers = userRespository.getAllUsers();

    }

    public void insert(List<User> list) {
        userRespository.insert(list);
    }

    public List<User> query(String searchText) {
        return userRespository.query(searchText);
    }

    public LiveData<List<User>> getAllUsers()
    {
        return getAllUsers;
    }


}
