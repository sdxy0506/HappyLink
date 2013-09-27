package tk.sdxuyan.fragment;

import java.util.Timer;
import java.util.TimerTask;

import tk.sdxuyan.game.BoardView;
import tk.sdxuyan.game.MyView;
import tk.sdxuyan.game.RefreshGameView;
import tk.sdxuyan.tool.Contants;
import tk.sdxuyan.tool.Music;
import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuyan.happylink.R;

public class GameFragment extends Fragment implements Music {

	private View gameView;
	private ImageButton btnRefresh;
	private ImageButton btnTip;
	private MyView myview;
	private ProgressBar progress;
	private TextView textRefreshNum;
	private TextView textTipNum;

	private MediaPlayer player_play;

	private boolean isStop = false;
	private boolean isPause = false;
	private boolean isPlay = false;

	private int leftTime;
	private int RefreshNum;
	private int TipNum;

	private Timer timer;
	private TimerTask task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		gameView = inflater.inflate(R.layout.play_game, container, false);
		timer = new Timer();
		timer.schedule(getTask(), 0, 1000);
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
		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RefreshNum > 0) {
					myview.change();
					RefreshNum--;
				}
			}
		});
		btnTip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TipNum > 0) {
					myview.auto_clear();
					TipNum--;
				}
			}
		});
		start();
	}

	private void start() {
		progress.setProgress(myview.getTotalTime());

		isPlay = true;

		setMusic();
		myview.play();
		leftTime = myview.getTotalTime();
		RefreshNum = myview.getRefreshNum();
		TipNum = myview.getTipNum();
	}

	// private MyThread thread;
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
				else {
					m.what = 2;
				}
				mHandler.sendMessage(m);
			}
		}
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
							// thread = new MyThread();
							// thread.start();
							// Message m = new Message();
							// m.what = 4;
							// mHandler.sendMessage(m);
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
							// thread = new MyThread();
							// thread.start();
							// Message m = new Message();
							// m.what = 4;
							// mHandler.sendMessage(m);
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
	}

	@Override
	public void onPause() {
		super.onPause();
		player_play.pause();
		isPause = true;
		stopTimer();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (timer == null) {
			startTimer();
		}
		isPause = false;
		if (isPlay) {
			player_play.start();
		}
	}

	// 启动定时器
	private void startTimer() {
		timer = new Timer();
		timer.schedule(getTask(), 0, 1000);
	}

	private void stopTimer() {
		timer.cancel();
		timer = null;
	}

	public TimerTask getTask() {
		task = new TimerTask() {

			@Override
			public void run() {
				if (leftTime > 0) {
					RefreshGameView refreshGameView = new RefreshGameView();
					refreshGameView.execute(myview, Contants.game_refresh,
							leftTime--);
					progress.setProgress(leftTime);
				}

			}
		};
		return task;
	}

	@Override
	public void setMusic() {
		player_play = MediaPlayer.create(getActivity(), R.raw.back2new);
		player_play.setLooping(true);
		player_play.start();

	}

}
