package com.example.bpcardread;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ScanActivity3 extends AppCompatActivity {

    String s="";
    ImageView image_aadhar_f, image_aadhar_b;
    ProgressDialog progressDialog;
    private  String TAG = this.getClass().getSimpleName();

    Bitmap imageBitmap;
    //List<Bitmap> list=new ArrayList<Bitmap>();
    List<String> ocr_strings = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan3);


        image_aadhar_f = findViewById(R.id.image_aadhar_f);
        image_aadhar_b = findViewById(R.id.image_aadhar_b);

        image_aadhar_f.setBackgroundResource(R.drawable.aadharf);
        image_aadhar_b.setBackgroundResource(R.drawable.aadharb);


    }



    public void Next(View view)
    {
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)ocr_strings);

        //Toast.makeText(getApplicationContext(),"Sending...\n"+s,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ScanActivity3.this, DetailsActivity4.class).putExtra("key1", args));
    }

    public void dispatchTakePictureIntent(View view)
    {
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA},101);
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 101);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch(requestCode) {
                case 101: // Camera
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    if (image_aadhar_f.getDrawable() == null) {
                        image_aadhar_f.setImageBitmap(imageBitmap);
                        Image_Process(imageBitmap);

                    } else if (image_aadhar_b.getDrawable() == null) {
                        image_aadhar_b.setImageBitmap(imageBitmap);
                        Image_Process(imageBitmap);
                    }
                    break;
                case 5: // Gallery
                    Uri filePath = data.getData();

                    Bitmap img = null;
                    try {
                        img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(img!= null)
                    {
                        //image.setImageBitmap(img);
                        if (image_aadhar_f.getDrawable() == null) {
                            image_aadhar_f.setImageBitmap(img);
                            Image_Process(img);
                        } else if (image_aadhar_b.getDrawable() == null) {
                            image_aadhar_b.setImageBitmap(img);
                            Image_Process(img);
                        }
                    }
                    break;

            }
        }
    }

    public void Image_Process(Bitmap bit_obj)
    {
        progressDialog = ProgressDialog.show(ScanActivity3.this,
                "Processing Image",
                "Please Wait ...");
        // Image Processing
        InputImage image = InputImage.fromBitmap(bit_obj,0);
        TextRecognizer recognizer = TextRecognition.getClient();


        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                ocr_strings.add(visionText.getText());
                                Log.d(TAG,"----ocr---\n"+visionText.getText());
                                Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();


                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Not Processed Retry !",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        int PICK_IMAGE_REQUEST = 5;
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

}

