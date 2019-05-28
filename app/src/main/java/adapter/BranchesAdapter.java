package adapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import model.BranchesBean;


public class BranchesAdapter extends ArrayAdapter<BranchesBean> {

	private List<BranchesBean> item;

	public BranchesAdapter(Context context, List<BranchesBean> items) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.branches_adapter, items);
		this.item = items;
	}

	@Override
	public int getCount() {
		return item.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater li = LayoutInflater.from(getContext());
			v = li.inflate(R.layout.branches_adapter, null);
		}

		BranchesBean bean = item.get(position);

		if (bean != null) {
			ImageView icon = (ImageView) v.findViewById(R.id.ivBranchLogo);
			TextView tvBranchName = (TextView) v
					.findViewById(R.id.tvBranchName);
			TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
			TextView tvKmInNumber = (TextView) v
					.findViewById(R.id.tvkmInNumber);
			TextView tvMile = (TextView) v.findViewById(R.id.tvMile);
			TextView tvAddress = (TextView) v.findViewById(R.id.tvaddresstown);
			TextView tvpostcode = (TextView) v.findViewById(R.id.tvPostCode);

			// LinearLayout ratingCntr =
			// (LinearLayout)v.findViewById(R.id.ratingCntr);
			// TextView dlText = (TextView)v.findViewById(R.id.dlTxt);

			if (tvBranchName.getText().toString() != null)
				tvBranchName.setText(bean.getBranches());

			if (tvAddress.getText().toString() != null) {
				//NumberFormat nf = NumberFormat.getNumberInstance();
				tvAddress.setText(bean.getAddress());
			}
			if(tvKmInNumber.getText().toString() != null){
				double  value = bean.getDistance();
				NumberFormat nf = NumberFormat.getNumberInstance();
				 nf.setMaximumFractionDigits(2);
				 String valueaferfraction = nf.format(value);
				tvKmInNumber.setText(valueaferfraction);
			}
//			if(tvpostcode.getText().toString() != null){
//				String postcode = bean.getPostcode();
//				String valueconvert = postcode.toUpperCase();
//				tvpostcode.setText(valueconvert);
//			}
			
			if(icon != null){
				 new
				 ImageDownloaderTask(icon).execute(bean.getImageURL());
			}
//			if (icon != null) {
//				
//				icon.setImageBitmap(bean.getIconURL());
//
//			}
		}
		return v;
	}
	class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;

		public ImageDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				return convertImage(params[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					if (bitmap != null) {
						imageView.setImageBitmap(bitmap);
						//bitmap.recycle();
					} else {
						Drawable placeholder = imageView.getContext()
								.getResources()
								.getDrawable(R.drawable.branch_icon);
						imageView.setImageDrawable(placeholder);
					}
				}
			}
		}
	}
	public Bitmap convertImage(String imageURL) throws IOException {
		URL url = new URL(imageURL);
		InputStream is = url.openConnection().getInputStream();
		BitmapFactory.Options options = new BitmapFactory.Options();
		 options.inSampleSize = 8;
	     options.inPurgeable = true;
	     options.inScaled = true;
		Bitmap bitMap = BitmapFactory.decodeStream(is);
		return bitMap;
	}

}
