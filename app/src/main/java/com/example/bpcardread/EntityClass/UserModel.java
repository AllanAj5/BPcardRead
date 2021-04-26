package com.example.bpcardread.EntityClass;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


//@Entity(tableName = "kycData")
@Entity(tableName = "kycData", indices = @Index(value = {"idNumber"}, unique = true))

public class UserModel {

    //primary key
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int key;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "dob")
    public String dob;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "idNumber")
    public String idNumber;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "timestmp_created")
    public String timestmp_created;

    @ColumnInfo(name = "timestmp_modified")
    public String timestmp_modified;

    @ColumnInfo(name = "createdUser")
    public String createdUser;

    @ColumnInfo(name = "modifiedUser")
    public String modifiedUser;


//    @ColumnInfo(name ="timestamp")
//    String timestamp;

    //Getters and Setters

//    public int getKey() {
//        return key;
//    }
//
//    public void setKey(int key) {
//        this.key = key;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDob() {
//        return dob;
//    }
//
//    public void setDob(String dob) {
//        this.dob = dob;
//    }
//
//    public String getSex() {
//        return sex;
//    }
//
//    public void setSex(String sex) {
//        this.sex = sex;
//    }
//
//    public String getIdNumber() {
//        return idNumber;
//    }
//
//    public void setIdNumber(String idNumber) {
//        this.idNumber = idNumber;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }
}
