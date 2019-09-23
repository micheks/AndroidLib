package com.alliky.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alliky.core.Toasty;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toasty.normal(this,"这是什么").show();

    }
}
