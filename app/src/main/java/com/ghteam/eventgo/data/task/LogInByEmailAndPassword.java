package com.ghteam.eventgo.data.task;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by nikit on 19.01.2018.
 */

public class LogInByEmailAndPassword extends BaseTask<Void, String> {

    private String mEmail;
    private String mPassword;

    private static final String TAG = LogInByEmailAndPassword.class.getSimpleName();

    public LogInByEmailAndPassword(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    public void execute(Void... params) {
        changeStatus(TaskStatus.IN_PROGRESS);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.getException() == null) {
                            AuthResult result = task.getResult();

                            if (result.getUser() != null && !result.getUser().getUid().isEmpty()) {
                                publishResult(task.getResult().getUser().getUid());
                                changeStatus(TaskStatus.SUCCESS);
                            }else{
                                changeStatus(TaskStatus.ERROR);
                            }
                        } else {
                            changeStatus(TaskStatus.ERROR);
                            exception = task.getException();
                            Log.w(TAG, "onComplete: ", exception);
                        }
                    }
                });
    }
}
