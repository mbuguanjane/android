package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rodikenya.rodiseriou.Adaptor.OrderDetailAdapter;
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    public TextView order_id,order_price,order_address,order_comment,order_status;
    RecyclerView recyclerView_order_Detail;
    Button CancelOrder;
    IpService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        order_id=(TextView)findViewById(R.id.order_id);

        CancelOrder=(Button)findViewById(R.id.Cancelled_order);
        if(Common.CurrentOrder.getOrderStatus()==0)
        {
            CancelOrder.setVisibility(View.VISIBLE);
        }
        CancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelOrders();
            }
        });
        recyclerView_order_Detail=(RecyclerView)findViewById(R.id.Recycle_orderdetail);
        recyclerView_order_Detail.setHasFixedSize(true);
        recyclerView_order_Detail.setLayoutManager(new LinearLayoutManager(this));
        order_price=(TextView)findViewById(R.id.orderPrice_value);
        order_address=(TextView)findViewById(R.id.text_order_Address);
        order_comment=(TextView)findViewById(R.id.text_order_comment);
        order_status=(TextView)findViewById(R.id.text_order_status);


        order_id.setText(new StringBuilder("#").append(Common.CurrentOrder.getOrderId()));
        order_address.setText(Common.CurrentOrder.getOrderAddress());
        order_comment.setText(Common.CurrentOrder.getOrderComment());
        order_price.setText(new StringBuilder("KES ").append(Common.CurrentOrder.getOrderPrice()));
        order_status.setText(new StringBuilder("order Status: ").append(Common.convertCodeToStatus(Common.CurrentOrder.getOrderStatus())));
        displayOrderDetails();
    }

    private void CancelOrders() {
        mService=Common.getIpService();
        if(Common.CurrentOrder!=null) {
            mService.cancelorder(String.valueOf(Common.CurrentOrder.getOrderId()), Common.CurrentOrder.getUserPhone())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                                Toast.makeText(OrderDetailActivity.this, response.body(), Toast.LENGTH_LONG).show();
                            getFirebaseToken(Common.CurrentOrder.getOrderId());

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(OrderDetailActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }else
        {
            Toast.makeText(OrderDetailActivity.this, "An Error Occurred", Toast.LENGTH_LONG).show();
        }
    }
    private void getFirebaseToken(final long orderId){



        mService.getFireBaseToken("Server_01","1")
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        //send Notification
                        if(!TextUtils.isEmpty(response.body().getToken())) {
                            SendNotification(response.body().getToken(),orderId);
                            System.out.println("Token Remote" + response.body().getToken());
                            System.out.println("Token Local" + FirebaseInstanceId.getInstance().getToken());
                        }
                        //Toast.makeText(CartActivity.this,"Token "+response.body().getToken(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(OrderDetailActivity.this,"Failed "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }



    private void SendNotification(String token, long orderId)  {
        Map<String,String> contentSend=new HashMap<>();
        contentSend.put("title","# "+orderId+"Order  was Cancelled ");
        contentSend.put("message","This Order has Been Cancelled");
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
                            Toast.makeText(OrderDetailActivity.this,"Notification Sent Successfully"+response.body(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(OrderDetailActivity.this,"Failed Notification",Toast.LENGTH_LONG).show();
                        }
                    });
        }else {
            Toast.makeText(OrderDetailActivity.this,"Empty Token",Toast.LENGTH_LONG).show();
        }
    }
    private void displayOrderDetails() {
        List<Cart> orderDetail=  new Gson().fromJson(Common.CurrentOrder.getOrderDetail(),
                   new TypeToken<List<Cart>>(){}.getType());
        recyclerView_order_Detail.setAdapter(new OrderDetailAdapter(this, orderDetail));


    }
}