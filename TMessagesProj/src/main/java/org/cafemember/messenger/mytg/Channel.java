package org.cafemember.messenger.mytg;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.cafemember.messenger.ApplicationLoader;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.util.FileConvert;
import org.cafemember.tgnet.TLRPC;

/**
 * Created by Masoud on 6/25/2016.
 */

public class Channel {

    public String name;
    public boolean hasPhoto = false;
    public String title;
    public String byteString;
    public long id;
    public TLRPC.FileLocation photo;
    public TLRPC.InputChannel inputChannel;
    public Channel(String name, long id){
        this.name = name;
        this.id = id;
    }

    public Channel(){

    }

    public void setPhoto(String byteString){
        if(byteString == null || byteString.length() == 0){
            hasPhoto = false;
            return;
        }
        hasPhoto = true;
        this.byteString = byteString;
    }

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
