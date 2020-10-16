package com.example.dvcbaberbooking.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dvcbaberbooking.Adapter.HomeSliderAdapter;
import com.example.dvcbaberbooking.Adapter.LookbookAdapter;
import com.example.dvcbaberbooking.BookingActivity;
import com.example.dvcbaberbooking.CartActivity;
import com.example.dvcbaberbooking.Common.Common;
import com.example.dvcbaberbooking.Database.CartDatabase;
import com.example.dvcbaberbooking.Database.DatabaseUtils;
import com.example.dvcbaberbooking.Interface.IBannerLoadListener;
import com.example.dvcbaberbooking.Interface.IBookingInfoLoadListener;
import com.example.dvcbaberbooking.Interface.IBookingInformationChangeListener;
import com.example.dvcbaberbooking.Interface.ICountItemInCartListener;
import com.example.dvcbaberbooking.Interface.ILookbookLoadListener;
import com.example.dvcbaberbooking.Model.Banner;
import com.example.dvcbaberbooking.Model.BookingInformation;
import com.example.dvcbaberbooking.Model.User;
import com.example.dvcbaberbooking.R;
import com.example.dvcbaberbooking.Service.PicassoImageLoadingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import ss.com.bannerslider.Slider;

public class HomeFragment extends Fragment implements IBannerLoadListener, ILookbookLoadListener, IBookingInfoLoadListener, IBookingInformationChangeListener, ICountItemInCartListener {

    //video 12 hiển thị danh sách giỏ hàng và thêm số hàng mua và cộng vào tổng tiền
    AlertDialog dialog;
    CartDatabase cartDatabase;


    @BindView(R.id.notification_badge)
    NotificationBadge notificationBadge;


    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;
    @BindView(R.id.txt_user_name)
    TextView txt_user_name;
    @BindView(R.id.txt_your_address)
    TextView txt_your_address;
    @BindView(R.id.banner_slider)
    Slider banner_slider;
    @BindView(R.id.recycler_look_book)
    RecyclerView recycler_look_book;

    @BindView(R.id.card_booking_infor)
    CardView card_booking_infor;
    @BindView(R.id.txt_salon_address)
    TextView txt_salon_address;
    @BindView(R.id.txt_salon_barber)
    TextView txt_salon_barber;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_time_remain)
    TextView txt_time_remain;

    @OnClick(R.id.btn_delete_booking)
    void deleteBooking() {
        deleteBookingFromBaber(false);
    }

    @OnClick(R.id.btn_change_booking)
    void changeBooking() {
        changeBookingFromUser();
    }

    private void changeBookingFromUser() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("Hey!")
                .setMessage("asasasasas")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteBookingFromBaber(true);
                    }
                }).show();
    }

    private void deleteBookingFromBaber(boolean isChange) {
        if (Common.currentBooking != null) {
            dialog.show();
            DocumentReference barberBookingInfo = FirebaseFirestore.getInstance()
                    .collection("AllSalon")
                    .document(Common.currentBooking.getCityBook())
                    .collection("Branch")
                    .document(Common.currentBooking.getSalonId())
                    .collection("Barbers")
                    .document(Common.currentBooking.getBarberId())
                    .collection(Common.convertTimeSlotToStringKey(Common.currentBooking.getTimestamp()))
                    .document(Common.currentBooking.getSlot().toString());

            barberBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT);

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    deleteBookingFromUser(isChange);
                }
            });

        } else {
            Toast.makeText(getContext(), "Current booking must not be null", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBookingFromUser(boolean isChange) {
        if (!TextUtils.isEmpty(Common.currentBookingId)) {
            DocumentReference userBookingInfo = FirebaseFirestore.getInstance()
                    .collection("User")
                    .document(Common.currentUser.getPhoneNumber())
                    .collection("Booking")
                    .document(Common.currentBookingId);

            userBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Paper.init(getActivity());
                    Uri uri = Uri.parse(Paper.book().read(Common.EVENT_URI_CACHE).toString());
                    getActivity().getContentResolver().delete(uri, null, null);
                    Toast.makeText(getActivity(), "Succsess delete Booking", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadUserBooking();
                    if (isChange) {
                        iBookingInformationChangeListener.onBookinginformationChange();
                        dialog.dismiss();
                    }
                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(getContext(), "Booking information ID must not be empty", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.card_view_booking)
    void booking() {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

    @OnClick(R.id.card_view_Cart)
    void opCartActivity() {
        startActivity(new Intent(getActivity(), CartActivity.class));
    }

    // firestore
    CollectionReference bannerRef, lookbookRef;
    IBannerLoadListener iBannerLoadListener;
    ILookbookLoadListener iLookbookLoadListener;
    IBookingInfoLoadListener iBookingInfoLoadListener;
    IBookingInformationChangeListener iBookingInformationChangeListener;

//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private Unbinder unbinder;

    public HomeFragment() {
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        lookbookRef = FirebaseFirestore.getInstance().collection("Lookbook");

    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserBooking();
        countCartItem();
    }

    private void loadUserBooking() {
        CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.currentUser.getPhoneNumber())
                .collection("Booking");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Timestamp todayTimestamp = new Timestamp(calendar.getTime());
        userBooking
                .whereGreaterThanOrEqualTo("timestamp", todayTimestamp)
                .whereEqualTo("done", false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    iBookingInfoLoadListener.onBookingInforLoadSuccess(bookingInformation, queryDocumentSnapshot.getId());
                                    break;
                                }
                            } else {
                                iBookingInfoLoadListener.onBookingInforLoadEmpty();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBookingInfoLoadListener.onBookingInforLoadFailed(e.getMessage());
            }
        });
    }

//    // TODO: Rename and change types and number of parameters
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        cartDatabase = CartDatabase.getInstance(getContext());

        //init
        Slider.init(new PicassoImageLoadingService());
        iBannerLoadListener = this;
        iLookbookLoadListener = this;
        iBookingInfoLoadListener = this;
        iBookingInformationChangeListener = this;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            setUserInformation();
            loadBanner();
            loadLookBook();
            loadUserBooking();
            countCartItem();
        }

        return view;
    }

    private void countCartItem() {
        DatabaseUtils.countItemInCart(cartDatabase, this);
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_user_name.setText(Common.currentUser.getName());
        txt_your_address.setText(Common.currentUser.getAddress());
    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));

    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    public void onLookbookLoadSuccess(List<Banner> banners) {
        recycler_look_book.setHasFixedSize(true);
        recycler_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_look_book.setAdapter(new LookbookAdapter(getActivity(), banners));

    }

    @Override
    public void onLookbookLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    private void loadBanner() {
        bannerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Banner> banners = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot bannerSnapShot : task.getResult()) {
                        Banner banner = bannerSnapShot.toObject(Banner.class);
                        banners.add(banner);
                    }
                    iBannerLoadListener.onBannerLoadSuccess(banners);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBannerLoadListener.onBannerLoadFailed(e.getMessage());
            }
        });
    }

    private void loadLookBook() {
        lookbookRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Banner> lookbooks = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot bannerSnapShot : task.getResult()) {
                        Banner banner = bannerSnapShot.toObject(Banner.class);
                        lookbooks.add(banner);
                    }
                    iLookbookLoadListener.onLookbookLoadSuccess(lookbooks);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookbookLoadListener.onLookbookLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onBookingInforLoadEmpty() {
        card_booking_infor.setVisibility(View.GONE);
    }

    @Override
    public void onBookingInforLoadSuccess(BookingInformation bookingInformation, String bookingId) {

        Common.currentBooking = bookingInformation;
        Common.currentBookingId = bookingId;
        txt_salon_address.setText(bookingInformation.getSalonAddress());
        txt_salon_barber.setText(bookingInformation.getBarberName());
        txt_time.setText(bookingInformation.getTime());
        String dateRemain = DateUtils.getRelativeTimeSpanString(
                Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
                Calendar.getInstance().getTimeInMillis(), 0).toString();

        txt_time_remain.setText(dateRemain);

        card_booking_infor.setVisibility(View.VISIBLE);

        dialog.dismiss();

    }

    @Override
    public void onBookingInforLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookinginformationChange() {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

    @Override
    public void onCartItemCountSuccess(int count) {
        notificationBadge.setText(String.valueOf(count));
    }
}