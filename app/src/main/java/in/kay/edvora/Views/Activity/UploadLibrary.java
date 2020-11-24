package in.kay.edvora.Views.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadLibrary extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 999;
    TextView tvSem, tvType, tvName,etSubject;
    EditText etTitle;
    ImageView ivSelect;
    RelativeLayout rl;
    Uri uri;
    SpinnerDialog spinnerDialog;
    ArrayList<String> year = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();
    ArrayList<String> subject = new ArrayList<>();
    String choosen, imgUrl;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_library);
        Initz();
        ivSelect.setOnClickListener(view -> {
            SelectFile();
        });
        tvSem.setOnClickListener(view -> {
            SelectYear();
        });
        tvType.setOnClickListener(view -> {
            SelectType();
        });
        etSubject.setOnClickListener(view -> {
            SelectSubject();
        });

    }

    private void SelectType() {
        type.add("Books");
        type.add("Notes");
        type.add("Papers");
        spinnerDialog = new SpinnerDialog(this, type, "Select doc type", R.style.DialogAnimations, "");// With 	Animation
        spinnerDialog.setCancellable(false); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.setItemColor(Color.parseColor("#263238"));
        spinnerDialog.showSpinerDialog();
        spinnerDialog.bindOnSpinerListener((item, position) -> {
            choosen = item;
            type.clear();
            tvType.setText(choosen);
            tvType.setTextColor(getResources().getColor(R.color.colorBlack));
        });
    }

    private void SelectYear() {
        year.add("1st Year");
        year.add("2nd Year");
        year.add("3rd Year");
        year.add("Final Year");
        spinnerDialog = new SpinnerDialog(this, year, "Select  year", R.style.DialogAnimations, "");// With 	Animation
        spinnerDialog.setCancellable(false); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.setItemColor(Color.parseColor("#263238"));
        spinnerDialog.showSpinerDialog();
        spinnerDialog.bindOnSpinerListener((item, position) -> {
            choosen = item;
            year.clear();
            tvSem.setText(choosen);
            tvSem.setTextColor(getResources().getColor(R.color.colorBlack));
        });
    }

    private void SelectSubject() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setMessage("Loading...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getSubjects();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            subject.add(name);
                        }
                        DoSelectSubject();
                        pd.dismiss();
                    }
                    else {
                        pd.dismiss();
                        CustomToast customToast=new CustomToast();
                        customToast.ShowToast(getBaseContext(),"Server down...");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast=new CustomToast();
                customToast.ShowToast(getBaseContext(),"Server down...");
            }
        });
    }

    private void DoSelectSubject() {
        spinnerDialog = new SpinnerDialog(UploadLibrary.this, subject, "Select subject", R.style.DialogAnimations, "");// With 	Animation
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.setItemColor(Color.parseColor("#263238"));
        spinnerDialog.showSpinerDialog();
        spinnerDialog.bindOnSpinerListener((item, position) -> {
            choosen = item;
            subject.clear();
            runOnUiThread(() -> {
                etSubject.setText(choosen);
                etSubject.setTextColor(getResources().getColor(R.color.colorBlack));
            });
        });
    }

    private void SelectFile() {
        Intent chooseIntent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"text/csv", "application/pdf", "application/msword", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/vnd.openxmlformats-officedocument.presentationml.presentation"};
        chooseIntent.setType("*/*");
        chooseIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(chooseIntent, PICK_FILE_REQUEST);
    }

    private void Initz() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        tvName = findViewById(R.id.tvFileName);
        tvSem = findViewById(R.id.et_sem);
        tvType = findViewById(R.id.et_type);
        etTitle = findViewById(R.id.et_topic);
        etSubject = findViewById(R.id.et_subject);
        ivSelect = findViewById(R.id.iv_select);
        rl = findViewById(R.id.rl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String fileName = getFileName(uri);
            rl.setVisibility(View.VISIBLE);
            ivSelect.setVisibility(View.GONE);
            tvName.setText(fileName);
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void Upload(View view) {
        if (TextUtils.isEmpty(tvSem.getText().toString())) {
            tvSem.setError("Please choose an year");
            tvSem.requestFocus();
            return;
        } else if (TextUtils.isEmpty(tvType.getText().toString())) {
            tvType.setError("Please choose doc type");
            tvType.requestFocus();
            return;
        } else if (TextUtils.isEmpty(etSubject.getText().toString())) {
            etSubject.setError("Please enter proper subject");
            etSubject.requestFocus();
            return;
        } else if (TextUtils.isEmpty(etTitle.getText().toString())) {
            etTitle.setError("Please choose appropriate title.");
            etTitle.requestFocus();
            return;
        } else if (uri == null) {
            CustomToast customToast = new CustomToast();
            customToast.ShowToast(UploadLibrary.this, "Please choose something to upload...");
            return;
        } else {
            long sizeinMb = getFileSize(uri) / 1024 / 1024;
            if (sizeinMb < 10)
                UploadImageToDatabase(uri);
            else {
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(UploadLibrary.this, "Please use a document <10 MB.\nReduce further to " + Integer.toString((int) (sizeinMb - 10)) + " MB");
            }
        }
    }

    private long getFileSize(Uri fileUri) {
        Cursor returnCursor = getContentResolver().
                query(fileUri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();

        return returnCursor.getLong(sizeIndex);
    }

    private void UploadImageToDatabase(Uri uri) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setMessage("Uploading document...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        final StorageReference ImageName = mStorageRef.child("Docs").child("doc_" + getFileName(uri));
        ImageName.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            ImageName.getDownloadUrl().addOnSuccessListener(uri1 -> {
                imgUrl = String.valueOf(uri1);
                DoWork(imgUrl, pd);
            });
        }).addOnFailureListener(e -> {
            pd.dismiss();
            CustomToast customToast = new CustomToast();
            customToast.ShowToast(UploadLibrary.this, "Unable to upload image " + e.getLocalizedMessage());
        });
    }

    private void DoWork(String imgUrl, ProgressDialog pd) {
        Integer year;
        year = GetYear();
        String strYear = Integer.toString(year);
        String doctype = tvType.getText().toString();
        String title = etTitle.getText().toString();
        String subject = etSubject.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.uploadFile(title, strYear, subject, doctype, imgUrl,"Bearer "+Prefs.getString("accessToken",""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("MYTAG", "onResponse: "+response.code());
                if (response.isSuccessful()) {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(UploadLibrary.this, "Added " + doctype + " Successfully...");
                    onBackPressed();
                } else if (response.code() == 502) {
                    MyApplication application = new MyApplication();
                    application.RefreshToken(Prefs.getString("refreshToken", ""), UploadLibrary.this);
                    DoWork(imgUrl, pd);
                } else {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(UploadLibrary.this, "Something went wrong");
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(UploadLibrary.this, "Something went wrong " + t.getLocalizedMessage());
                onBackPressed();
            }
        });
    }

    private int GetYear() {
        switch (tvSem.getText().toString()) {
            case "1st Year":
                return 1;
            case "2nd Year":
                return 2;
            case "3rd Year":
                return 3;
            case "Final Year":
                return 4;
            default:
                return 0;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideDown(UploadLibrary.this);
        finish();
    }

    public void onBack(View view) {
        onBackPressed();
    }
}