package fr.odai.smsdiffusion;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import fr.odai.smsdiffusion.adapter.ContactAdapter;
import fr.odai.smsdiffusion.adapter.POJOContact;

public class DiffusionContactFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.activity_diffusion_contact, container, false);


		// List adapter
		final ContactAdapter adapter = new ContactAdapter(getActivity(), R.layout.item_contact, new ArrayList<POJOContact>());
		setListAdapter(adapter);
		
		// Autocomplete adapter
		final AutoCompleteTextView phoneNumber = (AutoCompleteTextView) root.findViewById(R.id.text_contact);
		ArrayList<POJOContact> allContacts = POJOContact.getAllContacts(getActivity());
		final ContactAdapter autoAdapter = new ContactAdapter(getActivity(), R.layout.item_contact,
				allContacts);
		

		phoneNumber.setAdapter(autoAdapter);
		phoneNumber.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> contactAdapter, View arg1, int position, long arg3) {
				adapter.add(autoAdapter.contacts.get(position));
				phoneNumber.setText("");
			}
		});
		

		
		return root;
	}
}
