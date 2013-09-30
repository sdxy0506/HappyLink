package tk.sdxuyan.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuyan.happylink.R;

public class ScoreFragment extends Fragment {

	private View view;
	private TextView tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.score_history, container, false);
		SharedPreferences scoreInfo = getActivity().getSharedPreferences(
				"scoreInfo", Context.MODE_PRIVATE);
		tv = (TextView) view.findViewById(R.id.tv_score_history);
		tv.setText("" + scoreInfo.getInt("score", 0));
		return view;
	}

}
