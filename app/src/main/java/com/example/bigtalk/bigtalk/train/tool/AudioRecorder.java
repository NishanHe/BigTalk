package com.example.bigtalk.bigtalk.train.tool;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AudioRecorder {


    public static final String TAG = "AudioRecorder";
    private AudioRecord mRecorder;
    private DataOutputStream dos;
    private Thread recordThread;
    private boolean isStart = false;
    private static AudioRecorder mInstance;
    private  int bufferSize;
    private int bs;
    private OnAudioStatusUpdateListener audioStatusUpdateListener;
    PcmToWavUtil pcmToWavUtil;

    private boolean isRun = false;
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);



    public AudioRecorder() {
        bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize * 2);
    }

    private long startTime;
    private long endTime;

    /**
     * 获取单例引用
     *
     * @return
     */
    public static AudioRecorder getInstance() {
        if (mInstance == null) {
            synchronized (AudioRecorder.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecorder();
                }
            }
        }
        return mInstance;
    }

    /**
     * 销毁线程方法
     */
    private void destroyThread() {
        try {
            isStart = false;
            if (null != recordThread && Thread.State.RUNNABLE == recordThread.getState()) {
                try {
                    Thread.sleep(500);
                    recordThread.interrupt();
                } catch (Exception e) {
                    recordThread = null;
                }
            }

            recordThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordThread = null;
        }
    }

    /**
     * 启动录音线程
     */
    private void startThread() {
        destroyThread();
        isStart = true;
        if (recordThread == null) {
            recordThread = new Thread(recordRunnable);
            recordThread.start();
        }
    }

    /**
     * 录音线程
     */
    Runnable recordRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                int bytesRecord;
                //int bufferSize = 320;
                byte[] tempBuffer = new byte[bufferSize];
                if (mRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
                    stopRecord();
                    return;
                }
                mRecorder.startRecording();
                //writeToFileHead();
                while (isStart) {
                    if (null != mRecorder) {
                        bytesRecord = mRecorder.read(tempBuffer, 0, bufferSize);
                        if (bytesRecord == AudioRecord.ERROR_INVALID_OPERATION || bytesRecord == AudioRecord.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (bytesRecord != 0 && bytesRecord != -1) {
                            //在此可以对录制音频的数据进行二次处理 比如变声，压缩，降噪，增益等操作
                            //我们这里直接将pcm音频原数据写入文件 这里可以直接发送至服务器 对方采用AudioTrack进行播放原数据
                            dos.write(tempBuffer, 0, bytesRecord);
                        } else {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * 保存文件
     *
     * @param path
     * @throws Exception
     */
    private void setPath(String path) throws Exception {
        pcmToWavUtil.pcmToWav(path,path);
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        dos = new DataOutputStream(new FileOutputStream(file, true));
    }

    /**
     * 启动录音
     *
     * @param path
     */
    public void startRecord(String path) {
        try {
            setPath(path);
            startThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        try {
            destroyThread();
            if (mRecorder != null) {
                if (mRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    mRecorder.stop();
                }
                if (mRecorder != null) {
                    mRecorder.release();
                }
            }
            if (dos != null) {
                dos.flush();
                dos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }


    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {

        if (mRecorder != null) {

            byte[] buffer = new byte[bs];
            isRun = true;
            while (isRun) {
                int r = mRecorder.read(buffer, 0, bs);
                int v = 0;
                // 将 buffer 内容取出，进行平方和运算
                for (int i = 0; i < buffer.length; i++) {
                    // 这里没有做运算的优化，为了更加清晰的展示代码
                    v += buffer[i] * buffer[i];
                }


                double db = 0;// 分贝
                if (r > 1) {
                    db = 10 * Math.log10(v / (double) r);
                    if (null != audioStatusUpdateListener) {
                        audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
                    }
                }
                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);

            }
        }
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        public void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param
         */
        public void onStop();
    }

}
