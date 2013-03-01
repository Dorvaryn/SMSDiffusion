package fr.odai.smsdiffusion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import fr.odai.smsdiffusion.adapter.POJOList;
import fr.odai.smsdiffusion.db.DBHelper;

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

					String body = sms.getMessageBody().toString();
					String address = sms.getOriginatingAddress();
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
					String lastSMSForwarded = prefs.getString("lastSMS", "");
					if(!body.equalsIgnoreCase(lastSMSForwarded)){
						HashSet<String> numbersToSend = new HashSet<String>();
						ArrayList<POJOList> lists = DBHelper.getEnabledDiffusionLists(context);
						for(POJOList list : lists){
							ArrayList<String> keywords = DBHelper.getKeywords(context, list.getId());
							boolean sending = false;
							Iterator<String> it = keywords.iterator();
							while (it.hasNext() && !sending) {
								String keyword = (String) it.next();
								if(body.contains(keyword)){
									ArrayList<String> contactsPhone = DBHelper.getContactsPhoneOnly(context, list.getId());
									numbersToSend.addAll(contactsPhone);
									sending = true;
								}
							}
						}
						if(!numbersToSend.isEmpty()){
							prefs.edit().putString("lastSMS", body).commit();
							for(String number : numbersToSend){
								if(!PhoneNumberUtils.compare(address,number)){
									SmsManager smsManager = SmsManager.getDefault();
									smsManager.sendTextMessage(number, null, body, null, null);
								}
							}
						}
					}
				}
			}
		}
	}
}