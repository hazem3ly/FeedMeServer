package com.neway.feedmeserver.activities.foodlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.neway.feedmeserver.R;
import com.neway.feedmeserver.adapters.FoodsViewHolder;
import com.neway.feedmeserver.bases.BasePresenter;
import com.neway.feedmeserver.interfaces.OnItemClickListener;
import com.neway.feedmeserver.lib.imagesUtils.ImagesUtils;
import com.neway.feedmeserver.model.Category;
import com.neway.feedmeserver.model.Food;
import com.neway.feedmeserver.widget.FButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class FoodListPresenter extends BasePresenter<FoodListContract.View> implements FoodListContract.Presenter {

    FirebaseDatabase database;
    DatabaseReference food;
    FirebaseRecyclerAdapter<Food, FoodsViewHolder> adapter = null;

    // storage configuration
    FirebaseStorage storage;
    StorageReference storageReference;
    private String selectedFile = null;
    private Food newFood = null;

    public FoodListPresenter() {
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Foods");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("uploaded/");
    }

    @Override
    public void loadFoodsList(String menuKey) {
        getView().showLoading();
        adapter = new FirebaseRecyclerAdapter<Food, FoodsViewHolder>(
                Food.class, R.layout.food_item, FoodsViewHolder.class, food.orderByChild("menuId").equalTo(menuKey)) {
            @Override
            protected void populateViewHolder(FoodsViewHolder viewHolder, Food model, int position) {
                viewHolder.food_text.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.food_image);

                final Food s = model;
                viewHolder.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        getView().onItemClicked(s, adapter.getRef(position).getKey());
                    }
                });

            }
        };

        getView().hideLoading();
        getView().onDataLoaded(adapter);
    }

    @Override
    public void showAddDialog(final Context context, final String  categoryId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add New Food");
        builder.setMessage("Please Fill Full Information");
        View v = LayoutInflater.from(context).inflate(R.layout.add_new_food_layout, null);

        final MaterialEditText edt_name = v.findViewById(R.id.edt_name);
        final MaterialEditText edt_description = v.findViewById(R.id.edt_description);
        final MaterialEditText edt_price = v.findViewById(R.id.edt_price);
        final MaterialEditText edt_discount = v.findViewById(R.id.edt_discount);

        final FButton fButtonSelect = v.findViewById(R.id.btn_select);
        FButton fButtonUpload = v.findViewById(R.id.btn_upload);

        fButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesUtils.startSelectImageIntent(context, new ImagesUtils.OnChooserFileResult() {
                    @Override
                    public void OnResult(String filePath) {
                        selectedFile = filePath;
                        fButtonSelect.setText("Image Selected");
                    }
                });
            }
        });
        fButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFile != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();

                    String imageName = UUID.randomUUID().toString();
                    final StorageReference reference = storageReference.child("images/" + imageName);
                    Uri uri = ImagesUtils.getUriFromFilePath(context, selectedFile);
                    if (uri != null)
                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageName = edt_name.getText().toString();
                                        String description = edt_description.getText().toString();
                                        String discount = edt_discount.getText().toString();
                                        String price = edt_price.getText().toString();

                                        newFood = new Food(imageName, uri.toString(), description,
                                                price, discount, categoryId);
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Uploaded Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() /
                                        taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + progress + "%");
                            }
                        });
                }
            }
        });

        builder.setView(v);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (newFood != null) {
                    food.push().setValue(newFood);
                    Toast.makeText(context, "New Food Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    @Override
    public void showUpdateDialog(final Context context, final String key, final Food foodItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Food");
        builder.setMessage("Please Fill Full Information");
        View v = LayoutInflater.from(context).inflate(R.layout.add_new_food_layout, null);

        final MaterialEditText edt_name = v.findViewById(R.id.edt_name);
        final MaterialEditText edt_description = v.findViewById(R.id.edt_description);
        final MaterialEditText edt_price = v.findViewById(R.id.edt_price);
        final MaterialEditText edt_discount = v.findViewById(R.id.edt_discount);

        edt_name.setText(foodItem.getName());
        edt_description.setText(foodItem.getDescription());
        edt_price.setText(foodItem.getPrice());
        edt_discount.setText(foodItem.getDiscount());

        final FButton fButtonSelect = v.findViewById(R.id.btn_select);
        FButton fButtonUpload = v.findViewById(R.id.btn_upload);

        fButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesUtils.startSelectImageIntent(context, new ImagesUtils.OnChooserFileResult() {
                    @Override
                    public void OnResult(String filePath) {
                        selectedFile = filePath;
                        fButtonSelect.setText("Image Selected");
                    }
                });
            }
        });
        fButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFile != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();

                    String imageName = UUID.randomUUID().toString();
                    final StorageReference reference = storageReference.child("images/" + imageName);
                    Uri uri = ImagesUtils.getUriFromFilePath(context, selectedFile);
                    if (uri != null)
                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        foodItem.setImage(uri.toString());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Uploaded Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() /
                                        taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + progress + "%");
                            }
                        });
                }
            }
        });

        builder.setView(v);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                foodItem.setName(edt_name.getText().toString());
                foodItem.setDescription(edt_description.getText().toString());
                foodItem.setPrice(edt_price.getText().toString());
                foodItem.setDiscount(edt_discount.getText().toString());

                food.child(key).setValue(foodItem);

            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void showDeleteDialog(final Context context, final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Food");
        builder.setMessage("Are You Sure Delete This Food Item");

        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                food.child(key).removeValue();

            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

}
