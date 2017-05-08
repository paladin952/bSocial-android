package com.clpstudio.bsocial.presentation.calling;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.sinch.SinchService;
import com.f2prateek.dart.InjectExtra;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.calling.Call;

public class PlaceCallSinchActivity extends BaseSinchActivity {

    @InjectExtra
    String userEmail;
    @InjectExtra
    boolean isVoiceCall;

    public static void startActivity(Activity activity, String userEmail, boolean isVoiceCall) {
        Intent intent = Henson.with(activity)
                .gotoPlaceCallSinchActivity()
                .isVoiceCall(isVoiceCall)
                .userEmail(userEmail)
                .build();
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_call_sinch);
    }

    @Override
    protected void onServiceConnected() {
        call();
    }

    private void call() {
        if (isVoiceCall) {
            callVoice();
        } else {
            callVideo();
        }
    }

    private void callVideo() {
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userEmail);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, VideoCallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
        finish();
    }

    private void callVoice() {
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Call call = getSinchServiceInterface().callUser(userEmail);
            if (call == null) {
                // Service failed for some reason, show a Toast and abort
                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                        + "placing a call.", Toast.LENGTH_LONG).show();
                return;
            }
            String callId = call.getCallId();
            Intent callScreen = new Intent(this, CallScreenSinchActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            startActivity(callScreen);
            finish();
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
            call();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }
}
