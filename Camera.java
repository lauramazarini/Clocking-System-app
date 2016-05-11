package com.firstapp.mazarinilaura.clockingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
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


public class Camera extends Activity implements CvCameraViewListener2 {

    static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    String a;
    Button train;
    static final int TRAINING= 0;
    static final int SEARCHING= 1;
    ToggleButton toggleButtonGrabar;
    public static final int IDLE= 2;
    int faceState=IDLE;
    Mat                    mRgba;
    Mat                    mGray;
    File                   mCascadeFile;
    CascadeClassifier      mJavaDetector;
    float                  mRelativeFaceSize   = 0.2f;
    int                    mAbsoluteFaceSize   = 0;
    int mLikely=999;
    String path="";
    JavaCameraView mOpenCvCameraView;
    EditText text;
    TextView textresult;
    Bitmap mBitmap;
    Handler mHandler;
    PersonRecognition fr;

    SharedPreferences prefs;
    Button adaugaAngajat;
    static final long MAXIMG = 10;
    int countImages=0;
    String nume;
    String salariu;
    String pozitie;
    String cnp;
    String numePoza;
    String securitate;

    Pictures labelsFile;
    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    fr=new PersonRecognition(path);
                    try {
                        fr.load();
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
    public Camera() {
        }
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.mycamera);
        mOpenCvCameraView.setCvCameraViewListener(this);
        path=getFilesDir()+"/facerecogOCV/";
        labelsFile= new Pictures(path);
        textresult = (TextView) findViewById(R.id.textView1);
        prefs= this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        prefs.edit().putString("mPath", path).apply();
        Bundle bundle=getIntent().getExtras();
        nume=bundle.getString("nume");
        salariu= bundle.getString("salariu");
        pozitie=bundle.getString("pozitie");
        cnp=bundle.getString("cnp");
        securitate=bundle.getString("securitate");
        adaugaAngajat=(Button)findViewById(R.id.ButonAdaugaAngajat);
        toggleButtonGrabar=(ToggleButton)findViewById(R.id.toggleButtonGrabar);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj=="IMG")
                {

                    if (countImages>=MAXIMG-1)
                    {
                        toggleButtonGrabar.setChecked(false);
                        grabarOnclick();
                    }
                }
                else
                {
                    textresult.setText(msg.obj.toString());



                }
            }
        };
        text=(EditText)findViewById(R.id.editText1);
        train=(Button)findViewById(R.id.ButonTrain);
        text.setVisibility(View.INVISIBLE);
        textresult.setVisibility(View.INVISIBLE);
        text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((text.getText().toString().length()>0))
                    toggleButtonGrabar.setVisibility(View.VISIBLE);
                else
                    toggleButtonGrabar.setVisibility(View.INVISIBLE);

                return false;
            }
        });
        adaugaAngajat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String pass;
                pass=cnp.substring(0,5);
                numePoza=text.getEditableText().toString();
                UserLog.date.createEmployee(nume, salariu, pozitie, cnp,numePoza,pass,securitate);
                prefs.edit().putString("username", text.getText().toString()).apply();
                Intent intent=new Intent(Camera.this,DataEmployees.class);
                startActivity(intent);

            }
        });
            train.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textresult.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    if (text.getText().toString().length() > 0)
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.Straininig), Toast.LENGTH_LONG).show();
                    try {
                        fr.train();
                    } catch (LengthException e) {
                        e.printStackTrace();
                    }
                }
            });
        toggleButtonGrabar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                grabarOnclick();
            }
        });

        boolean success=(new File(path)).mkdirs();
        if (!success)
        {
            Log.e("Error","Error creating directory");
        }
    }
    void grabarOnclick()
    {
        if (toggleButtonGrabar.isChecked())
            faceState=TRAINING;
        else
        { if (faceState==TRAINING)	;

            countImages=0;
            faceState=IDLE;
        }


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
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
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
        if ((facesArray.length==1)&&(faceState==TRAINING)&&(countImages<MAXIMG)&&(!text.getText().toString().isEmpty()))
        {
            Mat m=new Mat();
            Rect r=facesArray[0];
            m=mRgba.submat(r);
            mBitmap = Bitmap.createBitmap(m.width(),m.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, mBitmap);
            Message msg = new Message();
            String textTochange = "IMG";
            msg.obj = textTochange;
            mHandler.sendMessage(msg);
            if (countImages<MAXIMG)
            {
                fr.add(m, text.getText().toString());
                countImages++;
            }
        }
        else
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
                textTochange=fr.predict(m);
            } catch (LengthException e) {
                e.printStackTrace();
            }
            mLikely=fr.getProb();
            msg = new Message();
            msg.obj = textTochange;
            mHandler.sendMessage(msg);
        }
        for (int i = 0; i < facesArray.length; i++)
            Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
        return mRgba;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}

















