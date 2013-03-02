package fr.odai.smsdiffusion.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import fr.odai.smsdiffusion.FragementCallbacks;
import fr.odai.smsdiffusion.R;
import fr.odai.smsdiffusion.adapter.KeywordAdapter;
import fr.odai.smsdiffusion.db.DBHelper;
import fr.odai.smsdiffusion.utils.AndroidUtils;
import fr.odai.smsdiffusion.widget.HiddenQuickActionSetup;
import fr.odai.smsdiffusion.widget.HiddenQuickActionSetup.OnQuickActionListener;

public class DiffusionKeywordFragment extends ListFragment implements
		OnQuickActionListener {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private FragementCallbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private static final class QuickAction {
		public static final int CONFIRM = 1;
	}

	private HiddenQuickActionSetup mQuickActionSetup;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
		View root = inflater.inflate(R.layout.fragment_diffusion_keyword, container, false);

		final EditText keyword = (EditText) root
				.findViewById(R.id.text_keyword);
		final ImageButton add = (ImageButton) root
				.findViewById(R.id.button_add);
		add.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				String value = keyword.getText().toString();
				if(!value.equalsIgnoreCase("")){
					if(!((KeywordAdapter) getListAdapter()).contains(value)){
						DBHelper.insertKeyword(getActivity(), mCallbacks.getListId(),
								keyword.getText().toString());
						((ArrayAdapter<String>) getListAdapter()).add(keyword.getText()
								.toString());
						((BaseAdapter) getListAdapter()).notifyDataSetChanged();
						keyword.setText("");
					}
				}
			}
		});

		keyword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					add.performClick();
					return true;
				}
				return false;
			};
		});
		setupQuickAction();
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
		ArrayList<String> keywords = DBHelper.getKeywords(getActivity(),
				mCallbacks.getListId());
		setListAdapter(new KeywordAdapter(getActivity(), R.layout.item_list, keywords, mQuickActionSetup));
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

	private void setupQuickAction() {
		Context ctx = getActivity();
		mQuickActionSetup = new HiddenQuickActionSetup(ctx);
		mQuickActionSetup.setOnQuickActionListener(this);

		int imageSize = AndroidUtils.dipToPixel(ctx, 40);
		
		// a nice cubic ease animation
		mQuickActionSetup.setOpenAnimation(new Interpolator() {
			@Override
			public float getInterpolation(float v) {
				v -= 1;
				return v * v * v + 1;
			}
		});
		mQuickActionSetup.setCloseAnimation(new Interpolator() {
			@Override
			public float getInterpolation(float v) {
				return v * v * v;
			}
		});

		mQuickActionSetup.setBackgroundResource(android.R.color.darker_gray);
		mQuickActionSetup.setImageSize(imageSize, imageSize);
		mQuickActionSetup.setAnimationSpeed(700);
		mQuickActionSetup.setStartOffset(AndroidUtils.dipToPixel(ctx, 20));
		mQuickActionSetup.setStopOffset(AndroidUtils.dipToPixel(ctx, 50));
		mQuickActionSetup.setStickyStart(false);
		mQuickActionSetup.setSwipeOnLongClick(true);

		mQuickActionSetup.setConfirmationMessage(QuickAction.CONFIRM,
				R.string.diffusion_keyword_remove_confirm, R.drawable.ic_confirm,
				R.string.diffusion_keyword_remove_message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onQuickAction(AdapterView<?> parent, View view, int position,
			int quickActionId) {
		switch (quickActionId) {
		case QuickAction.CONFIRM:
			String toDelete = (String) getListAdapter().getItem(position);
			((ArrayAdapter<String>) getListAdapter()).remove(toDelete);
			DBHelper.removeKeyword(getActivity(), mCallbacks.getListId(),
					toDelete);
			((BaseAdapter) getListAdapter()).notifyDataSetChanged();
			break;

		default:
			break;
		}
	}

}
