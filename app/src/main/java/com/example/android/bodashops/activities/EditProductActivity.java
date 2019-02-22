package com.example.android.bodashops.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.VolleySingleton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EditProductActivity extends AppCompatActivity
{
    String id,prodName, qty, price, img;
    private AppCompatImageView imageView;
    private TextInputEditText nameET, priceET, qtyET;
    private Spinner spinner;
    private Button nxtBtn, clrBtn;
    private ImageButton cameraBtn;
    private ProgressBar progressBar;

    private String newName, newPrice, newQuantity, newTypeId;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> producttypes;

    private Context context;
    private Integer REQUEST_CAMERA = 1, SELECT_FILE=0;
    private static Bitmap imgBitmap;
    private static String imgbitmapstr;

    private Intent intent;
    private Boolean imageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_product);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Edit product");

        context = EditProductActivity.this;

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.PRODIDKEY);
        prodName = intent.getStringExtra(Config.PRODNAMEKEY);
        price = intent.getStringExtra(Config.PRICEKEY);
        qty = intent.getStringExtra(Config.QTYKEY);
        img = intent.getStringExtra(Config.IMG_BASE_URL);

        setUI();
    }

    private void setUI()
    {
        RequestOptions options;
        options = new RequestOptions().placeholder(R.drawable.loading_shape).error(R.drawable.image_error);

        imageView = findViewById(R.id.iv_edit_product);
        nameET = findViewById(R.id.nameET_edit_product);
        priceET = findViewById(R.id.priceET_edit_product);
        qtyET = findViewById(R.id.qtyET_edit_product);

        nxtBtn = findViewById(R.id.nextBtn_edit_product);
        clrBtn = findViewById(R.id.clearBtn_edit_product);
        cameraBtn = findViewById(R.id.ib_edit_product);
        progressBar = findViewById(R.id.progressBarEditProduct);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });
        clrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSubmit();
            }
        });

        nameET.setText(prodName);
        priceET.setText(price);
        qtyET.setText(qty);

        populateSpinner();

        Glide.with(getApplicationContext()).load(Config.IMG_BASE_URL + img).apply(options).into(imageView);

    }

    private void clearFields()
    {
        nameET.setText("");
        qtyET.setText("");
        priceET.setText("");
        spinner.refreshDrawableState();
    }

    private void gotoSubmit()
    {
        if (getValues())
        {
            if (!imageChanged){
                intent = new Intent(EditProductActivity.this, UpdateProductActivity.class);
                intent.putExtra(Config.PRODNAMEKEY,newName);
                intent.putExtra(Config.QTYKEY, newQuantity);
                intent.putExtra(Config.TYPEKEY,newTypeId);
                intent.putExtra(Config.PRICEKEY,newPrice);
                intent.putExtra(Config.PRODIDKEY,id);
                startActivity(intent);
            }else {
                new BackgroundWorker().execute(imgBitmap);
            }

            //intent to camera activity

        }
    }

    private boolean getValues()
    {
        newName = nameET.getText().toString().trim();
        String strprice = priceET.getText().toString().trim();
        String strqty = qtyET.getText().toString().trim();

        if (newName.isEmpty()){
            nameET.setError("Name can't be empty!");
            nameET.requestFocus();
            return false;
        }
        if (strqty.isEmpty()){
            qtyET.setError("Quantity is required!");
            qtyET.requestFocus();
            return false;
        }
        if (strprice.isEmpty()){
            priceET.setError("Price is required!");
            priceET.requestFocus();
            return false;
        }

        newPrice = strprice;
        newQuantity = strqty;
        newTypeId = String.valueOf(spinner.getSelectedItemPosition());
        return true;
    }

    private void populateSpinner()
    {
        producttypes = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest;
        RequestQueue requestQueue;
        jsonArrayRequest = new JsonArrayRequest(Config.GET_TYPES_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                int index = Integer.parseInt(object.getString("typeId"));
                                String type = object.getString("type");

                                producttypes.add(type);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(EditProductActivity.this).addToRequestQue(jsonArrayRequest);

        adapter = new ArrayAdapter<String>(
                this,
                R.layout.spinner_types_layout,
                producttypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.spinner_edit_product);
        spinner.setAdapter(adapter);
    }

    private void editImage()
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

                if (imgBitmap != null)
                {
                    imageView.setImageBitmap(imgBitmap);
                    imageChanged = true;
                }

            }else if (requestCode == SELECT_FILE){

                Uri selectedImageUri = data.getData();


                try {
                    imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);

                    if (imgBitmap != null)
                    {
                        imageView.setImageURI(selectedImageUri);
                        imageChanged = true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class BackgroundWorker extends AsyncTask<Bitmap,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

            byte[] imagebytes = byteArrayOutputStream.toByteArray();
            imgbitmapstr = Base64.encodeToString(imagebytes,Base64.DEFAULT);

            return imgbitmapstr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            intent = new Intent(EditProductActivity.this, UpdateProductActivity.class);
            intent.putExtra(Config.PRODNAMEKEY,newName);
            intent.putExtra(Config.QTYKEY, newQuantity);
            intent.putExtra(Config.TYPEKEY,newTypeId);
            intent.putExtra(Config.PRICEKEY,newPrice);
            intent.putExtra(Config.PRODIDKEY,id);
            intent.putExtra(Config.IMAGEBITMAPSTRING,s);

            intent.putExtra(Config.OLDIMGLINK, img);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.GONE);
        }
    }
}
