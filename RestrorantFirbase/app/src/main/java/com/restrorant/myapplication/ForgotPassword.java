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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.restrorant.myapplication.Model.User;
import com.restrorant.myapplication.Retrofit.MpesaInterface;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    EditText editPhone;
    Button BtnretrievePass;
    MpesaInterface mpesaInterface;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mpesaInterface=Common.getMpesa();
        editPhone=(EditText)findViewById(R.id.editPhone);

        BtnretrievePass=(Button)findViewById(R.id.BtnretrievePass);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        BtnretrievePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(ForgotPassword.this);
                progressDialog.setMessage("Processing");
                progressDialog.show();
                if(checkEmpty())
                {
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.child(editPhone.getText().toString()).exists())
                            {
                                progressDialog.dismiss();
                                User user=snapshot.child(editPhone.getText().toString()).getValue(User.class);

                                    SendMessage(editPhone.getText().toString(),user.getPassword());
                            }else{
                                progressDialog.dismiss();
                                makeAlert(ForgotPassword.this, "An Error","User does not exist in Database !!!" );
                                //Toast.makeText(ForgotPassword.this, "User does not exist in Database !!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
    private void SendMessage(String phone,String Password)
    {
        mpesaInterface.forgotPassword("+"+phone,Password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                Intent success=new Intent(ForgotPassword.this,SuccessActivity.class);
                success.putExtra("Title","Success");
                success.putExtra("Description","Check the password on your Phone");
                startActivity(success);
                makeAlert(ForgotPassword.this,"Success","Check your password on the phone");
                //Toast.makeText(ForgotPassword.this,"Check your password on the phone "+response.body().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Intent success=new Intent(ForgotPassword.this,FailedActivity.class);
                success.putExtra("Title","Failed");
                success.putExtra("Description","Changing Failed!");
                success.putExtra("Class","SignIn");
                startActivity(success);
                //Toast.makeText(ForgotPassword.this,"Forgot password Failed! "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private  boolean checkEmpty()
    {
        if(editPhone.getText().toString().isEmpty())
        {
            //Toast.makeText(this,"Phone is Empty",Toast.LENGTH_LONG).show();
            makeAlert(ForgotPassword.this,"Error","Phone Empty");
            return false;
        }else {
            return true;
        }
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

//        builder1.setNegativeButton(
//                "No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}