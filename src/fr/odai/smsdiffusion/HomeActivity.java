package fr.odai.smsdiffusion;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import fr.odai.smsdiffusion.adapter.ListAdapter;
import fr.odai.smsdiffusion.adapter.POJOList;
import fr.odai.smsdiffusion.db.DBHelper;

public class HomeActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setListAdapter(new ListAdapter(this, R.layout.item_list,
				DBHelper.getDiffusionLists(this)));
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
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(getResources().getString(R.string.home_menu_add));
			alert.setMessage(getResources().getString(
					R.string.home_menu_add_dialog));

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			input.setSingleLine();
			alert.setView(input);

			alert.setPositiveButton(
					getResources().getString(R.string.dialog_button_positive),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
							String value = input.getText().toString();
							DBHelper.insertList(getBaseContext(), value, false);
							setListAdapter(new ListAdapter(
									getBaseContext(),
									R.layout.item_list,
									DBHelper.getDiffusionLists(getBaseContext())));
						}
					});

			alert.setNegativeButton(
					getResources().getString(R.string.dialog_button_negative),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});

			alert.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
