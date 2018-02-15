package isee.ja.isee;



import org.json.JSONArray;
import org.json.JSONObject;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import java.util.Iterator;
import java.util.List;
import okhttp3.OkHttpClient;



public class ClarifaiApi {
    ClarifaiClient client;

    public ClarifaiApi(String api_key) {


        this.client = new ClarifaiBuilder(api_key)
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync();
    }

    public void PredictbyUrl(String url) {

        final List<ClarifaiOutput<Concept>> predictionResults =
                this.client.getDefaultModels().generalModel() // You can also do client.getModelByID("id") to get your custom models
                        .predict()
                        .withInputs(
                                ClarifaiInput.forImage(url))
                        .executeSync()
                        .get();
        //  System.out.println(predictionResults);

    }

    public List<ClarifaiOutput<Concept>> PredictLive(byte[] data) {

        final List<ClarifaiOutput<Concept>> predictionResults =
                this.client.getDefaultModels().generalModel() // You can also do client.getModelByID("id") to get your custom models
                        .predict()
                        .withInputs(
                                ClarifaiInput.forImage(data))
                        .executeSync()
                        .get();
        if (predictionResults != null && predictionResults.size() > 0) {

            // Prediction List Iteration
            for (int i = 0; i < predictionResults.size(); i++) {

                ClarifaiOutput<Concept> clarifaiOutput = predictionResults.get(i);

                List<Concept> concepts = clarifaiOutput.data();

                if(concepts != null && concepts.size() > 0) {
                    for (int j = 0; j < concepts.size(); j++) {

                        resultList.add(concepts.get(j).name());
                    }
                }
            }
        }

    }
        return resultList;
}


            // System.out.println(predictionResults);



        }
        return predictionResults;

    }
}