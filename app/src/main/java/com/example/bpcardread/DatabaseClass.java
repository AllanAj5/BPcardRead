package com.example.bpcardread;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bpcardread.DaoClass.DaoAccount;
import com.example.bpcardread.DaoClass.Daoclass;
import com.example.bpcardread.EntityClass.AccountModel;
import com.example.bpcardread.EntityClass.UserModel;

@Database(entities = {UserModel.class, AccountModel.class},version = 2)
public abstract class DatabaseClass extends RoomDatabase {

    public abstract Daoclass getDao();
    public abstract DaoAccount daoAccount();


    private static DatabaseClass instance;

    static DatabaseClass getDatabase(final Context context){
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext() , DatabaseClass.class, "DATABASE")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }


}
