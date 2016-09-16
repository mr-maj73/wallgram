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
public class ChannelOrderMemberShip {

  public String status;
  public String name;
  public String total;
  public String done;
  public String left;
  public String type;
  public String date;
  public String byteString;


    public Bitmap getBitMap(){
        if(byteString != null && byteString.length() > 0){
//            Log.e("CH","Orginal Bitmap");
            return FileConvert.getBitmapFromString(byteString);
        }
        else {
            Log.e("CHC","Default Bit");
            return BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.default_channel_icon);
            /*ColorGenerator generator = ColorGenerator.MATERIAL;
            String s = name;
            int color = generator.getColor(s);
            TextDrawable ic1 = TextDrawable.builder().buildRect(s, color);
            return FileConvert.convertToBitmap(ic1, ic1.getIntrinsicWidth(), ic1.getIntrinsicHeight());*/
        }
    }




}
