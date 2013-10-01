package tk.sdxuyan.slidemenudemo;

import tk.sdxuyan.fragment.AboutMe;
import tk.sdxuyan.fragment.ScoreFragment;
import tk.sdxuyan.fragment.WelcomeFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xuyan.happylink.R;

public class ColorMenuFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] colors = getResources().getStringArray(R.array.color_names);
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, colors);
		setListAdapter(colorAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			newContent = new WelcomeFragment();
			break;
		case 1:
			Toast.makeText(getActivity(), "下个版本开放", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			newContent = new ScoreFragment();
			break;
		case 3:
			newContent = new AboutMe();
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
		Log.i("newContent", "" + newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof FragmentChangeActivity) {
			FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
			fca.switchContent(fragment);
		}

	}

}
