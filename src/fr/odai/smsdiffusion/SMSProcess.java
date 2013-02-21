package fr.odai.smsdiffusion;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSProcess extends BroadcastReceiver {

	public static final String SMS_EXTRA_NAME = "pdus";
	private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(ACTION_RECEIVE_SMS)) {
			// Get the SMS map from Intent
			Bundle extras = intent.getExtras();

			if (extras != null) {
				// Get received SMS array
				Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);

				for (int i = 0; i < smsExtra.length; ++i) {
					SmsMessage sms = SmsMessage
							.createFromPdu((byte[]) smsExtra[i]);
					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(context);
					String body = sms.getMessageBody().toString();
					String keyword = prefs.getString("keyword", "");
					if (!keyword.equalsIgnoreCase("") && body.contains(keyword)) {
						String[] splited = body.split(keyword);
						String new_body = new String();
						for (int j = 0; j < splited.length; j++) {
							new_body = new_body.concat(splited[j]);
						}

						String[] tabnumbers = ListPreferenceMultiSelect
								.parseStoredValue(prefs.getString(
										"selected_numbers", ""));
						ArrayList<String> numbers;
						if (tabnumbers != null) {
							numbers = new ArrayList<String>(
									Arrays.asList(tabnumbers));
						} else {
							numbers = new ArrayList<String>();
						}
						if (!numbers.isEmpty() && numbers.get(0).equalsIgnoreCase("#ALL#")) {
							tabnumbers = ListPreferenceMultiSelect
									.parseStoredValue(prefs.getString(
											"numbers", ""));
							if (tabnumbers != null) {
								numbers = new ArrayList<String>(
										Arrays.asList(tabnumbers));
							} else {
								numbers = new ArrayList<String>();
							}
						}
						
						for (String number : numbers) {
							SmsManager smsManager = SmsManager.getDefault();
							smsManager.sendTextMessage(number, null, new_body, null, null);
						}
					}
				}
			}
		}
	}
}