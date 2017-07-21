package com.example.lcc.acountingbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calendar(View v){
        startActivity(new Intent(this ,calendar_main.class));
    }

    public void account(View v){
        startActivity(new Intent(this , account_menu.class));
    }
}
