package in.kay.edvora.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.List;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.Models.DeletePostRequestModel;
import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import in.kay.edvora.Views.Activity.LibraryContent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MySubjetsAdapter extends RecyclerView.Adapter<MySubjetsAdapter.ViewHolder> {
    List<String> list;
    Context context;

    public MySubjetsAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subject_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvSubject.setText(list.get(position));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, LibraryContent.class);
            intent.putExtra("Subject", list.get(position));
            context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(view -> {
            ShowDiag(position);
            return false;
        });
    }

    private void ShowDiag(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Subject");
        builder.setMessage("Do you really want to delete " + list.get(position) + " ?");
        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            DeleteSubject(dialogInterface,position);
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void DeleteSubject(DialogInterface dialogInterface, int position) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setMessage("Deleting...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        DeletePostRequestModel deletePostRequestModel=new DeletePostRequestModel(list.get(position));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.deleteSubject(deletePostRequestModel,"Bearer "+ Prefs.getString("accessToken",""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    pd.dismiss();
                    CustomToast customToast=new CustomToast();
                    customToast.ShowToast(context,"Successfully deleted Subject");
                    dialogInterface.dismiss();
                }
                else if (response.code()==502) {
                    MyApplication myApplication = new MyApplication();
                    myApplication.RefreshToken(Prefs.getString("refreshToken", ""), context);
                    DeleteSubject(dialogInterface, position);
                }
                else {
                    pd.dismiss();
                    try {
                        String msg=response.errorBody().string();
                        CustomToast customToast=new CustomToast();
                        customToast.ShowToast(context,"Error "+msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialogInterface.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast=new CustomToast();
                customToast.ShowToast(context,"Failure "+t.getLocalizedMessage());
                dialogInterface.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
        }
    }
}
