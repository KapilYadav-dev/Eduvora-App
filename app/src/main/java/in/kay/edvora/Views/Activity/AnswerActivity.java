package in.kay.edvora.Views.Activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.kay.edvora.Adapter.AnswersAdapter;
import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.Models.Answers;
import in.kay.edvora.Models.HomeModel;
import in.kay.edvora.R;
import in.kay.edvora.Repository.AnswerRepository;
import in.kay.edvora.Utils.CustomToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnswerActivity extends AppCompatActivity {
    String userId, userImage, question, postImage, name, days, postID, topic;
    TextView tvName, tvDays, tvQuestion, tvTopic;
    ImageView iv_profileImage, iv_postImage, ivBack, ivChat;
    RelativeLayout rlAnswer;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        GetValues();
        Initz();
        Uri uri = getIntent().getData();
        if (uri != null) {
            if (Prefs.getBoolean("isLoggedIn", false)) {
                List<String> params = uri.getPathSegments();
                String postId = params.get(params.size() - 1);
                ivBack.setVisibility(View.GONE);
                LoadDataFromServer(postId, 0);
            } else {
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(this, "Please login first..");
                startActivity(new Intent(this, Landing.class));
            }

        } else
            LoadData();
        OnPhotoClick();
    }

    private void OnPhotoClick() {
        iv_profileImage.setOnClickListener(view -> {
            View image = iv_profileImage;
            View text = tvName;
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AnswerActivity.this, Pair.create(image, "Profile"), Pair.create(text, "Name"));
            Intent intent = new Intent(AnswerActivity.this, Profile.class);
            intent.putExtra("userId", userId);
            startActivity(intent, options.toBundle());
        });
    }

    private void LoadDataFromServer(final String string, final int i) {
        Log.d("postId", "onCreate: " + string);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setMessage("Loading...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<HomeModel> call = apiInterface.getParticularFeed(string, "Bearer " + Prefs.getString("accessToken", ""));
        call.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (response.isSuccessful()) {
                    if (i == 0) {
                        List<Answers> answers = response.body().getAnswers();
                        HomeModel list = response.body();
                        question = list.getQuestion();
                        userId = list.getPostedBy().getId().get_id();
                        userImage = list.getPostedBy().getId().getImageUrl();
                        postImage = list.getImageUrl();
                        name = list.getPostedBy().getId().getName();
                        days = list.getCreatedAt();
                        Date postDate = GetDate(days);
                        int difference = GetDateDiff(postDate);
                        days = Integer.toString(difference);
                        topic = list.getSubject() + " ● " + list.getTopic();
                        ivChat.setVisibility(View.GONE);
                        rlAnswer.setVisibility(View.GONE);
                        LoadData();
                        LoadAnswers(answers);
                    } else if (i == 1) {
                        List<Answers> answers = response.body().getAnswers();
                        LoadAnswers(answers);
                    }
                    pd.dismiss();
                } else if (response.code() == 502) {
                    MyApplication application = new MyApplication();
                    application.RefreshToken(Prefs.getString("refreshToken", ""), AnswerActivity.this);
                    LoadDataFromServer(string, i);
                } else {
                    pd.dismiss();
                    try {
                        Toast.makeText(AnswerActivity.this, "Error is " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(AnswerActivity.this, "Failure  is " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int GetDateDiff(Date date) {
        Date currentDate = new Date();
        Date postDate = date;
        int diff = DateTimeUtils.getDateDiff(currentDate, postDate, DateTimeUnits.DAYS);
        return diff;
    }

    private void LoadData() {
        ivBack.setOnClickListener(view -> onBackPressed());
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
        if (!topic.equalsIgnoreCase(" ● ")) {
            tvTopic.setText(topic);
        } else {
            tvTopic.setVisibility(View.GONE);
        }

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
        rlAnswer.setOnClickListener(view -> ShowDiag());
        LoadDataFromServer(postID, 1);
    }

    private void LoadAnswers(List<Answers> list) {
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AnswersAdapter answersAdapter = new AnswersAdapter(list, this);
        recyclerView.setAdapter(answersAdapter);
        if (answersAdapter.getItemCount() == 0) {
            findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
            findViewById(R.id.rv).setVisibility(View.GONE);
        } else {
            findViewById(R.id.rv).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_no).setVisibility(View.GONE);
        }
    }

    private void ShowDiag() {
        ImageView close;
        TextView post, dvQuestion;
        final EditText answer;
        dialog.setContentView(R.layout.answer_diag);
        dvQuestion = dialog.findViewById(R.id.dv_question);
        close = dialog.findViewById(R.id.close);
        post = dialog.findViewById(R.id.tv_submit);
        answer = dialog.findViewById(R.id.et_answer);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dvQuestion.setText(question);
        close.setOnClickListener(view -> dialog.dismiss());
        post.setOnClickListener(view -> {
            if (TextUtils.isEmpty(answer.getText().toString())) {
                answer.setHint("Enter some answer to post");
                answer.requestFocus();
                return;
            } else {
                AnswerRepository answerRepository = new AnswerRepository(AnswerActivity.this);
                answerRepository.SendAnswer(postID, answer.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void DoRefresh(SwipeRefreshLayout layout) {
        final Handler ha = new Handler();
        ha.postDelayed(() ->RestartApp(layout), 2500);
    }

    private void RestartApp(SwipeRefreshLayout layout) {
        layout.setRefreshing(false);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void Initz() {
        dialog = new Dialog(this);
        tvDays = findViewById(R.id.tvDays);
        tvName = findViewById(R.id.tvName);
        ivBack = findViewById(R.id.back);
        rlAnswer = findViewById(R.id.rl_one);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvTopic = findViewById(R.id.tvTopic);
        iv_profileImage = findViewById(R.id.iv_profile);
        ivChat = findViewById(R.id.iv_chat);
        iv_postImage = findViewById(R.id.iv_postimg);
        SwipeRefreshLayout layout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        layout.setOnRefreshListener(() -> DoRefresh(layout) );
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
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    public void Chat(View view) {
        startActivity(new Intent(this,ChatActivity.class));
        Animatoo.animateShrink(this);
    }
}