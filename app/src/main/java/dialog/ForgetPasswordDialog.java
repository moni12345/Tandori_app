package dialog;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.skytech.aminatandoori.R;

import parser.ServiceHandlerReturnString;
import parser.loginParser;

public class ForgetPasswordDialog extends Dialog implements
		View.OnClickListener {

	Activity _context;
	private Button bSubmit;
	private EditText etEmail;
	private ImageView ivDismiss;
	Dialog dialog;
	String URL = "http://placeorderonline.com/aminah/webservices/CheckEmail.php";
	String forgotpassURL="http://placeorderonline.com/aminah/webservices/ForgetPassword.php";
	String email;

	public ForgetPasswordDialog(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub

		this._context = context;

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.forget_password_dialog);
		dialog.show();
		dialog.setCancelable(false);
		InitDialogUI(dialog);
	}

	private void InitDialogUI(Dialog dialog) {
		// TODO Auto-generated method stub
		bSubmit = (Button) dialog.findViewById(R.id.bSubmit);
		etEmail = (EditText) dialog.findViewById(R.id.etEmail);
		ivDismiss = (ImageView) dialog.findViewById(R.id.ivDismissDialog);

		bSubmit.setOnClickListener(this);
		ivDismiss.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bSubmit:
			boolean check = checkTextFieldAndEmailFormat();
			if (check == true) {
			//	Toast.makeText(getContext(), "Request Send", 0).show();
			//new ForgetPassAsyncTask().execute();
				new ForgetPassCheckEmail().execute();
			} else {
				Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.ivDismissDialog:
			dialog.dismiss();
			break;
		default:
			break;
		}
	}

	private final boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	private boolean checkTextFieldAndEmailFormat() {
		email = etEmail.getText().toString();
		boolean emailvalidation = isValidEmail(email);
		if (etEmail.getText().toString().equals(null) || email.length() == 0) {
			etEmail.setError("Please Enter Your Email Address");
			return false;
		} else if (emailvalidation == false) {
			return false;
		}
		return true;

	}

	class ForgetPassCheckEmail extends AsyncTask<Void, Void, String> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(_context);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			loginParser parser = new loginParser();
			ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();
			arrlst.add(new BasicNameValuePair("cid", "0"));
			arrlst.add(new BasicNameValuePair("email", email));
			JSONArray jarr = parser.makeHttpRequest(URL, arrlst);
			Log.v("forgot pass", ""+jarr);
			try {
				String status = jarr.getString(0);
				Log.v("Status", ""+status);
				return status;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v("forgot pass", ""+jarr);

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			if(result.equals("1")){
				new ForgetPassAsyncTask().execute();
				//Toast.makeText(getContext(), "Email Does n't Exits" + result, 0).show();
			}else{
				Toast.makeText(getContext(), "Email Does n't Exits" + result, Toast.LENGTH_SHORT).show();
			}
		}

	}
	class ForgetPassAsyncTask extends AsyncTask<Void, Void, String> {

		ProgressDialog dialog;
		String eml ;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(_context);
			eml = etEmail.getText().toString();
			dialog.show();

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHandlerReturnString parser = new ServiceHandlerReturnString();
			ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();

			arrlst.add(new BasicNameValuePair("email", eml));
			String jarr = parser.makeHttpRequest(forgotpassURL, arrlst);
			Log.v("forgot pass", ""+jarr);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			alertForCheckDeliver();
		}

	}
	private void alertForCheckDeliver() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				_context);

		// set title
		alertDialogBuilder.setTitle("");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Please Check Your Email Address an Email has Sent to your Account")
				.setCancelable(false)
				.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						dialog.dismiss();

					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// show it
		alertDialog.show();
	}
}
