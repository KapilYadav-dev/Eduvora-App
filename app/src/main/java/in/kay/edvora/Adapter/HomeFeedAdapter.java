package in.kay.edvora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kay.edvora.Models.HomeModel;
import in.kay.edvora.R;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    List<HomeModel> list;
    Context context;
    public HomeFeedAdapter(List<HomeModel> list, Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTopic.setText(list.get(position).getSubject()+" ● "+list.get(position).getTopic());
        holder.tvQuestion.setText(list.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvName, tvTopic, tvQuestion;
        CardView cardView;
        LinearLayout llAnswer, llBookmark, llShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTopic = itemView.findViewById(R.id.tvTopic);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            cardView = itemView.findViewById(R.id.cardView);
            llAnswer = itemView.findViewById(R.id.llAnswer);
            llBookmark = itemView.findViewById(R.id.llBookmark);
            llShare = itemView.findViewById(R.id.llShare);
            cardView.setVisibility(View.GONE);
        }
    }
}
