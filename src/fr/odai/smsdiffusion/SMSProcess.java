package fr.odai.smsdiffusion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import fr.odai.smsdiffusion.db.DBHelper;
import fr.odai.smsdiffusion.model.POJOList;

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

					HashSet<String> numbersToSend = new HashSet<String>();
					ArrayList<POJOList> lists = DBHelper
							.getEnabledDiffusionLists(context);
					for (POJOList list : lists) {
						ArrayList<String> keywords = DBHelper.getKeywords(
								context, list.getId());
						if(list.lastMessage == null || !list.lastMessage.equalsIgnoreCase(body)){
							boolean sending = false;
							Iterator<String> it = keywords.iterator();
							while (it.hasNext() && !sending) {
								String keyword = (String) it.next();
								if (body.toLowerCase().contains(keyword.toLowerCase())) {
									ArrayList<String> contactsPhone = DBHelper
											.getContactsPhoneOnly(context,
													list.getId());
									numbersToSend.addAll(contactsPhone);
									sending = true;
									list.lastMessage = body;
									list.lastSentDate = new Date().getTime();
									list.totalMessageSent += 1;
									DBHelper.updateList(context, list);
								}
							}
						}
					}
					if (!numbersToSend.isEmpty()) {
						for (String number : numbersToSend) {
							if (!PhoneNumberUtils.compare(address, number)) {
								SmsManager smsManager = SmsManager.getDefault();
								smsManager.sendTextMessage(number, null, body,
										null, null);
							}
						}
					}
				}
			}
		}
	}
}