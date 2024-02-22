package com.example.recipe_app.ui.user.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.ui.activities.auth.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.ui.activities.auth.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.recipe_app.ui.user.Adapter.AdapterNotification;
import com.example.recipe_app.Model.NotificationModel;
import com.example.recipe_app.R;
import com.example.recipe_app.data.remote.DataApi;
import com.example.recipe_app.data.remote.InterfaceNotification;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {
    ShimmerRecyclerView rv_notification;
    ImageButton btn_back;
    SwipeRefreshLayout swipeRefreshLayout;
    String username, userid;
    AdapterNotification adapterNotification;
    List<NotificationModel> notificationModelList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    TextView tv_no_notif;
    ConnectivityManager conMgr;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        btn_back = root.findViewById(R.id.btn_back);
        rv_notification = root.findViewById(R.id.rv_notification);
        swipeRefreshLayout =  root.findViewById(R.id.swipe_refresh);
        tv_no_notif = root.findViewById(R.id.tv_no_notification);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);


        // button back click litener
        btn_back.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });

        // call swipe refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllNotification();
            }
        });

        // Set color swipe icon
        swipeRefreshLayout.setColorSchemeResources(R.color.main);


        return root;
    }

    // get all notification
    private void getAllNotification(){
        InterfaceNotification interfaceNotification = DataApi.getClient().create(InterfaceNotification.class);
        interfaceNotification.getAllNotification(userid).enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                notificationModelList = response.body();
                if (notificationModelList.size() > 0) {
                    adapterNotification = new AdapterNotification(getContext(), notificationModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_notification.setLayoutManager(linearLayoutManager);
                    rv_notification.setAdapter(adapterNotification);
                    rv_notification.setHasFixedSize(true);
                    tv_no_notif.setVisibility(View.GONE);

                    swipeRefreshLayout.setRefreshing(false);
                    rv_notification.showShimmer();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rv_notification.hideShimmer();
                        }
                    }, 1200);
                } else {
                    rv_notification.hideShimmer();
                    tv_no_notif.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                }

            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                tv_no_notif.setVisibility(View.GONE);
                getAllNotification();

            }
        });
    }


    // init shimmer
    private void initShimmer() {
        rv_notification.setLayoutManager(new LinearLayoutManager(getContext()),
                R.layout.template_notification);

        rv_notification.setAdapter(adapterNotification);


        /* Shimmer layout view type depending on List / Gird */
        rv_notification.setItemViewType((type, position) -> {
            switch (type) {
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
                            ? R.layout.template_notification
                            : R.layout.template_notification;

                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_notification
                            : R.layout.template_notification;
            }
        });

        rv_notification.showShimmer();     // to start showing shimmer

    }


    // method check connection
    private void checkConnection() {
        conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    &&
                    conMgr.getActiveNetworkInfo().isAvailable()
                    &&
                    conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        initShimmer();
        getAllNotification();
        checkConnection();
        super.onResume();
    }

    @Override
    public void onPause() {
        rv_notification.hideShimmer();
        super.onPause();
    }
}