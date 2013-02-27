package fr.odai.smsdiffusion;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import fr.odai.smsdiffusion.adapter.ContactAdapter;
import fr.odai.smsdiffusion.adapter.POJOContact;

public class DiffusionContactFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.activity_diffusion_contact, container, false);

		AutoCompleteTextView phoneNumber = (AutoCompleteTextView) root.findViewById(R.id.text_contact);

		ArrayList<POJOContact> allContacts = POJOContact.getAllContacts(getActivity());
		ContactAdapter adapter = new ContactAdapter(getActivity(), R.layout.item_contact,
				allContacts);

		phoneNumber.setAdapter(adapter);
		return root;
	}
}
