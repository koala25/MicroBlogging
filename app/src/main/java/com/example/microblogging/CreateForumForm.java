package com.example.microblogging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.HashMap;

public class CreateForumForm extends AppCompatActivity {

    Button submit;
    EditText name;
    EditText description;
    ImageButton image;

    Uri originaluri;
    Uri cropUri;
    ProgressBar progressBar;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    private static final int Image_Request_Code=7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_forum_form);

        submit=findViewById(R.id.forumSubmitBtn_id);
        name=findViewById(R.id.forumName_id);
        description=findViewById(R.id.forumDescription_id);
        image=findViewById(R.id.imgBtn_id);

        progressBar=findViewById(R.id.progressBar_id);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Defining Implicit Intent to mobile gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, Image_Request_Code); // gallery opens
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent (getApplicationContext() , FetchData.class);
                startActivity(i);*/
                if( TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(description.getText().toString()) )
                {
                    Toast.makeText(CreateForumForm.this, "Field is empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadData();

                }
            }
        });
    }

    //called when image is selected from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Image_Request_Code && resultCode==RESULT_OK && data != null )
        {
            originaluri = data.getData(); //original image ka uri
            CropImage.activity(originaluri).start(this);//crop image // also it is possible to fix aspect ratio
        }
            //check whether crop succeful or not
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                cropUri = result.getUri();
                image.setImageURI(cropUri); //temporary set
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadData() {

        storageReference.child("Forum Image")
                        .putFile(cropUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {

                        HashMap<String,Object> map = new HashMap<>();
                        map.put("forum name",name.getText().toString());
                        map.put("forum description",description.getText().toString());
                        map.put("forum profile uri",uri.toString());

                        databaseReference.child("Forums")
                                .push()          //push
                                .setValue(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Picasso.get().load(uri.toString()).into(image);
                                        Toast.makeText(CreateForumForm.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }
                });
            }
        });



    }
}