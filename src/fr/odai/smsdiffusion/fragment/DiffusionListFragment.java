package fr.odai.smsdiffusion.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import fr.odai.smsdiffusion.FragementCallbacks;
import fr.odai.smsdiffusion.R;
import fr.odai.smsdiffusion.db.DBHelper;
import fr.odai.smsdiffusion.model.POJOList;

public class DiffusionListFragment extends Fragment {

	private EditText name;
	private TextView infos;
	private POJOList list;
	private CompoundButton enabled;
	private FragementCallbacks mCallbacks = sDummyCallbacks;

	private static FragementCallbacks sDummyCallbacks = new FragementCallbacks() {
		@Override
		public long getListId() {
			return 0;
		}

		@Override
		public void updateTitle(String title) {
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_diffusion_list,
				container, false);
		name = (EditText) root.findViewById(R.id.editName);
		infos = (TextView) root.findViewById(R.id.infos);
		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				mCallbacks.updateTitle(s.toString());
			}
		});
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
		Context ctx = getActivity();
		list = DBHelper.getDiffusionList(ctx, mCallbacks.getListId());
		name.setText(list.name);
		enabled.setChecked(list.enable);		
		if(list.isInit(ctx)){
			if(list.lastSentDate != 0){
				String date = DateUtils.formatDateTime(ctx, list.lastSentDate, 0);
				String format = ctx.getResources().getString(R.string.diffusion_list_textInfos_full);
				String infosN = String.format(format, list.totalMessageSent, list.getNbContacts(ctx), date, list.lastMessage);
				infos.setText(infosN);
			}else {
				String format = ctx.getResources().getString(R.string.diffusion_list_textInfos_semi);
				String infosN = String.format(format, list.totalMessageSent, list.getNbContacts(ctx));
				infos.setText(infosN);
			}
		}else {
			infos.setText(R.string.diffusion_list_textInfos);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		list.enable = enabled.isChecked();
		if (!name.getText().toString().equalsIgnoreCase("")) {
			list.name = name.getText().toString();
		}
		mCallbacks.updateTitle(list.name);
		DBHelper.updateList(getActivity(), list);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

}
