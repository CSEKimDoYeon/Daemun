package com.example.kimdoyeon.daemun.Daemun_main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.kimdoyeon.daemun.R;

/**
 * Created by KimDoYeon on 2017-10-23.
 */

public class MainActivity extends AppCompatActivity {

    Button gotoDB_button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);

        gotoDB_button = (Button)findViewById(R.id.Activity_main_gotoDB_btn);
        gotoDB_button.setOnClickListener(listener);

    }

    Button.OnClickListener listener = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            switch(v.getId()){
                case R.id.Activity_main_gotoDB_btn:
                    Intent myintent = new Intent(MainActivity.this, BoardActivity.class);
                    startActivity(myintent);
                    overridePendingTransition(R.anim.rightin_activity, R.anim.not_move_activity);
                    break;
            }
        }
    };

    public void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity);
    }
}
