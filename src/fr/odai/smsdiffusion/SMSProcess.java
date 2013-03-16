package fr.odai.smsdiffusion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

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
			Map<String, String> msg = RetrieveMessages(intent);
			if (msg != null) {
				for (String sender : msg.keySet()) {
					String body = msg.get(sender);

					HashSet<String> numbersToSend = new HashSet<String>();
					ArrayList<POJOList> lists = DBHelper
							.getEnabledDiffusionLists(context);
					for (POJOList list : lists) {
						ArrayList<String> keywords = DBHelper.getKeywords(
								context, list.getId());
						if (list.lastMessage == null
								|| !list.lastMessage.equalsIgnoreCase(body)) {
							boolean sending = false;
							Iterator<String> it = keywords.iterator();
							while (it.hasNext() && !sending) {
								String keyword = (String) it.next();
								if (body.toLowerCase().contains(
										keyword.toLowerCase())) {
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
							if (!PhoneNumberUtils.compare(sender, number)) {
								SmsManager smsManager = SmsManager.getDefault();
								ArrayList<String> parts = smsManager.divideMessage(body);
								smsManager.sendMultipartTextMessage(number, null, parts, null, null);
							}
						}
					}
				}
			}
		}
	}

	private static Map<String, String> RetrieveMessages(Intent intent) {
		Map<String, String> msg = null;
		SmsMessage[] msgs = null;
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("pdus")) {
			Object[] pdus = (Object[]) bundle.get("pdus");

			if (pdus != null) {
				int nbrOfpdus = pdus.length;
				msg = new HashMap<String, String>(nbrOfpdus);
				msgs = new SmsMessage[nbrOfpdus];

				// There can be multiple SMS from multiple senders, there can be
				// a maximum of nbrOfpdus different senders
				// However, send long SMS of same sender in one message
				for (int i = 0; i < nbrOfpdus; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

					String originatinAddress = msgs[i].getOriginatingAddress();

					// Check if index with number exists
					if (!msg.containsKey(originatinAddress)) {
						// Index with number doesn't exist
						// Save string into associative array with sender number
						// as index
						msg.put(msgs[i].getOriginatingAddress(),
								msgs[i].getMessageBody());

					} else {
						// Number has been there, add content but consider that
						// msg.get(originatinAddress) already contains
						// sms:sndrNbr:previousparts of SMS,
						// so just add the part of the current PDU
						String previousparts = msg.get(originatinAddress);
						String msgString = previousparts
								+ msgs[i].getMessageBody();
						msg.put(originatinAddress, msgString);
					}
				}
			}
		}

		return msg;
	}
}