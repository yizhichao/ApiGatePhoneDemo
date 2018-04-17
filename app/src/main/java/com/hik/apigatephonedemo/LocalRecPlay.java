package com.hik.apigatephonedemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hik.apigatephonedemo.utils.TimerUtil;

import org.MediaPlayer.PlayM4.Player;
import org.MediaPlayer.PlayM4.PlayerCallBack;

import java.io.File;
import java.util.Locale;

public class LocalRecPlay extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener,
        PlayerCallBack.PlayerDisplayCB, PlayerCallBack.PlayerPlayEndCB, PlayerCallBack.PlayerFileRefCB {

    private final String TAG = "LocalRecPlay";
    private SurfaceHolder surfaceHolder;
    private SurfaceView svPlay;
    private LocalRecPlayHandler mMessageHandler;

    /**
     * 播放文件时间更新消息标签
     */
    private final int PLAYLOCAL_PLAYTIME_UPDATE = 100;
    /**
     * 更新UI消息标签
     */
    private final int PLAYLOCAL_PLAYUI_UPDATE = 101;
    /**
     * 开始播放按钮
     */
    private Button startBtn;
    /**
     * 设置播放时间
     */
    private Button settingTimeBtn;
    /**
     * 通道号
     */
    private int port;
    /**
     * SurfaceView上的按钮
     */
    private ImageButton btnCenterPlay;
    /**
     * 海康播放器
     */
    private Player player;
    /**
     * 录像总时长
     */
    private long fileTotalTime;
    /**
     * 播放进度条
     */
    private SeekBar sbPlayProgress;
    /**
     * 停止播放状态
     */
    private final int STATUS_STOP = 0;
    /**
     * 正在播放状态
     */
    private final int STATUS_PLAYING = 1;
    /**
     * 暂停播放状态
     */
    private final int STATUS_PAUSE = 2;
    /**
     * 当前播放的状态
     */
    private int mCurrentPlaybackStatus = STATUS_STOP;
    /**
     * 定时器
     */
    private TimerUtil mTimer;
    /**
     * 当前播放时间文本框
     */
    private TextView tvCurrentPlayTime;
    /**
     * 录像总时间文本框
     */
    private TextView tvTotalPlayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_rec_play);
        initData();
        initView();
        initPlayView();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume……");

        super.onResume();
        if (player == null) {
            player = Player.getInstance();
        }
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop……");
        stopFilePlay();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy……");
    }

    private void initView() {
        svPlay = (SurfaceView) findViewById(R.id.surface);
        startBtn = (Button) findViewById(R.id.start);
        startBtn.setOnClickListener(this);
        btnCenterPlay = (ImageButton) findViewById(R.id.btnCenterPlay);
        btnCenterPlay.setOnClickListener(this);
        settingTimeBtn = (Button) findViewById(R.id.btn_setting_time);
        settingTimeBtn.setOnClickListener(this);
        sbPlayProgress = (SeekBar) findViewById(R.id.sbPlayProgress);
        tvCurrentPlayTime = (TextView) findViewById(R.id.tvCurrentPlayTime);
        tvTotalPlayTime = (TextView) findViewById(R.id.tvTotalPlayTime);
    }

    private void initData() {
        mMessageHandler = new LocalRecPlayHandler();
    }

    private void initPlayView() {
        svPlay.getHolder().addCallback(this);
        sbPlayProgress.setProgress(0);
        sbPlayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                if (fromUser) {
                    setPlayByProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                player.pause(port, 1);
                if (null != mTimer) {
                    mTimer.cancelTime();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.pause(port, 0);
                resumeTimer();
            }
        });

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCenterPlay:
            case R.id.start:
                //                startFilePlay("/storage/emulated/0/HikvisionMobile/Album/1039/20171219/20171219145923241_249_470907294.mp4");
                startFilePlay("/sdcard/HIKVISION/Video8254.mp4");
                break;
            case R.id.btn_setting_time:
                break;
            default:
                break;
        }
    }

    private void startFilePlay(String fileStr) {
        if (new File(fileStr).exists()) {
            if (mCurrentPlaybackStatus == STATUS_STOP) {
                Log.e(TAG, "port：" + port);
                port = Player.getInstance().getPort();
                if (player == null) {
                    player = Player.getInstance();
                }
                player.setDisplayCB(port, this);
                player.setFileEndCB(port, this);
                player.setFileRefCB(port, this);
                btnCenterPlay.setVisibility(View.INVISIBLE);
                player.openFile(port, fileStr);
                player.setStreamOpenMode(port, Player.STREAM_REALTIME);
                boolean ret = player.play(port, surfaceHolder);
                if (ret) {
                    mCurrentPlaybackStatus = STATUS_PLAYING;
                    startBtn.setText("PAUSE");
                }
                fileTotalTime = player.getFileTime(port);
                sbPlayProgress.setProgress(0);
                sbPlayProgress.setMax(100);
                tvTotalPlayTime.setText(getFormatTime(fileTotalTime));
                Log.e(TAG, "录像时长：" + String.valueOf(fileTotalTime));
            } else if (mCurrentPlaybackStatus == STATUS_PLAYING) {
                boolean ret = player.pause(port, 1);//暂停播放
                if (ret) {
                    mCurrentPlaybackStatus = STATUS_PAUSE;
                    startBtn.setText("START");
                }
            } else if (mCurrentPlaybackStatus == STATUS_PAUSE) {
                boolean ret = player.pause(port, 0);//恢复播放
                if (ret) {
                    mCurrentPlaybackStatus = STATUS_PLAYING;
                    startBtn.setText("PAUSE");
                }
            }
        }
    }

    private void setSeekBarProgress(final float percent) {
        sbPlayProgress.setProgress((int) percent);
    }

    @Override
    public void onDisplay(int i, byte[] bytes, int i1, int i2, int i3, int i4, int i5, int i6) {
        updateStartPlaySuccessView();
    }

    @Override
    public void onFileRefDone(int i) {

    }

    @Override
    public void onPlayEnd(int i) {
        sendMessage(PLAYLOCAL_PLAYUI_UPDATE, null, null);
    }

    private class LocalRecPlayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAYLOCAL_PLAYTIME_UPDATE:
                    Bundle bundle = msg.getData();
                    //                    Log.e(TAG, "播放进度："+bundle.getFloat("percent"));
                    setSeekBarProgress(bundle.getFloat("percent"));
                    break;
                case PLAYLOCAL_PLAYUI_UPDATE:
                    stopFilePlay();
                    break;
                default:
                    break;
            }
        }
    }

    public void sendMessage(int caseId, Bundle data, Object object) {
        if (mMessageHandler == null) {
            Log.e(TAG, "sentMessage,param error,msgHandler is null");
            return;
        }
        Message msg = Message.obtain();
        msg.what = caseId;
        if (data != null) {
            msg.setData(data);
        }

        if (object != null) {
            msg.obj = object;
        }

        mMessageHandler.sendMessage(msg);
    }

    public void updateStartPlaySuccessView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resumeTimer();
            }
        });

    }

    public void resumeTimer() {
        if (null == mTimer) {
            mTimer = new TimerUtil();
        }
        mTimer.startTime(new Runnable() {
            @Override
            public void run() {
                int curPlayTime = player.getPlayedTimeEx(port);
                float progress = (float) curPlayTime / (float) (fileTotalTime * 1000);
                updateSeekBar(getFormatTime(curPlayTime / 1000), progress * 100);
            }
        }, 0);
    }

    public void updateSeekBar(final String currentTime, final float percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sbPlayProgress.setProgress((int) percent);
                tvCurrentPlayTime.setText(currentTime);
            }
        });
    }


    public void stopFilePlay() {
        if (mTimer != null) {
            mTimer.stopTime();
        }
        mCurrentPlaybackStatus = STATUS_STOP;
        startBtn.setText("START");
        player.stop(port);
        player.closeStream(port);
        player.closeFile(port);
        player.freePort(port);
        sbPlayProgress.setProgress(100);
        tvCurrentPlayTime.setText(getFormatTime(fileTotalTime));
        btnCenterPlay.setVisibility(View.VISIBLE);
    }

    public void setPlayByProgress(int progress) {
        if(null == player){
            return;
        }

        float percent = (float) progress / 100.0f;
        boolean isSuccess = player.setPlayPos(port, percent);
        int curPlayTime = player.getPlayedTimeEx(port);
        Log.e(TAG, "mPlayerHandle.setPlayPos isSuccess = " + isSuccess);
        Log.e(TAG, "mPlayerHandle.setPlayPos" + player.getLastError(port));
        updateSeekBar(getFormatTime(curPlayTime / 1000), progress);

        if (!isSuccess) {
            Log.e(TAG, "setPlayByPercent() is fail");
        }
    }

    public String getFormatTime(long time) {
        // 先将时间换算成00:00:00的格式
        long hours = time / 3600;
        long minites = time % 3600 / 60;
        long seconds = time % 3600 % 60;
        String strTime = String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minites, seconds);
        return strTime;
    }
}
