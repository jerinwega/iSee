package isee.ja.isee;

import android.media.MediaActionSound;



public class Shutter {

    public void playShutterSound()
    {

        MediaActionSound sound = new MediaActionSound();
        sound.play(MediaActionSound.SHUTTER_CLICK);
    }
}
