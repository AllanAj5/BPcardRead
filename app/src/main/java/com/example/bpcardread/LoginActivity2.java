package com.example.bpcardread;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bpcardread.AddOnFiles.SessionManagement;
import com.example.bpcardread.AddOnFiles.User;

public class LoginActivity2 extends AppCompatActivity {

    public EditText user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        user = findViewById(R.id.editText_userName);
        pass = findViewById(R.id.editText_pass);


    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();

    }

    public void checkSession(){
        //Check if User is Logged in
        // If yes go to main Activity

        SessionManagement sessionManagement = new SessionManagement(LoginActivity2.this);
        String check_uname = sessionManagement.getSession();

        if(!check_uname.equals("")){
            // User is Logged in so move to Main activity
            moveToSelectionActivity();
        }
        else{ // Condition for Logged Out / New User
            Toast.makeText(getApplicationContext(),check_uname+" Enter Credentials",Toast.LENGTH_SHORT).show();
        }
    }

    public void Login(View view) {
        String user_validate = user.getText().toString();
        String upass_validate = pass.getText().toString();

        DatabaseClass dbClass = DatabaseClass.getDatabase(this.getApplicationContext());
        //dbClass.getDao().getAllData();
        int valid_login =  dbClass.daoAccount().getaccountTableData(user_validate,upass_validate);

        if(valid_login>0){
            //1. login and save session
            User user = new User(user_validate,upass_validate);

            SessionManagement sessionManagement = new SessionManagement(LoginActivity2.this);
            sessionManagement.saveSession(user);


            Toast.makeText(getApplicationContext(),"Welcome "+sessionManagement.getSession(),Toast.LENGTH_SHORT).show();

            //2. Move to Main Activity
            moveToSelectionActivity();
        }
        else{
            user.setError("Retry");
            pass.setError("Retry");
            Toast.makeText(getApplicationContext(),"Incorrect Username / Password !",Toast.LENGTH_LONG).show();
        }


    }

    private void moveToSelectionActivity() {

        Intent i = new Intent(LoginActivity2.this,Selection.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    public void signUp(View view) {

        Intent o = new Intent(getApplicationContext(),signUpActivity.class);
        startActivity(o);

    }
}