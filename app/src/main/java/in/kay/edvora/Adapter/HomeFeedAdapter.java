package in.kay.edvora.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kay.edvora.Models.HomeModel;
import in.kay.edvora.Models.Id;
import in.kay.edvora.Models.PostedBy;
import in.kay.edvora.R;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    List<HomeModel> list;
    Context context;

    public HomeFeedAdapter(List<HomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setNewData(List<HomeModel> list, Context context)
    {
        this.context = context;
        this.list=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        PostedBy pb = list.get(position).getPostedBy();
        Id id = pb.getId();
        String username = id.getName();
        String strDate = list.get(position).getCreatedAt();
        Date postDate = GetDate(strDate);
        Integer difference = GetDateDiff(postDate);
        if (difference == 0) {
            holder.tvDate.setText("Asked Today");
        } else if (difference == 1) {
            holder.tvDate.setText("Asked Yesterday");
        } else if (difference > 356) {
            holder.tvDate.setText("Asked a long time ago");
        } else {
            holder.tvDate.setText(difference + " days ago");
        }
        if (TextUtils.isEmpty(list.get(position).getSubject()) && TextUtils.isEmpty(list.get(position).getTopic()))
        {
            holder.tvTopic.setVisibility(View.GONE);
        }
        else if (TextUtils.isEmpty(list.get(position).getTopic()))
        {
            holder.tvTopic.setText(list.get(position).getSubject());
        }
        else if (TextUtils.isEmpty(list.get(position).getSubject()))
        {
            holder.tvTopic.setText(list.get(position).getTopic());
        }
        else {
            holder.tvTopic.setText(list.get(position).getSubject() + " ● " + list.get(position).getTopic());
        }
        holder.tvQuestion.setText(list.get(position).getQuestion());
        holder.tvName.setText(username);
        if (!TextUtils.isEmpty(list.get(position).getImageUrl())) {
            holder.cardView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(list.get(position).getImageUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.iv_postimg, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(list.get(position).getImageUrl())
                                    .placeholder(R.drawable.ic_image_holder)
                                    .error(R.drawable.ic_image_holder)
                                    .into(holder.iv_postimg);
                        }
                    });
        } else  holder.cardView.setVisibility(View.GONE);
    }

    public int GetDateDiff(Date date) {
        Date currentDate = new Date();
        Date postDate = date;
        int diff = DateTimeUtils.getDateDiff(currentDate, postDate, DateTimeUnits.DAYS);
        return diff;
    }

    public Date GetDate(String string) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = format.parse(string);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvName, tvTopic, tvQuestion, tvDate;
        CardView cardView;
        LinearLayout llAnswer, llBookmark, llShare;
        ImageView iv_postimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTopic = itemView.findViewById(R.id.tvTopic);
            tvDate = itemView.findViewById(R.id.tvDays);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            iv_postimg = itemView.findViewById(R.id.iv_postimg);
            cardView = itemView.findViewById(R.id.cardView);
            llAnswer = itemView.findViewById(R.id.llAnswer);
            llBookmark = itemView.findViewById(R.id.llBookmark);
            llShare = itemView.findViewById(R.id.llShare);
            cardView.setVisibility(View.GONE);
        }
    }
}
