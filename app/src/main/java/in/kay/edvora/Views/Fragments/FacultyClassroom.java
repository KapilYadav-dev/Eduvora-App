package in.kay.edvora.Views.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pixplicity.easyprefs.library.Prefs;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

import in.kay.edvora.R;

public class FacultyClassroom extends Fragment {
    Context context;
    View view;
    Button btnStart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faculty_classroom, container, false);
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
        btnStart=view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(view -> {
            StartMeet(Prefs.getString("userId","DefaultRoom@Edvora"));
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
        userInfo.setDisplayName(Prefs.getString("name","Teacher"));
        userInfo.setEmail(Prefs.getString("email","teacherMail"));


        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setUserInfo(userInfo)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setFeatureFlag("meeting-name.enabled",false)
                .setRoom(string)
                .build();
        JitsiMeetActivity.launch(context, options);
    }
}