package tk.sdxuyan.game;

import com.xuyan.happylink.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LinkActivity extends Activity {
	private ImageButton btnPlay;
	private ImageButton btnRefresh;
	private ImageButton btnTip;
	private ImageView imgTitle;
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

	private void setMusic(Context context) {
		player_init = MediaPlayer.create(this, R.raw.bg);
		player_play = MediaPlayer.create(this, R.raw.back2new);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_link);
		/**
		 * 绑定各类控件
		 * */
		btnPlay = (ImageButton) findViewById(R.id.play_btn);
		imgTitle = (ImageView) findViewById(R.id.title_img);
		myview = (MyView) findViewById(R.id.myview);
		btnRefresh = (ImageButton) findViewById(R.id.refresh_btn);
		btnTip = (ImageButton) findViewById(R.id.tip_btn);
		textRefreshNum = (TextView) findViewById(R.id.text_refresh_num);
		textTipNum = (TextView) findViewById(R.id.text_tip_num);
		progress = (ProgressBar) findViewById(R.id.timer);

		BoardView.initSound(this);

		setMusic(this);

		// player_init = MediaPlayer.create(this, R.raw.bg);
		// player_play=MediaPlayer.create(this, R.raw.back2new);
		player_init.setLooping(true);// 设置循环播放
		player_init.start();

		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnPlay.setVisibility(View.GONE);
				imgTitle.setVisibility(View.GONE);
				myview.setVisibility(View.VISIBLE);
				btnRefresh.setVisibility(View.VISIBLE);
				btnTip.setVisibility(View.VISIBLE);
				progress.setVisibility(View.VISIBLE);
				textRefreshNum.setVisibility(View.VISIBLE);
				textTipNum.setVisibility(View.VISIBLE);

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

		});
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.link, menu);
		return true;
	}

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

	private void myDialog(View v, String state, int time, int msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
				LinkActivity.this.finish();
				player_play.stop();
			}
		}).create();
		builder.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		player_play.pause();
		player_init.pause();
		isPause = true;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		setMusic(this);
		isPause = false;
		if (isPlay) {
			player_play.start();
			thread = new MyThread();
			thread.start();
		} else {
			player_init.start();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
