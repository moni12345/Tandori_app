package dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.skytech.aminatandoori.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import activity.MainMenuActivity;
import model.ProfileModel;
import parser.loginParser;
import session.SessionManager;
import util.GlobalClass;

public class loginDialog extends Dialog implements
        View.OnClickListener, OnCheckedChangeListener {

    public static String loginstatus;
    final Dialog logindialog;
    Button bSignUp, bLogin, bForgetPass;
    EditText etEmail, etPass;
    ImageView closeDialog;
    Activity act;
    SharedPreferences mLoginPreferences;
    String URL = "http://placeorderonline.com/aminah/webservices/loginApp.php";
    SessionManager session;
    RadioButton bRememberMe;
    SharedPreferences pref;
    boolean isLogin;
    boolean bcheckSharedPref;
    boolean mRadioCheck;
    String id;

    Communicator com;
    String comingFormBasket = "";
    ProfileModel bean = new ProfileModel();

    public loginDialog(Activity context, String str) {

        super(context);
        act = context;

        com = (Communicator) context;
        comingFormBasket = str;

        // TODO Auto-generated constructor stub
        logindialog = new Dialog(context);
        logindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logindialog.setContentView(R.layout.logindialog);

        InitDialogUI(logindialog);
        session = new SessionManager(act);

        isLogin = session.isLoggedIn();

        if (isLogin == true) {

            pref = act.getSharedPreferences("fraddys", 0);
            String prefemail = pref.getString(SessionManager.KEY_EMAIL, "");
            String prefpass = pref.getString(SessionManager.KEY_PASSWORD, "");

            etEmail.setText(prefemail);
            etPass.setText(prefpass);
        }
        mLoginPreferences = act.getSharedPreferences("AminhaLoginPref",
                Context.MODE_PRIVATE);
        String pEmail = mLoginPreferences.getString("Email", "");
        String pPassword = mLoginPreferences.getString("Password", "");
        etEmail.setText(pEmail);
        etPass.setText(pPassword);

    }

    public loginDialog(Activity context) {

        super(context);
        act = context;

        try {
            com = (Communicator) context;
        } catch (ClassCastException e) {

        }
        // TODO Auto-generated constructor stub
        logindialog = new Dialog(context);
        logindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logindialog.setContentView(R.layout.logindialog);

        InitDialogUI(logindialog);
        session = new SessionManager(act);

        isLogin = session.isLoggedIn();
        if (isLogin == true) {
            pref = act.getSharedPreferences("fraddys", 0);
            String prefemail = pref.getString(SessionManager.KEY_EMAIL, "");
            String prefpass = pref.getString(SessionManager.KEY_PASSWORD, "");

            etEmail.setText(prefemail);
            etPass.setText(prefpass);
        }
        mLoginPreferences = act.getSharedPreferences("AminhaLoginPref",
                Context.MODE_PRIVATE);
        String pEmail = mLoginPreferences.getString("Email", "");
        String pPassword = mLoginPreferences.getString("Password", "");
        etEmail.setText(pEmail);
        etPass.setText(pPassword);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
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

    @SuppressLint("DefaultLocale")
    private void checkSharedPreference() {

        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        email.toLowerCase();
        if (email.trim().length() > 0 && pass.trim().length() > 0) {

            session.createLoginSession(id, pass, email);

        } else {
            Toast.makeText(getContext(), "null error", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @SuppressLint("CutPasteId")
    private void InitDialogUI(Dialog dialog) {
        // TODO Auto-generated method stub
        bSignUp = (Button) dialog.findViewById(R.id.bSignUp);
        bLogin = (Button) dialog.findViewById(R.id.blogin);
        bForgetPass = (Button) dialog.findViewById(R.id.bForgetPassword);
        etEmail = (EditText) dialog.findViewById(R.id.etloginEmail);
        etPass = (EditText) dialog.findViewById(R.id.etloginPass);
        bRememberMe = (RadioButton) dialog.findViewById(R.id.rbrememberMe);
        closeDialog = (ImageView) dialog.findViewById(R.id.ibCloseLogin);

        bSignUp.setOnClickListener(this);
        bLogin.setOnClickListener(this);
        bForgetPass.setOnClickListener(this);
        bRememberMe.setOnCheckedChangeListener(this);
        closeDialog.setOnClickListener(this);
        dialog.show();

    }

    @SuppressLint("DefaultLocale")
    private boolean checker() {
        // TODO Auto-generated method stub

        String email = etEmail.getText().toString();
        email.toLowerCase();
        boolean emailvalidation = isValidEmail(email);
        // Toast.makeText(getContext(), "" + emailvalidation, 0).show();

        if (etEmail.getText().equals(null) || etEmail.length() == 0) {
            etEmail.setError("Email require");
            return false;
        } else if (etPass.getText().equals(null) || etPass.length() == 0) {
            etPass.setError("Password require");
            return false;
        } else {

            if (!emailvalidation) {
                etEmail.setError("Email Address is not valid ");
                return false;
            } else if (emailvalidation) {
                return true;
            }
        }

        return true;

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bSignUp:
                logindialog.dismiss();
                new SignUpDialog(act);
                break;

            case R.id.blogin:
                // Toast.makeText(getContext(), "Problem in TextField", 0).show();
                if (isLogin) {
                    logindialog.dismiss();

                } else {
                    boolean textfieldchecker = checker();
                    if (textfieldchecker) {
                        new LoginAsyncTask().execute();
                        logindialog.dismiss();

                    } else {

                        Toast.makeText(getContext(), "Problem in TextField", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;
            case R.id.bForgetPassword:
                logindialog.dismiss();
                new ForgetPasswordDialog(act);

                break;
            case R.id.ibCloseLogin:
                logindialog.dismiss();
            default:
                break;
        }
    }

    @SuppressWarnings("deprecation")
    void AlertDialogshow(String msg, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder.setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
         mRadioCheck =isChecked;
        // checkSharedPreference();

    }

    // public interface LoginImageListener{
    // public void changeLoginImage();
    // }
    public interface Communicator {
        void loginResponce(String data);
    }

    class LoginAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;
        String pass;
        String email;
        private String firstName;
        private String status;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(act);
            dialog.show();
            dialog.setCancelable(false);
            email = etEmail.getText().toString();
            pass = etPass.getText().toString();
        }

        @SuppressLint("DefaultLocale")
        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {

            loginParser parser = new loginParser();
            ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();


            email = email.toLowerCase();

            String passMD5 = md5(email + pass);
            Log.v("encrypt pass", passMD5);
            arrlst.add(new BasicNameValuePair("email", email));
            arrlst.add(new BasicNameValuePair("password", passMD5));

            JSONArray jsonarr = parser.makeHttpRequest(URL, arrlst);
            Log.v("TAG login", "" + jsonarr);

            try {
                JSONObject jsonObj = jsonarr.getJSONObject(0);

                firstName = jsonObj.getString("email");
                id = jsonObj.getString("id");
                status = jsonObj.getString("status");

                bean.setFirstname(jsonObj.getString("firstName"));
                bean.setLastname(jsonObj.getString("lastName"));
                bean.setEmail(jsonObj.getString("email"));
                bean.setAddress1(jsonObj.getString("address1"));
                bean.setAddress2(jsonObj.getString("address2"));
                bean.setContact1(jsonObj.getString("contact1"));
                bean.setContact2(jsonObj.getString("contact2"));
                bean.setCity(jsonObj.getString("town_city"));
                bean.setPostcode(jsonObj.getString("postcode"));

                GlobalClass.firstName = jsonObj.getString("firstName");
                GlobalClass.lastName = jsonObj.getString("lastName");
                GlobalClass.email = jsonObj.getString("email");
                GlobalClass.address = jsonObj.getString("address1");
                GlobalClass.address2 = jsonObj.getString("address2");
                GlobalClass.contact1 = jsonObj.getString("contact1");
                GlobalClass.contact2 = jsonObj.getString("contact2");
                GlobalClass.city = jsonObj.getString("town_city");
                GlobalClass.postcode = jsonObj.getString("postcode");
                GlobalClass.status = jsonObj.getInt("status");
                GlobalClass.customerId = jsonObj.getString("id");
                // session.createLoginSession(bean);
                Log.v("Tag", status);

            } catch (Exception e) {

            }
            return status;
        }
        public void saveLoginPasswordToShearedPreferance() {

            SharedPreferences.Editor editor = mLoginPreferences.edit();
            editor.putString("Email", etEmail.getText().toString());
            editor.putString("Password", etPass.getText().toString());
            editor.commit();

        }

        public void clearLoginPasswordToShearedPrefenrenc() {
            SharedPreferences.Editor editor = mLoginPreferences.edit();
            editor.clear();
            editor.commit();

        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
                loginstatus = result;
                if (result.equals("1")) {
                    AlertDialogshow("Login Successful.", "Login Dialog");

                    MainMenuActivity.ivlogin.setImageResource(R.drawable.log_out);
                    MainMenuActivity.tvLogin.setText("Logout");
                    // SettingFragment.changeLogoutText();
                    GlobalClass.status = 1;

                    com.loginResponce("Profile");
                    if (mRadioCheck)
                        saveLoginPasswordToShearedPreferance();
                    else
                        clearLoginPasswordToShearedPrefenrenc();

//				if (comingFormBasket.equals("Basket")) {
//					com.loginResponce("Done");
//
//				} else if (comingFormBasket.equals("MainMenuActivity")) {
//					com.loginResponce("Logout");
//				} else {
//					com.loginResponce("profile");
//
//				}
                    if (comingFormBasket.equals("MainMenuActivity")) {
					com.loginResponce("Logout");
				} else {
					com.loginResponce("Profile");

				}

                    if (bcheckSharedPref == true) {
                        checkSharedPreference();
                        session.createLoginSession(bean);
                        com.loginResponce("Logout");
                    }
                } else if (result.equals("")) {
                    AlertDialogshow("Incorrect email address or password.",
                            "Login Fail!");
                }

        }
    }

}
