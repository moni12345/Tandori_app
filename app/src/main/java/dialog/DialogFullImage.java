package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Window;
import android.widget.ImageView;

import com.skytech.aminatandoori.R;


public class DialogFullImage extends Dialog{

	final Dialog DialogFullImage;
	Activity act;
	ImageView iv;
	
	
	public DialogFullImage(Context c, Drawable d) {
		super(c);
		// TODO Auto-generated constructor stub
		act = (Activity) c;
		DialogFullImage = new Dialog(c);
		DialogFullImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
		DialogFullImage.setContentView(R.layout.full_image_dialog);
		DialogFullImage.show();
	
		
		iv = (ImageView) DialogFullImage.findViewById(R.id.ivOF);
		iv.setBackgroundDrawable(d);
	}

}
