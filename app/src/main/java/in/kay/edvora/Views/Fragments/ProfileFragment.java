package in.kay.edvora.Views.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pixplicity.easyprefs.library.Prefs;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import in.kay.edvora.Views.Activity.Landing;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    Context context;
    View view;
    CircleImageView circleImageView;
    TextView logout;
    ShimmerTextView etName, etBranch, etYear, etClg;
    Shimmer shimmer1, shimmer2, shimmer3, shimmer4;
    ImageView etPhotoEdit;
    StorageReference mStorageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        Initz();
    }

    private void Initz() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        circleImageView = view.findViewById(R.id.circleImageView);
        etPhotoEdit = view.findViewById(R.id.editPhoto);
        etName = view.findViewById(R.id.et_name);
        logout = view.findViewById(R.id.logout);
        etBranch = view.findViewById(R.id.et_branch);
        etYear = view.findViewById(R.id.et_year);
        etClg = view.findViewById(R.id.et_college);
        shimmer1 = new Shimmer();
        shimmer1.start(etName);
        shimmer2 = new Shimmer();
        shimmer2.start(etBranch);
        shimmer3 = new Shimmer();
        shimmer3.start(etClg);
        shimmer4 = new Shimmer();
        shimmer4.start(etYear);
        FetchValue();
        etPhotoEdit.setOnClickListener(view -> {
            Integer difference;
            Long date = Prefs.getLong("updatedImageDate", 0);
            if (date != null) {
                difference = GetDateDiff(new Date(date));
            } else
                difference = null;

            Toast.makeText(context, "duff is"+difference, Toast.LENGTH_SHORT).show();
            if (difference == null || difference > 7)
                ChoosePhoto();
            else {
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "You can change your profile photo after "+Integer.toString(7-difference)+" days...");
            }
        });
        logout.setOnClickListener(view -> {
            Logout();
        });
    }

    private void Logout() {
        Prefs.clear();
        Intent intent = new Intent(context, Landing.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void ChoosePhoto() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setOutputCompressQuality(50)
                .start(getActivity());
    }

    private void FetchValue() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = api.viewProfile(Prefs.getString("userId", ""), "Bearer " + Prefs.getString("accessToken", ""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        shimmer1.cancel();
                        shimmer2.cancel();
                        shimmer3.cancel();
                        shimmer4.cancel();
                        String string = response.body().string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject object = jsonObject.getJSONObject("user");
                        String name = object.getString("name");
                        String imageUrl;
                        try {
                            imageUrl = object.getString("imageUrl");
                        } catch (JSONException e) {
                            imageUrl = "";
                        }
                        String branch = object.getString("branch");
                        String college = object.getString("college");
                        Integer year = object.getInt("year");
                        if (year == 0) {
                            etYear.setText("N/A");
                        } else {
                            etYear.setText(Integer.toString(year));
                        }
                        etBranch.setText(branch);
                        etClg.setText(college);
                        etName.setText(name);
                        if (TextUtils.isEmpty(imageUrl))
                            Picasso.get().load(R.drawable.ic_image_holder).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(circleImageView);
                        else
                            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(circleImageView);
                    } else if (response.code() == 502) {
                        MyApplication myApplication = new MyApplication();
                        myApplication.RefreshToken(Prefs.getString("refreshToken", ""), context);
                        FetchValue();
                    } else {
                        CustomToast customToast = new CustomToast();
                        customToast.ShowToast(context, "Error: Server is down..");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Failure: Server is down.." + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get().load(resultUri).into(circleImageView);
                UploadImageToDatabase(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Error occurred while choosing image " + error);
            } else {

            }
        }
    }

    private void UploadImageToDatabase(Uri uri) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setMessage("Uploading image...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        final StorageReference ImageName = mStorageRef.child("PostImages").child("img_" + uri.getLastPathSegment());
        ImageName.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            ImageName.getDownloadUrl().addOnSuccessListener(uri1 -> {
                String imgUrl = String.valueOf(uri1);
                UploadDatatoServer(imgUrl, pd);
            });
        }).addOnFailureListener(e -> {
            pd.dismiss();
            CustomToast customToast = new CustomToast();
            customToast.ShowToast(context, "Error while uploading image to server..." + e.getLocalizedMessage());
        });

    }

    public int GetDateDiff(Date date) {
        Date currentDate = new Date();
        Date postDate = date;
        int diff = DateTimeUtils.getDateDiff(currentDate, postDate, DateTimeUnits.DAYS);
        return diff;
    }


    private void UploadDatatoServer(String imgUrl, ProgressDialog pd) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.updateProfileImage(imgUrl, "Bearer " + Prefs.getString("accessToken", ""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(context, "Your profile image has been updated successfully...");
                    Date date = new Date();
                    Prefs.putLong("updatedImageDate", date.getTime());
                } else if (response.code() == 502) {
                    MyApplication myApplication = new MyApplication();
                    myApplication.RefreshToken(Prefs.getString("refreshToken", ""), context);
                    UploadDatatoServer(imgUrl, pd);
                } else {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(context, "Error occurred...");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Failure occured " + t.getLocalizedMessage());
            }
        });
    }
}