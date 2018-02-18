package isee.ja.isee;


import android.util.Log;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleVisionApi {


    String apikey;
    Speech speech;
    VisionRequestInitializer requestInitializer;

    public GoogleVisionApi(String key, Speech speech) {
        this.apikey = key;
        this.speech = speech;
        this.requestInitializer =
                new VisionRequestInitializer(this.apikey);

    }

    public void PredictLive( byte[] data) throws IOException {
    final byte[] img = data;


                HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                builder.setVisionRequestInitializer(this.requestInitializer);

                Vision vision = builder.build();

                BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                        new BatchAnnotateImagesRequest();
                batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                    // Add the image
                    Image base64EncodedImage = new Image();
                    // Convert the bitmap to a JPEG

                    // Base64 encode the JPEG
                    base64EncodedImage.encodeContent(img);
                    annotateImageRequest.setImage(base64EncodedImage);

                    // add the feature
                    annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                        Feature labelDetection = new Feature();
                        labelDetection.setType("LABEL_DETECTION");
                        labelDetection.setMaxResults(10);
                        add(labelDetection);
                    }});

                    // Add the list of one thing to the request
                    add(annotateImageRequest);
                }});

                Vision.Images.Annotate annotateRequest =
                        vision.images().annotate(batchAnnotateImagesRequest);

                BatchAnnotateImagesResponse response = annotateRequest.execute();
                List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
                if (labels != null) {
                    Log.d("iSee",labels.toString());
                    speech.talk(labels.get(0).getDescription());

                }
            }


            }




