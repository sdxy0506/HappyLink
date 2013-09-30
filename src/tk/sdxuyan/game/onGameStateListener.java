package tk.sdxuyan.game;

import tk.sdxuyan.fragment.GameFragment;
import tk.sdxuyan.tool.Contants;
import tk.sdxuyan.tool.GameDone;
import tk.sdxuyan.tool.MyDialog;
import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class onGameStateListener extends AsyncTask<Object, Object, integer>
		implements GameDone {

	private GameView myView;
	private int leftTime;
	private int state;
	private GameFragment fragment;

	@Override
	protected integer doInBackground(Object... params) {
		fragment = (GameFragment) params[0];
		myView = (GameView) params[1];
		state = (Integer) params[2];
		leftTime = (Integer) params[3];
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
		setSharedPreferences();
		new MyDialog(fragment.getActivity(), myView, fragment, "胜利了！",
				fragment.getTotalTime() - leftTime).show();
		fragment.stopMusic();
		fragment.stopTimer();

	}

	@Override
	public void GameOver() {
		setSharedPreferences();
		new MyDialog(fragment.getActivity(), myView, fragment, "你输了！",
				fragment.getTotalTime() - leftTime).show();
		BoardView.soundPlay.play(Contants.ID_SOUND_LOSE, 0);
		fragment.stopMusic();
		fragment.stopTimer();
		// Log.i("scorepp", "" + scoreInfo.getInt("score", 1));

	}

	private void setSharedPreferences() {
		SharedPreferences scoreInfo = fragment.getActivity()
				.getSharedPreferences("scoreInfo", Context.MODE_PRIVATE);
		if (scoreInfo.getInt("score", 0) != 0) {
			int score = scoreInfo.getInt("score", 1);
			if (myView.getScore() >= score) {
				scoreInfo.edit().putInt("score", myView.getScore()).commit();
			}
		} else {
			scoreInfo.edit().putInt("score", myView.getScore()).commit();
		}

	}
}
