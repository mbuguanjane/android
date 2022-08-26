package com.rodikenya.rodiseriou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;
import com.nex3z.notificationbadge.NotificationBadge;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rodikenya.rodiseriou.Adaptor.CategoryAdaptor;
import com.rodikenya.rodiseriou.Adaptor.CentrePricingAdaptor;
import com.rodikenya.rodiseriou.Adaptor.TrainingMenuAdaptor;
import com.rodikenya.rodiseriou.Model.Banner;
import com.rodikenya.rodiseriou.Model.Category;
import com.rodikenya.rodiseriou.Model.CentreModel;
import com.rodikenya.rodiseriou.Model.Products;
import com.rodikenya.rodiseriou.Model.TrainingMenuModel;
import com.rodikenya.rodiseriou.Model.User;
import com.rodikenya.rodiseriou.Remote.IpService;
import com.rodikenya.rodiseriou.RoomDatabase.Database.CartRepository;
import com.rodikenya.rodiseriou.RoomDatabase.Database.FavouriteRepository;
import com.rodikenya.rodiseriou.RoomDatabase.Local.FavouriteDatasource;
import com.rodikenya.rodiseriou.RoomDatabase.Local.RodiDatabaseProj;
import com.rodikenya.rodiseriou.RoomDatabase.Local.CartDatasource;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;
import com.squareup.picasso.Picasso;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    IpService ipService;
    AlertDialog alertDialog;
    NotificationBadge badge;
    RecyclerView recyclerView,recycle_training,recycle_centre;
    SliderLayout sliderlayout;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    CircleImageView img_avatar;
    int CAMERA=2,GALLERY=1;
    AlertDialog waitingDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseAuth mAuth;
    String selectedUrl=null;
    String ImageUrl=null;
    String CodeSent,mediaPath;
    Button signIn;
    String Verifiedphone;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        try {
            mAuth = FirebaseAuth.getInstance();
            drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
            navigationView = (NavigationView) findViewById(R.id.navigationview);
            navigationView.setNavigationItemSelectedListener(this);
            View view = navigationView.getHeaderView(0);

            name = (TextView) view.findViewById(R.id.Usernamehere);
            toolbar = (Toolbar) findViewById(R.id.Toolbar);
            img_avatar = (CircleImageView) view.findViewById(R.id.Imageavatar);
            img_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Common.CurrentUser != null) {
                        getImage();
                    } else {
                        Toast.makeText(DashBoard.this, "Please Login First", Toast.LENGTH_LONG).show();
                    }
                }
            });
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);

            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    getBannerImages();
                    //Menu here
                    getCategoryMenu();

                    // UpdateFirebaseToken();
                    LoadCentrePricing();
                    LoadTraining();
                    if (Common.CurrentUser != null) {
                        // UpdateFirebaseToken();
                    }
                }
            });
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getBannerImages();
                    //Menu here
                    getCategoryMenu();

                }
            });
            swipeRefreshLayout.setEnabled(false);
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            sliderlayout = (SliderLayout) findViewById(R.id.Slider);
            ipService = Common.getIpService();
            recyclerView = (RecyclerView) findViewById(R.id.Lst_menu);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            //training
            recycle_training = (RecyclerView) findViewById(R.id.Trainingservice);
            recycle_training.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recycle_training.setHasFixedSize(true);
            //Hiring Services
            recycle_centre = (RecyclerView) findViewById(R.id.HallHire);
            recycle_centre.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recycle_centre.setHasFixedSize(true);

            alertDialog = new SpotsDialog(DashBoard.this);

            getBannerImages();
            //Menu here
            getCategoryMenu();

            // UpdateFirebaseToken();
            LoadCentrePricing();
            LoadTraining();
            initDB();
            AutoLogin();
        }catch (Exception Ex)
        {
            Toast.makeText(getApplicationContext(),"Error "+Ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void AutoLogin() {



        //login session
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
           LoginWithServer(user.getPhoneNumber());
           //
           System.out.println("Phone "+user.getPhoneNumber());
        } else {
            // User is signed out

        }
        //end session
    }

    private void UpdateFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                ipService.UpdateFireBaseToken(Common.CurrentUser.getPhone(), FirebaseInstanceId.getInstance().getToken(),"0")
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                System.out.println("Token Back "+response.body().toString());
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                System.out.println("Failed "+t.getMessage());
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DashBoard.this,"Token Failed "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getImage() {
        AlertDialog.Builder picturedialog=new AlertDialog.Builder(this);
        picturedialog.setTitle("Select Action");
                String[] pictureDialogItems={"Camera","Photo Gallery"};
        picturedialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                       // getfromCamera();
                        break;
                    case 1:
                        getfromGallery();
                        break;
                }
            }
        });
        picturedialog.show();
    }

    private void getfromGallery() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,GALLERY);
    }

    private void getfromCamera() {
           Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           startActivityForResult(intent,CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==this.RESULT_CANCELED)
        {
            Toast.makeText(DashBoard.this,"user  cancelled ",Toast.LENGTH_LONG).show();
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA || requestCode == GALLERY) {
                if (data != null) {
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    // Set the Image in ImageView for Previewing the Media
                    img_avatar.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                    cursor.close();
                    selectedUrl=mediaPath;
                    System.out.println("Urlimage "+selectedUrl);
                    uploadToServer(selectedUrl);
                }
            }

        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }
    private void uploadToServer(String filePath) {
        if(!TextUtils.isEmpty(selectedUrl)) {
            System.out.println("found back"+selectedUrl);
            //Create a file object using file path
            final File file = new File(filePath);
            // Create a request body with file and image media type
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            // Create MultipartBody.Part using file request-body,file name and part name
            MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", file.getName(), fileReqBody);
            //Create request body with text description and text media type
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
            //
            ipService.getUploadAvatorFile(part)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            ImageUrl=new StringBuilder("Images/")
                                    .append(file.getName())
                                    .toString();
                            System.out.println("Success "+ImageUrl);
                            System.out.println("product name"+response.body().toString());
                           UpdateAvator(Common.CurrentUser.getPhone(),ImageUrl);

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            System.out.println("Failed "+t.getMessage());
                        }
                    });
        }else
        {
            Toast.makeText(DashBoard.this,"File Path is Empty",Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateAvator(String phone, String imageUrl) {

        ipService.UpdateAvator(imageUrl,phone)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(DashBoard.this,"Avator Updated Success",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(DashBoard.this,"Failed to Update "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void LoadData()
    {
        Disposable disposable=Common.cartRepository.getAllCart()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> cart) throws Exception {
                        if(cart.size()>0)
                        {
                            badge.setText(String.valueOf(cart.size()));
                            badge.setVisibility(View.VISIBLE);

                        }else
                        {
                            badge.setVisibility(View.GONE);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(DashBoard.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Toast.makeText(DashBoard.this, "Loaded Successfully " , Toast.LENGTH_LONG).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void initDB() {
        Common.rodiDatabaseProj = RodiDatabaseProj.getInstance(this);
        Common.cartRepository= CartRepository.getInstance(CartDatasource.getInstance(Common.rodiDatabaseProj.cartDAO()));
        Common.FavouriteRepository= FavouriteRepository.getInstance(FavouriteDatasource.getInstance(Common.rodiDatabaseProj.favDAO()));
    }

    private void getBannerImages() {

        compositeDisposable.add(ipService.getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                               @Override
                               public void accept(List<Banner> banners) throws Exception {

                                   displayImage(banners);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(DashBoard.this, "Error occurred " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                               }
                           }
                )
        );

    }
    //menu
    private void getCategoryMenu() {

        compositeDisposable.add(ipService.getMenu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                               @Override
                               public void accept(List<Category> categories) throws Exception {
                                   //displayMenu(categories);
                                   displayMenu(categories);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(DashBoard.this, "Error occurred " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                               }
                           }

                ));


    }
    private void displayMenu(List<Category> categories) {
        CategoryAdaptor adaptor=new CategoryAdaptor(this,categories);
        recyclerView.setAdapter(adaptor);
        swipeRefreshLayout.setRefreshing(false);
    }
    //trainng Services
    private void LoadTraining() {

        compositeDisposable.add(ipService.getTrainingMenu()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<TrainingMenuModel>>() {
                    @Override
                    public void accept(List<TrainingMenuModel> trainingMenuModels) throws Exception {
                        LoadTraining(trainingMenuModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(DashBoard.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        );

    }

    private void LoadTraining(List<TrainingMenuModel> trainingMenus) {
        TrainingMenuAdaptor adaptor=new TrainingMenuAdaptor(DashBoard.this,trainingMenus);
        recycle_training.setAdapter(adaptor);

    }
    //Centre hiring
    private void LoadCentrePricing() {

        compositeDisposable.add(ipService.FetchCentrePricing()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<CentreModel>>() {
                    @Override
                    public void accept(List<CentreModel> centreModels) throws Exception {
                        loadList(centreModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(DashBoard.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        );

    }

    private void loadList(List<CentreModel> centreModels) {
        CentrePricingAdaptor adaptor=new CentrePricingAdaptor(DashBoard.this,centreModels);
        recycle_centre.setAdapter(adaptor);

    }

    private void displayImage(List<Banner> banners) {
        HashMap<String,String> bannerMap=new HashMap<>();
        for(Banner item:banners)
        {
            bannerMap.put(item.getName(),Common.BASE_URL+item.getLink());
        }
        for(String name:bannerMap.keySet())
        {
            TextSliderView textSliderView=new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderlayout.addSlider(textSliderView);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();

        super.onDestroy();
    }
    boolean isBackButtonClicked=false;

    @Override
    protected void onResume() {


        this.isBackButtonClicked=false;
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawers();
        }else{
            if(isBackButtonClicked) {
                super.onBackPressed();
                return;
            }
            this.isBackButtonClicked=true;
            Toast.makeText(DashBoard.this,"Please Clicked Again To Exit ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu,menu);
        View view =menu.findItem(R.id.cartoon).getActionView();
        badge=(NotificationBadge)view.findViewById(R.id.badge);
        badge.setText("1");
        badge.setVisibility(View.VISIBLE);
        ImageView img=(ImageView)view.findViewById(R.id.cart_icon);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoard.this,CartActivity.class));
            }
        });
        LoadData();

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.cartoon:
                startActivity(new Intent(DashBoard.this,CartActivity.class));
                break;
            case R.id.search:
                startActivity(new Intent(DashBoard.this,SearchActivity.class));
                break;
            case R.id.URL:
                createURl();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createURl() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashBoard.this);
        alertDialog.setTitle("Server URL");
        alertDialog.setMessage("Enter server url ");

        final EditText input = new EditText(DashBoard.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Common.BASE_URL = input.getText().toString();

                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();

        //alertDialog.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        switch (id)
        {
            case R.id.FavouritMenu:
                Intent intent=new Intent(DashBoard.this,FavouriteListActivity.class);
                startActivity(intent);
                break;
            case R.id.products:
                Intent prod=new Intent(DashBoard.this,DashBoard.class);
                startActivity(prod);
                break;
            case R.id.register:
                  RegisterUser();
                break;
            case R.id.Login:
                LoginUser();
                break;
            case R.id.OrderItems:
                startActivity(new Intent(DashBoard.this,ShowOrderActivity.class));
                break;
            case R.id.TrainingOrder:
                startActivity(new Intent(DashBoard.this,TrainingOrders.class));
                break;
            case R.id.centrebooking:
                startActivity(new Intent(DashBoard.this,CentreHallOrder.class));
                break;
            case R.id.Ourlocation:
                //startActivity(new Intent(DashBoard.this,MapDirection.class));
                startActivity(new Intent(DashBoard.this,MyLocation.class));
                break;
            case R.id.aboutus:
                startActivity(new Intent(DashBoard.this,Introduction.class));
                break;
            case R.id.Logout:
               LogoutUser();
                break;


        }
           drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void LogoutUser() {



        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Exit Application");
        builder.setMessage("Do you want To Exit");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent=new Intent(DashBoard.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        FirebaseAuth.getInstance().signOut();
                        finish();

                    }
                });
        builder.show();

    }

    private void LoginUser() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(DashBoard.this);
        builder.setTitle("Login");

        LayoutInflater inflater=this.getLayoutInflater();
        View register_layout=inflater.inflate(R.layout.login_layout,null);
        final MaterialEditText edt_phone=(MaterialEditText)register_layout.findViewById(R.id.edt_Phone);
        final MaterialEditText edt_Code=(MaterialEditText)register_layout.findViewById(R.id.edt_phonecode);
        Button verifybutton=(Button)register_layout.findViewById(R.id.verifyBtn);
        final CountryCodePicker Countrycode=(CountryCodePicker)register_layout.findViewById(R.id.countrycode);
         signIn=(Button)register_layout.findViewById(R.id.Loginbttn);
        builder.setView(register_layout);
        final AlertDialog alertDialog=builder.create();

        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if( TextUtils.isEmpty(edt_phone.getText().toString()))
                {
                    Toast.makeText(DashBoard.this,"Phone missing",Toast.LENGTH_LONG).show();
                }

                else  if(edt_phone.getText().toString().length()<9)
                {
                    Toast.makeText(DashBoard.this,"Invalid Phone Number",Toast.LENGTH_LONG).show();
                }else {
                    waitingDialog=new SpotsDialog(DashBoard.this);
                    waitingDialog.show();
                    waitingDialog.setMessage("Login in");

                    // System.out.println("Phonenumber= "+Countrycode.getFullNumberWithPlus());
                    String phonenumber="";
                    String countrycode = Countrycode.getDefaultCountryCodeWithPlus();
                    if(edt_phone.getText().toString().startsWith("0"))
                    {

                        phonenumber = countrycode + edt_phone.getText().toString().substring(1);
                    }else {

                         phonenumber = countrycode + edt_phone.getText().toString();
                    }
                    //LoginWithServer(phonenumber);
                    SendVerificationCode(phonenumber);
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edt_Code.getText().toString()))
                {
                    Toast.makeText(DashBoard.this,"Code missing",Toast.LENGTH_LONG).show();
                }else {
                    alertDialog.dismiss();
                    String codeEntered = edt_Code.getText().toString();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent, codeEntered);
                    CheckCredential(credential);
                }
            }
        });
        alertDialog.show();
    }

    private void CheckCredential(PhoneAuthCredential credential) {

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = task.getResult().getUser();
                                Verifiedphone=user.getPhoneNumber();
                                System.out.println("Success Login welcome "+Verifiedphone);
                                Toast.makeText(DashBoard.this,"Login Successful",Toast.LENGTH_LONG).show();
                                LoginWithServer(Verifiedphone);
                                // ...
                            } else {
                                // Sign in failed, display a message and update the UI
                                System.out.println("Error "+task.getException());
                                Toast.makeText(DashBoard.this,"Failed login "+task.getException(),Toast.LENGTH_LONG).show();
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                }
                            }
                        }
                    });

    }

    private void LoginWithServer(String verifiedphone) {
        ipService.LoginUser(verifiedphone).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                   System.out.println("tunizo "+response.body().toString());
                if(TextUtils.isEmpty(response.body().getError_msg()))
                {
                    Common.CurrentUser=response.body();
                    UpdateFirebaseToken();
                    if(Common.CurrentUser!=null) {
                        name.setText(Common.CurrentUser.getName());
                        if(!TextUtils.isEmpty(Common.CurrentUser.getAvatarLink())) {
                            Picasso.with(DashBoard.this).load(Common.BASE_URL+Common.CurrentUser.getAvatarLink()).into(img_avatar);
                            //img_avatar.setImageURI(Uri.parse(Common.CurrentUser.getAvatarLink()));
                            System.out.println("User Avator" + Common.CurrentUser.getAvatarLink());
                        }

                    }
                    Toast.makeText(DashBoard.this,"Login Successful "+response.body().getName(),Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(DashBoard.this,"Failed to login"+response.body().getError_msg(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(DashBoard.this,"Failed "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void SendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
             Toast.makeText(DashBoard.this,"Successful Verification",Toast.LENGTH_LONG).show();
            waitingDialog.dismiss();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            signIn.setEnabled(false);
            Toast.makeText(DashBoard.this,"Failed Sending Code "+e.getMessage(),Toast.LENGTH_LONG).show();
            System.out.println("error "+e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            CodeSent=s;
            signIn.setEnabled(true);
        }
    };


    private void RegisterUser() {
             final AlertDialog.Builder builder=new AlertDialog.Builder(DashBoard.this);
        builder.setTitle("Register");

        LayoutInflater inflater=this.getLayoutInflater();
        View register_layout=inflater.inflate(R.layout.regitser_layout,null);
        final MaterialEditText edt_name=(MaterialEditText)register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_address=(MaterialEditText)register_layout.findViewById(R.id.edt_Address);
        final MaterialEditText edt_birthdate=(MaterialEditText)register_layout.findViewById(R.id.edt_birthdate);
        final MaterialEditText edt_phone=(MaterialEditText)register_layout.findViewById(R.id.edt_Phone);
        edt_birthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        final CountryCodePicker Countrycode=(CountryCodePicker)register_layout.findViewById(R.id.countrycode);
        builder.setView(register_layout);
        final AlertDialog alertDialog=builder.create();
        Button registerbutton=(Button)register_layout.findViewById(R.id.Registerbttn);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

               if( TextUtils.isEmpty(edt_name.getText().toString()))
                {
                    Toast.makeText(DashBoard.this,"Name missing",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(edt_address.getText().toString()))
                {
                    Toast.makeText(DashBoard.this,"Address missing",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(edt_birthdate.getText().toString()))
                {
                    Toast.makeText(DashBoard.this,"Birth date missing",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(edt_phone.getText().toString()))
                {
                    Toast.makeText(DashBoard.this,"Phone number is missing",Toast.LENGTH_LONG).show();
                }else {
                   ipService.RegisterUser(Countrycode.getDefaultCountryCodeWithPlus() + edt_phone.getText().toString(),
                           edt_name.getText().toString(),
                           edt_birthdate.getText().toString(),
                           edt_address.getText().toString())
                           .enqueue(new Callback<User>() {
                               @Override
                               public void onResponse(Call<User> call, Response<User> response) {

                                   Common.CurrentUser = response.body();
                                   User user = response.body();
                                   UpdateFirebaseToken();
                                   if (TextUtils.isEmpty(user.getError_msg())) {

                                       Toast.makeText(DashBoard.this, "Registered Successfully " + response.body(), Toast.LENGTH_LONG).show();
                                   } else {
                                       Toast.makeText(DashBoard.this, "Registration Failed " + response.body(), Toast.LENGTH_LONG).show();
                                   }
                               }

                               @Override
                               public void onFailure(Call<User> call, Throwable t) {
                                   Toast.makeText(DashBoard.this, "Failed  " + t.getMessage(), Toast.LENGTH_LONG).show();
                                   System.out.println("Error " + t.getMessage());
                               }
                           });
               }
            }
        });
         alertDialog.show();
    }

}
