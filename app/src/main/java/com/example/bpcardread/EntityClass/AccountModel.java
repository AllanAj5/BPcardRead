package com.example.bpcardread.EntityClass;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "accountTable", indices = @Index(value = {"username"}, unique = true))
public class AccountModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int key;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "email")
    public String email;
}
