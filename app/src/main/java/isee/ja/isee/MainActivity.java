package isee.ja.isee;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Locale;

/*CREATED BY JERIN ABRAHAM*/

public class MainActivity extends AppCompatActivity  {

    Camera camera;
    FrameLayout fm;
    ShowCamera showCamera;
    ClarifaiApi clarifaiClient;
    Speech speech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speech = new Speech(getApplicationContext());




        clarifaiClient = new ClarifaiApi("f715dbc340b44f428dc06d303bca1c50");

        //connecting to frame_layout
        fm = (FrameLayout) findViewById(R.id.fl);

        // for permission check
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);

        }

        StartCamera();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance

            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
    public void StartCamera() {
        try {
            camera = Camera.open();
            showCamera = new ShowCamera(this, camera);
            //to open camera
            fm.addView(showCamera);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //permission check
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 50: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                    StartCamera();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


// to get the picture after callback

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {

                      String talk_text =  clarifaiClient.PredictLive(data);

                        speech.talk(talk_text);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            camera.startPreview();

        }
    };

    public void IdentifyImage(View v) {

        if (camera != null)
        {
            camera.takePicture(null, null, mPictureCallback);
        }
    }


}




