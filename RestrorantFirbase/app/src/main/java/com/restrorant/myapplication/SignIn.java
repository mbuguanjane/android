package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.restrorant.myapplication.Model.User;

public class SignIn extends AppCompatActivity {
       EditText editPhone,editPassword;
       Button BtnSignIn;
       TextView BtnSignUp;
       TextView forgotpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editPhone=(EditText)findViewById(R.id.editPhone);
        editPassword=(EditText)findViewById(R.id.editPassword);
        BtnSignIn=(Button)findViewById(R.id.BtnSignIn);
        BtnSignUp=(TextView)findViewById(R.id.BtnSignUp);
        forgotpassword=(TextView)findViewById(R.id.forgotpassword);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignIn.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
        BtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty())
                {
                    ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Processing....");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(editPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            User user = snapshot.child(editPhone.getText().toString()).getValue(User.class);
                            user.setPhone(editPhone.getText().toString());
                            if (user.getPassword().equals(editPassword.getText().toString())) {
                                //Toast.makeText(SignIn.this, "Login Success", Toast.LENGTH_SHORT).show();
                                Common.currentUser = user;
                                if (user.getUserLevel().equals("Admin")) {
                                    Intent intent = new Intent(SignIn.this, AdminPanel.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(SignIn.this, Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                //Toast.makeText(SignIn.this, "Login Failed !!!", Toast.LENGTH_SHORT).show();
                                makeAlert(SignIn.this, "Error", "Wrong Password !!!");
                            }
                        } else {
                            mDialog.dismiss();
                            makeAlert(SignIn.this, "Error", "User does not exist in Database !!!");
                            //Toast.makeText(SignIn.this, "User does not exist in Database !!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                       makeAlert(SignIn.this,"Failed","Error Login"+error.getMessage());
                    }
                });
              }
            }
        });
    }
    public void makeAlert(Context context, String Title, String Message )
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(Title);
        builder1.setMessage(Message);
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.ic_baseline_info_24);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public boolean checkEmpty()
    {
        if(editPhone.getText().toString().isEmpty())
        {
            makeAlert(SignIn.this, "Error", "Phone is Required" );
            return false;
        }else if(editPassword.getText().toString().isEmpty())
        {
            makeAlert(SignIn.this, "Error", "Password is Required" );
            return false;
        }

        return true;
    }

}