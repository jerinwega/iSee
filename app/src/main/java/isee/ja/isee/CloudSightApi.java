package isee.ja.isee;


import android.util.Log;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CloudSightApi {
    OkHttpClient client = new OkHttpClient();
    private static final String IMAGE_REQUEST_URL = "https://api.cloudsight.ai/v1/images";
    public static final MediaType IMG= MediaType.parse("image/jpg");
    public int sleep;

    String apikey;
    Speech speech;

    public CloudSightApi(String key, int sleep, Speech speech) {
        this.apikey = key;
        this.sleep = sleep;
        this.speech = speech;
    }


    public void PredictLive(byte[] data) throws Exception {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("locale", "en_US")
                .addFormDataPart("image", "image.jpg",
                        RequestBody.create(IMG, data))
                .build();

        Request request = new Request.Builder()
            .url(IMAGE_REQUEST_URL)
            .header("Authorization", "CloudSight " + this.apikey)
            .header("cache-control", "no-cache")
            .header("content-type", "multipart/form-data")
            .post(requestBody)
            .build();

        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onResponse(Call call, final Response response) throws IOException {
                                                if (!response.isSuccessful()) {
                                                    throw new IOException("Unexpected code " + response);
                                                } else {
                                                    final String myResponse = response.body().string();

                                                   try {
                                                       JSONObject json = new JSONObject(myResponse);
                                                      final String token = json.getString("token");
                                                      Thread.sleep(sleep);
                                                       getResponse(token);

                                                   }catch(Exception e)
                                                   {
                                                       e.printStackTrace();
                                                   }
                                                }
                                            }
                                        });

    }

    public void getResponse(String token)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(IMAGE_REQUEST_URL + "/" + token)
                .header("Authorization", "CloudSight " + this.apikey)
                .header("cache-control", "no-cache")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    System.out.println(myResponse);
                    Log.d("iSee",myResponse);

                    try {
                        JSONObject json = new JSONObject(myResponse);
                        final String name = json.getString("name");
                        speech.talk(name);



                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

}
