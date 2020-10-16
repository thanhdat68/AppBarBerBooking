package com.example.dvcbaberbooking.Interface;

import com.example.dvcbaberbooking.Model.BookingInformation;

public interface IBookingInfoLoadListener {
    void onBookingInforLoadEmpty();
    void onBookingInforLoadSuccess(BookingInformation bookingInformation,String documentId);
    void onBookingInforLoadFailed(String message);
}
