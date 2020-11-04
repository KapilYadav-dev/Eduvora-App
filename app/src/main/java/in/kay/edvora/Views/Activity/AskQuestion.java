package in.kay.edvora.Views.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AskQuestion extends AppCompatActivity {
    EditText etTopic,etQuestion,etSubject;
    TextView tvTopic,tvQuestion,tvSubject;
    ImageView ivAttach,close;
    Button ask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        Initz();
    }

    private void Initz() {
        etTopic=findViewById(R.id.et_topic);
        etQuestion=findViewById(R.id.et_question);
        etSubject=findViewById(R.id.et_subject);
        tvTopic=findViewById(R.id.tvTopic);
        close=findViewById(R.id.close);
        ask=findViewById(R.id.button);
        tvQuestion=findViewById(R.id.tvQuestion);
        tvSubject=findViewById(R.id.tvSubject);
        TextWatcherLogic();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AskQuestion.this, "Clicked", Toast.LENGTH_SHORT).show();
                GoBack();
            }
        });
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddQuestion();
            }
        });
    }

    private void AddQuestion() {
        String question=etQuestion.getText().toString();
        String topic=etTopic.getText().toString();
        String subject=etSubject.getText().toString();
        if (TextUtils.isEmpty(question))
        {
            etQuestion.setFocusable(true);
            etQuestion.setError("Please enter a question to continue.");
        }else
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.askQuestion(question,topic,subject,"Bearer "+ Prefs.getString("accessToken",""));
            final ProgressDialog pd = new ProgressDialog( this);
            pd.setMax(100);
            pd.setMessage("Adding question...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
            pd.setCancelable(false);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful())
                    {
                        pd.dismiss();
                        CustomToast customToast=new CustomToast();
                        customToast.ShowToast(AskQuestion.this,"Your question has been added successfully.");
                        GoBack();
                    }
                    else if (response.code()==502)
                    {
                        MyApplication myApplication=new MyApplication();
                        myApplication.RefreshToken(Prefs.getString("refreshToken",""));
                        AddQuestion();
                    }
                    else {
                        try {
                            pd.dismiss();
                            String error=response.errorBody().string();
                            CustomToast customToast=new CustomToast();
                            customToast.ShowToast(AskQuestion.this,"Response error " +error);
                        } catch (IOException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }

                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                    CustomToast customToast=new CustomToast();
                    customToast.ShowToast(AskQuestion.this,"Retrpfot error " +t.getLocalizedMessage());
                }
            });
        }


    }

    private void TextWatcherLogic() {
        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length=etSubject.getText().toString().length();
                tvSubject.setText(length+"/50");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length=etQuestion.getText().toString().length();
                tvQuestion.setText(length+"/200");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length=etTopic.getText().toString().length();
                tvTopic.setText(length+"/50");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        GoBack();
    }

    private void GoBack() {
        startActivity(new Intent(this,MainActivity.class));
        Animatoo.animateSlideDown(this);
        this.finish();
    }
}