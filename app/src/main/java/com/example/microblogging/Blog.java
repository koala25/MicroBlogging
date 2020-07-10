package com.example.microblogging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Blog extends AppCompatActivity {

    private FloatingActionButton createForum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_drawer);

        createForum=(FloatingActionButton)findViewById(R.id.floatingBtn_id);
        createForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateForumForm.class);
                startActivity(i);
            }
        });

    }
}