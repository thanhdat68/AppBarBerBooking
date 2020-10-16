package com.example.dvcbaberbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.dvcbaberbooking.Adapter.MyCartAdapter;
import com.example.dvcbaberbooking.Database.CartDatabase;
import com.example.dvcbaberbooking.Database.CartItem;
import com.example.dvcbaberbooking.Database.DatabaseUtils;
import com.example.dvcbaberbooking.Interface.ICartItemLoadListener;
import com.example.dvcbaberbooking.Interface.ICartItemUpdateListener;
import com.example.dvcbaberbooking.Interface.ISumCartListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartItemLoadListener, ICartItemUpdateListener, ISumCartListener {
    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;

    @BindView(R.id.txt_total_price)
    TextView txt_total_price;

    @BindView(R.id.btn_submit_cart)
    Button btn_submit_cart;

    CartDatabase cartDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
         cartDatabase=CartDatabase.getInstance(this);
        ButterKnife.bind(CartActivity.this);

        DatabaseUtils.getAllCart(cartDatabase, this);

        //View
        recycler_cart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(linearLayoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
    }

    @Override
    public void onGetAllItemFromCartSuccess(List<CartItem> cartItemList) {
        //lay tat du lieu tu data va gan vao recylerview

        MyCartAdapter adapter=new MyCartAdapter(this,cartItemList,this);
        recycler_cart.setAdapter(adapter);
    }


    @Override
    public void onCartItemUpdateSuccess() {
        DatabaseUtils.sumCart(cartDatabase,this);

    }

    @Override
    public void onSumCartSuccess(Long value) {
        txt_total_price.setText(new StringBuilder("$").append(value));
    }
}