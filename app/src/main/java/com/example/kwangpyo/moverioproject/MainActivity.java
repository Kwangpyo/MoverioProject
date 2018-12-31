package com.example.kwangpyo.moverioproject;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Size;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    private static final Size[] RESOLUTIONS = {
            new Size(640, 480),
            new Size(1280, 720),
            new Size(1920, 1080),
    };

    private int mResolutionIndex;

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    //   private TextView mTextView;

    private Button mBtNetCam;
    private MediaRecorder mRecorder;
    boolean isRecording = false;
    private WebView mWebView;
    private WebSettings mWebSettings;

    private Button face_detect_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        View view = this.getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mBtNetCam = (Button)findViewById(R.id.bt_networkcam);
        face_detect_btn = (Button)findViewById(R.id.bt_detect);


        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://210.107.197.240:3000/moverio");



        //   mTextView = (TextView) findViewById(R.id.textView);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);


        face_detect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
                Intent intent = new Intent(getApplicationContext(),FaceDetect.class);
                startActivity(intent);

            }
        });



        mBtNetCam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NetworkCamActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mResolutionIndex = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        changeResolution();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    mResolutionIndex--;
                    changeResolution();
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    mResolutionIndex++;
                    changeResolution();
                    break;
                default:
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void changeResolution() {
        mCamera.stopPreview();
        Camera.Parameters parameters = mCamera.getParameters();
        Size resolution = getResolution();
        parameters.setPreviewSize(resolution.getWidth(), resolution.getHeight());
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        //  setText(resolution);
    }

    private Size getResolution() {
        if (RESOLUTIONS.length <= mResolutionIndex) {
            mResolutionIndex = 0;
        } else if (mResolutionIndex < 0) {
            mResolutionIndex = RESOLUTIONS.length - 1;
        }
        return RESOLUTIONS[mResolutionIndex];
    }

/*    private void setText(@NonNull Size size) {
        String text = String.valueOf(size.getWidth()) + " x " + String.valueOf(size.getHeight());
        mTextView.setText(text);
    }*/



/*
    void startVideoRecorder() {
        if(isRecording) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            mCamera.lock();
            isRecording = false;

            mBtCamcording.setText("Start Camcording");
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecorder = new MediaRecorder();
                    mCamera.unlock();
                    mRecorder.setCamera(mCamera);
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                    mRecorder.setOrientationHint(90);

                    mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.mp4";
                    Log.d(TAG, "file path is " + mPath);
                    mRecorder.setOutputFile(mPath);

                    mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                    try {
                        mRecorder.prepare();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    mRecorder.start();
                    isRecording = true;

                    mBtCamcording.setText("Stop Camcording");
                }
            });
        }
    }*/





}
