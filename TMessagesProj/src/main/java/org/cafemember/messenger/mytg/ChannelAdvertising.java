
package org.cafemember.messenger.mytg;


        import android.content.res.Resources;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.util.Log;

        import org.cafemember.messenger.R;
        import org.cafemember.messenger.mytg.util.FileConvert;

/**
 * Created by Masoud on 6/25/2016.
 */

public class ChannelAdvertising {

    public String name;
    public boolean hasPhoto = false;
    public String title;
    public String byteString;
    public long id;
    public int price ;
    public String admin_link;
    public String description;

    public ChannelAdvertising(String name, long id){
        this.name = name;
        this.id = id;
    }

    public ChannelAdvertising(){

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
            Log.e("CHA","Default Bit");
            return BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.default_channel_icon);
            /*ColorGenerator generator = ColorGenerator.MATERIAL;
            String s = title.equals("")?name:title;
            int color = generator.getColor(s);
            TextDrawable ic1 = TextDrawable.builder().buildRect(s, color);
            return FileConvert.convertToBitmap(ic1, ic1.getIntrinsicWidth(), ic1.getIntrinsicHeight());*/
        }
    }
}

