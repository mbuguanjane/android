package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.Model.Category;
import com.restrorant.myapplication.Model.Food;
import com.restrorant.myapplication.Model.RestrorauntModel;
import com.restrorant.myapplication.ViewHolder.AddRestrourantViewHolder;
import com.restrorant.myapplication.ViewHolder.CategoryViewHolder;
import com.restrorant.myapplication.ViewHolder.RestrourantViewHolder;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class AddRestorant extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference Restourant;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView fullname; //username
    RecyclerView recyclerMenu;
    FirebaseRecyclerAdapter<RestrorauntModel, AddRestrourantViewHolder> adapter;
    FloatingActionButton fab;
    MaterialEditText editName,editReserveprice;
    EditText editDescription;
    FButton BtnSelect,BtnUpload;
    RestrorauntModel restrorauntModel;
    Uri saveURI;
    private final int PICK_IMAGE_REQUEST=71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restorant);
        database= FirebaseDatabase.getInstance();
        Restourant=database.getReference("Restaurants");

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        recyclerMenu=(RecyclerView)findViewById(R.id.recyclermenu);
        recyclerMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        loadMenu();
    }

    private void showDialog() {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(AddRestorant.this);
        alertdialog.setTitle("Add new Restaurants");
        alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.addrestrolayout,null);
        editName=add_menu_layout.findViewById(R.id.editName);
        editDescription=add_menu_layout.findViewById(R.id.editDescription);
        editReserveprice=add_menu_layout.findViewById(R.id.editReserveprice);
        BtnSelect=add_menu_layout.findViewById(R.id.BtnSelect);
        BtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddRestorant.this, "yes man", Toast.LENGTH_SHORT).show();
                chooseImage();
            }
        });
        BtnUpload=add_menu_layout.findViewById(R.id.BtnUpload);
        BtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_baseline_add_shopping_cart_24);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                if(restrorauntModel!=null)
                {
                    Restourant.push().setValue(restrorauntModel);
                }else{
                    makeAlert(AddRestorant.this,"Error","Restaurant is Empty");
                   // Toast.makeText(AddRestorant.this,"Restaurants is Empty",Toast.LENGTH_LONG).show();
                }
            }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();
    }

    private void uploadImage() {
        if(saveURI!=null)
        {
            ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName= UUID.randomUUID().toString();
            StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            //Toast.makeText(AddRestorant.this,"Uploaded successfully!",Toast.LENGTH_LONG).show();
                            makeAlert(AddRestorant.this,"Success","Uploaded Successfully");
                            BtnUpload.setText("Uploaded ");
                            BtnUpload.setBackgroundColor(Color.CYAN);
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(@NonNull Uri uri) {
                                    restrorauntModel=new RestrorauntModel(editName.getText().toString(),uri.toString(),editDescription.getText().toString(),Long.parseLong(editReserveprice.getText().toString()));
                                    //Toast.makeText(AddRestorant.this,"Url "+uri.toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                             makeAlert(AddRestorant.this,"Failed","Fail to upload"+e.getMessage());
                            //Toast.makeText(AddRestorant.this,"Failed to Upload "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress=(100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            mDialog.setMessage("Upload "+progress+" %");
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            saveURI=data.getData();
            BtnSelect.setText("Image Selected!");
        }else
        {
            makeAlert(AddRestorant.this,"Failed","Failed Picking Image");
           // Toast.makeText(AddRestorant.this,"Failed Picking Image "+data.getData(),Toast.LENGTH_LONG).show();
        }
    }

    private void loadMenu() {
        adapter=new FirebaseRecyclerAdapter<RestrorauntModel, AddRestrourantViewHolder>(RestrorauntModel.class,R.layout.menulayout,AddRestrourantViewHolder.class,Restourant) {
            @Override
            protected void populateViewHolder(AddRestrourantViewHolder menuViewHolder, RestrorauntModel category, int i) {
                menuViewHolder.menuname.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage()).into(menuViewHolder.imageView);
                RestrorauntModel clickItem=category;
                menuViewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean loongClick) {
                        Intent Menulist=new Intent(AddRestorant.this,AddMenu.class);
                        Menulist.putExtra("RestourantID",adapter.getRef(i).getKey());
                        startActivity(Menulist);
                       // Toast.makeText(AddRestorant.this,""+clickItem.getName(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        recyclerMenu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE))
        {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
            String Url=adapter.getItem(item.getOrder()).getImage();
            storageReference = storage.getReferenceFromUrl(Url);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    makeAlert(AddRestorant.this,"Success","Image Deleted successfully");
                    //Toast.makeText(AddRestorant.this,"Image Deleted successfully "+saveURI,Toast.LENGTH_LONG).show();
                }
            });
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        Restourant.child(key).removeValue();

    }

    private void showUpdateDialog(String key, RestrorauntModel item) {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(AddRestorant.this);
        alertdialog.setTitle("Update Restourant");
        alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.addrestrolayout,null);
        editReserveprice=add_menu_layout.findViewById(R.id.editReserveprice);
        editName=add_menu_layout.findViewById(R.id.editName);
        editDescription=add_menu_layout.findViewById(R.id.editDescription);
        editName.setText(item.getName());
        editReserveprice.setText(String.valueOf(item.getPrice()));
        editDescription.setText(item.getDescription());
        BtnSelect=add_menu_layout.findViewById(R.id.BtnSelect);
        BtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(AddRestorant.this, "yes man", Toast.LENGTH_SHORT).show();
                chooseImage();
            }
        });
        BtnUpload=add_menu_layout.findViewById(R.id.BtnUpload);
        BtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);

            }
        });
        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_baseline_add_shopping_cart_24);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                item.setName(editName.getText().toString());
                item.setDescription(editDescription.getText().toString());
                item.setPrice(Long.parseLong(editReserveprice.getText().toString()));
                Restourant.child(key).setValue(item);

            }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();
    }

    private void changeImage(RestrorauntModel item) {
        if(saveURI!=null)
        {
            ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName= UUID.randomUUID().toString();
            StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                           // Toast.makeText(AddRestorant.this,"Uploaded successfully!",Toast.LENGTH_LONG).show();
                            makeAlert(AddRestorant.this,"Success","Uploaded Successfully");
                            BtnUpload.setText("Uploaded");
                            BtnUpload.setBackgroundColor(Color.CYAN);
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(@NonNull Uri uri) {

                                    restrorauntModel=new RestrorauntModel(editName.getText().toString(),uri.toString(),editDescription.getText().toString(),Long.parseLong(editReserveprice.getText().toString()));
                                    item.setImage(uri.toString());
                                    item.setName(editName.getText().toString());
                                    item.setDescription(editDescription.getText().toString());

                                   // Toast.makeText(AddRestorant.this,"Url "+uri.toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            makeAlert(AddRestorant.this,"Failed","Failed to Upload");
                            //Toast.makeText(AddRestorant.this,"Failed to Upload "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress=(100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            mDialog.setMessage("Upload "+progress+" %");
                        }
                    });
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

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}