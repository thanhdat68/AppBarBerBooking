package com.example.dvcbaberbooking.Common;

import android.content.Context;
import android.text.TextUtils;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.NotificationCompat;

import com.example.dvcbaberbooking.Model.Barber;
import com.example.dvcbaberbooking.Model.BookingInformation;
import com.example.dvcbaberbooking.Model.Salon;
import com.example.dvcbaberbooking.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import com.example.dvcbaberbooking.Model.MyToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_SALON_STORE = "SALON_SAVE";
    public static final String KEY_BARBER_LOAD_DONE = "BARBER_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";
    public static final int TIME_SLOT_T0TAL = 20;//làm việc từ 9h sáng đến 5h tối trung bình 30p 1 đầu thì ta có 10*2 = 20 slot/ 1 thợ cắt tóc
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static final String EVENT_URI_CACHE = "URI_EVENT_SAVE";
    public static String IS_LOGIN = "IsLogin";


    //    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
//    public static final String KEY_SALON_STORE = "SALON_SAVE";
//    public static final String KEY_BARBER_LOAD_DONE = "BARBER_LOAD_DONE";
//    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
//    public static final String KEY_STEP = "STEP";
//    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";
//    public static final int TIME_SLOT_TOTAL = 20;
//    public static final Object DISABLE_TAG = "DISABLE";
//    public static final String KEY_TIME_SLOT = "TIME_SLOT";
//    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
//    public static final String EVENT_URI_CACHE = "URI_EVENT_SAVE";
//    public static final String TITLE_KEY = "title";
//    public static final String CONTENT_KEY = "content"
    public static final String LOGGED_KEY = "UserLogged";
    public static Salon currentSalon;
    public static User currentUser;
    public static int step = 0; // init first step = 0
    public static String city = "";
    public static Calendar bookingDate = Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
    public static Barber currentBarber;
    public static int currentTimeSlot = -1;
    public static BookingInformation currentBooking;
    public static String currentBookingId = "";

    public static String convertTimeSlotToString(int slot) {
        switch (slot) {
            case 0:
                return "9:00-9:30";
            case 1:
                return "9:30-10:00";
            case 2:
                return "10:00-10:30";
            case 3:
                return "10:30-11:00";
            case 4:
                return "11:00-11:30";
            case 5:
                return "11:30-12:00";
            case 6:
                return "12:00-12:30";
            case 7:
                return "12:30-13:00";
            case 8:
                return "13:00-13:30";
            case 9:
                return "13:30-14:00";
            case 10:
                return "14:00-14:30";
            case 11:
                return "14:30-15:00";
            case 12:
                return "15:00-15:30";
            case 13:
                return "15:30-16:00";
//            case 14:
//                return "16:00-16:30";
//            case 15:
//                return "16:30-17:00";
//            case 16:
//                return "17:00-17:30";
//            case 17:
//                return "17:30-18:00";
//            case 18:
//                return "18:00-18:30";
//            case 19:
//                return "18:30-19:00";
            default:
                return "Closed";
        }
    }

    public static String convertTimeSlotToStringKey(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        return simpleDateFormat.format(date);
    }

    public static String formatShoppingItemName(String name) {

        return name.length()>13 ? new StringBuilder(name.substring(0,10)).append("...").toString():name;
    }


    public static enum TOKEN_TYPE {
        CLIENT,
        BARBER,
        MANAGER

    }

    public static void updateToken(Context context, final String s) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {


            MyToken myToken = new MyToken();
            myToken.setToken(s);
            myToken.setTokenType(TOKEN_TYPE.CLIENT);
            myToken.setUserPhone(user.getPhoneNumber());

            FirebaseFirestore.getInstance()
                    .collection("User")
                    .document(user.getPhoneNumber())
                    .set(myToken)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });


        } else {
            Paper.init(context);
            String locaUser = Paper.book().read(Common.LOGGED_KEY);
            if (locaUser != null) {
                if (!TextUtils.isEmpty(locaUser)) {
                    MyToken myToken = new MyToken();
                    myToken.setToken(s);
                    myToken.setTokenType(TOKEN_TYPE.CLIENT);
                    myToken.setUserPhone(locaUser);
//Tokens
                    FirebaseFirestore.getInstance()
                            .collection("User")
                            .document(locaUser)
                            .set(myToken)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
            }

        }
    }
}


