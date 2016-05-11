package com.firstapp.mazarinilaura.clockingapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class VerifyFace extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    Camera mycamera;

    private ImageButton recognize;
    public static TextView rezultat;

    static final Scalar FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);

    static final int TRAINING= 0;
    static final int SEARCHING= 1;
    public static final int IDLE= 2;
    int faceState=IDLE;
    Mat                    mRgba;
    Mat                    mGray;
    File                   mCascadeFile;
    CascadeClassifier      mJavaDetector;
    float                  mRelativeFaceSize   = 0.2f;
    int                    mAbsoluteFaceSize   = 0;
    int mLikely=999;

    JavaCameraView mOpenCvCameraView;

    Bitmap mBitmap;
    Handler mHandler;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    PersonRecognition personRecognition;
    String path;
    String parola;
    public static String rez;




    Pictures labelsFile;
    ArrayList<Pictures.label> listaImagini;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_verify_face);
        mOpenCvCameraView=(JavaCameraView)findViewById(R.id.mycamera2);
        mOpenCvCameraView.setCvCameraViewListener(this);


        recognize=(ImageButton)findViewById(R.id.ButonRecognize);
        rezultat=(TextView)findViewById(R.id.textViewrezultat);
        rezultat.setVisibility(View.INVISIBLE);
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        path = prefs.getString("mPath", "fraiera");
        path=getFilesDir()+"/facerecogOCV/";
        labelsFile=new Pictures(path);

        Bundle getBasket=getIntent().getExtras();
        parola =getBasket.getString("parola");

        Toast.makeText(getApplicationContext(), parola, Toast.LENGTH_LONG).show();





        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceState=SEARCHING;


                if(!personRecognition.predictWasMade())
                {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.SCanntoPredic), Toast.LENGTH_LONG).show();
                    return;

                }

                else {

                        mHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.obj=="IMG")
                                {
                                    Canvas canvas = new Canvas();
                                    canvas.setBitmap(mBitmap);


                                }
                                else
                                {

                                           rezultat.setText(msg.obj.toString());

                                            if(UserLog.date.passAndPicExist(parola,rezultat.getText().toString()))
                                            {
                                                if(UserLog.date.getpoz(parola)=="HR")
                                                {

                                                Intent intent = new Intent(VerifyFace.this, ClockInOut.class);
                                                startActivity(intent);
                                                }
                                                else
                                                {
                                                    Intent intent = new Intent(VerifyFace.this, HrOperation.class);
                                                    startActivity(intent);
                                                }
                                            }

                                            else if(UserLog.date.passExist(parola) )
                                            {

                                                Intent intent = new Intent(VerifyFace.this, SecurityQuestion.class);
                                                startActivity(intent);



                                            }
                                             else if(UserLog.date.picExist(rezultat.getText().toString()))
                                            {
                                                Intent intent = new Intent(VerifyFace.this, SecurityQuestion.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                Intent intent = new Intent(VerifyFace.this, UserLog.class);
                                                  startActivity(intent);}
                                }
                            }

                        };
                    }

                }
                   });

    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {

                    personRecognition=new PersonRecognition(path);
                    try {
                        personRecognition.load();
                    } catch (LengthException e) {
                        e.printStackTrace();
                    }


                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);

                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();
                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            mJavaDetector = null;
                       } else
                            cascadeDir.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
        public  VerifyFace(){}







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            return true;
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }
    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }




    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame)  {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }
        MatOfRect faces = new MatOfRect();
        if (mJavaDetector != null)
            mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        else {
            Log.e("Error","No JavaDetector exist");
        }
        Rect[] facesArray = faces.toArray();


        if ((facesArray.length>0)&& (faceState==SEARCHING))
        {
            Mat m=new Mat();
            m=mGray.submat(facesArray[0]);
            mBitmap = Bitmap.createBitmap(m.width(),m.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, mBitmap);
            Message msg = new Message();
            String textTochange = "IMG";
            msg.obj = textTochange;
            mHandler.sendMessage(msg);
            try {
                textTochange=personRecognition.predict(m);
            } catch (LengthException e) {
                e.printStackTrace();
            }
            mLikely=personRecognition.getProb();
            msg = new Message();
            msg.obj = textTochange;
            mHandler.sendMessage(msg);
        }
        else


        for (int i = 0; i < facesArray.length; i++)
            Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);


        return mRgba;




    }
}
