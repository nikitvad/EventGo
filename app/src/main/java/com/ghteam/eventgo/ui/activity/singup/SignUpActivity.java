package com.ghteam.eventgo.ui.activity.singup;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.network.FirebaseAccountManager;
import com.ghteam.eventgo.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.ilhasoft.support.validation.Validator;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private Validator validator;

    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        ActivitySignUpBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        dataBinding.setViewModel(viewModel);
        ButterKnife.bind(this);

        setSupportActionBar(dataBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        validator = new Validator(dataBinding);
        validator.enableFormValidationMode();
        validator.validate();
    }


    @OnClick(R.id.bt_submit)
    void onSubmitClick() {
        if (validator.validate()) {
            Log.d(TAG, "onSubmitClick: " + viewModel.toString());

            FirebaseAccountManager.getInstance()
                    .createNewAccount(viewModel.getEmail(),
                            viewModel.getPassword(), viewModel.getUserData());

        }

    }


}
