package in.kay.edvora.Adapter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.Models.FindLibraryModel;
import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import in.kay.edvora.Views.Activity.AskQuestion;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindLibraryAdapter extends RecyclerView.Adapter<FindLibraryAdapter.ViewHolder> {
    List<FindLibraryModel> list;
    Context context;

    public FindLibraryAdapter(List<FindLibraryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.library_content_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        FindLibraryModel model = list.get(position);
        holder.tvSubject.setText(model.getSubject() + "   ||   " + model.getYear() + " Year");
        holder.tvTitle.setText(model.getTitle());
        holder.tvBy.setText("Uploaded by "+model.getCreatedBy().getName());
        Uri uri = Uri.parse(model.getUrl());
        String fileExt = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (fileExt.equalsIgnoreCase("pdf"))
        {
            holder.iv.setImageResource(R.drawable.ic_pdf);
        }
        else if (fileExt.equalsIgnoreCase("doc") || fileExt.equalsIgnoreCase("docx"))
        {
            holder.iv.setImageResource(R.drawable.ic_doc);
        }
        else if (fileExt.equalsIgnoreCase("ppt") || fileExt.equalsIgnoreCase("pptx"))
        {
            holder.iv.setImageResource(R.drawable.ic_ppt);
        }
        holder.itemView.setOnClickListener(view -> {
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            if (fileExt.equalsIgnoreCase("pdf"))
                newIntent.setDataAndType(uri,"application/pdf");
            else if (fileExt.equalsIgnoreCase("doc"))
                newIntent.setDataAndType(uri,"application/msword");
            else if (fileExt.equalsIgnoreCase("docx"))
                newIntent.setDataAndType(uri,"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            else if (fileExt.equalsIgnoreCase("ppt") ||fileExt.equalsIgnoreCase("pptx") )
                newIntent.setDataAndType(uri,"application/vnd.openxmlformats-officedocument.presentationml.presentation");
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }
        });
        holder.ivDownload.setOnClickListener(view -> {
            File myDirectory = new File("/Edvora");
            if(!myDirectory.exists()) {
                myDirectory.mkdirs();
            }
            MyApplication myApplication=new MyApplication();
            myApplication.DownloadFile(model.getUrl(), Prefs.getString("subject",""),model.getTitle(),myDirectory.getPath(),fileExt,context);
            CustomToast customToast=new CustomToast();
            customToast.ShowToast(context,"Downloading Started...");
        });
        holder.ivBookmark.setOnClickListener(view -> {
            DoWork(model);
        });
    }

    private void DoWork(FindLibraryModel model) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setMessage("Loading...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call=api.bookMarkContent(model.getId(),"Bearer "+Prefs.getString("accessToken",""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(context, "Added to bookmark");
                } else if (response.code() == 502) {
                    MyApplication myApplication = new MyApplication();
                    myApplication.RefreshToken(Prefs.getString("refreshToken", ""), context);
                    DoWork(model);
                } else {
                    try {
                        String error = response.errorBody().string();
                        CustomToast customToast = new CustomToast();
                        JSONObject jsonObject=new JSONObject(error);
                        String msg=jsonObject.getString("message");
                        customToast.ShowToast(context, msg);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Server down...");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubject,tvBy;
        ImageView iv,ivBookmark,ivDownload;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBy = itemView.findViewById(R.id.tvBy);
            ivBookmark = itemView.findViewById(R.id.ivBookmark);
            ivDownload = itemView.findViewById(R.id.ivDownload);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
