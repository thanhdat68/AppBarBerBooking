package com.example.dvcbaberbooking.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dvcbaberbooking.Adapter.MyShoppingItemAdapter;
import com.example.dvcbaberbooking.Common.SpaceItemDecoration;
import com.example.dvcbaberbooking.Interface.IShoppingDataLoadListener;
import com.example.dvcbaberbooking.Model.ShoppingItem;
import com.example.dvcbaberbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ShoppingFragment extends Fragment implements IShoppingDataLoadListener {
    CollectionReference shoppingItemRef;
    //interface
    IShoppingDataLoadListener iShoppingDataLoadListener;

    Unbinder unbinder;
    //anh xa id layout Fragment_Shopping
    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    //wax
    @BindView(R.id.chip_wax)
    Chip chip_wax;
    @OnClick(R.id.chip_wax)
    void waxChipClick() {
        setSelectedChip(chip_wax);
        loadShoppingItem("Wax");
    }

    //spray
    @BindView(R.id.chip_spray)
    Chip chip_spray;
    @OnClick(R.id.chip_spray)
    void sprayChipClick() {
        setSelectedChip(chip_spray);
        loadShoppingItem("Spray");
    }
    //recyclerView
    @BindView(R.id.recycler_items)
    RecyclerView recycler_items;

    //click wax
    private void loadShoppingItem(String itemMenu) {
        shoppingItemRef = FirebaseFirestore.getInstance().collection("Shopping")
                .document(itemMenu)
                .collection("Items");
        //get Data
        shoppingItemRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iShoppingDataLoadListener.onShopingDataLoadFaile(e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ShoppingItem> shoppingItems = new ArrayList<>();
                    for (DocumentSnapshot itemSnapShop : task.getResult()) {
                        ShoppingItem shoppingItem = itemSnapShop.toObject(ShoppingItem.class);
                      shoppingItem.setId(itemSnapShop.getId());
                        shoppingItems.add(shoppingItem);
                    }
                    iShoppingDataLoadListener.onShoppingDataLoadSuccess(shoppingItems);
                }
            }
        });
    }

    //click wax
    private void setSelectedChip(Chip chip) {
        //set color
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chipItem = (Chip) chipGroup.getChildAt(i);
            if (chipItem.getId() != chip_wax.getId()) {
                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }


    public ShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_shopping, container, false);

        unbinder = ButterKnife.bind(this, itemView);
        iShoppingDataLoadListener = this;
        //Defaut load
        loadShoppingItem("Wax");
        initView();
        return itemView;
    }

    //tao cot va dong cho san pham
    private void initView() {
        recycler_items.setHasFixedSize(true);
        recycler_items.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycler_items.addItemDecoration(new SpaceItemDecoration(8));
    }

    @Override
    public void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList) {
        MyShoppingItemAdapter adapter = new MyShoppingItemAdapter(getContext(), shoppingItemList);
        recycler_items.setAdapter(adapter);
    }

    @Override
    public void onShopingDataLoadFaile(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }
}