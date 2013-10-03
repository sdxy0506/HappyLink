package tk.sdxuyan.game;

import tk.sdxuyan.fragment.GameFragment;
import tk.sdxuyan.tool.Contants;
import tk.sdxuyan.tool.GameDone;
import tk.sdxuyan.tool.MyDialog;
import tk.sdxuyan.tool.Perference;
import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class onGameStateListener extends AsyncTask<Object, Object, integer>
		implements GameDone, Perference {

	private GameView myView;
	private int leftTime;
	private int state;
	private GameFragment fragment;
	SharedPreferences scoreInfo;

	@Override
	protected integer doInBackground(Object... params) {
		fragment = (GameFragment) params[0];
		myView = (GameView) params[1];
		state = (Integer) params[2];
		leftTime = (Integer) params[3];
		setSharedPreferences();
		return null;
	}

	@Override
	protected void onPostExecute(integer result) {
		super.onPostExecute(result);
		if (myView.die()) {
			myView.change();
		}
		switchState();
	}

	private void switchState() {
		if (myView.win()) {
			state = Contants.game_win;
			GameWin();
		} else if (leftTime++ == -1) {
			state = Contants.game_over;
			GameOver();
		} else {
			state = Contants.game_isPlaying;
		}
		fragment.setGameState(state);
	}

	@Override
	public void GameWin() {
		fragment.stopMusic();
		fragment.stopTimer();
		StringBuffer msg = new StringBuffer();
		msg.append("胜利了！");
		if (myView.getScore() > getScore()) {
			addScore();
			msg.append("新纪录：" + myView.getScore() + "分。");
		}
		new MyDialog(fragment.getActivity(), myView, fragment, msg.toString(),
				fragment.getTotalTime() - leftTime).show();

	}

	@Override
	public void GameOver() {
		fragment.stopMusic();
		fragment.stopTimer();
		StringBuffer msg = new StringBuffer();
		msg.append("时间到！");
		if (myView.getScore() > getScore()) {
			addScore();
			msg.append("新纪录：" + myView.getScore() + "分。");
		}
		new MyDialog(fragment.getActivity(), myView, fragment, msg.toString(),
				fragment.getTotalTime() - leftTime).show();
		BoardView.soundPlay.play(Contants.ID_SOUND_LOSE, 0);

		// Log.i("scorepp", "" + scoreInfo.getInt("score", 1));

	}

	@Override
	public void setSharedPreferences() {
		scoreInfo = fragment.getActivity().getSharedPreferences("scoreInfo",
				Context.MODE_PRIVATE);
	}

	@Override
	public int getScore() {
		return scoreInfo.getInt("score", 1);
	}

	@Override
	public void addScore() {
		myView.setScore(myView.getScore() + leftTime * myView.getSingleScore()
				/ 10);
		if (scoreInfo.getInt("score", 0) != 0) {
			scoreInfo.edit().putInt("score", myView.getScore()).commit();
		} else {
			scoreInfo.edit().putInt("score", myView.getScore()).commit();
		}
	}
}
