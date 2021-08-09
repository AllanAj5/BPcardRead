package com.example.bpcardread;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bpcardread.AddOnFiles.SessionManagement;
import com.example.bpcardread.AddOnFiles.str_Similarity_Check;
import com.example.bpcardread.EntityClass.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.icu.text.SimpleDateFormat;

public class DetailsActivity4 extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private  String TAG=this.getClass().getSimpleName();
    List<String> name_parts = new ArrayList<String>();

    //List<String> pic1_str_arr = new ArrayList<String>();

    EditText editText_Address, editText_Name,editText_Sex,editText_Date,editTextAadhar,data;
    String full="";
    String session_user="";

    // TimeStamp
    Date now = new Date();
    long timestamp = now.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public String dateStr = sdf.format(timestamp);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details44);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        editText_Name = findViewById(R.id.editText_Name);
        editText_Date = findViewById(R.id.editText_Date);
        editText_Sex = findViewById(R.id.editText_Sex);
        editText_Address = findViewById(R.id.editText_Address);
        editTextAadhar = findViewById(R.id.editTextAadhar);
        data = findViewById(R.id.data);
        data.setVisibility(View.INVISIBLE);



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Intent intent_get = getIntent();
        Bundle args = intent_get.getBundleExtra("key1");
        ArrayList<Object> got_ocr_strings = (ArrayList<Object>) args.getSerializable("ARRAYLIST");


        String part1 = got_ocr_strings.get(0).toString();
        String part2 = got_ocr_strings.get(1).toString();

        full = part1.concat(part2);

        Log.d(TAG,"part1 = \n"+part1);
        Log.d(TAG,"part2 = \n"+part2);


        String name = process_Name(part1);

        String sex = process_Sex(part1);
        String dob = process_DOB(part1);
        String Aadhar = process_Aadhar(part1);


        part2 = process_Address(part2);


        editText_Name.setText(name);
        editText_Name.setEnabled(false);

        editText_Date.setText(dob);
        editText_Date.setEnabled(false);

        editText_Sex.setText(sex);
        editText_Sex.setEnabled(false);


        Aadhar= Aadhar.replaceAll("\\s","");
        editTextAadhar.setText(Aadhar);
        editTextAadhar.setEnabled(false);

        editText_Address.setText(part2);
        editText_Address.setEnabled(false);

        SessionManagement sessionManagement = new SessionManagement(DetailsActivity4.this);

        session_user = sessionManagement.getSession();

    }




    public void Edit(View view) {
        editText_Name.setEnabled(true);
        editText_Date.setEnabled(true);
        editText_Sex.setEnabled(true);
        editTextAadhar.setEnabled(true);
        editText_Address.setEnabled(true);

    }

    public void saveData(View view) {

        String txt_Name = editText_Name.getText().toString().trim();
        String txt_dob = editText_Date.getText().toString().trim();
        String txt_sex = editText_Sex.getText().toString().trim();
        String txt_id = editTextAadhar.getText().toString().trim();
        String txt_Address = editText_Address.getText().toString().trim();


        if(txt_Name!=null &&txt_dob!=null &&txt_sex!=null &&txt_id!=null &&txt_Address!=null){

            DatabaseClass dbClass = DatabaseClass.getDatabase(this.getApplicationContext());

            UserModel model = new UserModel();

            model.name = txt_Name;
            model.dob = txt_dob;
            model.sex = txt_sex;
            model.idNumber = txt_id;
            model.address = txt_Address;
            model.timestmp_created = dateStr;
            model.createdUser = session_user;


            try{
                dbClass.getDao().insertAllData(model);
                Toast.makeText(this,"Saving...",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this,"ERROR Saving Data !\n\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            finally {
                editText_Name.setText("");
                editText_Date.setText("");
                editText_Sex.setText("");
                editTextAadhar.setText("");
                editText_Address.setText("");

                Intent i = new Intent(getApplicationContext(),GetData.class);
                this.startActivity(i);
            }
            //finish();

        }
    }

    private String process_Name(String part1)
    {
        String[] got_split = part1.split("\n");
        String gov = "Government of India";

        int min = low__index_name(gov,got_split);
        int max = high_index_name(got_split,part1); // If DOB or YOB not found in splits , 2nd Arg 'part1' will be sent for default processing
        if(max == 0){Toast.makeText(getApplicationContext(),"0 Names Found",Toast.LENGTH_LONG).show();}
        min+=1;

        // Setting the Name Below Gov
        String assumed_name = got_split[min];

        for(int i=min;i<max;i++){
            name_parts.add(got_split[i]); // Suggestion Names
        }


    return assumed_name;
    }

    public String process_Sex(String first)
    {
        if(first.contains("Female") || first.contains("female"))
        {
           // Toast.makeText(getApplicationContext(),"Female found",Toast.LENGTH_SHORT).show();
            return "Female";
        }
        else if(first.contains("Male") || first.contains("male"))
        {
            return "Male";
        }
        return "Click to Select";
    }

    public String process_DOB(String first)
    {
        String full_str = first;
        String d_o_b = "";
        String y_o_b = "";

        // Date Matching
        if(full_str.contains("DOB")||full_str.contains("dob")||full_str.contains("DoB")||full_str.contains("Dob"))
        {
            try {
                Pattern pattern = Pattern.compile("[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}");// Capturing Date Regex
                Matcher matcher = pattern.matcher(full_str);
                if (matcher.find()) {
                    d_o_b = matcher.group(0);
                    return d_o_b;
                }
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Retry \n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else if(full_str.contains("YoB")||full_str.contains("YOB")||full_str.contains("yob")||full_str.contains("Yob"))
        {
            try {
                Pattern pattern = Pattern.compile("\\d{4}");// Capturing Date Regex
                Matcher matcher = pattern.matcher(full_str);
                if (matcher.find()) {
                    y_o_b = matcher.group(0);
                    return y_o_b;
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Retry \n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return "Enter Manually";
    }

    public String process_Aadhar(String part1)
    {
        String full_str = part1;
        String full_str1 = full_str.replaceAll("\n","");
        String Aadhar ="";
        try {
            Pattern pattern = Pattern.compile("[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}");// Capturing Date Regex
            Matcher matcher = pattern.matcher(full_str1);
                if (matcher.find())
                {
                    Aadhar = matcher.group(0);
                    return Aadhar;
                }
            else{
                return "Invalid Aadhar #";
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Retry \n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
      return "Enter Manually";
    }

    public String process_Address(String address){
        String convoluted_Address = "";
        String[] address_array = address.split("\n");

        convoluted_Address = find_closest_match_to_term("Address",address_array);

        if(address.contains("Address"))        // 2 Cases - 1st if "Address" gets correctly identified , use that to split string
        {                                   // when it fails , catch the next closest match to "Address"
            address = pre_process_Address(address,"Address");
        }
        else if (address.contains(convoluted_Address))
        {
            address = pre_process_Address(address,convoluted_Address);
        }
    return address;
    }

    public String find_closest_match_to_term(String y_truth,String[] y_pred){
        int index = low__index_name(y_truth,y_pred); // Fetching the closest word to "Address"
        String closest_term = y_pred[index];
        Log.d(TAG,"Convoluted add_ress_ters = "+closest_term);
        return closest_term;
    }

    public String pre_process_Address(String addr,String term){
        String pincode="";
        addr = addr.split(term)[1]; //Splitting based on keyword "Address",and saving 2nd part, ie what comes after that
        Log.d(TAG,"1st_split_address = "+addr);
        addr = lead_trail_char_remove(addr);
        Log.d(TAG,"clean_address = "+addr);
        try {
            Pattern pattern = Pattern.compile("\\d{6}");// Catching Pincode with 6 digit Regex
            Matcher matcher = pattern.matcher(addr);
            if (matcher.find()) {
                pincode = matcher.group(0);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Retry \n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        String pincodeb4 = addr.split("\\d{6}")[0]; // Eliminating text after Pincode
        Log.d(TAG,"pincode = "+pincodeb4);
        addr = pincodeb4.concat(pincode); // Address + Pincode
        Log.d(TAG,"final_Address = "+addr);
        return addr;
    }

    public static String lead_trail_char_remove(String str){
        String clean = str;
        clean = clean.replaceAll("^:+", "");
        clean = clean.trim();
        return clean;
    }
     public void getData(View view) {
         Toast.makeText(getApplicationContext(),"Opening DB",Toast.LENGTH_SHORT).show();
         startActivity(new Intent(getApplicationContext(),GetData.class));
     }

    public void getData1(View view) {
        //Toast.makeText(getApplicationContext(),"Opening DB",Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(getApplicationContext(),GetData.class));
        data.setVisibility(View.VISIBLE);
        data.setText(full);

        SpannableString highlightString = new SpannableString(data.getText());
        highlightString.setSpan(new BackgroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.gray))
                , 0, data.getText().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        data.setText(highlightString);
        copyToClipboard(data.getText().toString());
    }

    public void copyToClipboard(String copyText) {
        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("url", copyText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(),full,Toast.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(getApplicationContext(), "Text copied\nYou can fetch required data by pasting this anywhere", Toast.LENGTH_LONG);
        toast.show();
        data.setText("");
        data.setVisibility(View.INVISIBLE);
    }

    // Retreving Names - For Name Processing
    public static int low__index_name(String word,String[] got_split)
    {
        str_Similarity_Check strCheck = new str_Similarity_Check();
        List<Double> got_score = new ArrayList<Double>();

        for (int i=0;i<got_split.length;i++)
        {
            got_score.add(strCheck.printSimilarity(word,got_split[i]));
        }
        double maxVal = Collections.max(got_score); // should return 7
        Integer maxIdx = got_score.indexOf(maxVal);
        return maxIdx;
    }

    public int high_index_name(String[] got_split,String part1)
    {
        String dbo = "DOB";
        String yob = "YoB";


        for (int i=0;i<got_split.length;i++)
        {
            if(got_split[i].contains(dbo)){
                return i;
            }
            else if(got_split[i].contains(yob)){
                return i;
            }
        }
        process_Name2(part1);    // We dont want this Case

        return 0;
    }

    private  void process_Name2(String part1) // Manual Processing of Name

    {
        String full_str = part1;

        String first_half = full_str.split("DOB")[0];

        Log.d(TAG,"Name_Split_On_dob = "+first_half);

        first_half = first_half.replace("Govemment of India","");
        first_half = first_half.replace("Govemment","");
        first_half = first_half.replace("Government","");
        first_half = first_half.replace("of","");
        first_half = first_half.replace("India","");


        first_half = lead_trail_char_remove(first_half);
        Log.d(TAG,"Name_after_clean = "+first_half);
        editText_Name.setText(first_half);
    }

    public void openmenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(),editText_Name);
        popupMenu.setOnMenuItemClickListener(this);
        //popupMenu.inflate(R.menu.popup_menu);
        int items = name_parts.size();
        switch (items){
            case 1:
                popupMenu.getMenu().add(Menu.NONE,1,1,name_parts.get(0));
                break;
            case 2:
                popupMenu.getMenu().add(Menu.NONE,1,1,name_parts.get(0));
                popupMenu.getMenu().add(Menu.NONE,2,2,name_parts.get(1));
                break;
            case 3:
                popupMenu.getMenu().add(Menu.NONE,1,1,name_parts.get(0));
                popupMenu.getMenu().add(Menu.NONE,2,2,name_parts.get(1));
                popupMenu.getMenu().add(Menu.NONE,3,3,name_parts.get(2));
                break;
            default:
                Toast.makeText(getApplicationContext(),"Noting to Display",Toast.LENGTH_SHORT).show();
                break;
        }
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                editText_Name.setText(item.getTitle());
                return true;
            case 2:
                editText_Name.setText(item.getTitle());
                return true;
            case 3:
                editText_Name.setText(item.getTitle());
                return true;
            default:
                Toast.makeText(getApplicationContext(), "Type Manually", Toast.LENGTH_LONG).show();
                return false;
        }

    }

    public void select_gender(View view) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
        popupMenu.getMenu().add(Menu.NONE,5,5,"Male");
        popupMenu.getMenu().add(Menu.NONE,6,6,"Female");
        popupMenu.getMenu().add(Menu.NONE,7,7,"Transgender");


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 5:
                        editText_Sex.setText(item.getTitle());
                        return true;
                    case 6:
                        editText_Sex.setText(item.getTitle());
                        return true;
                    case 7:
                        editText_Sex.setText(item.getTitle());
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    // Retreving Names - For Name Processing  END

}