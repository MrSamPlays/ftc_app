package org.firstinspires.ftc.robotcontroller.teamcode.libs.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.qualcomm.ftcrobotcontroller.R;

import java.util.HashMap;

public class SoundPlayer {
    public static final int song = R.raw.dadadada_dadadadada_dadadadada_dada;
    public static final int siren = R.raw.siren;

    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;

    /**
     * Populate the SoundPool
     */
    public static void initSounds(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap(3);
        soundPoolMap.put(song, soundPool.load(context, R.raw.dadadada_dadadadada_dadadadada_dada, 1));
        soundPoolMap.put(siren, soundPool.load(context, R.raw.siren, 1));
    }

    /**
     * Play a given sound in the soundPool
     */
    public static void playSound(Context context, int soundID) {
        if (soundPool == null || soundPoolMap == null) {
            initSounds(context);
        }
        float volume = 1;
        soundPool.play(soundPoolMap.get(soundID), volume, volume, 1, 0, 1f);
    }
}
