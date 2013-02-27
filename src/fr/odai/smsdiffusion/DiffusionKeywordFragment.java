package fr.odai.smsdiffusion;

import java.util.ArrayList;

import fr.odai.smsdiffusion.adapter.ContactAdapter;
import fr.odai.smsdiffusion.adapter.POJOContact;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class DiffusionKeywordFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View root = inflater.inflate(R.layout.activity_diffusion_keyword, container, false);

		// List adapter
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		//TODO: read from DB
		setListAdapter(adapter);
		
		final TextView textKeyword = (TextView) root.findViewById(R.id.text_keyword);
		ImageButton buttonAdd = (ImageButton) root.findViewById(R.id.button_add);
		
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String word = textKeyword.getText().toString().trim();
				
				adapter.add(word);
				//TODO: commit to DB
				textKeyword.setText("");
			}
		});
		
		return root;
	}
	
	
}
