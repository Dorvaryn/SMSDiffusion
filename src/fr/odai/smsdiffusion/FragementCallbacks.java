package fr.odai.smsdiffusion;

import android.support.v4.app.Fragment;

public interface FragementCallbacks {

	public void onItemSelected(int position, Fragment source);
	public int getListId();

}
