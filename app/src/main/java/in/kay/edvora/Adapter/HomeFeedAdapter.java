package in.kay.edvora.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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
import in.kay.edvora.Utils.CustomToast;
import in.kay.edvora.Views.Activity.AnswerActivity;
import in.kay.edvora.Views.Activity.Profile;

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
        final String username = id.getName();
        final String userID = id.get_id();
        final String userimage = id.getImageUrl();
        String strDate = list.get(position).getCreatedAt();
        Date postDate = GetDate(strDate);
        final Integer difference = GetDateDiff(postDate);
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
                                    .placeholder(R.drawable.img_place_holder)
                                    .error(R.drawable.img_place_holder)
                                    .into(holder.iv_postimg);
                        }
                    });
        } else  holder.cardView.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(userimage))
        {
            Picasso.get()
                    .load(userimage)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(userimage)
                                    .placeholder(R.drawable.img_place_holder)
                                    .error(R.drawable.img_place_holder)
                                    .into(holder.circleImageView);
                        }
                    });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,holder.cl, ViewCompat.getTransitionName(holder.cl));
                Intent intent=new Intent(context,AnswerActivity.class);
                intent.putExtra("question",list.get(position).getQuestion());
                intent.putExtra("days",Integer.toString(difference));
                intent.putExtra("postID",list.get(position).get_id());
                intent.putExtra("name",list.get(position).getPostedBy().getId().getName());
                intent.putExtra("topic",list.get(position).getSubject() + " ● " + list.get(position).getTopic());
                if (list.get(position).getImageUrl()!=null)
                {
                    intent.putExtra("postimageUrl",list.get(position).getImageUrl());
                }
                else {
                    intent.putExtra("postimageUrl", "");
                }
                if (!TextUtils.isEmpty(userimage))
                {
                    intent.putExtra("profileimageUrl",userimage);
                }
                else {
                    intent.putExtra("profileimageUrl", "");
                }
                intent.putExtra("userId",userID);
                context.startActivity(intent,options.toBundle());
            }
        });
        holder.llBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomToast customToast=new CustomToast();
                customToast.ShowToast(context,"Bookmark is clicked");
            }
        });
        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Edvora");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, list.get(position).getQuestion().substring(0,Math.min(list.get(position).getQuestion().length(),160))+"..."+"\n"+"-by  "+list.get(position).getPostedBy().getId().getName()+"\n"+"https://www.edvora.in/"+list.get(position).get_id());
                context.startActivity(Intent.createChooser(txtIntent ,"Share"));
            }
        });
        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View image = holder.circleImageView;
                View text = holder.tvName;
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) context, Pair.create(image, "Profile"), Pair.create(text, "Name"));
                Intent intent=new Intent(context, Profile.class);
                intent.putExtra("userId",userID);
                context.startActivity(intent,options.toBundle());
            }
        });
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
        ConstraintLayout cl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tvName);
            cl=itemView.findViewById(R.id.cl);
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
