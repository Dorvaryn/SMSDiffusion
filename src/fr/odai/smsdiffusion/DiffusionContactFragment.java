package fr.odai.smsdiffusion;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import fr.odai.smsdiffusion.adapter.ContactAdapter;
import fr.odai.smsdiffusion.adapter.POJOContact;
import fr.odai.smsdiffusion.adapter.SwipeDismissListViewTouchListener;

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
				adapter.add(autoAdapter.getItem(position));
				phoneNumber.setText("");
			}
		});	
		
		return root;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView listView = getListView();
		final ArrayAdapter<POJOContact> adapter = (ArrayAdapter<POJOContact>) getListAdapter();
		SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                		listView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                	adapter.remove(adapter.getItem(position));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
	}
	
	
}
