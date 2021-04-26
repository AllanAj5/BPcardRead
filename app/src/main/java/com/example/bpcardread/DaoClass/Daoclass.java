package com.example.bpcardread.DaoClass;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bpcardread.EntityClass.UserModel;

import java.util.List;

@Dao
public interface Daoclass {

    @Insert
    void insertAllData(UserModel... userModel);
    @Delete
    void delete(UserModel user);
    //Select All data
    @Query("select * from kycData")
    List<UserModel> getAllData();

}
