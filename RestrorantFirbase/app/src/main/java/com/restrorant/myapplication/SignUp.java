package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.restrorant.myapplication.Model.Request;
import com.restrorant.myapplication.Model.User;
import com.restrorant.myapplication.Retrofit.MpesaInterface;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;
import com.restrorant.myapplication.ViewHolder.OrderDetailAdaptor;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    EditText editFirstName,etSecondName,etLastName,etPhone,etEmail,editPassword,etConfirmPassword;
    Button BtnSignUp;
    TextView SigIn;
    EditText editText;
    ProgressDialog progressDialog;
    AlertDialog alert;

    String verifyingtoken="0000";
    //firebase auth object
    private FirebaseAuth mAuth;
    private String mVerificationId;
     FirebaseDatabase database;
     MpesaInterface mpesaInterface;
     DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editFirstName=(EditText)findViewById(R.id.editFirstName);
        etSecondName=(EditText)findViewById(R.id.etSecondName);
        etLastName=(EditText)findViewById(R.id.etLastName);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etEmail=(EditText)findViewById(R.id.etEmail);
        editPassword=(EditText)findViewById(R.id.editPassword);
        etConfirmPassword=(EditText)findViewById(R.id.etConfirmPassword);

        mAuth = FirebaseAuth.getInstance();
         database=FirebaseDatabase.getInstance();
         table_user=database.getReference("User");
        SigIn=(TextView)findViewById(R.id.SigIn);
        mpesaInterface=Common.getMpesa();



        SigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignIn.class));
                finish();
            }
        });
        BtnSignUp=(Button)findViewById(R.id.BtnSignUp);
        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmpty()) {
                    Random rand = new Random();
                    verifyingtoken = String.format("%04d", rand.nextInt(10000));
                    System.out.println("Random number "+verifyingtoken);

                    SendMessage(etPhone.getText().toString(),verifyingtoken);

                }

            }
        });
    }

    public boolean checkEmpty()
    {
      if(editFirstName.getText().toString().isEmpty())
      {
          makeAlert(SignUp.this, "Error", "First Name is Required" );
          return false;
      }else if(etSecondName.getText().toString().isEmpty())
        {
            makeAlert(SignUp.this, "Error", "Second Name is Required" );
            return false;
        }
      else if(etLastName.getText().toString().isEmpty())
      {
          makeAlert(SignUp.this, "Error", "Last Name is Required" );
          return false;
      }
       else if(etEmail.getText().toString().isEmpty())
        {
            makeAlert(SignUp.this, "Error", "Email is Required" );
            return false;
        }
      else if(etPhone.getText().toString().isEmpty())
      {
          makeAlert(SignUp.this, "Error", "Phone is Required" );
          return false;
      }else if(!editPassword.getText().toString().equals(etConfirmPassword.getText().toString())&& editPassword.getText().toString().length()<6)
      {
          makeAlert(SignUp.this, "Error", "Password did not Match" );
          return false;
      }
      return true;
    }

    public void SignUpwithFirebase()
    {

        final ProgressDialog progressDialog=new ProgressDialog(SignUp.this);
        progressDialog.setMessage("Please waiting");
        progressDialog.show();
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(etPhone.getText().toString()).exists())
                {
                    progressDialog.dismiss();
                    //Toast.makeText(SignUp.this, "User already exist", Toast.LENGTH_SHORT).show();
                    makeAlert(SignUp.this,"Error","User already exist");
                }else
                {
                    //User user=new User(editName.getText().toString(),editPassword.getText().toString(),"254"+editPhone.getText().toString(),"User");

                    User user=new User(editFirstName.getText().toString(),
                            etSecondName.getText().toString(),
                            etLastName.getText().toString(),
                            etPhone.getText().toString(),
                            etEmail.getText().toString(),
                            "User",
                            etConfirmPassword.getText().toString());
                    table_user.child(etPhone.getText().toString()).setValue(user);
                    Common.currentUser=user;
                    if(user.getUserLevel().equals("Admin"))
                    {
                        Intent intent=new Intent(SignUp.this,AdminPanel.class);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Intent intent=new Intent(SignUp.this,Dashboard.class);
                        intent.putExtra("mobile",etPhone.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                    //Toast.makeText(SignUp.this, "Registered successfully!!", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(SignUp.this, "Registered successfully!!", Toast.LENGTH_LONG);
                    toast.getView().setBackgroundColor(Color.parseColor("#8BC34A"));
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               // Toast.makeText(SignUp.this, "Registration Failed !!", Toast.LENGTH_SHORT).show();
                makeAlert(SignUp.this,"Failed","Registration Failed!!!");
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
//    private void showVerificationDialog() {
//        AlertDialog.Builder alertdialog=new AlertDialog.Builder(SignUp.this);
//        LayoutInflater inflater=this.getLayoutInflater();
//        View add_menu_layout=inflater.inflate(R.layout.verificationlayout,null);
//        EditText editTextCode=(EditText) add_menu_layout.findViewById(R.id.editTextCode);
//        Button buttonVerify=(Button) add_menu_layout.findViewById(R.id.buttonVerify);
//        TextView textView=(TextView)findViewById(R.id.Messagehere);
//        buttonVerify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                 if(editTextCode.getText().toString().equals(verifyingtoken))
//                 {
//                     //textView.setText("Verification Success");
//                     alert.dismiss();
//
//                 }else
//                 {
////                     textView.setText("Verification Failed");
////                     textView.setTextColor(Color.RED);
//                     makeAlert(SignUp.this,"Success","Verification Success");
//                 }
//            }
//        });
//        alertdialog.setView(add_menu_layout);
//        alertdialog.setIcon(R.drawable.ic_baseline_order);
//        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.dismiss();
//
//            }
//        });
//        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        alert = alertdialog.create();
//    }
//    private  void  showVerify()
//    {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//
//        // ...Irrelevant code for customizing the buttons and title
//
//        LayoutInflater inflater = this.getLayoutInflater();
//
//        View dialogView= inflater.inflate(R.layout.verificationlayout, null);
//        dialogBuilder.setView(dialogView);
//
//        Button button = (Button)dialogView.findViewById(R.id.buttonVerify);
//         editText = (EditText)
//                dialogView.findViewById(R.id.editTextCode);
//        TextView Messagehere = (TextView)
//                dialogView.findViewById(R.id.Messagehere);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(editText.getText().toString().equals(verifyingtoken))
//                {
//                    //Messagehere.setText("Verification Success");
//                    makeAlert(SignUp.this,"Success","Verification Success");
//                    SignUpwithFirebase();
//                }else
//                {
////                    Messagehere.setText("Verification Failed");
////                    Messagehere.setTextColor(Color.RED);
//                    makeAlert(SignUp.this,"Failed","Verification Failed");
//                }
//            }
//        });
//
//        dialogBuilder.show();
//
//    }
     private void SendMessage(String phone,String message)
    {
        progressDialog=new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Processing");
                progressDialog.show();
        mpesaInterface.forgotPassword("+"+phone,message).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                  progressDialog.dismiss();

                showAlert();
                //makeAlert(SignUp.this,"Success ","Success "+response.body().toString());


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Intent success=new Intent(SignUp.this,FailedActivity.class);
                success.putExtra("Title","Failed");
                success.putExtra("Description","Changing Failed!");
                success.putExtra("Class","SignIn");
                startActivity(success);
                //Toast.makeText(ForgotPassword.this,"Forgot password Failed! "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUp.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View alertView = inflater.inflate(R.layout.verificationlayout, null);
        alertDialog.setView(alertView);

        final AlertDialog show = alertDialog.show();

        Button alertButton = (Button) alertView.findViewById(R.id.buttonVerify);
        EditText editTextCode = (EditText) alertView.findViewById(R.id.editTextCode);
        TextView Messagehere = (TextView) alertView.findViewById(R.id.Messagehere);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextCode.getText().toString().equals(verifyingtoken))
                {
                    Messagehere.setText("Verification Success");
                    //
                    show.dismiss();
                    SignUpwithFirebase();
                    //makeAlert(SignUp.this,"Success","Verification Success");
                }else
                {
                    Messagehere.setText("Verification Failed");
                    Messagehere.setTextColor(Color.RED);

                   // makeAlert(SignUp.this,"Failed","Verification Failed");
                }


            }
        });
    }
}