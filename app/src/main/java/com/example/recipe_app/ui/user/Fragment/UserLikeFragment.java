package com.example.recipe_app.ui.user.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.recipe_app.ui.user.Adapter.LikeCommentAdapter;
import com.example.recipe_app.ui.user.Adapter.LikeCommentReplyAdapter;
import com.example.recipe_app.ui.user.Adapter.LikeRecipeAdapter;
import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.Model.ReplyCommentModel;
import com.example.recipe_app.R;
import com.example.recipe_app.data.remote.DataApi;
import com.example.recipe_app.data.remote.InterfaceComment;
import com.example.recipe_app.data.remote.InterfaceRecipe;
import com.example.recipe_app.data.remote.InterfaceReplyComment;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserLikeFragment extends Fragment {
    private ImageButton btnBack;
    private ShimmerRecyclerView rvUser;
    SearchView searchView;
    private String commentId, replyId, recipeId;
    private List<CommentModel> commentModelList = new ArrayList<>();
    private List<ReplyCommentModel> replyCommentModelList = new ArrayList<>();
    private List<RecipeModel> recipeModelLis = new ArrayList<>();
    private LikeRecipeAdapter likeRecipeAdapter;
    private LikeCommentAdapter likeCommentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    ConnectivityManager conMgr;
    private TextView tvNotFOund;
    private LikeCommentReplyAdapter likeCommentReplyAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_like, container, false);

        btnBack = root.findViewById(R.id.btn_back);
        rvUser = root.findViewById(R.id.rv_user);
        searchView = root.findViewById(R.id.search_bar);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        tvNotFOund = root.findViewById(R.id.tv_notfound);

        // get string from bundle
        commentId = getArguments().getString("comment_id");
        replyId = getArguments().getString("reply_id");
        recipeId = getArguments().getString("recipe_id");


        if (getArguments().getString("comment_id") != null) {
            getAllUser(commentId);


            // set searchiew to get user like coment
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    rvUser.showShimmer();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvUser.hideShimmer();
                        }
                    }, 1200);

                    filter(newText);
                    return false;
                }



            });

            // set swipe refresh
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getAllUser(commentId);
                }
            });

        } else if (getArguments().getString("reply_id") != null) {
            getAllLikeCommentReply(replyId);

            // set searchview to get like comment reply
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterLikeCommentReply(newText);
                    return false;
                }
            });

            // set swipe refresh
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getAllUser(commentId);
                }
            });

        } else if (getArguments().getString("recipe_id") != null) {
            getLikeRecipe(recipeId);

            // set searchview to get like comment reply
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterLikeRecipe(newText);
                    return false;
                }
            });

            // set swipe refresh
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getLikeRecipe(recipeId);
                }
            });

        }





        // btn back listener
        initBtnBack();





        return root;
    }

    private void filterLikeRecipe(String newText) {
        ArrayList<RecipeModel> filterLikeRecipe = new ArrayList<>();
        for (RecipeModel item : recipeModelLis) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filterLikeRecipe.add(item);
            }

            likeRecipeAdapter.filterRecipeLike(filterLikeRecipe);

            if (filterLikeRecipe.isEmpty()) {
                Toasty.warning(getContext(), "User not found", Toasty.LENGTH_SHORT).show();
            } else {
                likeRecipeAdapter.filterRecipeLike(filterLikeRecipe);
            }
        }
    }

    private void filterLikeCommentReply(String newText) {
        ArrayList<ReplyCommentModel> filterdLikeCommentReply = new ArrayList<>();
        for (ReplyCommentModel item : replyCommentModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filterdLikeCommentReply.add(item);
            }
            likeCommentReplyAdapter.filterData(filterdLikeCommentReply);
        }
        if (filterdLikeCommentReply.isEmpty()) {
            Toasty.warning(getContext(), "User not found", Toasty.LENGTH_SHORT).show();
        } else {
            likeCommentReplyAdapter.filterData(filterdLikeCommentReply);
        }
    }

    private void filter(String newText) {
        ArrayList<CommentModel> filtered = new ArrayList<>();
        for (CommentModel item : commentModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filtered.add(item);
            }
            likeCommentAdapter.filterlist(filtered);

            if (filtered.isEmpty()) {
                Toasty.warning(getContext(), "User not found", Toasty.LENGTH_SHORT).show();
            } else {
                likeCommentAdapter.filterlist(filtered);
            }
        }
    }

    // Get all like comment
    private void getAllUser(String commentId) {
        InterfaceComment interfaceComment = DataApi.getClient().create(InterfaceComment.class);
        interfaceComment.getLikeComment(commentId).enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                commentModelList = response.body();
                if (commentModelList.size() > 0) {
                    likeCommentAdapter = new LikeCommentAdapter(getContext(), commentModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvUser.setAdapter(likeCommentAdapter);
                    rvUser.setLayoutManager(linearLayoutManager);
                    rvUser.setHasFixedSize(true);
                    tvNotFOund.setVisibility(View.GONE);

                    rvUser.showShimmer();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvUser.hideShimmer();
                        }
                    }, 1200);

                    swipeRefreshLayout.setRefreshing(false);




                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    tvNotFOund.setVisibility(View.VISIBLE);
                    rvUser.hideShimmer();
                    tvNotFOund.setText("No one liked your comment yet.");

                }

            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(true);
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                getAllUser(commentId);

            }
        });
    }

    // method get like comment reply
    private void getAllLikeCommentReply(String replyId){
        InterfaceReplyComment interfaceReplyComment = DataApi.getClient().create(InterfaceReplyComment.class);
        interfaceReplyComment.getAllLike(replyId).enqueue(new Callback<List<ReplyCommentModel>>() {
            @Override
            public void onResponse(Call<List<ReplyCommentModel>> call, Response<List<ReplyCommentModel>> response) {
                replyCommentModelList = response.body();
                if (replyCommentModelList.size() > 0 ) {
                    likeCommentReplyAdapter = new LikeCommentReplyAdapter(getContext(), replyCommentModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvUser.setAdapter(likeCommentReplyAdapter);
                    rvUser.setLayoutManager(linearLayoutManager);
                    rvUser.setHasFixedSize(true);

                    rvUser.showShimmer();
                    tvNotFOund.setVisibility(View.GONE);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvUser.hideShimmer();
                        }
                    }, 1200);

                    swipeRefreshLayout.setRefreshing(false);
                }   else {
                    rvUser.hideShimmer();
                    swipeRefreshLayout.setRefreshing(false);
                    tvNotFOund.setVisibility(View.VISIBLE);
                    tvNotFOund.setText("No one liked your comment yet.");
                }
            }

            @Override
            public void onFailure(Call<List<ReplyCommentModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                getAllLikeCommentReply(replyId);
                rvUser.hideShimmer();


            }
        });
    }

    // Method get like recipe
    private void getLikeRecipe(String  recipeId) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.getAllLikeRecipe(recipeId).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelLis = response.body();
                if (recipeModelLis.size() > 0) {
                    likeRecipeAdapter = new LikeRecipeAdapter(getContext(), recipeModelLis);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvUser.setAdapter(likeRecipeAdapter);
                    rvUser.setLayoutManager(linearLayoutManager);

                    rvUser.setHasFixedSize(true);
                    tvNotFOund.setVisibility(View.GONE);

                    rvUser.showShimmer();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvUser.hideShimmer();
                        }
                    }, 1200);

                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    tvNotFOund.setVisibility(View.VISIBLE);
                    rvUser.hideShimmer();
                    tvNotFOund.setText("No one liked your recipe yet.");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                getLikeRecipe(recipeId);

            }
        });
    }

    private void initBtnBack() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
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

        // SetSHimmer
        likeCommentAdapter = new LikeCommentAdapter(getContext(), commentModelList);
        rvUser.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvUser.setAdapter(likeCommentAdapter);
        rvUser.showShimmer();

        // Check connection
        checkConnection();


        // Change color swipe refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);
        super.onResume();
    }

    @Override
    public void onPause() {
        rvUser.hideShimmer();
        super.onPause();
    }
}