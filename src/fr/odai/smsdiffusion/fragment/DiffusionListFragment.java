package fr.odai.smsdiffusion.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import fr.odai.smsdiffusion.FragementCallbacks;
import fr.odai.smsdiffusion.R;
import fr.odai.smsdiffusion.db.DBHelper;
import fr.odai.smsdiffusion.model.POJOList;

public class DiffusionListFragment extends Fragment {

	private EditText name;
	private POJOList list;
	private CompoundButton enabled;
	private FragementCallbacks mCallbacks = sDummyCallbacks;

	private static FragementCallbacks sDummyCallbacks = new FragementCallbacks() {
		@Override
		public long getListId() {
			return 0;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_diffusion_list,
				container, false);
		name = (EditText) root.findViewById(R.id.editName);
		enabled = (CompoundButton) root.findViewById(R.id.switchEnable);
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
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (FragementCallbacks) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		list = DBHelper.getDiffusionList(getActivity(), mCallbacks.getListId());
		name.setText(list.name);
		enabled.setChecked(list.enable);
	}

	@Override
	public void onPause() {
		super.onPause();
		list.enable = enabled.isChecked();
		list.name = name.getText().toString();
		DBHelper.updateList(getActivity(), list);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

}
