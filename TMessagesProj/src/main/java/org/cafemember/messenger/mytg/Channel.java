package org.cafemember.messenger.mytg;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.util.FileConvert;
import org.cafemember.tgnet.TLRPC;

/**
 * Created by Masoud on 6/25/2016.
 */

public class Channel {

    public String name;
    public boolean hasPhoto = false;
    public String title = "";
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
            return BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.default_channel_icon);
            /*Log.e("CH","Default Bit");
            ColorGenerator generator = ColorGenerator.MATERIAL;
            String s = title.equals("")?name:title;
            int color = generator.getColor(s);
            TextDrawable ic1 = TextDrawable.builder().buildRect(s, color);
            return FileConvert.convertToBitmap(ic1, ic1.getIntrinsicWidth(), ic1.getIntrinsicHeight());*/
        }
    }
}
