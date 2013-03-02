package fr.odai.smsdiffusion;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import fr.odai.smsdiffusion.adapter.ListAdapter;
import fr.odai.smsdiffusion.db.DBHelper;
import fr.odai.smsdiffusion.model.POJOList;
import fr.odai.smsdiffusion.utils.AndroidUtils;
import fr.odai.smsdiffusion.widget.HiddenQuickActionSetup;
import fr.odai.smsdiffusion.widget.HiddenQuickActionSetup.OnQuickActionListener;

public class HomeActivity extends ListActivity implements OnQuickActionListener {

	private static final class QuickAction {
		public static final int CONFIRM = 1;
	}

	private HiddenQuickActionSetup mQuickActionSetup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setTitle(R.string.app_name);
		setupQuickAction();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setListAdapter(new ListAdapter(this, R.layout.item_list,
				DBHelper.getDiffusionLists(this), mQuickActionSetup));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_home, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent it = new Intent(this, DiffusionTabActivity.class);
		it.putExtra("list_id",
				((POJOList) getListAdapter().getItem(position)).getId());
		startActivity(it);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Click on title in actionbar
		switch (item.getItemId()) {
		case R.id.menu_add:
			long id = DBHelper.insertList(getBaseContext(), getResources()
					.getString(R.string.home_default_list_name), true);
			Intent it = new Intent(this, DiffusionTabActivity.class);
			it.putExtra("list_id", id);
			startActivity(it);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupQuickAction() {
		mQuickActionSetup = new HiddenQuickActionSetup(this);
		mQuickActionSetup.setOnQuickActionListener(this);

		int imageSize = AndroidUtils.dipToPixel(this, 40);

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
		mQuickActionSetup.setStartOffset(AndroidUtils.dipToPixel(this, 20));
		mQuickActionSetup.setStopOffset(AndroidUtils.dipToPixel(this, 50));
		mQuickActionSetup.setStickyStart(false);
		mQuickActionSetup.setSwipeOnLongClick(true);

		mQuickActionSetup.setConfirmationMessage(QuickAction.CONFIRM,
				R.string.home_remove_confirm, R.drawable.ic_confirm,
				R.string.home_remove_message);
	}

	@Override
	public void onQuickAction(AdapterView<?> parent, View view, int position,
			int quickActionId) {
		switch (quickActionId) {
		case QuickAction.CONFIRM:
			POJOList toDelete = (POJOList) getListAdapter().getItem(position);
			DBHelper.removeList(this, toDelete.getId());
			setListAdapter(new ListAdapter(this, R.layout.item_list,
					DBHelper.getDiffusionLists(this), mQuickActionSetup));
			break;

		default:
			break;
		}
	}

}
