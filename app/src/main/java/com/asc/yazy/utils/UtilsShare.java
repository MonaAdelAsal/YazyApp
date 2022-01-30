package com.asc.yazy.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.asc.yazy.R;


public class UtilsShare {


    public static void sendEmail(Context context, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "" + subject);
        intent.putExtra(Intent.EXTRA_TEXT, "" + body);

        try {
            if (context != null)
                context.startActivity(Intent.createChooser(intent, "Email via..."));
        } catch (Exception e) {

            Toast.makeText(context, context.getResources().getString(R.string.no_app_found), Toast.LENGTH_SHORT).show();
        }


    }

    public static void dial(Context context, String phone) {

        Intent intentPhone = new Intent(Intent.ACTION_DIAL);
        intentPhone.setData(Uri.parse("tel:" + phone));
        try {
            if (context != null)
                context.startActivity(intentPhone);
        } catch (Exception e) {

            Toast.makeText(context, context.getResources().getString(R.string.no_app_found), Toast.LENGTH_SHORT).show();
        }


    }


    public static void sendSMS(Context context, String smsBody) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", smsBody);
        sendIntent.setType("vnd.android-dir/mms-sms");
        try {
            if (context != null)
                context.startActivity(sendIntent);
        } catch (Exception e) {

            Toast.makeText(context, context.getResources().getString(R.string.no_app_found), Toast.LENGTH_SHORT).show();
        }

    }

    public static void openMap(FragmentActivity context, String address) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q=" + address));
        try {
            if (context != null)
                context.startActivity(intent);
        } catch (Exception e) {

            Toast.makeText(context, context.getResources().getString(R.string.no_app_found), Toast.LENGTH_SHORT).show();
        }
    }
}
