package adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;
import com.squareup.picasso.Picasso;

import dialog.DialogFullImage;
import activity.BranchDetailActivity;
import activity.DealOfferItemActivity;
import model.DealOffersBean;

public class DealOffersAdapter extends ArrayAdapter<DealOffersBean> {

	List<DealOffersBean> _arrlst;
	private LayoutInflater layoutInflater;
	Activity c;
	private boolean zoomOut =  false;
	Bitmap bitMap;
	public static View ActivityView;
	
	

	public DealOffersAdapter(Activity context, List<DealOffersBean> objects, View v) {
		super(context, R.layout.deal_offers_adapter, objects);
		this._arrlst = objects;
		// TODO Auto-generated constructor stub
		ActivityView = v;
		c = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _arrlst.size();
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		// if (convertView == null) {
		layoutInflater = LayoutInflater.from(getContext());
		convertView = layoutInflater
				.inflate(R.layout.deal_offers_adapter, null);
		holder = new ViewHolder();
		Log.v("convert", "run");
		holder.tvName = (TextView) convertView.findViewById(R.id.tvOfferName);
		holder.tvDesc = (TextView) convertView.findViewById(R.id.tvOfferDesc);
		holder.ivImage = (ImageView) convertView
				.findViewById(R.id.ivOfferImage2);
		
		//holder.ivImage.setTag(new Integer(position));
		
		holder.ivAdd = (ImageView) convertView.findViewById(R.id.ivAddItem);
		holder.tvPrice = (TextView) convertView.findViewById(R.id.tvOfferPrice);
		holder.ivAdd.setTag(new Integer(position));
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }
		DealOffersBean bean = _arrlst.get(position);
		if (bean != null) {
			holder.tvName.setText(bean.getOfferName());
			holder.tvDesc.setText(bean.getOfferDesc());
			holder.tvPrice.setText(bean.getOfferPrice());
			
			if (holder.ivImage != null) {	
				Picasso.with(c).load(bean.getOfferspicURL()).into(holder.ivImage);
				}
			
			holder.ivAdd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int j = (Integer) holder.ivAdd.getTag();
					
					String itemname = holder.tvName.getText().toString();
					String itemPrice = holder.tvPrice.getText().toString();
					String itemDesc = holder.tvDesc.getText().toString();
					String offerID =  _arrlst.get(j).getOfferID();
					
					Intent i = new Intent(c, DealOfferItemActivity.class);
					i.putExtra("branch_ID", BranchDetailActivity.branch_id2);
					i.putExtra("itemID", offerID);
					i.putExtra("Name", itemname);
					i.putExtra("price", itemPrice);
					i.putExtra("desc", itemDesc);
				//	i.putExtra("view", ActivityView);
				//	int counter = 1;
					
				//	GlobalClass.setCircleCountValuePlus(ActivityView);

					c.startActivity(i);
					
				}
			});
			holder.ivImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					holder.ivImage.setDrawingCacheEnabled(true);
//					Bitmap bmap = Bitmap.createBitmap(holder.ivImage.getDrawingCache());
				//	Bitmap bit = ((BitmapDrawable)holder.ivImage.getDrawable()).getBitmap();

					Drawable d = holder.ivImage.getDrawable();

						 new DialogFullImage(getContext(),d);
		}
			});
		
			// }
		}
		return convertView;
	}

	
	
	static class ViewHolder {
		TextView tvName;
		TextView tvDesc;
		TextView tvPrice;
		ImageView ivImage;
		ImageView ivAdd;
	}
/*

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
		InputStream is = null;
		BitmapFactory.Options options = null;
		 BufferedInputStream buffer = null;
		try{
		 is = url.openConnection().getInputStream();
		 buffer = new BufferedInputStream(is);
		 
		 options = new BitmapFactory.Options();
		 options.inSampleSize = 4;
	     options.inPurgeable = true;
	     options.inScaled = true;
		 bitMap = BitmapFactory.decodeStream(buffer);
	}finally{
		bitMap = BitmapFactory.decodeStream(buffer, null , options);
	}
		return bitMap;
	}
	
	*/
}
