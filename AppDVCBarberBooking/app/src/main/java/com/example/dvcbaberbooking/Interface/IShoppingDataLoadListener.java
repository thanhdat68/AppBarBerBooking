package com.example.dvcbaberbooking.Interface;

import com.example.dvcbaberbooking.Model.ShoppingItem;

import java.util.List;

public interface IShoppingDataLoadListener {
    void onShoppingDataLoadSuccess(List<ShoppingItem>shoppingItemList);
    void onShopingDataLoadFaile(String message);
}

