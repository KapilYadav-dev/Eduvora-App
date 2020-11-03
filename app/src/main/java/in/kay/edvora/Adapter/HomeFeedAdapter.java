package in.kay.edvora.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        PostedBy pb=list.get(position).getPostedBy();
        Id id=pb.getId();
        String username=id.getName();
        String strDate=list.get(position).getCreatedAt();
        Log.d("DATEIS", "GetDate: "+ strDate);
        Date postDate=GetDate(strDate);
        Integer difference=GetDateDiff(postDate);
        if (difference==0)
        {
            holder.tvDate.setText("Asked Today");
        }
        else if (difference==1)
        {
            holder.tvDate.setText("Asked Yesterday");
        }
        else {
            holder.tvDate.setText(difference+" days ago");
        }
        holder.tvTopic.setText(list.get(position).getSubject()+" ● "+list.get(position).getTopic());
        holder.tvQuestion.setText(list.get(position).getQuestion());
        holder.tvName.setText(username);
    }

    private int GetDateDiff(Date date) {
        Date currentDate = new Date();
        Date postDate=date;
        int diff = DateTimeUtils.getDateDiff(currentDate,postDate, DateTimeUnits.DAYS);
        return diff;
    }

    private Date GetDate(String string) {

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
        TextView tvName, tvTopic, tvQuestion,tvDate;
        CardView cardView;
        LinearLayout llAnswer, llBookmark, llShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTopic = itemView.findViewById(R.id.tvTopic);
            tvDate = itemView.findViewById(R.id.tvDays);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            cardView = itemView.findViewById(R.id.cardView);
            llAnswer = itemView.findViewById(R.id.llAnswer);
            llBookmark = itemView.findViewById(R.id.llBookmark);
            llShare = itemView.findViewById(R.id.llShare);
            cardView.setVisibility(View.GONE);
        }
    }
}
