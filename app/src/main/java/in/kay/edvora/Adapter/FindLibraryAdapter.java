package in.kay.edvora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        FindLibraryModel model=list.get(position);
            holder.tvSubject.setText(list.get(position).getSubject());
            holder.tvTitle.setText(list.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject=itemView.findViewById(R.id.tvSubject);
            tvTitle=itemView.findViewById(R.id.tvTitle);
        }
    }
}
