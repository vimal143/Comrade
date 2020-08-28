package com.vimal.commrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

public class MemberInput extends AppCompatActivity {
    TextInputLayout memberName, memberRelation, memberMobile;
    String name, relation, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_input);
        memberName = findViewById(R.id.fullNameMember);
        memberRelation = findViewById(R.id.relationship);
        memberMobile = findViewById(R.id.MobileNumberFamily);


    }

    public void memberAdded(View view) {
        if (!validatefullName() && !validateMobileNumber()) {
            return;
        }
        name = memberName.getEditText().getText().toString();
        relation = memberRelation.getEditText().getText().toString();
        mobile = memberMobile.getEditText().getText().toString();
//        Log.d("checkMember", "memberAdded: "+name);
        DatabaseHandler databaseHandler = new DatabaseHandler(MemberInput.this);
        databaseHandler.addFamilyMember(name, relation, mobile);
        Intent i = new Intent(MemberInput.this, AddMember.class);
        startActivity(i);
        finish();
    }

    private boolean validatefullName() {
        String fullName = memberName.getEditText().getText().toString().trim();
        if (fullName.isEmpty()) {
            memberName.setError("Field can not be Empty");
            return false;
        } else {
            memberName.setError(null);
            memberName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMobileNumber() {
        String number = memberMobile.getEditText().getText().toString().trim();
        if (number.isEmpty()) {
            memberMobile.setError("Field can not be  Empty");
            return false;
        } else if (number.length() != 10) {
            memberMobile.setError("Invalid Mobile Number");
            return false;
        } else {
            memberMobile.setError(null);
            memberMobile.setErrorEnabled(false);
            return true;
        }
    }
}