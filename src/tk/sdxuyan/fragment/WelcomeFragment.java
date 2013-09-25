package tk.sdxuyan.fragment;

import tk.sdxuyan.slidemenudemo.FragmentChangeActivity;

import com.xuyan.happylink.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class WelcomeFragment extends Fragment {

	private View welcomeView;
	private ImageButton btnPlay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		welcomeView = inflater.inflate(R.layout.welcome, container, false);
		init();
		return welcomeView;
	}

	private void init() {
		btnPlay = (ImageButton) welcomeView.findViewById(R.id.aplay_btn);
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActivity() == null)
					return;

				if (getActivity() instanceof FragmentChangeActivity) {
					FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
					fca.switchContent(new GameFragment());
				}

			}
		});
	}

}
