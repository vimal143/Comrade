package com.vimal.commrade;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

public class MemberInput extends AppCompatActivity {
    TextInputLayout memberName,memberRelation,memberMobile;
    String name,relation,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_input);
        memberName=findViewById(R.id.fullNameMember);
        memberRelation=findViewById(R.id.relationship);
        memberMobile=findViewById(R.id.MobileNumberFamily);


    }
    public void memberAdded(View view){
        name=memberName.getEditText().getText().toString();
        relation=memberRelation.getEditText().getText().toString();
        mobile=memberMobile.getEditText().getText().toString();
        Log.d("checkMember", "memberAdded: "+name);
        DatabaseHandler databaseHandler=new DatabaseHandler(MemberInput.this);
        databaseHandler.addFamilyMember(name,relation,mobile);

    }
}