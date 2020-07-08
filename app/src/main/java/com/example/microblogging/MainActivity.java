package com.example.microblogging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


   /* @Override
    protected void onStart() {
        super.onStart();




    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user =mAuth.getCurrentUser();
        if(user==null) {
            Intent i = new Intent(this, SignIn.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(this, Blog.class);
            startActivity(i);
        }




    }
}