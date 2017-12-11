package com.example.kimdoyeon.daemun.Kakao_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.kimdoyeon.daemun.Daemun_DB.User;
import com.example.kimdoyeon.daemun.Daemun_main.BoardActivity;
import com.example.kimdoyeon.daemun.Daemun_main.MainActivity;
import com.example.kimdoyeon.daemun.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by KimDoYeon on 2017-10-23.
 */

public class LoginSuccessActivity  extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    TextView user_nickname,user_email;
    CircleImageView user_img;
    Button logout_btn;
    Button start_btn;
    AQuery aQuery;

    String name;
    String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        // DB 관련 변수 초기화
        database = FirebaseDatabase.getInstance();
        // message Reference가 없어도 상관 x
        myRef = database.getReference();



        Intent intent = getIntent();

        user_nickname =(TextView)findViewById(R.id.user_nickname);
        name = intent.getExtras().getString("name");
        user_nickname.setText(name);

        user_img =(CircleImageView) findViewById(R.id.user_img);
        aQuery = new AQuery(this);
        aQuery.id(user_img).image(intent.getExtras().getString("image"));

        user_email =(TextView)findViewById(R.id.user_email);
        email = intent.getExtras().getString("email");
        user_email.setText(email);

        User us = new User(name,email);
        Log.e("TAG", "저장된 유저이름 : "+ us.getUsername());
        Log.e("TAG", "저장된 유저이메일 : "+ us.getEmail());
        writeNewUser("1",name,email);

        logout_btn = (Button)findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Session.getCurrentSession().isOpened()) {
                    requestLogout();
                }
            }
        });
        start_btn = (Button)findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Session.getCurrentSession().isOpened()) {
                   Intent intent_Activity_main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent_Activity_main);
                    overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity);
                }
                /*Intent intent_Activity_main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_Activity_main);*/
            }
        });
    }
    private void requestLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {

            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginSuccessActivity.this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                        /*Intent intent_Activity_login = new Intent(getApplicationContext(), LoginActivity.class);
                        intent_Activity_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_Activity_login);*/
                        finish();
                        overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);
                    }
                });
            }
        });
    }

    public void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        myRef.child("users").child(userId).setValue(user);
    }

    public void onDestroy() {
        super.onDestroy();
        //Session.getCurrentSession().removeCallback(callback);
        requestLogout();
    }
}
