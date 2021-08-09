package com.example.bpcardread;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bpcardread.EntityClass.AccountModel;

public class signUpActivity extends AppCompatActivity {
    EditText signupUserName,signupUserPassword,signupEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        signupUserName = findViewById(R.id.signupUserName);
        signupUserPassword = findViewById(R.id.signupUserPassword);
        signupEmail = findViewById(R.id.signupEmail);

    }

    public void signUpAcc(View view) {

        DatabaseClass dbClass = DatabaseClass.getDatabase(this.getApplicationContext());
        AccountModel model = new AccountModel();

        model.username = signupUserName.getText().toString();
        model.password =  signupUserPassword.getText().toString();
        model.email = signupEmail.getText().toString();

        try{
            dbClass.daoAccount().insertAccount(model);
            //Toast.makeText(this,"Creating Account...",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this,"User Name Taken!\n\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        finally
        {
            Intent i = new Intent(getApplicationContext(),LoginActivity2.class);
            this.startActivity(i);
        }
        }



    }
