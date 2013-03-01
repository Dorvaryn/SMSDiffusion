package fr.odai.smsdiffusion;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
					
					ArrayList<POJOList> lists = DBHelper.getEnabledDiffusionLists(context);
					for(POJOList list : lists){
						ArrayList<String> keywords = DBHelper.getKeywords(context, list.getId());
						for(String keyword : keywords){
							if(body.contains(keyword)){
								ArrayList<String> contactsPhone = DBHelper.getContactsPhoneOnly(context, list.getId());
								for(String number : contactsPhone){
									if(!PhoneNumberUtils.compare(address,number)){
										String[] splited = body.split(keyword);
										String new_body = new String();
										for (int j = 0; j < splited.length; j++) {
											new_body = new_body.concat(splited[j]);
										}
										SmsManager smsManager = SmsManager.getDefault();
										smsManager.sendTextMessage(number, null, new_body, null, null);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}