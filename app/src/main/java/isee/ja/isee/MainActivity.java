package isee.ja.isee;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;



/*CREATED BY JERIN ABRAHAM*/

public class MainActivity extends AppCompatActivity  {

    Camera camera;
    FrameLayout fm;
    ShowCamera showCamera;
    ClarifaiApi clarifaiClient;
    CloudSightApi cloudsightClient;
    GoogleVisionApi googleClient;
    MicrosoftApi microsoftClient;
    Speech speech;
    Shutter shutter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        speech = new Speech(getApplicationContext());
         shutter = new Shutter();
        cloudsightClient = new CloudSightApi("UqT8ql42WXdWWFo7SFI7bQ", 10000, speech);
        googleClient= new GoogleVisionApi("AIzaSyDVegXm7a88cHEWuVGHAaJTKXSTiUcMB1A", speech);
        microsoftClient = new MicrosoftApi("4eed96992ea845c0b4f564747505a440", speech);
        clarifaiClient = new ClarifaiApi("e824e2c1bc464f1286aadc7cbb793d4b", speech);

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

    Camera.PictureCallback ObjectCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                    googleClient.PredictLive(data);
                    clarifaiClient.PredictLive(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            camera.startPreview();

        }
    };
    Camera.PictureCallback SceneCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        microsoftClient.PredictLive(data);
                        cloudsightClient.PredictLive(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            camera.startPreview();

        }
    };
    Camera.PictureCallback TextCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {

                        microsoftClient.PredictOcr(data);


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
        Button b = (Button)v;
        String buttonText = b.getText().toString();

        if (camera != null)
        {   final Camera.Parameters params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);

            shutter.playShutterSound();
            switch (buttonText)
            {
                case "OBJECT" :
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run(){
                            camera.takePicture(null, null, ObjectCallback);
                             params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(params);
                            shutter.playShutterSound();
                        }
                    }, 3000);

                    break;

                case "SCENE" :
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run(){
                            camera.takePicture(null, null, SceneCallback);
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(params);
                            shutter.playShutterSound();
                        }
                    }, 3000);

                    break;

                case "TEXT" :
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run(){
                            camera.takePicture(null, null, TextCallback);
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(params);
                            shutter.playShutterSound();
                        }
                    }, 3000);
                    break;

            }

        }

    }


}




