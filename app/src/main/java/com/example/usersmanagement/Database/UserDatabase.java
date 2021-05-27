package com.example.usersmanagement.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.usersmanagement.Dao.UserDao;
import com.example.usersmanagement.Model.Converters;
import com.example.usersmanagement.Model.User;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class UserDatabase extends RoomDatabase {
    private static final String DATABASE_NAME="UserDatabase";

    public abstract UserDao userDao();

    private static volatile UserDatabase INSTANCE;

    public static UserDatabase getInstance(Context context){
        if(INSTANCE == null)
        {
            synchronized (UserDatabase.class){
                if(INSTANCE == null)
                {
                    INSTANCE= Room.databaseBuilder(context, UserDatabase.class,
                            DATABASE_NAME)
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static Callback callback=new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsynTask(INSTANCE);
        }
    };
    static class PopulateAsynTask extends AsyncTask<Void,Void,Void>
    {
        private UserDao userDao;
        PopulateAsynTask(UserDatabase userDatabase)
        {
            userDao = userDatabase.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAll();
            return null;
        }
    }
}
