package com.example.dvcbaberbooking.Interface;

import com.example.dvcbaberbooking.Model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String massage);
    void onTimeSlotLoadEmpty();
}
