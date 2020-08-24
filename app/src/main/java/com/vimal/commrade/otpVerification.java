package com.vimal.commrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class otpVerification extends AppCompatActivity {
    String checkFlag, Name, Mobile, Gender, MSG;
    PinView pinfromUser;
    String codeBySystem;
    TextView editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        pinfromUser = findViewById(R.id.otpPinView);
        editText = findViewById(R.id.numberShowingText);


        Intent intent = getIntent();
        checkFlag = intent.getStringExtra("flag");
        Name = intent.getStringExtra("name");
        Mobile = intent.getStringExtra("mobile");
        Gender = intent.getStringExtra("gender");
        MSG = "Enter Otp Sent to" + Mobile;
        editText.setText(MSG);

        sendVerifcationCodeToUser(Mobile);
    }

    private void sendVerifcationCodeToUser(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;

                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinfromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(otpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && (checkFlag.equals("SignUp"))) {
                            storeNewusersData();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finishAffinity();
                            startActivity(i);

                        } else if (task.isSuccessful() && (checkFlag.equals("Login"))) {
                            Query checkUser = FirebaseDatabase.getInstance().getReference("UserDetails").orderByChild("mobile").equalTo(Mobile);
                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String _Name = dataSnapshot.child(Mobile).child("name").getValue(String.class);
                                    String _Mobile = dataSnapshot.child(Mobile).child("mobile").getValue(String.class);
                                    String _Gender = dataSnapshot.child(Mobile).child("gender").getValue(String.class);

                                    SessionManager sessionManager = new SessionManager(otpVerification.this);
                                    sessionManager.createuserSession(_Name, _Mobile, _Gender);

                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finishAffinity();
                                    startActivity(i);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(otpVerification.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(otpVerification.this, "Verifiaction Failed", Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void storeNewusersData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("UserDetails");
        UserHelper addNewUser = new UserHelper(Name, Mobile, Gender);
        reference.child(Mobile).setValue(addNewUser);

        SessionManager sessionManager = new SessionManager(otpVerification.this);
        sessionManager.createuserSession(Name, Mobile, Gender);
    }

    public void verifyOtpButton(View view) {
        String code = pinfromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }
    }

}