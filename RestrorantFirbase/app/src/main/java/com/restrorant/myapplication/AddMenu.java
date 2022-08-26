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
import com.restrorant.myapplication.Model.MenuModel;
import com.restrorant.myapplication.ViewHolder.AddMenuViewHolder;
import com.restrorant.myapplication.ViewHolder.CategoryViewHolder;
import com.restrorant.myapplication.ViewHolder.MenuListViewHolder;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class AddMenu extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView fullname; //username
    RecyclerView recyclerMenu;
    FirebaseRecyclerAdapter<MenuModel, AddMenuViewHolder> adapter;
    FloatingActionButton fab;
    MaterialEditText editName;
    FButton BtnSelect,BtnUpload;
    MenuModel menuModel;
    String RestorautId="";
    Uri saveURI;
    private final int PICK_IMAGE_REQUEST=71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        database= FirebaseDatabase.getInstance();


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
        if(getIntent()!=null)
        {
            RestorautId=getIntent().getStringExtra("RestourantID");
            category=database.getReference("Menu/"+RestorautId);

        }
        if(!RestorautId.isEmpty() && RestorautId!=null)
        {
            loadMenu(RestorautId);

        }

    }

    private void showDialog() {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(AddMenu.this);
        alertdialog.setTitle("Add new Menu");
        alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_menu_layout,null);
        editName=add_menu_layout.findViewById(R.id.editName);
        BtnSelect=add_menu_layout.findViewById(R.id.BtnSelect);
        BtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddMenu.this, "yes man", Toast.LENGTH_SHORT).show();
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
                if(menuModel!=null)
                {
                    category.push().setValue(menuModel);
                }else{
                    Toast.makeText(AddMenu.this,"Menu is Empty",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AddMenu.this,"Uploaded successfully!",Toast.LENGTH_LONG).show();
                            BtnUpload.setText("Uploaded ");
                            BtnUpload.setBackgroundColor(Color.CYAN);
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(@NonNull Uri uri) {
                                    menuModel=new MenuModel(uri.toString());
                                    Toast.makeText(AddMenu.this,"Url "+uri.toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(AddMenu.this,"Failed to Upload "+e.getMessage(),Toast.LENGTH_LONG).show();
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
            Toast.makeText(AddMenu.this,"Failed Picking Image "+data.getData(),Toast.LENGTH_LONG).show();
        }
    }

    private void loadMenu(String restorautId) {

        adapter=new FirebaseRecyclerAdapter<MenuModel, AddMenuViewHolder>(MenuModel.class,R.layout.menurowlayout, AddMenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(AddMenuViewHolder menuViewHolder, MenuModel menuModel, int i) {
                Toast.makeText(AddMenu.this,"We are here "+category,Toast.LENGTH_LONG).show();
               // menuViewHolder.menuname.setText(category.getName());
                Picasso.with(getBaseContext()).load(menuModel.getImage()).into(menuViewHolder.imageView);
                MenuModel clickItem=menuModel;
                menuViewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean loongClick) {

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
                    Toast.makeText(AddMenu.this,"Image Deleted successfully "+saveURI,Toast.LENGTH_LONG).show();
                }
            });
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        category.child(key).removeValue();

    }

    private void showUpdateDialog(String key, MenuModel item) {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(AddMenu.this);
        alertdialog.setTitle("Update Menu");
        alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_menu_layout,null);
        editName=add_menu_layout.findViewById(R.id.editName);
        BtnSelect=add_menu_layout.findViewById(R.id.BtnSelect);
        BtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(AddMenu.this, "yes man", Toast.LENGTH_SHORT).show();
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
                category.child(key).setValue(item);

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

    private void changeImage(MenuModel item) {
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
                            Toast.makeText(AddMenu.this,"Uploaded successfully!",Toast.LENGTH_LONG).show();
                            BtnUpload.setText("Uploaded ");
                            BtnUpload.setBackgroundColor(Color.CYAN);
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(@NonNull Uri uri) {
                                    menuModel=new MenuModel(uri.toString());
                                    item.setImage(uri.toString());
                                    //Toast.makeText(AddMenu.this,"Url "+uri.toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(AddMenu.this,"Failed to Upload "+e.getMessage(),Toast.LENGTH_LONG).show();
                            makeAlert(AddMenu.this,"Failed","Failed to Upload"+e.getMessage());
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