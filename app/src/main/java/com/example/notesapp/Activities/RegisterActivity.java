package com.example.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button btn_register;
    EditText et_fullName;
    EditText et_password;
    EditText et_email;
    TextView tv_alreadyRegistered;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializare();

        tv_alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String fullName = et_fullName.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    et_email.setError("Invalid E-mail");
                    et_email.setFocusable(true);
                } else if (password.length() < 6) {
                    et_password.setError("The password is too short!");
                    et_password.setFocusable(true);
                } else if (fullName.length() < 3) {
                    et_fullName.setError("The full name is too short!");
                    et_fullName.setFocusable(true);
                }else{
                    registerUser(email,password,fullName);
                }
            }
        });
    }

    private void registerUser(String email, String password,String fullName) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = auth.getCurrentUser();


                    String email = user.getEmail();
                    String uid = user.getUid();
                    HashMap<Object,String> hashMap= new HashMap<>();

                    hashMap.put("uid",uid);
                    hashMap.put("email",email);
                    hashMap.put("fullName",fullName);
                    hashMap.put("image","");

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference("Users");
                    reference.child(uid).setValue(hashMap);

                    Toast.makeText(RegisterActivity.this, "Authentication was a success.",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), AfterLoginActivity.class));
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void initializare(){
        btn_register=findViewById(R.id.registerActivity_btn_register);
        et_password=findViewById(R.id.registerActivity_et_Password);
        et_email=findViewById(R.id.registerActivity_et_Email);
        et_fullName=findViewById(R.id.registerActivity_et_FullName);
        tv_alreadyRegistered=findViewById(R.id.registerActivity_tv_AlreadyRegistered);
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}