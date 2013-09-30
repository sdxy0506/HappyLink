package tk.sdxuyan.slidemenudemo;

import tk.sdxuyan.fragment.WelcomeFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xuyan.happylink.R;

public class FragmentChangeActivity extends BaseActivity {

	private Fragment mContent;
	private long exitTime = 0;

	public FragmentChangeActivity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new WelcomeFragment();

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new ColorMenuFragment()).commit();

		// 控制SlidingMenu的滑动打开方式
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Dialog dialog = new AlertDialog.Builder(mContent.getActivity())
			// .setIcon(R.drawable.buttons_bg20)
			// .setTitle(R.string.quit)
			// .setMessage(R.string.sure_quit)
			// .setCancelable(false)
			// .setPositiveButton(R.string.alert_dialog_ok,
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int whichButton) {
			// mContent.getActivity().finish();
			// }
			// })
			// .setNegativeButton(R.string.alert_dialog_cancel,
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int whichButton) {
			//
			// }
			// }).create();
			// dialog.show();
			exit();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

	@Override
	protected void onDestroy() {
		System.exit(0);
		super.onDestroy();
	}

}
