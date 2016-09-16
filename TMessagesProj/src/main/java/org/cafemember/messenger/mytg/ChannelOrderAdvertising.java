package org.cafemember.messenger.mytg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
            Log.e("CHB","Default Bit");
            return BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.default_channel_icon);
            /*ColorGenerator generator = ColorGenerator.MATERIAL;
            String s = title.equals("")?name:title;
            int color = generator.getColor(s);
            TextDrawable ic1 = TextDrawable.builder().buildRect(s, color);
            return FileConvert.convertToBitmap(ic1, ic1.getIntrinsicWidth(), ic1.getIntrinsicHeight());*/
        }
    }
}
