package com.example.appfcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginActivity extends AppCompatActivity {

    EditText mEdtTk, mEdtMk;
    Button mBtnDangky, mBtnDangNhap;
    TextView mTvThongTin;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdtMk = findViewById(R.id.edittextMatKhau);
        mEdtTk = findViewById(R.id.edittextTaiKhoan);
        mBtnDangky = findViewById(R.id.buttonDangKy);
        mBtnDangNhap = findViewById(R.id.buttonDangNhap);
        mTvThongTin = findViewById(R.id.textViewThongTin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();


        mBtnDangky.setOnClickListener(view -> {
            String tk = mEdtTk.getText().toString();
            String mk = mEdtMk.getText().toString();
            mAuth.createUserWithEmailAndPassword(tk, mk)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        mBtnDangNhap.setOnClickListener(view -> {
            String tk = mEdtTk.getText().toString();
            String mk = mEdtMk.getText().toString();
            mAuth.signInWithEmailAndPassword(tk, mk)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                updateUI(user);
                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                mRef.child("user").child(user.getUid()).setValue(new User(task.getResult().getToken(),user.getEmail()));
                                            }
                                        });
                            } else {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null){
            mTvThongTin.setVisibility(View.GONE);
            return;
        }
        mTvThongTin.setText("Email : " + user.getEmail() + "\n" + "Uid : " + user.getUid());
    }
}