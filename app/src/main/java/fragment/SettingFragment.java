package fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.skytech.aminatandoori.R;

import dialog.changePasswordDialog;
import dialog.loginDialog;
import activity.AboutUsActivity;
import activity.FindusActivity;
import activity.MainActivity;
import activity.PreviousOrderActivity;
import activity.ProfileActivity;
import activity.SettingActivity;
import activity.TermsAndConditionActivity;
import adapter.SettingListAdapter;
import session.SessionManager;
import util.GlobalClass;

public class SettingFragment extends Fragment {

	String names[] = { " Previous Order", "Change Password", "Profile Setting",
			"Find Us","Terms And Conditions", "About App \t \t \t \t \t 1.0", "Log Out" };

	static String namelist[] = { " Previous Order", "Change Password",
			"Profile Setting", "Find Us","Terms And Conditions", "About App \t \t \t \t \t 1.0",
			"Exit" };

	Activity activity;
	static ListView list;
	SessionManager session;
	SharedPreferences preferences;
	boolean isLogin;
	static SettingListAdapter adapter;
	TextListener textListener;

	public SettingFragment(Context context) {
		textListener = (TextListener) context;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = null;
		try {
			view = inflater
					.inflate(R.layout.setting_fragment, container, false);
			if (GlobalClass.status == 1)
				SettingActivity.bLogin.setText("Profile");
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		list = (ListView) view.findViewById(R.id.settinglistview);
		session = new SessionManager(getActivity());

		adapter = new SettingListAdapter(this.getActivity(), names);

		list.setAdapter(adapter);

		if (GlobalClass.status == 0) {
			names[6] = "Exit";
		}else{
			
		}

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), ""+ position, 0).show();

				switch (position) {
				case 0: // Previous Order
					isLogin = session.isLoggedIn();
					if (isLogin == true || GlobalClass.status == 1) {
						Intent i = new Intent(getActivity(),
								PreviousOrderActivity.class);
						startActivity(i);
					} else {
						new loginDialog(getActivity());
					}
					break;
				case 1:
					if (position == 1) { // Change Password
						boolean loginstatus = session.isLoggedIn();

						if (loginstatus || GlobalClass.status == 1) {
							new changePasswordDialog(getActivity());
						} else {
							alertForLogin();
						}
					}
					break;
				case 2: // ProfileActivity Setting
					isLogin = session.isLoggedIn();
					if (GlobalClass.status == 1 || isLogin ) {
						startActivity(new Intent(getActivity(), ProfileActivity.class));
					} else {
						alertForLogin();
					}

					break;
				case 3: // Find Us
					startActivity(new Intent(getActivity(),
							FindusActivity.class));
					break;
					
				case 4:
					startActivity(new Intent(getActivity(),
							TermsAndConditionActivity.class));
					break;
				case 5: // About App

					startActivity(new Intent(getActivity(),
							AboutUsActivity.class));
					break;

				case 6: // Logout
					if (GlobalClass.status == 1) {
						try {
							logoutAccount();
						} catch (Exception e) {
						}
					} else if (GlobalClass.status == 0) {
						alertForExits();
					}
				default:
					break;

				}

			}

		});

		if (GlobalClass.status == 1) {

			SettingActivity.bLogin.setText("Profile");
		}

		return view;
	}

	private void logoutAccount() {
		// TODO Auto-generated method stub
		preferences = getActivity().getSharedPreferences("fraddys", 0);
		String email = preferences.getString(SessionManager.KEY_EMAIL, "");
		String pass = preferences.getString(SessionManager.KEY_PASSWORD, "");

		if (email.trim().length() > 0 && pass.trim().length() > 0
				|| GlobalClass.status == 1) {
			alertshowAreYouSure();

		} else {
			alertForLogin();
		}
	}

	public static void changeLogoutText( Activity s ){
		adapter = null;
		adapter = new SettingListAdapter(s,
				namelist);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	} 
	
	@SuppressWarnings("deprecation")
	void alertshowAreYouSure() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title

		alertDialogBuilder.setTitle("Logout Dialog");
		// set dialog message
		alertDialogBuilder
				.setMessage("Are you Sure to logout.")
				.setCancelable(false)
				.setNegativeButton("Yes",
						new OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								// getActivity().finish();
								// session.logoutUser();
								
								changeLogoutText(getActivity());

								GlobalClass.status = 0;
								GlobalClass.statusLogin = "Login";
								textListener.onLoginResponse("Login");
							//	MainMenuActivity.ivlogin.setImageResource(R.drawable.)
								// Intent i = getActivity().getPackageManager()
								// .getLaunchIntentForPackage(
								// getActivity().getPackageName());
								// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								// startActivity(i);

								GlobalClass.globalBasketList.clear();
								GlobalClass.counterValue = "0";

								// getActivity().finish();
								// getActivity().finishAffinity();

							}
						});
		alertDialogBuilder.setPositiveButton("NO", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void alertForLogin() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title
		alertDialogBuilder.setTitle("Logout Dialog");

		// set dialog message
		alertDialogBuilder.setMessage("Please Login Your Account")
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

		// show it
		alertDialog.show();
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void alertForExits() {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title
		alertDialogBuilder.setTitle("Exit Dialog");

		// set dialog message
		
		alertDialogBuilder
				.setMessage("Do You want to Exit")
				.setCancelable(false)
				.setNegativeButton("YES",
						new OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity

								GlobalClass.status = 0;
								GlobalClass.globalBasketList.clear();
								dialog.dismiss();

								Intent intent = new Intent(getActivity(),
										MainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("EXIT", true);
								getActivity().finishAffinity();
								startActivity(intent);
								getActivity().finish();

							}
						});
		alertDialogBuilder.setPositiveButton("NO", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public interface TextListener {
		void onLoginResponse(String loginButtonText);
	}
}
