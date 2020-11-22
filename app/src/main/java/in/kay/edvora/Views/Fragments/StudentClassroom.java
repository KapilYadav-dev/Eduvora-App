package in.kay.edvora.Views.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

import in.kay.edvora.R;

public class StudentClassroom extends Fragment {
    Context context;
    View view;
    EditText meetCode;
    Button btnJoin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_classroom, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        Initz();
    }

    private void Initz() {
       meetCode=view.findViewById(R.id.et_meet_code);
       btnJoin=view.findViewById(R.id.btn_join);
       btnJoin.setOnClickListener(view -> {
           if (TextUtils.isEmpty(meetCode.getText().toString()))
           {
               meetCode.setError("Please enter code");
               meetCode.requestFocus();
               return;
           }
           else if (meetCode.getText().toString().length()<=10){
               meetCode.setError("Please enter valid enter code (Code must be greater than 10 digits)");
               meetCode.requestFocus();
               return;
           }
           else {
               StartMeet(meetCode.getText().toString().trim());
           }
       });
    }

    private void StartMeet(String string) {
        URL serverURL;
        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetUserInfo userInfo=new JitsiMeetUserInfo();
        userInfo.setDisplayName(Prefs.getString("name","Student"));
        userInfo.setEmail(Prefs.getString("email","sendmail"));

        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setUserInfo(userInfo)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom(string)
                .setFeatureFlag("meeting-name.enabled",false)
                .setFeatureFlag("invite.enabled",false)
                .setFeatureFlag("kick-out.enabled",false)
                .build();
        JitsiMeetActivity.launch(context, options);
    }
}