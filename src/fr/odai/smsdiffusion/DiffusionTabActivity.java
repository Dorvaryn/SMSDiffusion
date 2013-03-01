package fr.odai.smsdiffusion;

import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

public class DiffusionTabActivity extends FragmentActivity implements
		TabHost.OnTabChangeListener, FragementCallbacks{

	private TabHost mTabHost;
	private int list_id;

	@SuppressWarnings("rawtypes")
	private HashMap mapTabInfo = new HashMap();
	private TabInfo mLastTab = null;

	@SuppressWarnings("rawtypes")
	private class TabInfo {
		private String tag;
		private Class clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	private class TabFactory implements TabContentFactory {

		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diffusion_tab);
		list_id = getIntent().getExtras().getInt("list_id");
		// On initialise les Tabs
		initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			// On restaure le tab s�lectionn� si il y a un �tat conserv�
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		// sauvegarde de la tab s�lectionn�e
		outState.putString("tab", mTabHost.getCurrentTabTag());
		super.onSaveInstanceState(outState);
	}

	/**
	 * Initialisation des Tabs
	 */
	@SuppressWarnings("unchecked")
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		Resources res = getResources();
		
		DiffusionTabActivity.addTab(
				this,
				this.mTabHost,
				this.mTabHost.newTabSpec("List").setIndicator(
						res.getString(R.string.diffusion_list_title)),
				(tabInfo = new TabInfo("List", DiffusionListFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		DiffusionTabActivity.addTab(
				this,
				this.mTabHost,
				this.mTabHost.newTabSpec("Keywords").setIndicator(
						res.getString(R.string.diffusion_keyword_title)),
				(tabInfo = new TabInfo("Keywords", DiffusionKeywordFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		DiffusionTabActivity.addTab(
				this,
				this.mTabHost,
				this.mTabHost.newTabSpec("Contacts").setIndicator(
						res.getString(R.string.diffusion_contact_title)),
				(tabInfo = new TabInfo("Contacts", DiffusionContactFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		// Tab par défaut
		this.onTabChanged("List");
		mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * Ajout d'une tab au conteneur
	 * 
	 * @param activity
	 *            activit� concern�e
	 * @param tabHost
	 *            Hote des Tabs
	 * @param tabSpec
	 *            nouvelle spec de la tab
	 * @param tabInfo
	 *            infos de la � cr�er
	 */
	private static void addTab(DiffusionTabActivity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {

		// On Attache une factory � la spec de la tab
		tabSpec.setContent(activity.new TabFactory(activity));
		String tag = tabSpec.getTag();

		// On v�rifie si un fragment est d�j� li� � cette spec au quel cas
		// on le d�sactive afin de garantir l'�tat initial cach�
		tabInfo.fragment = activity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = activity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			activity.getSupportFragmentManager().executePendingTransactions();
		}
		tabHost.addTab(tabSpec);
	}

	/**
	 * Changement de Tabs
	 * 
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	public void onTabChanged(String tag) {
		TabInfo newTab = (TabInfo) this.mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getSupportFragmentManager()
					.beginTransaction();
			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this,
							newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				} else {
					ft.attach(newTab.fragment);
				}
			}

			mLastTab = newTab;
			ft.commit();
			this.getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public int getListId() {
		return list_id;
	}

	@Override
	protected void onResume() {
		super.onResume();
		list_id = getIntent().getExtras().getInt("list_id");
	}

}
