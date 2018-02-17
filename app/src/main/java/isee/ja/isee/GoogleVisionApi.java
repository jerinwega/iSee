package isee.ja.isee;

import android.os.AsyncTask;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by Avirachan on 16-02-2018.
 */

public class GoogleVisionApi {

    public Vision.Builder client;
    String apikey;
    Speech speech;

    public GoogleVisionApi(String key, Speech speech) {
        this.apikey = key;
        this.speech = speech;
        this.client =  new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);
        this.client.setVisionRequestInitializer(
                new VisionRequestInitializer(this.apikey));
    }

    public void PredictLive(byte[] data) throws IOException {

    }
}
