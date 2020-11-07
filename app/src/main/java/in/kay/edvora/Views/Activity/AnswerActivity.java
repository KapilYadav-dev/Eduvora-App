package in.kay.edvora.Views.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import in.kay.edvora.R;
import in.kay.edvora.Repository.AnswerRepository;
import in.kay.edvora.Repository.HomeRepository;

public class AnswerActivity extends AppCompatActivity {
    String userId, userImage, question, postImage, name, days, postID, topic;
    TextView tvName, tvDays, tvQuestion, tvTopic;
    ImageView iv_profileImage, iv_postImage,ivBack,ivSend;
    EditText etAns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        GetValues();
        Initz();
        LoadData();
    }

    private void LoadData() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Integer difference = Integer.parseInt(days);
        if (difference == 0) {
            tvDays.setText("Asked Today");
        } else if (difference == 1) {
            tvDays.setText("Asked Yesterday");
        } else if (difference > 356) {
            tvDays.setText("Asked a long time ago");
        } else {
            tvDays.setText(difference + " days ago");
        }
        tvTopic.setText(topic);
        tvQuestion.setText(question);
        tvName.setText(name);
        if (!TextUtils.isEmpty(postImage)) {
            Picasso.get().load(postImage).error(R.drawable.ic_image_holder).placeholder(R.drawable.ic_image_holder).into(iv_postImage);
        } else {
            findViewById(R.id.cardView).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(userImage) && postImage != "") {
            Picasso.get().load(userImage).error(R.drawable.ic_image_holder).placeholder(R.drawable.ic_image_holder).into(iv_profileImage);
        }
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etAns.getText().toString()))
                {
                    etAns.setError("Please enter something");
                    etAns.requestFocus();
                    return;
                }
                else {
                    AnswerRepository answerRepository=new AnswerRepository(AnswerActivity.this);
                    answerRepository.SendAnswer(postID,etAns.getText().toString());
                    etAns.setText("");
                }
            }
        });
    }

    private void Initz() {
        tvDays = findViewById(R.id.tvDays);
        tvName = findViewById(R.id.tvName);
        ivBack=findViewById(R.id.back);
        tvQuestion = findViewById(R.id.tvQuestion);
        ivSend = findViewById(R.id.iv_send);
        tvTopic = findViewById(R.id.tvTopic);
        etAns = findViewById(R.id.et_answer);
        iv_profileImage = findViewById(R.id.iv_profile);
        iv_postImage = findViewById(R.id.iv_postimg);
    }

    private void GetValues() {
        question = getIntent().getStringExtra("question");
        userId = getIntent().getStringExtra("userId");
        userImage = getIntent().getStringExtra("profileimageUrl");
        postImage = getIntent().getStringExtra("postimageUrl");
        name = getIntent().getStringExtra("name");
        days = getIntent().getStringExtra("days");
        topic = getIntent().getStringExtra("topic");
        postID = getIntent().getStringExtra("postID");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}