package tk.sdxuyan.tool;

import tk.sdxuyan.fragment.GameFragment;
import tk.sdxuyan.fragment.WelcomeFragment;
import tk.sdxuyan.slidemenudemo.FragmentChangeActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xuyan.happylink.R;

public class MyDialog extends Dialog implements OnClickListener {

	private Context context;
	private GameFragment fragment;

	public MyDialog(Context context, GameFragment gameFragment, String msg,
			int time) {
		super(context, R.style.dialog);
		this.context = context;
		this.fragment = gameFragment;
		this.setContentView(R.layout.dialog);
		TextView text_msg = (TextView) findViewById(R.id.text_message);
		TextView text_time = (TextView) findViewById(R.id.text_time);
		ImageButton btn_menu = (ImageButton) findViewById(R.id.menu_imgbtn);
		ImageButton btn_next = (ImageButton) findViewById(R.id.next_imgbtn);
		ImageButton btn_replay = (ImageButton) findViewById(R.id.replay_imgbtn);

		text_msg.setText(msg);
		text_time.setText(text_time.getText().toString()
				.replace("$", String.valueOf(time)));
		btn_menu.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_replay.setOnClickListener(this);
		this.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
		switch (v.getId()) {
		case R.id.menu_imgbtn:
			Dialog dialog = new AlertDialog.Builder(context)
					.setIcon(R.drawable.buttons_bg20)
					.setTitle(R.string.quit)
					.setMessage(R.string.sure_quit)
					.setCancelable(false)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									((FragmentActivity) context).finish();
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (fragment.getActivity() == null)
										return;

									if (fragment.getActivity() instanceof FragmentChangeActivity) {
										FragmentChangeActivity fca = (FragmentChangeActivity) fragment
												.getActivity();
										fca.switchContent(new WelcomeFragment());
									}
								}
							}).create();
			dialog.show();
			break;
		case R.id.replay_imgbtn:
			fragment.start();
			break;
		case R.id.next_imgbtn:
			if (fragment.getTotalTime() > 10) {
				fragment.setTotalTime(fragment.getTotalTime() - 10);
				fragment.start();
				return;
			}
			fragment.start();
			break;
		}
	}
}
