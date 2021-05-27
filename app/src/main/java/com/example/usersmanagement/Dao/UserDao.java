package com.example.usersmanagement.Dao;

import com.example.usersmanagement.Model.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> userList);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM User")
    void deleteAll();

    @Query("SELECT * FROM User WHERE User.firstname LIKE :search || '%' OR User.lastname LIKE :search || '%' OR username LIKE :search || '%' ORDER BY User.firstname ASC")
    List<User> getSearchedUsersList(String search);
}
