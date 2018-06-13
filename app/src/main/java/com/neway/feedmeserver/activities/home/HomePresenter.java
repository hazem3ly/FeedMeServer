package com.neway.feedmeserver.activities.home;

import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neway.feedmeserver.R;
import com.neway.feedmeserver.bases.BasePresenter;
import com.neway.feedmeserver.model.Category;
import com.squareup.picasso.Picasso;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

//    FirebaseDatabase database;
//    DatabaseReference category;
//    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    public HomePresenter() {
//        database = FirebaseDatabase.getInstance();
//        category = database.getReference("category");
    }

    @Override
    public void loadCategories() {
        getView().showLoading();
//        adapter =
//                new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
//                        Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
//                    @Override
//                    protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
//                        viewHolder.menu_text.setText(model.getName());
//                        Picasso.get().load(model.getImage()).into(viewHolder.menu_image);
//
//                        final Category s = model;
//                        viewHolder.setItemClickListener(new OnItemClickListener() {
//                            @Override
//                            public void onClick(View view, int position, boolean isLongClick) {
//                                getView().onItemClicked(adapter.getRef(position).getKey(), s);
//                            }
//                        });
//
//                    }
//                };
//
//        getView().hideLoading();
//        getView().onDataLoaded(adapter);
    }
}
