package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

import util.GlobalClass;
import parser.HomeParser;
import session.SessionManager;

public class SignUpDialog extends Dialog implements
        View.OnClickListener {

    Dialog signUpDialog;
    Activity act;
    EditText etEmail, etPassword, etConfromPassword, etFirstName;
    Button bSubmit;
    ImageView ivcancel;

    int year, month, day;
    String date;
    String URL = "http://placeorderonline.com/aminah/webservices/QRegisterCustApp.php";

    SessionManager manager;

    public SignUpDialog(Activity context) {
        super(context);
        // TODO Auto-generated constructor stub
        act = context;
        signUpDialog = new Dialog(context);
        signUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signUpDialog.setContentView(R.layout.sign_up_dialog);
        signUpDialog.show();
        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);

        date = "" + year + "-" + day + "-" + month;

        manager = new SessionManager(act);

        InitUI(signUpDialog);

    }

    private void InitUI(Dialog dialog) {
        // TODO Auto-generated method stub
        etEmail = (EditText) dialog.findViewById(R.id.etUserEmail);
        etPassword = (EditText) dialog.findViewById(R.id.etPassword);
        etConfromPassword = (EditText) dialog
                .findViewById(R.id.etConfronPassword);
        ivcancel = (ImageView) dialog.findViewById(R.id.ivCancel);
        etFirstName = (EditText) dialog.findViewById(R.id.etFirstName);
        bSubmit = (Button) dialog.findViewById(R.id.bSubmit);
        bSubmit.setOnClickListener(this);
        ivcancel.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bSubmit:

                boolean checktextfieldValue = checker();
                //	Toast.makeText(getContext(), ""+ checktextfieldValue, 0).show();
                if (checktextfieldValue == true) {

                    new SignUpAsyncTask().execute();
                    signUpDialog.dismiss();
                    Toast.makeText(getContext(),
                            "Register Successful. ", Toast.LENGTH_SHORT).show();
                    loadEmailAndPasswordInSharedPref();
                    GlobalClass.status = 1;


                } else {
                    //	Toast.makeText(getContext(), "Request Didn't Send ", 0).show();
                }
                break;
            case R.id.ivCancel:
                signUpDialog.dismiss();
                break;

            default:
                break;
        }
    }

   /* @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bSubmit:

                boolean checktextfieldValue = checker();
                //	Toast.makeText(getContext(), ""+ checktextfieldValue, 0).show();
                if (checktextfieldValue == true) {

                    new SignUpAsyncTask().execute();
                    signUpDialog.dismiss();
                    Toast.makeText(getContext(),
                            "Register Successful. ", Toast.LENGTH_SHORT).show();
                    loadEmailAndPasswordInSharedPref();
                    GlobalClass.status = 1;


                } else {
                    //	Toast.makeText(getContext(), "Request Didn't Send ", 0).show();
                }
                break;
            case R.id.ivCancel:
                signUpDialog.dismiss();
                break;

            default:
                break;
        }
    }*/

    private void loadEmailAndPasswordInSharedPref() {
        // TODO Auto-generated method stub
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();
        String firstname = etFirstName.getText().toString();
        email.toLowerCase();
        if (email.trim().length() > 0 && pass.trim().length() > 0) {
            //	manager.createLoginSession(pass, email);
            GlobalClass.email = email;
            GlobalClass.firstName = firstname;
        }

    }    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    private boolean checker() {
        // TODO Auto-generated method stub

        String email = etEmail.getText().toString();
        boolean emailvalidation = isValidEmail(email);
        //	Toast.makeText(getContext(), "" + emailvalidation, 0).show();
        String passwordlength = etPassword.getText().toString();

        if (etFirstName.getText().equals(null) || etFirstName.length() == 0) {
            etFirstName.setError("FirstName Require");
            return false;
        } else if (etEmail.getText().equals(null) || etEmail.length() == 0) {
            etEmail.setError("Email require");
            return false;
        } else if (etPassword.getText().equals(null)
                || etPassword.length() == 0) {
            etPassword.setError("Password require");
        } else if (passwordlength.length() <= 5) {
            etPassword.setError("maximam 6 letter of Password");
            return false;

        } else if (etConfromPassword.getText().equals(null)
                || etConfromPassword.length() == 0) {
            etConfromPassword.setError("Require Confrom Password ");
            return false;
        } else if (!etConfromPassword.getText().toString()
                .equals(etPassword.getText().toString())) {
            etConfromPassword.setError("Password Did not Match");
            return false;

        } else {
            if (emailvalidation == false) {
                etEmail.setError("Email Address is not valid ");
                return false;
            } else if (emailvalidation == true) {
                return true;
            }
        }

        return true;

    }

    class SignUpAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        String email;
        String password;
        String firstname;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            firstname = etFirstName.getText().toString();
            dialog = new ProgressDialog(act);
            dialog.show();
            dialog.setCancelable(false);
        }

        @SuppressWarnings({"deprecation"})
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HomeParser service = new HomeParser();
            email.toLowerCase();

            //String passwordencryprt = md5(etPassword.getText().toString());
            String passString = email + password;
            String passMD5 = md5(passString);


            Log.v("email", passMD5);

            ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();
            arrlst.add(new BasicNameValuePair("fname", firstname));
            arrlst.add(new BasicNameValuePair("email", email));
            arrlst.add(new BasicNameValuePair("password", passMD5));
            arrlst.add(new BasicNameValuePair("regDate", date));

            JSONArray Jarr = service.makeHttpRequest(URL, arrlst);
            Log.v("Tag", "" + Jarr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();

        }

    }

}
