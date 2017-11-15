package com.ghteam.eventgo.ui.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.ui.activity.singup.SingUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.iv_show_password)
    ImageView ivShowPassword;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.cb_show_password)
    CheckBox cbShowPassword;

    @BindView(R.id.tv_forgot_your_password)
    TextView tvForgotYourPassword;

    @BindView(R.id.bt_sign_in)
    Button btSingIn;

    @BindView(R.id.bt_facebook_login)
    Button btFacebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.tv_create_account)
    void showCreateSingUpActivity() {
        Intent intent = new Intent(this, SingUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_show_password)
    void switchPasswordVisibility(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_email:
                etEmail.requestFocus();
                return;
            case R.id.et_password:
                etPassword.requestFocus();
                return;
        }
    }
}
