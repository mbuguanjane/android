package com.rodikenya.rodiseriou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rodikenya.rodiseriou.Adaptor.CartAdaptor;
import com.rodikenya.rodiseriou.Interface.OnItemClick;
import com.rodikenya.rodiseriou.Interface.RecyclerItemTouchHelper;
import com.rodikenya.rodiseriou.Interface.RecyclerItemTouchHelperListener;
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Model.Order;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;
import com.rodikenya.rodiseriou.Service.PrintDocumentAdaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener, OnItemClick {
    private static final int PAYMENT_REQUEST_CODE =7777 ;
    RecyclerView recyclerView_cart;
    Button Placeorder,printReceipt;
    public  TextView totalbill;
    CartAdaptor cartAdaptor;
    CompositeDisposable compositeDisposable;
    RelativeLayout rootLayout;
    IpService mService,mServiceScalar;
    float sumprice;
    String token,amount;
    String ProductTransport;
    int transportcost=0;
    int  count;
    List<Cart> cartList=new ArrayList<>();
    //declare mpesa here
    Daraja daraja;
    String Comments="",Addresses="",PaymentMethod="Credit Card";
    LatLng Origin;
    LatLng Destination;
    String dist,tym;
    HashMap<String,String> params;
    private FusedLocationProviderClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Placeorder=(Button)findViewById(R.id.placeorder);
        printReceipt=(Button)findViewById(R.id.PrintOrder);
        Dexter.withActivity(this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List< PermissionRequest > permissions, PermissionToken
                            token) {

                    }
                }).check();
        printReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             
            }
        });
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
        compositeDisposable=new CompositeDisposable();
        recyclerView_cart=(RecyclerView)findViewById(R.id.recyclecart);
        totalbill=(TextView)findViewById(R.id.TotalAmount);
        totalbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrice();
            }
        });

        recyclerView_cart.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_cart.setHasFixedSize(true);

        mService=Common.getIpService();
        mServiceScalar=Common.getScalarAPI();
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        //getting location

        //initialize Mpesa
        daraja = Daraja.with("Uku3wUhDw9z0Otdk2hUAbGZck8ZGILyh", "JDjpQBm5HpYwk38b", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(CartActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
                //Toast.makeText(CartActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(CartActivity.this.getClass().getSimpleName(), error);
            }
        });
        //end Initalize
        Placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getAddressFromGPS();
                placeOrder();
            }
        });
        ItemTouchHelper.SimpleCallback simpleCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView_cart);
        loadCartItems();
        getCount();
        getPrice();
        LoadBraintreeToken();
        //getAddressFromGPS();

        //getFirebaseToken();
    }
  //receive result payment from braintree
  private void requestPermission(){
      ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
  }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PAYMENT_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce methodNonce = result.getPaymentMethodNonce();
                String nonce = methodNonce.getNonce();
                if(sumprice>0)
                {
                    amount=String.valueOf(sumprice);
                    params=new HashMap<>();
                    params.put("amount",amount);
                    params.put("nonce",nonce);
                    sendPayment();
                }else{
                    Toast.makeText(CartActivity.this,"Payment amount is 0",Toast.LENGTH_LONG).show();
                }
            }else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(CartActivity.this,"User Cancelled",Toast.LENGTH_LONG).show();
            }else
            {
                Exception error=(Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Toast.makeText(CartActivity.this,"Error "+error.toString(),Toast.LENGTH_LONG).show();
            }
        }

    }

    private void sendPayment() {
        mServiceScalar.makePayment(params.get("nonce"),params.get("amount"))
                 .enqueue(new Callback<String>() {
                     @Override
                     public void onResponse(Call<String> call, Response<String> response) {
                         System.out.println(response.body().toString());
                         if(response.body().toString().contains("success"))
                         {
                             Toast.makeText(CartActivity.this,"Payment Made successful",Toast.LENGTH_LONG).show();
                             compositeDisposable.add(Common.cartRepository.getAllCart()
                                     .observeOn(AndroidSchedulers.mainThread())
                                     .subscribeOn(Schedulers.io())
                                     .subscribe(new Consumer<List<Cart>>() {
                                         @Override
                                         public void accept(List<Cart> carts) throws Exception {


                                         }
                                     }, new Consumer<Throwable>() {
                                         @Override
                                         public void accept(Throwable throwable) throws Exception {
                                             Toast.makeText(CartActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                                         }
                                     })

                             );

                         }else
                         {
                             Toast.makeText(CartActivity.this,"Failed Payment"+response.body(),Toast.LENGTH_LONG).show();
                         }
                     }

                     @Override
                     public void onFailure(Call<String> call, Throwable t) {

                     }
                 });

    }

    private void LoadBraintreeToken() {
        AsyncHttpClient client=new AsyncHttpClient();



        client.get(Common.API_Token, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(CartActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                Placeorder.setEnabled(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Toast.makeText(CartActivity.this," "+responseString,Toast.LENGTH_LONG).show();
                token=responseString;

                Placeorder.setEnabled(true);

            }
        });

    }

    private void MakeMpesaPayments() {
        LNMExpress lnmExpress = new LNMExpress(
                "174379",
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                TransactionType.CustomerPayBillOnline,
                "100",
                "254706058357",
                "174379",
                "254795563327",
                "https://mbuguanjane.com/ReceiverPayment.php",
                "yes man",
                "Goods Payment"
        );
        //make transaction
        daraja.requestMPESAExpress(lnmExpress,
                new DarajaListener<LNMResult>() {
                    @Override
                    public void onResult(@NonNull LNMResult lnmResult) {
                        Log.i(CartActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                        Toast.makeText(CartActivity.this,"Succes payment made"+lnmResult.ResponseDescription,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String error) {
                        Log.i(CartActivity.this.getClass().getSimpleName(), error);
                        Toast.makeText(CartActivity.this,"Failed payments "+error,Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    //calculate cost of deliver
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    private void drawRoute(){

        // Getting URL to the Google Directions API
        // String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood&key=AIzaSyB0WNWAdu_EOdbN8R8N92o49X2yBpwO84U";
         ///
       // Addresses=getCompleteAddressString(-1.167277, 36.959610);
       Destination=new LatLng(-1.167277, 36.959610);
        //Destination=new LatLng(-1.167277, 36.959610);

       if(Destination!=null && Origin!=null)
       {
           String url = getDirectionsUrl(Origin, Destination);


           DownloadTask downloadTask = new DownloadTask();

           // Start downloading json data from Google Directions API
           downloadTask.execute(url);
       }
       else
       {
           Toast.makeText(CartActivity.this,"Gps is Retreiving location \n PLease Try again",Toast.LENGTH_LONG).show();
       }
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=AIzaSyB0WNWAdu_EOdbN8R8N92o49X2yBpwO84U";

        // Building the parameters to the web service
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onClick(String value) {
        totalbill.setText("Total Bill Ksh "+value);
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Wendo "+result);
            Parseworker(result);

        }
    }
    private void Parseworker(String result) {
        try {
            JSONObject object = new JSONObject(result);
            JSONArray routes=object.getJSONArray("routes");
            System.out.println("Routes "+routes);
            JSONObject object1=routes.getJSONObject(0);
            //i want to get legs
            JSONArray legs=object1.getJSONArray("legs");
            System.out.println("Legs "+legs);
            JSONObject object3=legs.getJSONObject(0);
            JSONObject distance=object3.getJSONObject("distance");
            dist=distance.getString("value");
            System.out.println("Distance  "+dist);
            final JSONObject time=object3.getJSONObject("duration");
            tym=time.getString("text");
            System.out.println("Time  "+tym);

            //end legs here
            JSONObject object2=object1.getJSONObject("overview_polyline");
            System.out.println("Polylines "+object2);
            String polyline=object2.getString("points");
            System.out.println("Points "+polyline);

        }catch (Exception ex)
        {
            System.out.println("Error "+ex.getMessage());
        }
    }
    //end cost of deliver
    private void placeOrder() {
        //open Location activity
        transportcost=260;
        PaymentMethod="Cash On Delivery";
        Addresses="";
           // getAddressFromGPS();

        ProductTransport = "Get Product";
        transportcost=0;
        //end Location activity
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");
        View submit_order_layout= LayoutInflater.from(this).inflate(R.layout.submit_order_layout,null);


        final EditText edtcomment=(EditText)submit_order_layout.findViewById(R.id.edt_comment);
        final EditText otheraddress=(EditText)submit_order_layout.findViewById(R.id.edt_Address);
        final TextView paymentdetail=(TextView)submit_order_layout.findViewById(R.id.mpesapayment);
        String myHtmlText="Using MPESA <ul>" +
                "<li>Go to the M-pesa Menu </li>" +
                "<li>Select Pay Bill </li>" +
                "<li>Enter Business No. 220220 </li>" +
                "<li>Enter Account No. +254XXXXXX (Where XXXXXX is your Safaricom Mobile number) </li>" +
                "<li>Enter the Amount </li>" +
                "<li>Enter your M-Pesa PIN then send </ul>";
        paymentdetail.setText(Html.fromHtml(myHtmlText, null, new UlTagHandler()));
        //delivery option
        RadioButton rdi_user_fetch=(RadioButton)submit_order_layout.findViewById(R.id.fetchproduct);
        rdi_user_fetch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    ProductTransport = "Get Product";
                    transportcost=0;
                    //getAddressFromGPS();
                   // drawRoute();


                }
            }
        });

        RadioButton rdi_user_deliver=(RadioButton)submit_order_layout.findViewById(R.id.DeliverProduct);
        rdi_user_deliver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    ProductTransport = "Deliver Product";
                    transportcost=260;
                       // getAddressFromGPS();
                        //drawRoute();



                }
            }
        });
        //end Delivery option
        RadioButton rdi_user_address=(RadioButton)submit_order_layout.findViewById(R.id.user_address);
        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    otheraddress.setEnabled(true);

                    Addresses=otheraddress.getText().toString();
                }
            }
        });
        RadioButton rdi_user_gps=(RadioButton)submit_order_layout.findViewById(R.id.Gpsaddress);
        rdi_user_gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    otheraddress.setEnabled(false);

                    System.out.println("Addresses "+Addresses);


                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Your Location");
                        builder.setMessage("This is your Current Location--> \n :{" + Addresses + "}");
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                    }
                                });
                        builder.show();

                }
            }
        });
        RadioButton mpesaoption=(RadioButton)submit_order_layout.findViewById(R.id.Mpesa);
        final MaterialEditText codeInput=(MaterialEditText)submit_order_layout.findViewById(R.id.MpesaCode);
        mpesaoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentMethod="Mpesa";
                paymentdetail.setVisibility(View.VISIBLE);
                codeInput.setVisibility(View.VISIBLE);

            }
        });
        RadioButton codmpesa=(RadioButton)submit_order_layout.findViewById(R.id.COD);
        codmpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentMethod="Cash On Delivery";
                paymentdetail.setVisibility(View.GONE);
                codeInput.setVisibility(View.GONE);
            }
        });
        builder.setView(submit_order_layout);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Perform payment here

                        //end payment here

                        Comments =edtcomment.getText().toString();
                        Addresses=otheraddress.getText().toString();
                         //
                        if(PaymentMethod.equals("Credit Card") && TextUtils.isEmpty(ProductTransport))
                        {
                            Toast.makeText(CartActivity.this,"Selected Payment methods",Toast.LENGTH_LONG).show();
                        }
                        else if(PaymentMethod.equals("Cash On Delivery")) {
                           // DropInRequest dropInRequest = new DropInRequest().clientToken(token);
                           // startActivityForResult(dropInRequest.getIntent(CartActivity.this), PAYMENT_REQUEST_CODE);
                            Toast.makeText(CartActivity.this,"Cash On Delivery "+sumprice,Toast.LENGTH_LONG).show();
                            PaymentMethod="Cash On Delivery";
                            compositeDisposable.add(Common.cartRepository.getAllCart()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Consumer<List<Cart>>() {
                                        @Override
                                        public void accept(List<Cart> carts) throws Exception {
                                            if(carts.size()>0) {
                                                //Addresses=getAddressFromGPS();
                                                sumprice=0;
                                                for(Cart cating:carts)
                                                {
                                                    sumprice+= (cating.getPrice()*cating.getQuantity());
                                                }
                                                    SendOrderToServer(sumprice + transportcost, carts, Comments, Addresses);

                                                }else{
                                                Toast.makeText(CartActivity.this,"Cart is Empty ",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            Toast.makeText(CartActivity.this,"Failed here "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    })

                            );
                        }else if(PaymentMethod.equals("Mpesa"))
                        {
                           // Toast.makeText(CartActivity.this,"Mpesa Payment "+sumprice,Toast.LENGTH_LONG).show();
                            compositeDisposable.add(Common.cartRepository.getAllCart()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Consumer<List<Cart>>() {
                                        @Override
                                        public void accept(List<Cart> carts) throws Exception {
                                                  if(carts.size()>0) {
                                                      sumprice=0;
                                                      for(Cart cating:carts)
                                                      {
                                                          sumprice+= (cating.getPrice()*cating.getQuantity());
                                                      }
                                                      SendOrderToServer(sumprice+transportcost, carts, Comments, Addresses);
                                                  }else
                                                  {
                                                      Toast.makeText(CartActivity.this,"Cart is Empty",Toast.LENGTH_LONG).show();
                                                  }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            Toast.makeText(CartActivity.this,"Failed Room "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    })

                            );
                        }
                    }
                });
        builder.show();
    }

    private String getAddressFromGPS() {

        if (ActivityCompat.checkSelfPermission(CartActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return null;
        }
        client.getLastLocation().addOnSuccessListener(CartActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    System.out.println("Mathenge here"+location);

                    Addresses = getCompleteAddressString(location.getLatitude(), location.getLongitude());
                    System.out.println("Addresses "+getCompleteAddressString(location.getLatitude(), location.getLongitude()));
                    Origin= new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        });
        return Addresses;
    }
    public  JSONObject getLocationFormGoogle(String placesName) {

        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" +placesName+"&ka&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return jsonObject;
    }

    public  LatLng getLatLng(JSONObject jsonObject) {

        Double lon = new Double(0);
        Double lat = new Double(0);

        try {

            lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new LatLng(lat,lon);

    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                System.out.println("My Current loction address"+ strReturnedAddress.toString());
            } else {
                System.out.println("My Current loction address"+ " No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("My Current loction address"+ "Canont get Address!");
        }
        return strAdd;
    }

    public class UlTagHandler implements Html.TagHandler{
        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                              XMLReader xmlReader) {
            if(tag.equals("ul") && !opening) output.append("\n");
            if(tag.equals("li") && opening) output.append("\n\t•");
        }
    }
    private void SendOrderToServer(float sumprice, final List<Cart> carts, String orderComment, String useraddress) {
        System.out.println("Finally address "+Addresses);
        sumprice=0;
         if(carts.size()>0 && Common.CurrentUser!=null )
        {
            for(Cart cating:carts)
            {
                sumprice+= (cating.getPrice()*cating.getQuantity());
            }
            String orderDetail=new Gson().toJson(carts);
            mService.SubmitOrder(sumprice,orderDetail,Common.CurrentUser.getPhone(),useraddress,orderComment,PaymentMethod,ProductTransport)
                    .enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            if(response.body()!=null) {
                                Toast.makeText(CartActivity.this, "Order Submitted successfully", Toast.LENGTH_LONG).show();
                                createPDF(Common.getPath(CartActivity.this)+"Invoivetest.pdf", carts,2);
                                DeleteCart();

                                totalbill.setText("Total Bill Ksh 0.00");
                                QuantityUpdated(carts);
                                //getToken
                                getFirebaseToken(response.body().getOrderId());
                                System.out.println("Order id "+response.body().getOrderId());
                            }
                            System.out.println("Error "+response.body());
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Toast.makeText(CartActivity.this,"Failed "+t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });


        }
        else
        {
            Toast.makeText(CartActivity.this,"Please login First ",Toast.LENGTH_LONG).show();
        }
    }

    private void QuantityUpdated(List<Cart> carts) {
        for(Cart cato:carts) {
            mService.updateQuantity(String.valueOf(cato.getQuantity()), cato.getName())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            System.out.println("Server Feedback "+response.body());
                            Toast.makeText(CartActivity.this, "Quantity updated", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(CartActivity.this, "Failed Quantity updated", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    private void getFirebaseToken(final long orderId){



        mService.getFireBaseToken("Server_01","1")
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        //send Notification
                        if(!TextUtils.isEmpty(response.body().getToken())) {
                            Placeorder.setEnabled(true);
                            SendNotification(response.body().getToken(),orderId);
                            System.out.println("Token Remote" + response.body().getToken());
                            System.out.println("Token Local" + FirebaseInstanceId.getInstance().getToken());
                        }
                        //Toast.makeText(CartActivity.this,"Token "+response.body().getToken(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
  Toast.makeText(CartActivity.this,"Failed "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }



    private void SendNotification(String token, long orderId)  {
        Map<String,String> contentSend=new HashMap<>();
        contentSend.put("title","# "+orderId+" New Order ");
        contentSend.put("message","You Have a new Order");
        DataMessage dataMessage=new DataMessage();
        if(!TextUtils.isEmpty(token))
        {
            dataMessage.setTo(token);
            dataMessage.setData(contentSend);
            IFCMService ifcmService=Common.getFCMService();
            ifcmService.sendNotification(dataMessage)
                    .enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            Toast.makeText(CartActivity.this,"Notification Sent Successfully"+response.body(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(CartActivity.this,"Failed Notification",Toast.LENGTH_LONG).show();
                        }
                    });
        }else {
            Toast.makeText(CartActivity.this,"Empty Token",Toast.LENGTH_LONG).show();
        }
    }

    private void getPrice() {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                sumprice=Common.cartRepository.sumPrice();
                totalbill.setText("Total bill "+sumprice);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(CartActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                totalbill.setText("Total Bill "+sumprice);
                    }
                });
    }
    private void DeleteCart() {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                Common.cartRepository.deleteAllCarts();
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(CartActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                );
    }
    //get Count
    private void getCount() {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                count=Common.cartRepository.countCartItems();
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(CartActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //Toast.makeText(CartActivity.this,"Count  "+count,Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadCartItems() {
        compositeDisposable.add(Common.cartRepository.getAllCart()
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribeOn(Schedulers.io())
                     .subscribe(new Consumer<List<Cart>>() {
                         @Override
                         public void accept(List<Cart> carts) throws Exception {
                             displayCartItems(carts);
                         }
                     }, new Consumer<Throwable>() {
                         @Override
                         public void accept(Throwable throwable) throws Exception {
                             Toast.makeText(CartActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                         }
                     })

        );
    }

    private void displayCartItems(List<Cart> carts) {
        cartList=carts;
         cartAdaptor=new CartAdaptor(CartActivity.this,carts,this);

        recyclerView_cart.setAdapter(cartAdaptor);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onResume() {
        loadCartItems();
        super.onResume();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof  CartAdaptor.CartViewholder)
        {
            String name=cartList.get(viewHolder.getAdapterPosition()).getName();
            final Cart deletedItem=cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex=viewHolder.getAdapterPosition();
            cartAdaptor.removeItem(deletedIndex);
            DeletefromRoom(deletedItem);
            Snackbar snackbar=Snackbar.make(rootLayout,new StringBuilder(name).append(" removed from Cart list").toString(),Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdaptor.restoreItem(deletedItem,deletedIndex);
                    RecovertoRoomDatabase(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
    private void RecovertoRoomDatabase(final Cart cart) {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Common.cartRepository.insertCart(cart);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //LoadData();
                    }
                });
    }

    private void DeletefromRoom(final Cart cart) {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Common.cartRepository.deleteCart(cart);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //LoadData();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPDF(String path, List<Cart> carts, long orderId) {
        float totalAmout=0;
        Toast.makeText(CartActivity.this,"creating pdf",Toast.LENGTH_LONG).show();
        if(new File(path).exists())
            new File(path).delete();
        try{

            Document document=new Document();
            PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Mathew PDf");
            document.addCreator("Mathew Mbugua");
            BaseColor colorAccent=new BaseColor(0,153,204,255);
            float Titlefontsize=18.0f;
            float TableContentFontsize=14.0f;
            float TableTitleFontsize=16.0f;
            BaseFont fontname=BaseFont.createFont("assets/fonts/brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);

            Font  titleFonts=new Font(fontname,Titlefontsize,Font.NORMAL,colorAccent);
            Font  tablecontentFont=new Font(fontname,TableContentFontsize,Font.NORMAL,BaseColor.BLACK);
            Font  tableordertitle=new Font(fontname,TableTitleFontsize,Font.NORMAL,colorAccent);
            //addNewItemWithLeftAndRight(document,"SELDOM INTERNATIONAL LIMITED \nP.O. Box 1162-00232\nRuiru, Kenya.\nLocation: Small Lagos-28\nPhone: 072515386","\tINVOICE Date: 21/4/2020\n Invoice No. DM1",titleFonts,titleFonts);

            addNewItem(document,"RODI KENYA \nP.O. Box 1162-00232\nRuiru, Kenya.\nLocation: Delta petro station Ruiru\nPhone: 072515386",Element.ALIGN_LEFT,tableordertitle);
            addLineSpace(document);
            addNewItem(document,"INVOICE Date: 21/4/2020\n Invoice No. "+orderId,Element.ALIGN_RIGHT,tableordertitle);
            addLineSpace(document);
            addNewItem(document,"Invoice for: \n" +Addresses, Element.ALIGN_LEFT,titleFonts);
            //other
            addLineSpace(document);
            addNewItemWithLeftAndRight(document,"Client Name: "+Common.CurrentUser.getName(),"Buy goods till No. 5100195 ",tablecontentFont,tablecontentFont);
            //CREATE other
            addLineSpace(document);
            addNewItem(document,"Description: Deliveries made.",Element.ALIGN_LEFT,tablecontentFont);

            addLineSpace(document);
            addLineSeparator(document);

            addLineSpace(document);
            PdfPTable table=new PdfPTable(5);

            addTitleItem(table, "Item",Element.ALIGN_CENTER, tableordertitle,2);
            addTitleItem(table, "Quantity",Element.ALIGN_CENTER, tableordertitle,1);
            addTitleItem(table, "Price in Ksh per Unit ",Element.ALIGN_CENTER, tableordertitle,1);
            addTitleItem(table, "Subtotal Price in Ksh",Element.ALIGN_CENTER, tableordertitle,1);
            //kam
            for(Cart cart:carts)
            {
                addTitleItem(table, cart.getName(),Element.ALIGN_CENTER, tablecontentFont,2);
                addTitleItem(table, String.valueOf(cart.getQuantity()),Element.ALIGN_CENTER, tablecontentFont,1);
                addTitleItem(table, String.valueOf(cart.getPrice()),Element.ALIGN_CENTER, tablecontentFont,1);
                addTitleItem(table, String.valueOf(cart.getPrice()*cart.getQuantity()),Element.ALIGN_CENTER, tablecontentFont,1);
                totalAmout+=(cart.getPrice()*cart.getQuantity());
            }

            //
            //
            addNoBorderItem(table, "",Element.ALIGN_CENTER, tablecontentFont,2);
            addNoBorderItem(table, "",Element.ALIGN_CENTER, tablecontentFont,1);
            addTitleItem(table, "Total Cost",Element.ALIGN_CENTER, tableordertitle,1);

            addTitleItem(table, String.valueOf(totalAmout),Element.ALIGN_CENTER, tablecontentFont,1);
            //
            document.add(table);
            addLineSpace(document);
            addNewItem(document,"Thank you for using our products.",Element.ALIGN_LEFT,tablecontentFont);
            addLineSpace(document);
            addNewItem(document,"EQUITY BANK A/c: SELDOM INTERNATIONAL   0870 2796 70759 RUIRU BRANCH.",Element.ALIGN_LEFT,tableordertitle);
            document.close();


            Toast.makeText(CartActivity.this,"Success",Toast.LENGTH_LONG).show();
            printPDF();
        } catch (FileNotFoundException e) {
            Toast.makeText(CartActivity.this,"Faiiled "+e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (DocumentException e) {
            Toast.makeText(CartActivity.this,"Faiiled "+e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(CartActivity.this,"Faiiled "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPDF() {
        PrintManager printManager=(PrintManager)getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter=new PrintDocumentAdaptor(CartActivity.this,Common.getPath(CartActivity.this)+"Invoivetest.pdf");
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
            Toast.makeText(CartActivity.this,"Printed ",Toast.LENGTH_LONG).show();

        }catch(Exception Ex)
        {
            Toast.makeText(CartActivity.this,"Faiiled "+Ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textFontsLeft, Font textFontsRight) throws DocumentException {
        Chunk chunkTextleft=new Chunk(textLeft,textFontsLeft);
        Chunk chunkTextRight=new Chunk(textRight,textFontsRight);
        Paragraph p=new Paragraph(chunkTextleft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeparator(Document document) throws DocumentException, IOException {
        LineSeparator lineSeparator=new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);


    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk=new Chunk(text,font);
        Paragraph paragraph=new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
    private void addTitleItem(PdfPTable table, String text, int align, Font font, int colspan) throws DocumentException {
        Chunk chunk=new Chunk(text,font);
        Paragraph paragraph=new Paragraph(chunk);
        paragraph.setAlignment(align);
        PdfPCell cell=new PdfPCell(paragraph);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.BOX);
        table.addCell(cell);
    }
    private void addNoBorderItem(PdfPTable table, String text, int align, Font font,int colspan) throws DocumentException {
        Chunk chunk=new Chunk(text,font);
        Paragraph paragraph=new Paragraph(chunk);
        paragraph.setAlignment(align);
        PdfPCell cell=new PdfPCell(paragraph);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
    }
}
