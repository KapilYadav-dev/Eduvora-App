package in.kay.edvora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import in.kay.edvora.Models.QuestionsModel;
import in.kay.edvora.R;

public class QuestionSearchAdapter extends RecyclerView.Adapter<QuestionSearchAdapter.ViewHolder> implements Filterable {
    List<QuestionsModel> list;
    List<QuestionsModel> listAll;
    Context context;

    public QuestionSearchAdapter(List<QuestionsModel> list, Context context) {
        this.list = list;
        this.listAll = new ArrayList<>(list);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(view -> {
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<QuestionsModel> filteredlist =new ArrayList<>();
            if (charSequence.toString().isEmpty())
            {
                filteredlist.addAll(listAll);
            }else{
              String string=charSequence.toString().toLowerCase().trim();
              for (QuestionsModel model:listAll){
                  if (model.getName().toLowerCase().contains(string))
                  {
                      filteredlist.add(model);
                  }
              }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv);

        }
    }
}
