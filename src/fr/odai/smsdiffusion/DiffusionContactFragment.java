package fr.odai.smsdiffusion;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import fr.odai.smsdiffusion.adapter.ContactAdapter;
import fr.odai.smsdiffusion.adapter.POJOContact;

public class DiffusionContactFragment extends ListFragment {

	public OnTouchListener gestureListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.activity_diffusion_contact, container, false);

		gestureListener = new View.OnTouchListener() {
			private int padding = 0;
			private int initialx = 0;
			private int currentx = 0;

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					padding = 0;
					initialx = (int) event.getX();
					currentx = (int) event.getX();
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					currentx = (int) event.getX();
					padding = currentx - initialx;
				}

				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					padding = 0;
					initialx = 0;
					currentx = 0;
				}

				v.setPadding(padding, 0, 0, 0);

				return true;
			}
		};

		// List adapter
		final ContactAdapter adapter = new ContactAdapter(getActivity(), R.layout.item_contact,
				new ArrayList<POJOContact>(), gestureListener);
		setListAdapter(adapter);

		// Autocomplete adapter
		final AutoCompleteTextView phoneNumber = (AutoCompleteTextView) root
				.findViewById(R.id.text_contact);
		ArrayList<POJOContact> allContacts = POJOContact.getAllContacts(getActivity());
		final ContactAdapter autoAdapter = new ContactAdapter(getActivity(), R.layout.item_contact,
				allContacts, null);

		phoneNumber.setAdapter(autoAdapter);
		phoneNumber.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> contactAdapter, View arg1, int position,
					long arg3) {
				adapter.add(autoAdapter.getItem(position));
				phoneNumber.setText("");
			}
		});

		return root;
	}
}
