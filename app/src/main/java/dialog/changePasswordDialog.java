package dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.skytech.aminatandoori.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import util.GlobalClass;
import parser.loginParser;
import session.SessionManager;

public class changePasswordDialog extends Dialog implements
        View.OnClickListener {

    Activity activity;

    Dialog dialog;
    ImageView ivCancel;
    EditText etNewPass, etOldPass, etConfromPass;
    Button bSubmit;

    String newPassword, oldPassword, confromPassword;
    String URL = "http://placeorderonline.com/almarrakech/webservices/ChangeAppPassword.php";
    String email;
    SharedPreferences pref;

    String oldPasswordMD5;
    String newPasswordMD5;
    SessionManager session;

    public changePasswordDialog(Activity act) {
        super(act);
        // TODO Auto-generated constructor stub
        activity = act;
        dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_pass_dialog);
        dialog.show();
        dialog.setCancelable(false);

        session = new SessionManager(activity);

        InitUI(dialog);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void InitUI(Dialog dialog2) {
        // TODO Auto-generated method stub

        bSubmit = (Button) dialog2.findViewById(R.id.bSubmit);
        etNewPass = (EditText) dialog2.findViewById(R.id.etnewPass);
        etOldPass = (EditText) dialog2.findViewById(R.id.etOldPass);
        etConfromPass = (EditText) dialog2.findViewById(R.id.etConfromPass);
        ivCancel = (ImageView) dialog2.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(this);
        bSubmit.setOnClickListener(this);

    }

    private void getEmailFromSharedPreference() {

        pref = activity.getSharedPreferences("fraddys", 0);
        email = pref.getString(SessionManager.KEY_EMAIL, "");
        // if (email.trim().length() > 0) {

        Log.e("email", email + ".a");
        Log.e("pass", oldPassword + "");
        String ss = email;
        oldPasswordMD5 = md5(GlobalClass.email + oldPassword);
        Log.e("newemail", oldPasswordMD5);
        newPasswordMD5 = md5(GlobalClass.email + newPassword);
        Log.v("encrypted Password", oldPassword + "->" + newPasswordMD5);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ivCancel:
                dialog.dismiss();
                break;
            case R.id.bSubmit:
                boolean textfieldresponce = checkTextField();
                getEmailFromSharedPreference();
                if (textfieldresponce == true) {
                    new ChangePasswordAsyncTask().execute();
                } else {
                    Toast.makeText(getContext(), "Please Check Your Text Field", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            default:
                break;
        }
    }

    private boolean checkTextField() {
        // TODO Auto-generated method stub
        newPassword = etNewPass.getText().toString();
        oldPassword = etOldPass.getText().toString();
        confromPassword = etConfromPass.getText().toString();

        if (oldPassword.equals(null) || oldPassword.length() == 0) {
            etOldPass.setError("Old Password require");
            return false;
        } else if (newPassword.equals(null) || newPassword.length() == 0) {
            etNewPass.setError("new Password require");
            return false;
        } else if (newPassword.length() <= 5) {
            etNewPass.setError("Maximam  6 letter Password");
            return false;
        } else if (confromPassword.length() == 0
                || confromPassword.equals(null)) {
            etConfromPass.setError("Confrom  Password require");
            return false;
        } else if (!newPassword.equals(confromPassword)) {
            etConfromPass.setError("Password Does not Match");
            return false;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    private void alertForLogin(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        // set title
        alertDialogBuilder.setTitle("");

        // set dialog message
        alertDialogBuilder.setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialog3, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.dismiss();
                        dialog3.dismiss();

                    }
                });

        // // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // // show it
        alertDialog.show();
    }

    class ChangePasswordAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog Pdialog;
        String status;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Pdialog = new ProgressDialog(getContext());
            Pdialog.show();
            Pdialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            loginParser parser = new loginParser();

            ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();
            arrlst.add(new BasicNameValuePair("email", GlobalClass.email));
            arrlst.add(new BasicNameValuePair("Oldpassword", oldPasswordMD5));
            arrlst.add(new BasicNameValuePair("Newpassword", newPasswordMD5));
            JSONArray jsonarr = parser.makeHttpRequest(URL, arrlst);

            try {
                status = jsonarr.getString(0);
                Log.e("asf", oldPasswordMD5 + "");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Pdialog.dismiss();

            alertForLogin(result);
            if (result.equals("Password Changed")) {
                session.cleaneditor();
                session.createLoginSession(email, etNewPass.getText()
                        .toString());
            }

        }

    }
}
