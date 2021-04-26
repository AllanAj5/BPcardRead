package com.example.bpcardread;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpcardread.AddOnFiles.CSVWriter;
import com.example.bpcardread.AddOnFiles.DBHelper;
import com.example.bpcardread.AddOnFiles.SessionManagement;
import com.example.bpcardread.EntityClass.UserModel;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class GetData extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();
    private DetailsListAdapter detailsListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        initRecyclerView();
        getData();


    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);

        detailsListAdapter = new DetailsListAdapter(this);
        recyclerView.setAdapter(detailsListAdapter);
    }

    private void getData() {

        DatabaseClass dbClass = DatabaseClass.getDatabase(this.getApplicationContext());
        //dbClass.getDao().getAllData();
        List<UserModel> userList =  dbClass.getDao().getAllData();
        detailsListAdapter.setUserList(userList);

        //recyclerView.setAdapter(new UserAdapter(getApplicationContext(),DatabaseClass.getDatabase(getApplicationContext()).getDao().getAllData()));
    }

    public void export_CSV(View view) {

            DBHelper dbhelper = new DBHelper(getApplicationContext(),"DATABASE",null,2);
            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists())
            {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "BPReader_Aadhar_DATA.csv");
            try
            {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                SQLiteDatabase db = dbhelper.getReadableDatabase();
                Cursor curCSV = db.rawQuery("SELECT * FROM kycData",null);
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext())
                {
                    //Which column you want to exprort
                    String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),curCSV.getString(5),curCSV.getString(6),curCSV.getString(7),curCSV.getString(8),curCSV.getString(9)};
                    csvWrite.writeNext(arrStr);
                }
                csvWrite.close();
                curCSV.close();
            }
            catch(Exception sqlEx)
            {
                Toast.makeText(getApplicationContext(),"Unable to Export Data \n\n"+sqlEx.getMessage(),Toast.LENGTH_LONG).show();
            }
            finally {
                Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
            }


    }
    public void export_JSON(View view) {
    }

    public void logout(View view) {

        SessionManagement sessionManagement = new SessionManagement(GetData.this);

        String uname = sessionManagement.getSession();
        Toast.makeText(getApplicationContext(),uname+" Logged out",Toast.LENGTH_SHORT).show();

        sessionManagement.removeSession();
        moveToLogin();

    }

    private void moveToLogin() {
        Intent i = new Intent(GetData.this,MainActivity.class);
        startActivity(i);
    }

    public void Rescan(View view) {
        Intent intent = new Intent(getApplicationContext(),ScanActivity3.class);
        this.startActivity(intent);
    }
}