package dialog;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TimePicker;

import com.skytech.aminatandoori.R;

import interfaces.GeneralCommunicator;

public class AlertDialogManager {
    static GeneralCommunicator generalCommunicator;

    public static void showAlertDialog(Context context, String title,
                                       String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
            alertDialog
                    .setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }


    @SuppressWarnings("unused")
    public static void showAlertDialog(Activity act, String title, String msg, boolean b, int i) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                act);
        generalCommunicator = (GeneralCommunicator) act;
        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                generalCommunicator.gettingAgeResponce("YES");
                            }
                        });
        alertDialogBuilder.setPositiveButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        generalCommunicator.gettingAgeResponce("NO");
                        dialog.dismiss();

                    }

                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


    @SuppressWarnings("unused")
    public static void showAlertDialog(final Activity context, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Warning Dialog");

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

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
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(

                context.getResources().getColor(R.color.alertbuttonblue));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                context.getResources().getColor(R.color.alertbuttonblue));

    }

    public static void timePickerDialog(Activity con) {
        generalCommunicator = (GeneralCommunicator) con;
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(con,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";

                        } else {
                            AM_PM = "PM";
                            hourOfDay = hourOfDay - 12;
                        }
                        generalCommunicator.getTimeResponce(hourOfDay + " : " + minute + " : " + AM_PM);
                        // Display Selected time in textbox
                        //     txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }


}
