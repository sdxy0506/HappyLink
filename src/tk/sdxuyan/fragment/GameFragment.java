package tk.sdxuyan.fragment;

import java.util.Timer;
import java.util.TimerTask;

import tk.sdxuyan.game.BoardView;
import tk.sdxuyan.game.MyView;
import tk.sdxuyan.game.RefreshGameState;
import tk.sdxuyan.tool.Contants;
import tk.sdxuyan.tool.Music;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
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

	private int leftTime;
	private int RefreshNum;
	private int TipNum;
	private int GameState = Contants.game_isPlaying;// 游戏状态为正在进行游戏

	private Timer timer;
	private TimerTask task;

	private GameFragment fragment;

	public GameFragment() {
		super();
		fragment = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		gameView = inflater.inflate(R.layout.play_game, container, false);
		init();
		// startTimer();
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

	public void start() {
		setMusic();
		leftTime = myview.getTotalTime();
		RefreshNum = myview.getRefreshNum();
		TipNum = 1000;
		progress.setMax(leftTime);
		progress.setProgress(leftTime);
		startTimer();
		myview.play();
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
		stopTimer();
	}

	@Override
	public void onPause() {
		super.onPause();
		player_play.pause();
		stopTimer();
	}

	@Override
	public void onResume() {
		super.onResume();
		startTimer();
		player_play.start();
	}

	// 启动定时器
	private void startTimer() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(getTask(), 0, 1000);
		}
	}

	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	// 设置定时器的任务
	public TimerTask getTask() {
		task = new TimerTask() {

			@Override
			public void run() {
				progress.setProgress(leftTime);
				RefreshGameState refreshGameView = new RefreshGameState();
				refreshGameView.execute(fragment, myview, GameState, leftTime);
				Log.i("timerTask", "" + leftTime);
				leftTime--;
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

	public void setGameState(int state) {
		GameState = state;
	}

}
