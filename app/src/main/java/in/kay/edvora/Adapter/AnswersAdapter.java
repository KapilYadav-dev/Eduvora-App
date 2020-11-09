package in.kay.edvora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kay.edvora.Models.Answers;
import in.kay.edvora.Models.User;
import in.kay.edvora.R;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {
    List<Answers> list;
    Context context;

    public AnswersAdapter(List<Answers> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.answer_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        List<String> words = Arrays.asList(context.getResources().getStringArray(R.array.censored));
        Answers answers = list.get(position);
        User user = answers.getUser();
        String strans = answers.getAnswer();
        String newtext = GetCensored(words, strans);
        String date = answers.getAnsweredAt();
        String ans_id = answers.getAnsweredAt();
        int difference = GetDateDiff(date);
        String name = user.getName();
        String imageUrl = user.getImageUrl();
        String user_id = user.get_id();
        String college = user.getCollege();
        holder.tvName.setText(name);
        holder.tvAnswer.setText(newtext);
        if (difference == 0) {
            holder.tvDays.setText("Answered Today");
        } else if (difference == 1) {
            holder.tvDays.setText("Answered Yesterday");
        } else if (difference > 356) {
            holder.tvDays.setText("Answered a long time ago");
        } else {
            holder.tvDays.setText(difference + " days ago");
        }
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(holder.circleImageView);
    }

    private String GetCensored(List<String> words, String strans) {
        String string = "";
        for (String word : words) {
            Pattern rx = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);
            strans = rx.matcher(strans).replaceAll(new String(new char[word.length()]).replace('\0', '*'));
            string = strans;
        }
        return string;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAnswer, tvDays;
        CircleImageView circleImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            tvDays = itemView.findViewById(R.id.tvDays);
            tvName = itemView.findViewById(R.id.tvName);
            circleImageView = itemView.findViewById(R.id.circleImageView);
        }
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

    public int GetDateDiff(String string) {
        Date currentDate = new Date();
        Date postDate = GetDate(string);
        int diff = DateTimeUtils.getDateDiff(currentDate, postDate, DateTimeUnits.DAYS);
        return diff;
    }
}
