package tk.sdxuyan.game;

import com.xuyan.happylink.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class HappyLinkActivity extends Activity {

	private ImageButton btnPlay;
	private ImageView imgTitle;
	FragmentManager fragmentManager;
	Fragment mContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		btnPlay = (ImageButton) findViewById(R.id.play_btn);
		imgTitle = (ImageView) findViewById(R.id.title_img);
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}

		});
	}

}
