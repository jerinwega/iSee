package isee.ja.isee;


import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


public class MicrosoftApi {
    VisionServiceRestClient client;
    private static final String REQUEST_SELECT_IMAGE = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0";
    String apikey;
    Speech speech;

    public MicrosoftApi(String key, Speech speech) {
        this.apikey = key;
        this.speech = speech;
        this.client = new VisionServiceRestClient(apikey, REQUEST_SELECT_IMAGE);
    }

    public void PredictLive(byte[] data) throws VisionServiceException, IOException {

        // Put the image into an input stream for detection.
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        AnalysisResult v = this.client.describe(inputStream, 1);
        speech.talk(v.description.captions.get(0).text);

    }
    public void PredictOcr(byte[] data) throws VisionServiceException, IOException {
        OCR ocr;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ocr = this.client.recognizeText(inputStream, LanguageCodes.AutoDetect, true);

        List<Line> lines = ocr.regions.get(0).lines;
        for (int i = 0; i < lines.size(); i++) {
            List <Word> words = lines.get(i).words;
            for (int j = 0; j < words.size(); j++) {
              speech.talk(words.get(j).text.toString());
             System.out.println(words.get(j).text.toString());
            }
            }
        }

}
