package com.example.dvcbaberbooking.Interface;

import com.example.dvcbaberbooking.Model.Salon;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> areaNameList);
    void onBranchLoadFailed(String message);
}
