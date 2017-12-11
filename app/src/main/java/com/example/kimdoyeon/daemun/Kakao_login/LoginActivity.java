package com.example.kimdoyeon.daemun.Kakao_login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.kimdoyeon.daemun.R;

/* 페이스북 연동부 */

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

/* 카카오 연동부 */
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    TextView user_nickname, user_email;
    CircleImageView user_img;
    LinearLayout success_layout;
    Button logout_btn;
    LoginButton loginButton;
    AQuery aQuery;

    /* 페이스북 */
    private CallbackManager callbackManager;
    private List<String> permissionNeeds = Arrays.asList("email");
    private Button btnLogin;
    private Button CustomloginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this); // SDK 초기화, setContentView보다 먼저 실행되어야 한다.
        setContentView(R.layout.activity_login);


        try { // 해시키를 받아오는 메소드
            PackageInfo info = getPackageManager().getPackageInfo("your.package.name", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        aQuery = new AQuery(this);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        /* 페이스북 */
        callbackManager = CallbackManager.Factory.create(); // 로그인 응답을 처리할 콜백 관리자.

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("토큰",loginResult.getAccessToken().getToken());
                Log.e("유저아이디",loginResult.getAccessToken().getUserId());
                Log.e("퍼미션 리스트",loginResult.getAccessToken().getPermissions()+"");

                //loginResult.getAccessToken() 정보를 가지고 유저 정보를 가져올수 있습니다.
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken() ,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.e("user profile",object.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d("Tag", "로그인 하려다 맘");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Tag", "좆망 : " + error.getLocalizedMessage());
            }
        });

        btnLogin = (Button) findViewById(R.id.facebook_login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissionNeeds);
            }
        });

        CustomloginButton = (Button)findViewById(R.id.loginBtn);
        CustomloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "user_friends","user_status","email","user_hometown"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Log.e("onSuccess", "onSuccess");
                                Log.e("토큰", String.valueOf(loginResult.getAccessToken()));
                                Log.e("아이디", String.valueOf(Profile.getCurrentProfile().getId()));
                                Log.e("이름", String.valueOf(Profile.getCurrentProfile().getName())); // 이름
                                Log.e("프로필사진Uri", String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(200, 200)));//프로필 사진

                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken() ,
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                try {
                                                    Log.e("user profile",object.toString());
                                                    String hometown = response.getJSONObject().getString("user_hometown");
                                                    Log.e("고향", hometown);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                request.executeAsync();



                            }

                            @Override
                            public void onCancel() {
                                Log.e("onCancel", "onCancel");
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                Log.e("onError", "onError " + exception.getLocalizedMessage());
                            }
                        });
            }
        });

        // 카카오톡 로그인 버튼
        loginButton = (LoginButton) findViewById(R.id.com_kakao_login);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!isConnected()) {
                        Toast.makeText(LoginActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                if (isConnected()) {
                    return false;
                } else {
                    return true;
                }
            }
        });
        /*// 로그인 성공 시 사용할 뷰
        success_layout = (LinearLayout)findViewById(R.id.success_layout);
        user_nickname =(TextView)findViewById(R.id.user_nickname);
        user_img =(CircleImageView) findViewById(R.id.user_img);
        user_email =(TextView)findViewById(R.id.user_email);
        logout_btn = (Button)findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Session.getCurrentSession().isOpened()) {
                    requestLogout();
                }
            }
        });*/
        if (Session.getCurrentSession().isOpened()) {
            // requestMe();
        } else {
            /*success_layout.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);*/
        }
    }

    //인터넷 연결상태 확인
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data); // 이거 페북꺼임
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            //access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태. 일반적으로 로그인 후의 다음 activity로 이동한다.
            if (Session.getCurrentSession().isOpened()) { // 한 번더 세션을 체크해주었습니다.
                requestMe();
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    /*private void requestLogout() {
        success_layout.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }*/

    private void requestMe() {
        //success_layout.setVisibility(View.VISIBLE);
        //loginButton.setVisibility(View.GONE);

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("onFailure", errorResult + "");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("onSessionClosed", errorResult + "");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("onSuccess", userProfile.toString());
                //user_nickname.setText(userProfile.getNickname());
                //user_email.setText(userProfile.getEmail());
                //aQuery.id(user_img).image(userProfile.getThumbnailImagePath()); // <- 프로필 작은 이미지 , userProfile.getProfileImagePath() <- 큰 이미지

                Intent intent = new Intent(getApplicationContext(), LoginSuccessActivity.class);
                intent.putExtra("name", userProfile.getNickname());
                intent.putExtra("email", userProfile.getEmail());
                intent.putExtra("image", userProfile.getThumbnailImagePath());

                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity, R.anim.not_move_activity);
            }
            @Override
            public void onNotSignedUp() {
                Log.e("onNotSignedUp", "onNotSignedUp");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }
}


