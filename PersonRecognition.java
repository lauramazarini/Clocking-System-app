package com.firstapp.mazarinilaura.clockingapp;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.MatVector;
import com.googlecode.javacv.cpp.opencv_imgproc;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

public  class PersonRecognition {

    String path;
    FaceRecognizer faceRecognizer;
    int i=0;
    Pictures pictures;
    private int probability=999;
    static  final int WIDTH= 128;
    static  final int HEIGHT= 128;;



    PersonRecognition(String p)
    {
       faceRecognizer =  com.googlecode.javacv.cpp.opencv_contrib.createLBPHFaceRecognizer(2,8,8,8,200);
        this.path=p;
        pictures= new Pictures(path);
    }



    void add(Mat m, String description) {
        Bitmap bmp= Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m,bmp);
        bmp= Bitmap.createScaledBitmap(bmp, WIDTH, HEIGHT, false);
        FileOutputStream f;
        try {
            f = new FileOutputStream(path+description+"-"+i+".jpg",true);
            i++;
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, f);
            f.close();

        } catch (Exception e) {
            Log.e("error",e.getCause()+" "+e.getMessage());
            e.printStackTrace();

        }
    }
    void delete(){

        File imagesDirector = new File(path);
        imagesDirector.delete();
    }
    public boolean train() throws LengthException{

        File imagesDirector = new File(path);

        FilenameFilter jpg = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            };
        };

        File[] filesImage = imagesDirector.listFiles(jpg);
        MatVector imagesVector = new MatVector(filesImage.length);
        int[] tags = new int[filesImage.length];
        int j = 0;
        int tag;
        IplImage simpleImg=null;
        IplImage grayImg;
        int pathSize=path.length();
        for (File image : filesImage) {
          String pathImage = image.getAbsolutePath();
          simpleImg = cvLoadImage(pathImage);

            if (simpleImg==null)
            Log.e("Error","Error cVLoadImage");
            Log.i("image",pathImage);

            int lastIndexOfPath=pathImage.lastIndexOf("-");
            int lastIndexOfPath2=pathImage.lastIndexOf(".");
            int icount=Integer.parseInt(pathImage.substring(lastIndexOfPath+1,lastIndexOfPath2));
            if (i<icount)
                i++;

            String description=pathImage.substring(pathSize,lastIndexOfPath);

            if (pictures.get(description)<0)
                pictures.add(description, pictures.max()+1);

            tag = pictures.get(description);
            grayImg = IplImage.create(simpleImg.width(), simpleImg.height(), IPL_DEPTH_8U, 1);
            cvCvtColor(simpleImg, grayImg, CV_BGR2GRAY);
            imagesVector.put(j, grayImg);
            tags[j] = tag;

            j++;
        }
        if (j>0)
            if (pictures.max()>1)
                faceRecognizer.train(imagesVector, tags);
        pictures.Save();
        return true;
    }

    public boolean predictWasMade()
    {
        if (pictures.max()>1)
            return true;
        else
            return false;

    }

    public String predict(Mat img)  throws LengthException{
        if (!predictWasMade())
            return "";
        int n[] = new int[1];
        double p[] = new double[1];
        IplImage image = convertMatIntoIplImage(img,WIDTH, HEIGHT);


        faceRecognizer.predict(image, n, p);

        if (n[0]!=-1)
            probability=(int)p[0];
        else
            probability=-1;

        if (n[0] != -1)
            return pictures.get(n[0]);
        else
            return "Unkown";
    }




    IplImage convertMatIntoIplImage(Mat m,int width,int heigth)
    {


        Bitmap bmp=Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bmp);
        return BitmapToIplImage(bmp,width, heigth);

    }

    IplImage BitmapToIplImage(Bitmap bmp, int width, int height) {

        if ((width != -1) || (height != -1)) {
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width, height, false);
            bmp = bmp2;
        }

        IplImage image = IplImage.create(bmp.getWidth(), bmp.getHeight(),
                IPL_DEPTH_8U, 4);

        bmp.copyPixelsToBuffer(image.getByteBuffer());

        IplImage grayImg = IplImage.create(image.width(), image.height(),
                IPL_DEPTH_8U, 1);

        cvCvtColor(image, grayImg, opencv_imgproc.CV_BGR2GRAY);

        return grayImg;
    }






    public void load() throws LengthException {

        try {
            train();
        } catch (LengthException e) {
            e.printStackTrace();
            throw new LengthException("Nu exista persoane inregistrate in baza de date");
        }

    }

    public int getProb() {
        // TODO Auto-generated method stub
        return probability;
    }


}