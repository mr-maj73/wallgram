package org.cafemember.messenger.mytg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.cafemember.messenger.ApplicationLoader;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.util.FileConvert;

/**
 * Created by mohammad on 13/09/2016.
 */
public class ChannelOrderAdvertising {
    public String type;

    public String id;
    public String user;
    public String name;
    public String title;
    public String date;
    public String rule;
    public String price;
    public String admin_link;
    public String byteString;


    public Bitmap getBitMap(){
        if(byteString != null && byteString.length() > 0){
//            Log.e("CH","Orginal Bitmap");
            return FileConvert.getBitmapFromString(byteString);
        }
        else {
//            Log.e("CH","Default Bit");
            return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(),
                    R.drawable.default_channel_icon);
        }
    }
}
