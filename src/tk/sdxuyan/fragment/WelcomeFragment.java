package tk.sdxuyan.fragment;

import tk.sdxuyan.slidemenudemo.FragmentChangeActivity;
import tk.sdxuyan.tool.Music;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xuyan.happylink.R;

public class WelcomeFragment extends Fragment implements Music {

	private View welcomeView;
	private ImageButton btnPlay;
	private TextView version;
	private MediaPlayer play_welcome;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		welcomeView = inflater.inflate(R.layout.welcome, container, false);
		init();
		setMusic();
		return welcomeView;
	}

	private void init() {
		version = (TextView) welcomeView.findViewById(R.id.version);
		PackageManager manager = getActivity().getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getActivity()
					.getPackageName(), 0);
			version.setText("版本号:" + info.versionName);
		} catch (Exception e) {

		}

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

	@Override
	public void onDestroyView() {
		play_welcome.stop();
		super.onDestroyView();
	}

	@Override
	public void setMusic() {
		play_welcome = MediaPlayer.create(getActivity(), R.raw.bg);
		play_welcome.setLooping(true);// 设置循环播放
		play_welcome.start();
	}

	@Override
	public void onPause() {
		play_welcome.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		play_welcome.start();
		super.onResume();
	}

}
