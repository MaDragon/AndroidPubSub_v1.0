package com.amazonaws.demo.androidpubsub;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by zhizhong on 2017-01-17.
 */

public class SoundMeter {

    private MediaRecorder mRecorder = null;
    public double getNoiseDate() {
        return noiseDate;
    }

    public void setNoiseDate(double noiseDate) {
        this.noiseDate = noiseDate;
    }

    private double noiseDate=0.0;
    private float audiometers;
    private double amplitudeDb;

    public void start() {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
            Log.d("222343","2?"+getNoiseDate());


    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void getAmplitude() {
        if (mRecorder != null) {
            audiometers = mRecorder.getMaxAmplitude();
            amplitudeDb = 20 * Math.log10((double) Math.abs(audiometers));
            setNoiseDate(amplitudeDb);
        }
        else
            return ;

    }
}