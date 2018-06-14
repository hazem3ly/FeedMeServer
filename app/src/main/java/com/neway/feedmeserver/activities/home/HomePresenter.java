package com.neway.feedmeserver.activities.home;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.neway.feedmeserver.R;
import com.neway.feedmeserver.adapters.MenuViewHolder;
import com.neway.feedmeserver.bases.BasePresenter;
import com.neway.feedmeserver.interfaces.OnItemClickListener;
import com.neway.feedmeserver.lib.imagesUtils.ImagesUtils;
import com.neway.feedmeserver.model.Category;
import com.neway.feedmeserver.widget.FButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    // storage configuration
    FirebaseStorage storage;
    StorageReference storageReference;
    private String selectedFile = null;
    private Category newCategory = null;

    public HomePresenter() {
        database = FirebaseDatabase.getInstance();
        category = database.getReference("category");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("uploaded/");
    }

    @Override
    public void loadCategories() {
        getView().showLoading();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.menu_text.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.menu_image);

                final Category s = model;
                viewHolder.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        getView().onItemClicked(adapter.getRef(position).getKey(), s);
                    }
                });
            }
        };
        getView().hideLoading();
        getView().onDataLoaded(adapter);
    }

    @Override
    public void showAddDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add New Category");
        builder.setMessage("Please Fill Full Information");
        View v = LayoutInflater.from(context).inflate(R.layout.add_new_menu_layout, null);
        final MaterialEditText editText = v.findViewById(R.id.edt_name);
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
                                        String imageName = editText.getText().toString();
                                        newCategory = new Category();
                                        newCategory.setName(imageName);
                                        newCategory.setImage(uri.toString());
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
                if (newCategory != null) {
                    category.push().setValue(newCategory);
                    Toast.makeText(context, "New Category Added", Toast.LENGTH_SHORT).show();
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
    public void showUpdateDialog(final Context context, final String key, final Category categoryItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Category");
        builder.setMessage("Please Fill Full Information");
        View v = LayoutInflater.from(context).inflate(R.layout.add_new_menu_layout, null);
        final MaterialEditText editText = v.findViewById(R.id.edt_name);
        editText.setText(categoryItem.getName());

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
                                        String imageName = editText.getText().toString();
                                        categoryItem.setName(imageName);
                                        categoryItem.setImage(uri.toString());
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
                String imageName = editText.getText().toString();
                categoryItem.setName(imageName);

                category.child(key).setValue(categoryItem);

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
        builder.setTitle("Delete Category");
        builder.setMessage("Are You Sure Delete This Category");

        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                category.child(key).removeValue();

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
