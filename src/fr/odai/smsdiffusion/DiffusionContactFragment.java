package fr.odai.smsdiffusion;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import fr.odai.smsdiffusion.adapter.ContactAdapter;
import fr.odai.smsdiffusion.adapter.POJOContact;
import fr.odai.smsdiffusion.adapter.SwipeDismissListViewTouchListener;
import fr.odai.smsdiffusion.db.DBHelper;

public class DiffusionContactFragment extends ListFragment {

		private static final String STATE_ACTIVATED_POSITION = "activated_position";

		private FragementCallbacks mCallbacks = sDummyCallbacks;
		private int mActivatedPosition = ListView.INVALID_POSITION;

		
		private static FragementCallbacks sDummyCallbacks = new FragementCallbacks() {
			@Override
			public int getListId() {
				return 0;
			}
		};

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
				setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
			}View root = inflater.inflate(R.layout.fragment_diffusion_contact, container, false);
			
			// Autocomplete adapter
			final AutoCompleteTextView phoneNumber = (AutoCompleteTextView) root.findViewById(R.id.text_contact);
			ArrayList<POJOContact> allContacts = POJOContact.getAllContacts(getActivity());
			final ContactAdapter autoAdapter = new ContactAdapter(getActivity(), R.layout.item_contact,
					allContacts);
			

			phoneNumber.setAdapter(autoAdapter);
			phoneNumber.setOnItemClickListener(new OnItemClickListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onItemClick(AdapterView<?> contactAdapter, View arg1, int position, long arg3) {
					POJOContact newContact = autoAdapter.getItem(position);
					DBHelper.insertContact(getActivity(), mCallbacks.getListId(), newContact.phone);
					((ArrayAdapter<POJOContact>) getListAdapter()).add(newContact);
					phoneNumber.setText("");
				}
			});	
			
			final ImageButton add = (ImageButton) root.findViewById(R.id.button_add);
			add.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					if(!phoneNumber.getText().toString().equalsIgnoreCase("")){
						POJOContact newContact = autoAdapter.getItem(0);
						DBHelper.insertContact(getActivity(), mCallbacks.getListId(), newContact.phone);
						((ArrayAdapter<POJOContact>) getListAdapter()).add(newContact);
						phoneNumber.setText("");
					}
				}
			});
			
			phoneNumber.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						add.performClick();
						return true;
					}
					return false;
				};
			});
			
			return root;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setRetainInstance(true);
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			if (!(activity instanceof FragementCallbacks)) {
				throw new IllegalStateException("Activity must implement fragment's callbacks.");
			}
			mCallbacks = (FragementCallbacks) activity;
		}

		
		@Override
		public void onResume() {
			super.onResume();
			ArrayList<POJOContact> contacts = DBHelper.getContacts(getActivity(), mCallbacks.getListId());
			// List adapter
			final ContactAdapter adapter = new ContactAdapter(getActivity(), R.layout.item_contact, contacts);
			setListAdapter(adapter);
		}

		@Override
		public void onDetach() {
			super.onDetach();
			mCallbacks = sDummyCallbacks;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			if (mActivatedPosition != ListView.INVALID_POSITION) {
				outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
			}
		}

		public void setActivateOnItemClick(boolean activateOnItemClick) {
			getListView().setChoiceMode(
					activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
		}

		public void setActivatedPosition(int position) {
			if (position == ListView.INVALID_POSITION) {
				getListView().setItemChecked(mActivatedPosition, false);
			} else {
				getListView().setItemChecked(position, true);
			}

			mActivatedPosition = position;
		}

	@SuppressWarnings("unchecked")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView listView = getListView();
		SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                		listView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                	POJOContact toDelete = (POJOContact) getListAdapter().getItem(position);
                                	((ArrayAdapter<POJOContact>) getListAdapter()).remove(toDelete);
                                	DBHelper.removeContact(getActivity(), mCallbacks.getListId(), toDelete.phone);                                	
                                }
                                ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
	}
	
	
}
