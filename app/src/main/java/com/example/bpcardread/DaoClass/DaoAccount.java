package com.example.bpcardread.DaoClass;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bpcardread.EntityClass.AccountModel;

@Dao
public interface DaoAccount {
    /// Signup accountTable
    @Insert
    void insertAccount(AccountModel... accountModels);

    @Query("select count(*) from accountTable where username = :username and password = :password")
    int getaccountTableData(String username,String password);


}
