package com.example.usersmanagement.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.usersmanagement.Dao.UserDao;
import com.example.usersmanagement.Database.UserDatabase;
import com.example.usersmanagement.Model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRespository {
    private UserDatabase database;
    private LiveData<List<User>> getAllUsers;

    public UserRespository(Application application)
    {
        database = UserDatabase.getInstance(application);
        getAllUsers = database.userDao().getAllUsers();
    }

    public void insert(List<User> userList){
     new InsertAsyncTask(database).execute(userList);
    }

    public List<User> query(String editSearch){
        try {
            return new QueryAsyncTask(database).execute(editSearch).get();
        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException exception) {
            Log.e("QueryException" , "Exception" + exception);
        }
        return null;
    }

    public LiveData<List<User>> getAllUsers()
    {
        return getAllUsers;
    }

    static class InsertAsyncTask extends AsyncTask<List<User>,Void,Void>{
        private UserDao userDao;
         InsertAsyncTask(UserDatabase userDatabase)
         {
             userDao = userDatabase.userDao();
         }
        @Override
        protected Void doInBackground(List<User>... lists) {
             List<User> users = lists[0];
             List<User> updatedUserList = new ArrayList<>();
             for(int i = 0; i < users.size(); i++) {
                 User user = users.get(i);
                 if(user != null && user.getName() != null) {
                     String[] nameArray = user.getName().split(" ");
                     if(nameArray.length >= 2) {
                         user.setLastname(nameArray[1]);
                     }
                     user.setFirstname(nameArray[0]);
                     updatedUserList.add(user);
                 }
             }

             userDao.insert(updatedUserList);
            return null;
        }
    }
    static class QueryAsyncTask extends AsyncTask<String,Void,List<User>>{
        private UserDao userDao;
        QueryAsyncTask(UserDatabase userDatabase)
        {
            userDao = userDatabase.userDao();
        }

        @Override
        protected List<User> doInBackground(String... strings) {
            return userDao.getSearchedUsersList(strings[0]);
        }
    }
}
