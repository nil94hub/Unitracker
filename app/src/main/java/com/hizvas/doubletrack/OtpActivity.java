package com.hizvas.doubletrack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText etotp;
    Button btnotp;
    private String mVerificationId;
    ProgressDialog progressdialog;
    SwipeRefreshLayout swipeRefreshLayout;
    int bool = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        etotp = (EditText)findViewById(R.id.etOTP);
        progressdialog = new ProgressDialog(OtpActivity.this);
        progressdialog.setMessage("Authenticating your device");
        CountDownTimer cdt;
        SessionManager.initialize(this);
        /*if (bool==0){


        cdt = new CountDownTimer(12000, 11000) {
            @Override
            public void onTick(long millisUntilFinished) {

                //    Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                // Toast.makeText(this, ""+millisUntilFinished, Toast.LENGTH_SHORT).show();
                //  Toast.makeText(ScreenReceiver.this, "millis until finish "+millisUntilFinished/1000, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFinish() {
                // Log.i(TAG, "Timer finished");
                Toast.makeText(OtpActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        };

        cdt.start();
        }*/
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refreshotp);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
       // progressdialog.show();
        etotp.setEnabled(false);
       // btnotp = (Button)findViewById(R.id.btnOtpSubmit);
        SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String mobile =  SessionManager.getMobile();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,        // Phone number to verify
                10,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
          //  Toast.makeText(OtpActivity.this, ""+credential, Toast.LENGTH_SHORT).show();
            bool = 1;
           String code = credential.getSmsCode();
           etotp.setText(code);
            SharedPreferences.Editor editor = OtpActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit();
            editor.putBoolean("login",true);
            editor.apply();
          //  progressdialog.dismiss();
            LoginActivity.locpermission = 1;
            Toast.makeText(OtpActivity.this, "Authentication succesful", Toast.LENGTH_SHORT).show();
startActivity(new Intent(getApplicationContext(),MapsActivity.class));

           // verifyVerificationCode(code);
        }
      /*  private void verifyVerificationCode(String otp) {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

            //signing the user
            signInWithPhoneAuthCredential(credential);
        }*/

       /* private void verifyVerificationCode(String code) {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            Toast.makeText(OtpActivity.this, ""+credential, Toast.LENGTH_SHORT).show();
            //signing the user
            //signInWithPhoneAuthCredential(credential);
        }*/
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            // Log.w(TAG, "onVerificationFailed", e);
            Toast.makeText(OtpActivity.this, "Authentication failed"+e, Toast.LENGTH_LONG).show();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(OtpActivity.this, "Authentication failed"+e, Toast.LENGTH_LONG).show();

                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
              //  progressdialog.dismiss();
                Toast.makeText(OtpActivity.this, "Too many attempts, please try later", Toast.LENGTH_SHORT).show();
                // The SMS quota for the project has been exceeded
                // ...
            }
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            //  Log.d(TAG, "onCodeSent:" + verificationId);
          //  Toast.makeText(OtpActivity.this, ""+verificationId, Toast.LENGTH_SHORT).show();
            // Save verification ID and resending token so we can use them later
             mVerificationId = verificationId;
           // Toast.makeText(OtpActivity.this, "Authentication failed"+verificationId, Toast.LENGTH_LONG).show();

          //   mResendToken = token;

            // ...
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
          //  progressdialog.dismiss();
            Toast.makeText(OtpActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }
}
