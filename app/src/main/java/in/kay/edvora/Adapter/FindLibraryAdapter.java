package in.kay.edvora.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import in.kay.edvora.Models.FindLibraryModel;
import in.kay.edvora.R;

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
        holder.itemView.setOnClickListener(view -> {
            Uri uri = Uri.parse(model.getUrl());
            String fileExt = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            if (fileExt.equalsIgnoreCase("pdf"))
                newIntent.setDataAndType(uri,"application/pdf");
            else if (fileExt.equalsIgnoreCase("doc"))
                newIntent.setDataAndType(uri,"application/msword");
            else if (fileExt.equalsIgnoreCase("docx"))
                newIntent.setDataAndType(uri,"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
