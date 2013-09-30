package tk.sdxuyan.fragment;

import java.util.Timer;
import java.util.TimerTask;

import tk.sdxuyan.game.BoardView;
import tk.sdxuyan.game.GameView;
import tk.sdxuyan.game.onGameStateListener;
import tk.sdxuyan.game.onHelpNumChange;
import tk.sdxuyan.tool.Contants;
import tk.sdxuyan.tool.Music;
import tk.sdxuyan.tool.setTimer;
import android.media.MediaPlayer;
import android.os.Bundle;
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

public class GameFragment extends Fragment implements Music, setTimer {

	private View playView;
	private ImageButton btnRefresh;
	private ImageButton btnTip;
	private GameView gameView;
	private ProgressBar progress;
	private TextView textRefreshNum;
	private TextView textTipNum;
	private TextView textScore;

	private MediaPlayer player_play;

	private int leftTime;
	private int totalTime = Contants.game_time_initial;
	private int RefreshNum;
	private int TipNum;
	private int GameState = Contants.game_isPlaying;// 游戏状态为正在进行游戏

	private Timer timer;
	private Timer timerScore;
	private TimerTask task;
	private TimerTask taskScore;

	private GameFragment fragment;

	public GameFragment() {
		super();
		fragment = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		playView = inflater.inflate(R.layout.play_game, container, false);
		init();
		return playView;
	}

	public TextView getTextScore() {
		return textScore;
	}

	private void init() {
		/**
		 * 绑定各类控件
		 * */
		gameView = (GameView) playView.findViewById(R.id.amyview);
		btnRefresh = (ImageButton) playView.findViewById(R.id.arefresh_btn);
		btnTip = (ImageButton) playView.findViewById(R.id.atip_btn);
		textRefreshNum = (TextView) playView
				.findViewById(R.id.atext_refresh_num);
		textTipNum = (TextView) playView.findViewById(R.id.atext_tip_num);
		textScore = (TextView) playView.findViewById(R.id.tv_score);
		progress = (ProgressBar) playView.findViewById(R.id.atimer);
		BoardView.initSound(getActivity());
		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RefreshNum > 0) {
					gameView.change();
					RefreshNum--;
					new onHelpNumChange().execute(textRefreshNum, RefreshNum);
				}
			}
		});
		btnTip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TipNum > 0) {
					gameView.auto_clear();
					TipNum--;
					new onHelpNumChange().execute(textTipNum, TipNum);
				}
			}
		});
		start();
	}

	public void start() {
		setMusic();
		leftTime = getTotalTime();
		RefreshNum = Contants.game_refresh_number;
		TipNum = Contants.game_tip_number;
		new onHelpNumChange().execute(textRefreshNum, RefreshNum);
		new onHelpNumChange().execute(textTipNum, TipNum);
		progress.setMax(leftTime);
		progress.setProgress(leftTime);
		startTimer();
		gameView.play();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		stopMusic();
		stopTimer();
	}

	@Override
	public void onPause() {
		super.onPause();
		pauseMusic();
		stopTimer();
	}

	@Override
	public void onResume() {
		super.onResume();
		startTimer();
		startMusic();
	}

	// 设置定时器的任务
	public TimerTask getTask() {
		task = new TimerTask() {

			@Override
			public void run() {
				progress.setProgress(leftTime);
				onGameStateListener refreshGameView = new onGameStateListener();
				refreshGameView
						.execute(fragment, gameView, GameState, leftTime);
				Log.i("timerTask", "" + leftTime);
				leftTime--;
			}
		};
		return task;
	}

	public void setGameState(int state) {
		GameState = state;
	}

	@Override
	public void setMusic() {
		if (player_play == null) {
			player_play = MediaPlayer.create(getActivity(), R.raw.back2new);
			player_play.setLooping(true);
			startMusic();
		}

	}

	@Override
	public void startMusic() {
		if (player_play != null) {
			player_play.start();
		}

	}

	@Override
	public void stopMusic() {
		if (player_play != null) {
			player_play.stop();
			player_play = null;
		}

	}

	@Override
	public void pauseMusic() {
		if (player_play != null) {
			player_play.pause();
		}

	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public int getLeftTime() {
		return leftTime;
	}

	public Timer getTimer() {
		return timer;
	}

	@Override
	public void startTimer() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(getTask(), 0, 1000);
		}
		if (timerScore == null) {
			timerScore = new Timer();
			timerScore.schedule(taskScore = new TimerTask() {

				@Override
				public void run() {
					new onHelpNumChange().execute(textScore,
							gameView.getScore());
					new onGameStateListener().execute(fragment, gameView,
							GameState, leftTime);
				}
			}, 0, 100);
		}
	}

	@Override
	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerScore != null) {
			timerScore.cancel();
			timerScore = null;
		}
	}

}
