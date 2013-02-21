package fr.odai.smsdiffusion;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

import com.codinguser.android.contactpicker.ContactsPickerActivity;

public class Settings extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	private static final int GET_PHONE_NUMBER = 1337;

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		final EditTextPreference p = (EditTextPreference) findPreference("added_number");
		p.getEditText().setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				getContact();
				return true;
			}
		});
		p.getEditText().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				p.getEditText().setText("");
			}
		});
		updateEntries();
		ComponentName component = new ComponentName(Settings.this,
				SMSProcess.class);
		int status = this.getPackageManager().getComponentEnabledSetting(
				component);
		if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			prefs.edit().putBoolean("active", true).commit();
		} else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
			prefs.edit().putBoolean("active", false).commit();
		}
	}

	public void getContact() {
		startActivityForResult(new Intent(this, ContactsPickerActivity.class),
				GET_PHONE_NUMBER);
	}

	// Listen for results.
	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// See which child activity is calling us back.
		switch (requestCode) {
		case GET_PHONE_NUMBER:
			// This is the standard resultCode that is sent back if the
			// activity crashed or didn't doesn't supply an explicit result.
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "No phone number found",
						Toast.LENGTH_SHORT).show();
			} else {
				String phoneNumber = (String) data.getExtras().get(
						ContactsPickerActivity.KEY_PHONE_NUMBER);
				String name = (String) data.getExtras().get(
						ContactsPickerActivity.KEY_CONTACT_NAME);
				EditTextPreference p = (EditTextPreference) findPreference("added_number");
				p.getEditText().setText(name + ":" + phoneNumber);
			}
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public void updateEntries() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		ListPreferenceMultiSelect listPreferenceCustom = (ListPreferenceMultiSelect) findPreference("selected_numbers");
		ListPreferenceMultiSelect listPreferenceDeleteCustom = (ListPreferenceMultiSelect) findPreference("to_delete_numbers");
		if (listPreferenceCustom != null) {
			String[] names = ListPreferenceMultiSelect.parseStoredValue(prefs
					.getString("names", ""));
			String[] numbers = ListPreferenceMultiSelect.parseStoredValue(prefs
					.getString("numbers", ""));
			if (names != null) {
				CharSequence entries[] = new String[names.length];
				CharSequence entryValues[] = new String[numbers.length];
				for (int i = 0; i < names.length; i++) {
					entries[i] = names[i];
					entryValues[i] = numbers[i];
				}
				listPreferenceCustom.setEntries(entries);
				listPreferenceDeleteCustom.setEntries(entries);
				listPreferenceCustom.setEntryValues(entryValues);
				listPreferenceDeleteCustom.setEntryValues(entryValues);
			} else {
				prefs.edit().putString("added_number", "All:#ALL#").commit();
				updateEntries();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void addEntry(SharedPreferences prefs) {
		Resources res = getResources();
		String add = prefs.getString("added_number",
				res.getString(R.string.def_add));
		String[] tabnames = ListPreferenceMultiSelect.parseStoredValue(prefs
				.getString("names", ""));
		String[] tabnumbers = ListPreferenceMultiSelect.parseStoredValue(prefs
				.getString("numbers", ""));
		ArrayList<String> names;
		ArrayList<String> numbers;
		if (tabnames != null && tabnumbers != null) {
			names = new ArrayList<String>(Arrays.asList(tabnames));
			numbers = new ArrayList<String>(Arrays.asList(tabnumbers));
		} else {
			names = new ArrayList<String>();
			numbers = new ArrayList<String>();
		}
		String[] splited = add.split(":");
		if (splited.length == 2) {
			names.add(splited[0]);
			numbers.add(splited[1]);
			prefs.edit()
					.putString(
							"names",
							ListPreferenceMultiSelect.join(names,
									ListPreferenceMultiSelect.separator))
					.commit();
			prefs.edit()
					.putString(
							"numbers",
							ListPreferenceMultiSelect.join(numbers,
									ListPreferenceMultiSelect.separator))
					.commit();
		}
		EditTextPreference p = (EditTextPreference) findPreference("added_number");
		p.setText(res.getString(R.string.def_add));
	}

	public void delEntries(ArrayList<String> to_delete, SharedPreferences prefs) {
		String[] tabnames = ListPreferenceMultiSelect.parseStoredValue(prefs
				.getString("names", ""));
		String[] tabnumbers = ListPreferenceMultiSelect.parseStoredValue(prefs
				.getString("numbers", ""));
		ArrayList<String> names;
		ArrayList<String> numbers;
		if (tabnames != null && tabnumbers != null) {
			names = new ArrayList<String>(Arrays.asList(tabnames));
			numbers = new ArrayList<String>(Arrays.asList(tabnumbers));
		} else {
			names = new ArrayList<String>();
			numbers = new ArrayList<String>();
		}
		if(!to_delete.isEmpty() && to_delete.get(0).equalsIgnoreCase("#ALL#")){
			names.clear();
			numbers.clear();
		}else {
			for (String number : to_delete) {
				for (int i = 0; i < numbers.size(); i++) {
					if (numbers.get(i).equalsIgnoreCase(number)) {
						names.remove(i);
						numbers.remove(i);
					}
				}
			}
		}
		prefs.edit()
				.putString(
						"names",
						ListPreferenceMultiSelect.join(names,
								ListPreferenceMultiSelect.separator)).commit();
		prefs.edit()
				.putString(
						"numbers",
						ListPreferenceMultiSelect.join(numbers,
								ListPreferenceMultiSelect.separator)).commit();
		prefs.edit().putString("to_delete_numbers", "");
	}

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if (key.equalsIgnoreCase("added_number")) {
			String add = prefs.getString("added_number", getResources()
					.getString(R.string.def_add));
			if (!add.equalsIgnoreCase(getResources()
					.getString(R.string.def_add))) {
				addEntry(prefs);
				updateEntries();
			}
		} else if (key.equalsIgnoreCase("to_delete_numbers")) {
			String[] tabnumbers = ListPreferenceMultiSelect
					.parseStoredValue(prefs.getString("to_delete_numbers", ""));
			ArrayList<String> numbers;
			if (tabnumbers != null) {
				numbers = new ArrayList<String>(Arrays.asList(tabnumbers));
			} else {
				numbers = new ArrayList<String>();
			}
			delEntries(numbers, prefs);
			updateEntries();
		} else if (key.equalsIgnoreCase("active")) {
			ComponentName component = new ComponentName(Settings.this,
					SMSProcess.class);
			if (prefs.getBoolean("active", false)) {
				Settings.this.getPackageManager().setComponentEnabledSetting(
						component,
						PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
						PackageManager.DONT_KILL_APP);
			} else {
				Settings.this.getPackageManager().setComponentEnabledSetting(
						component,
						PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
						PackageManager.DONT_KILL_APP);
			}
		}
	}

}
