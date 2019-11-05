package com.alliky.core.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alliky.core.R;
import com.alliky.core.R2;
import com.alliky.core.base.BaseActivity;

import butterknife.BindView;


public class MainActivity extends BaseActivity {

    @BindView(R2.id.textView)
    TextView mTextView;

    @Override
    public Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {

    }

}
