package tk.sdxuyan.fragment;

import tk.sdxuyan.game.BoardView;
import tk.sdxuyan.game.MyView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuyan.happylink.R;

public class GameFragment extends Fragment {

	private View gameView;
	private ImageButton btnRefresh;
	private ImageButton btnTip;
	private MyView myview;
	private ProgressBar progress;
	private TextView textRefreshNum;
	private TextView textTipNum;

	private MediaPlayer player_init, player_play;

	private boolean isStop = false;
	private boolean isPause = false;
	private boolean isPlay = false;

	private int leftTime;
	private int RefreshNum;
	private int TipNum;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		gameView = inflater.inflate(R.layout.play_game, container, false);
		init();
		return gameView;
	}

	private void init() {
		/**
		 * 绑定各类控件
		 * */
		myview = (MyView) gameView.findViewById(R.id.amyview);
		btnRefresh = (ImageButton) gameView.findViewById(R.id.arefresh_btn);
		btnTip = (ImageButton) gameView.findViewById(R.id.atip_btn);
		textRefreshNum = (TextView) gameView
				.findViewById(R.id.atext_refresh_num);
		textTipNum = (TextView) gameView.findViewById(R.id.atext_tip_num);
		progress = (ProgressBar) gameView.findViewById(R.id.atimer);

		BoardView.initSound(getActivity());

		setMusic(getActivity());

		player_init.setLooping(true);// 设置循环播放
		player_init.start();

		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RefreshNum > 0) {
					myview.change();
					RefreshNum--;
				}
				Message m = new Message();
				m.what = 4;
				mHandler.sendMessage(m);
			}
		});
		btnTip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TipNum > 0) {
					myview.auto_clear();
					TipNum--;
				}
				Message m = new Message();
				m.what = 4;
				mHandler.sendMessage(m);
			}
		});
		start();
	}

	private void start() {
		progress.setProgress(myview.getTotalTime());
		player_init.pause();
		player_play.setLooping(true);
		player_play.start();
		isPlay = true;

		myview.play();
		leftTime = myview.getTotalTime();
		RefreshNum = myview.getRefreshNum();
		TipNum = myview.getTipNum();
		thread = new MyThread();
		// time=myview.total_time;
		thread.start();
		// myview.invalidate();
		Message m = new Message();
		m.what = 4;
		mHandler.sendMessage(m);
	}

	private MyThread thread;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				myDialog(myview, "胜利！", myview.getTotalTime() - leftTime, 0);
				break;
			case 1:
				progress.setProgress(leftTime);
				break;
			case 2:
				myDialog(myview, "失败！", myview.getTotalTime() - leftTime, 2);
				break;
			case 3:
				myview.change();
				break;
			case 4:
				textRefreshNum.setText(" " + RefreshNum);
				textTipNum.setText(" " + TipNum);
				break;
			}
		}

	};

	private class MyThread extends Thread {
		public void run() {
			while (leftTime >= 0 && !isStop && !isPause) {

				if (myview.win())
					isStop = true;
				else {
					if (myview.die()) {
						Message m = new Message();
						m.what = 3;
						mHandler.sendMessage(m);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Message m = new Message();
					m.what = 1;
					mHandler.sendMessage(m);
					leftTime--;
				}
			}
			if (!isPause) {
				Message m = new Message();
				if (isStop && !isPause)
					m.what = 0;
				// mHandler.sendMessage(m);
				else {
					m.what = 2;
					// mHandler.sendMessage(m);
				}
				mHandler.sendMessage(m);
			}
		}
	}

	private void setMusic(Context context) {
		player_init = MediaPlayer.create(getActivity(), R.raw.bg);
		player_play = MediaPlayer.create(getActivity(), R.raw.back2new);
	}

	private void myDialog(View v, String state, int time, int msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(false);
		builder.setTitle(state).setMessage("所用时间为：" + time + "秒！");

		if (msg == 0) {
			builder.setPositiveButton("下一关",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							myview.next();
							isStop = false;
							leftTime = myview.getTotalTime();
							RefreshNum = myview.getRefreshNum();
							TipNum = myview.getTipNum();
							thread = new MyThread();
							thread.start();
							Message m = new Message();
							m.what = 4;
							mHandler.sendMessage(m);
						}
					});
		} else {
			builder.setPositiveButton("重新开始",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							myview.play();
							isStop = false;
							myview.setTotal_time(100);
							leftTime = myview.getTotalTime();
							RefreshNum = myview.getRefreshNum();
							TipNum = myview.getTipNum();
							thread = new MyThread();
							thread.start();
							Message m = new Message();
							m.what = 4;
							mHandler.sendMessage(m);
						}
					});
		}

		builder.setNegativeButton("退出！", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				getActivity().finish();
				player_play.stop();
			}
		}).create();
		builder.show();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		player_play.stop();
		player_init.stop();
	}

	@Override
	public void onPause() {
		super.onPause();
		player_play.pause();
		player_init.pause();
		isPause = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		isPause = false;
		if (isPlay) {
			player_play.start();
			thread = new MyThread();
			thread.start();
		} else {
			player_init.start();
		}
	}

}
