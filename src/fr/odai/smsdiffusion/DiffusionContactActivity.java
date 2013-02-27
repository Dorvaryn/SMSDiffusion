package fr.odai.smsdiffusion;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import fr.odai.smsdiffusion.adapter.ContactAdapter;
import fr.odai.smsdiffusion.adapter.POJOContact;

public class DiffusionContactActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diffusion_contact);

		AutoCompleteTextView phoneNumber = (AutoCompleteTextView) findViewById(R.id.text_contact);

		ArrayList<POJOContact> allContacts = POJOContact.getAllContacts(this);
		ContactAdapter adapter = new ContactAdapter(this, R.layout.item_contact, allContacts);

		phoneNumber.setAdapter(adapter);
	}
}
