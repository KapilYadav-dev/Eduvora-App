package in.kay.edvora.Views.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

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

public class AskQuestion extends AppCompatActivity {
    EditText etTopic, etQuestion, etSubject;
    TextView tvTopic, tvQuestion, tvSubject;
    ImageView ivAttach, close;
    Button ask;
    Uri imgUri;
    private StorageReference mStorageRef;
    String imgUrl = null;
    Boolean isUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        Initz();
    }

    @Override
    protected void onResume() {
        super.onResume();
        close.setEnabled(true);
    }

    private void Initz() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        etTopic = findViewById(R.id.et_topic);
        etQuestion = findViewById(R.id.et_question);
        etSubject = findViewById(R.id.et_subject);
        tvTopic = findViewById(R.id.tvTopic);
        close = findViewById(R.id.close);
        ivAttach = findViewById(R.id.iv);
        ask = findViewById(R.id.button);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvSubject = findViewById(R.id.tvSubject);
        TextWatcherLogic();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close.setEnabled(false);
                onBackPressed();
            }
        });
        ask.setOnClickListener(view -> AddQuestion());
        ivAttach.setOnClickListener(view -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setOutputCompressQuality(50)
                .start(AskQuestion.this));
    }

    private void AddQuestion() {
        String question = etQuestion.getText().toString();
        String topic = etTopic.getText().toString();
        String subject = etSubject.getText().toString();
        if (TextUtils.isEmpty(question)) {
            etQuestion.setError("Please enter a question to continue.");
            etQuestion.requestFocus();
        } else {
            if (imgUri != null) {
                imgUrl = UploadImageToDatabase(imgUri);
            } else {
                imgUrl = null;
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiInterface.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<ResponseBody> call = apiInterface.askQuestion(question, topic, imgUrl, subject, "Bearer " + Prefs.getString("accessToken", ""));
                final ProgressDialog pd = new ProgressDialog(this);
                pd.setMax(100);
                pd.setMessage("Adding question...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();
                pd.setCancelable(false);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            pd.dismiss();
                            CustomToast customToast = new CustomToast();
                            customToast.ShowToast(AskQuestion.this, "Your question has been added successfully.");
                            GoBack();
                        } else if (response.code() == 502) {
                            MyApplication myApplication = new MyApplication();
                            myApplication.RefreshToken(Prefs.getString("refreshToken", ""), AskQuestion.this);
                            AddQuestion();
                        } else {
                            try {
                                pd.dismiss();
                                String error = response.errorBody().string();
                                CustomToast customToast = new CustomToast();
                                customToast.ShowToast(AskQuestion.this, "Response error " + error);
                            } catch (IOException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }

                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pd.dismiss();
                        CustomToast customToast = new CustomToast();
                        customToast.ShowToast(AskQuestion.this, "Retrpfot error " + t.getLocalizedMessage());
                    }
                });
            }

        }


    }

    private void TextWatcherLogic() {
        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = etSubject.getText().toString().length();
                tvSubject.setText(length + "/50");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = etQuestion.getText().toString().length();
                tvQuestion.setText(length + "/5000");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = etTopic.getText().toString().length();
                tvTopic.setText(length + "/50");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (etQuestion.getText().length() < 1) {
            super.onBackPressed();
            Animatoo.animateSlideDown(this);
            this.finish();
        } else {
            GoBack();
        }
    }

    private void GoBack() {
        startActivity(new Intent(this, MainActivity.class));
        Animatoo.animateSlideDown(this);
        this.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get().load(resultUri).into(ivAttach);
                imgUri = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(this, "Error occurred while choosing image " + error);
            }
        }
    }

    private String UploadImageToDatabase(Uri uri) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setMessage("Uploading image...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        final StorageReference ImageName = mStorageRef.child("PostImages").child("img_" + uri.getLastPathSegment());
        ImageName.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imgUrl = String.valueOf(uri);
                        UploadDatatoServer(imgUrl);
                    }
                });
            }
        }).addOnFailureListener(e -> pd.dismiss());
        return imgUrl;
    }

    private void UploadDatatoServer(String string) {
        String question = etQuestion.getText().toString();
        String topic = etTopic.getText().toString();
        String subject = etSubject.getText().toString();
        if (TextUtils.isEmpty(question)) {
            etQuestion.setError("Please enter a question to continue.");
            etQuestion.requestFocus();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.askQuestion(question, topic, string, subject, "Bearer " + Prefs.getString("accessToken", ""));
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMax(100);
            pd.setMessage("Adding question...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
            pd.setCancelable(false);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        pd.dismiss();
                        CustomToast customToast = new CustomToast();
                        customToast.ShowToast(AskQuestion.this, "Your question has been added successfully.");
                        GoBack();
                    } else if (response.code() == 502) {
                        MyApplication myApplication = new MyApplication();
                        myApplication.RefreshToken(Prefs.getString("refreshToken", ""), AskQuestion.this);
                        AddQuestion();
                    } else {
                        try {
                            pd.dismiss();
                            String error = response.errorBody().string();
                            CustomToast customToast = new CustomToast();
                            customToast.ShowToast(AskQuestion.this, "Response error " + error);
                        } catch (IOException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }

                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(AskQuestion.this, "Retrpfot error " + t.getLocalizedMessage());
                }
            });
        }

    }
}