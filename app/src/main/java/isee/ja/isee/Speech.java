package isee.ja.isee;


import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

public class Speech implements TextToSpeech.OnInitListener {
        private TextToSpeech tts;
        private Context context = null;

        public Speech (Context context) {
            this.context = context;
            this.tts = new TextToSpeech(context, this);
        }


        @Override
        public void onInit(int status) {

            if (status == TextToSpeech.SUCCESS) {

                int result = tts.setLanguage(Locale.US);

                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "This Language is not supported");
                }

            } else {
                Log.e("TTS", "Initilization Failed!");
            }

        }

        public void talk (String text) {

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
}
