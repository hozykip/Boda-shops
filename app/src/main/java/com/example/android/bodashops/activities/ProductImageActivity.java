package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;

import java.io.IOException;

public class ProductImageActivity extends AppCompatActivity {

    private AppCompatImageView imageView;
    private ImageButton cameraBtn;
    private Button nxtBtn;

    private Context context;

    Integer REQUEST_CAMERA = 1, SELECT_FILE=0;
    private static Bitmap imgBitmap;
    private static String imgbitmapstr;

    private String prodName;
    private int prodQty, prodType, prodPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image);
        setUI();
        setTitle("Item Image");
    }

    private void setUI()
    {
        Toolbar toolbar = findViewById(R.id.toolbarImageActivity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = ProductImageActivity.this;
        imageView = findViewById(R.id.itemImage);
        nxtBtn = findViewById(R.id.nextBtnToDets);
        cameraBtn = findViewById(R.id.cameraButton);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDets();
            }
        });
    }

    private void selectImage()
    {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera")){
                    cameraIntent();
                }else if (items[which].equals("Gallery")){
                    galleryIntent();
                }else if (items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select File"),SELECT_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                imgBitmap = (Bitmap) bundle.get("data");

                imageView.setImageBitmap(imgBitmap);
                nxtBtn.setVisibility(View.VISIBLE);

            }else if (requestCode == SELECT_FILE){

                Uri selctedImageUri = data.getData();


                try {
                    imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selctedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImageURI(selctedImageUri);
                nxtBtn.setVisibility(View.VISIBLE);

            }
        }
    }

    private void gotoDets()
    {
        Intent i = getIntent();
        prodName = i.getStringExtra(Config.PRODNAMEKEY);
        prodPrice = i.getIntExtra(Config.PRICEKEY,1);
        prodQty = i.getIntExtra(Config.QTYKEY,1);
        prodType = i.getIntExtra(Config.TYPEKEY,1);


        Intent intent = new Intent(ProductImageActivity.this, SubmitProductActivity.class);
        intent.putExtra(Config.IMAGEBITMAPSTRING, imgbitmapstr);
        intent.putExtra(Config.PRODNAMEKEY,prodName);
        intent.putExtra(Config.PRICEKEY,prodPrice);
        intent.putExtra(Config.QTYKEY,prodQty);
        intent.putExtra(Config.TYPEKEY,prodType);
        startActivity(intent);
    }
}
