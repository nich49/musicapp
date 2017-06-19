package com.example.urlconnection.showmusicfile;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

// TODO ファイルを開いたら、再生
// TODO 一時停止ボタンの実装

public class PlayMusicActivity extends AppCompatActivity
    implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

  private MediaPlayer mediaPlayer = null;
  private Button buttonStart, buttonStop, buttonBack, buttonDoubleSpeed;
  private Switch switchLoop;

  //再生時間を表すシークバー
  private SeekBar seekBar;

  private Handler handler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play_music);

    //音楽開始ボタン
    buttonStart = (Button) findViewById(R.id.button_audio_start);
    // リスナーをボタンに登録
    buttonStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
          Toast toast = Toast.makeText(PlayMusicActivity.this, "現在再生されています。", Toast.LENGTH_LONG);
          toast.show();
        } else {
          // 音楽再生
          audioPlay();
        }
      }
    });

    // 音楽停止ボタン
    buttonStop = (Button) findViewById(R.id.button_audio_stop);
    // リスナーをボタンに登録
    buttonStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!mediaPlayer.isPlaying()){
          Toast toast = Toast.makeText(PlayMusicActivity.this, "現在停止しています。", Toast.LENGTH_LONG);
          toast.show();
        } else {
          if (mediaPlayer != null) {
            // 音楽停止
            audioStop();
          }
        }
      }
    });

    switchLoop = (Switch) findViewById(R.id.switchLoop);
    switchLoop.setOnCheckedChangeListener(this);

    // 音楽を5秒だけ前に戻る
    buttonBack = (Button) findViewById(R.id.button_audio_back);
    buttonBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        audioBack();
      }
    });

    // 音楽を倍速にする
    buttonDoubleSpeed = (Button) findViewById(R.id.button_double_speed);
    buttonDoubleSpeed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v){
        audioDouble();
      }
    });

    seekBar = (SeekBar) findViewById(R.id.seekBar);
    seekBar.setProgress(0);
    seekBar.setOnSeekBarChangeListener(PlayMusicActivity.this);

    PlayMusicActivity.this.runOnUiThread(new Runnable() {

      @Override
      public void run() {
        if(mediaPlayer != null){
          seekBar.setProgress(mediaPlayer.getCurrentPosition());
        }
        handler.postDelayed(this, 1000);
      }
    });
  }

  private boolean audioSetup(){
    boolean fileCheck = false;

    // インタンスを生成
    mediaPlayer = new MediaPlayer();

    try {
      // 音楽ファイルを取得
      File file = (File) this.getIntent().getSerializableExtra("musicFile");
      String filePath = file.getPath();
      // assetsから mp3 ファイルを読み込み
      AssetFileDescriptor afdescripter = getAssets().openFd("sample01.mp3");
      // MediaPlayerに読み込んだ音楽ファイルを指定

      // Fileから mp3 ファイルを読み込み
      FileInputStream fileInputStream = new FileInputStream(file);
      FileDescriptor fileDescriptor = fileInputStream.getFD();

      mediaPlayer.setDataSource(filePath);

      // 音量調整を端末のボタンに任せる
      setVolumeControlStream(AudioManager.STREAM_MUSIC);
      mediaPlayer.prepare();
      fileCheck = true;
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    seekBar.setMax(mediaPlayer.getDuration());
    return fileCheck;
  }

  private void audioPlay() {
    if (mediaPlayer == null) {
      // audio ファイルを読出し
      if (audioSetup()){
        Toast.makeText(getApplication(), "Read audio file", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getApplication(), "Error: read audio file", Toast.LENGTH_SHORT).show();
        return;
      }
    } else {
      // 繰り返し再生する場合
      mediaPlayer.stop();
      mediaPlayer.reset();
      // リソースの解放
      mediaPlayer.release();
    }

    // 再生する
    mediaPlayer.start();

    // 終了を検知するリスナー
    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      public void onCompletion(MediaPlayer mp) {
        Log.d("debug","end of audio");
      }
    });
  }

  private void audioStop() {
    // 再生終了
    mediaPlayer.stop();
    // リセット
    mediaPlayer.reset();
    // リソースの解放
    mediaPlayer.release();

    mediaPlayer = null;
    this.finish();
  }

  private void audioBack() {
    //現在のプレイヤーの時間をmsで取得
    int nowPlayerTime = mediaPlayer.getCurrentPosition();

    int resultPlayerTime = mediaPlayer.getCurrentPosition() - 5000;

    mediaPlayer.seekTo(resultPlayerTime);
  }

  private void audioDouble() {
    PlaybackParams params = new PlaybackParams();
    params.setSpeed(2.f);
    mediaPlayer.setPlaybackParams(params);
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {

  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {

  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      if (fromUser) {
        mediaPlayer.seekTo(progress);
        seekBar.setProgress(progress);
      }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (isChecked){
      mediaPlayer.setLooping(true);
    } else {
      mediaPlayer.setLooping(false);
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
    if (keyCode != KeyEvent.KEYCODE_BACK) {
      return super.onKeyDown(keyCode, keyEvent);
    } else {
      if (mediaPlayer != null) {
        audioStop();
      }
      return false;
    }
  }
}
