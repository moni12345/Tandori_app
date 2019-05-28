package session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import activity.MainActivity;
import model.ProfileModel;

public class SessionManager {

	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "fraddys";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_PASSWORD = "password";

	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	public static final String KEY_ID = "id";

	public static final String FIRST_NAME = "fname";
	public static final String LAST_NAME = "lname";
	public static final String CONTACT1 = "contact1";
	public static final String CONTACT2 = "contact2";
	public static final String ADDRESS = "address1";
	public static final String ADDRESS2 = "address2";
	public static final String CITY = "city";
	public static final String POSTCODE = "postcode";

	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void createLoginSession(String password, String email) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_PASSWORD, password);

		// Storing email in pref
		editor.putString(KEY_EMAIL, email);

		// commit changes
		editor.commit();
	}

	public void createLoginSession(ProfileModel bean){
//			String id, String fname, String lname,
//			String email, String address1, String address2, String contact1,
//			String contact2, String city, String postcode) {
		editor.putString(FIRST_NAME, bean.getFirstname());
		editor.putString(LAST_NAME, bean.getLastname());
		editor.putString(KEY_EMAIL, bean.getEmail());
		editor.putString(ADDRESS, bean.getAddress1());
		editor.putString(ADDRESS2, bean.getAddress2());
		editor.putString(CONTACT1, bean.getContact1());
		editor.putString(CONTACT2, bean.getAddress2());
		editor.putString(CITY, bean.getCity());
		editor.putString(POSTCODE, bean.getPostcode());
		
		editor.commit();

	}

	public void createLoginSession(String id, String password, String email) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_PASSWORD, password);

		// Storing email in pref
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_ID, id);

		// commit changes
		editor.commit();
	}

	public void cleaneditor() {
		editor.clear();
		editor.commit();

	}

	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity

		Intent i = new Intent(_context, MainActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
