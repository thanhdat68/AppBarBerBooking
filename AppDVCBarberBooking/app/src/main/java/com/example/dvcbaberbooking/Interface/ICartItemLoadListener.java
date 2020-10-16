package com.example.dvcbaberbooking.Interface;



import com.example.dvcbaberbooking.Database.CartItem;

import java.util.List;

public interface ICartItemLoadListener {
    void onGetAllItemFromCartSuccess(List<CartItem> cartItemList);
}
