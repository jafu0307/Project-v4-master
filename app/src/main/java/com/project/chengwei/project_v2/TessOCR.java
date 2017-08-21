package com.project.chengwei.project_v2;

/**
 * Created by chiafu on 18/08/2017.
 */

import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class TessOCR {
    private TessBaseAPI mTess;

    public TessOCR() {
        // TODO Auto-generated constructor stub



        mTess = new TessBaseAPI();
        // AssetManager assetManager=
        String datapath = "/storage/emulated/0/tesseract/";
        String language = "chi_tra";
        // AssetManager assetManager = getAssets();
        File dir = new File(datapath + "/tessdata/");
        if (!dir.exists())
            dir.mkdirs();
        mTess.init(datapath, language);
    }

    public String getOCRResult(Bitmap bitmap) {

        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();

        return result;
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }

}

