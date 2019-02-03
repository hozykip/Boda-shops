package com.example.android.bodashops.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.activities.SubmitProductActivity;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductImageFragment extends Fragment {


    private AppCompatImageView imageView;
    private ImageButton cameraBtn;
    private Button nxtBtn;

    private Context context;

    Integer REQUEST_CAMERA = 1, SELECT_FILE=0;
    private static Bitmap imgBitmap;
    private static String imgbitmapstr;

    public ProductImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Item image");

        View view = inflater.inflate(R.layout.fragment_product_image, container, false);
        setUI(view);

        return view;
    }

    private void setUI(View view) {
        context = getContext();
        imageView = view.findViewById(R.id.itemImage);
        nxtBtn = view.findViewById(R.id.nextBtnToDets);
        cameraBtn = view.findViewById(R.id.cameraButton);
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

    private void selectImage(){
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

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    @Override
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
                    imgBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selctedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImageURI(selctedImageUri);
                nxtBtn.setVisibility(View.VISIBLE);

            }
        }
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imgBytes = outputStream.toByteArray();

        imgbitmapstr = Base64.encodeToString(imgBytes,Base64.DEFAULT);

        return imgbitmapstr;

    }

    private void gotoDets()
    {
        Intent intent = new Intent(getContext(), SubmitProductActivity.class);
        intent.putExtra("imageBitmapString", imgbitmapstr);
        startActivity(intent);

        /*ProductDetailsFragment fragment = new ProductDetailsFragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.frameLayout,fragment)
                .commit();*/
    }

    private void uploadImage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("")

                return super.getParams();
            }
        };
    }
}
