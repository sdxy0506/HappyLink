package tk.sdxuyan.game;

import tk.sdxuyan.fragment.GameFragment;
import tk.sdxuyan.tool.Contants;
import tk.sdxuyan.tool.GameDone;
import tk.sdxuyan.tool.MyDialog;
import android.R.integer;
import android.os.AsyncTask;
import android.widget.Toast;

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
		switchState();
	}

	private void switchState() {
		if (myView.win()) {
			state = Contants.game_win;
			GameWin();
		} else if (leftTime == 0) {
			state = Contants.game_over;
			GameOver();
		} else {
			state = Contants.game_isPlaying;
		}
		fragment.setGameState(state);
	}

	@Override
	public void GameWin() {
		new MyDialog(fragment.getActivity(), fragment, "胜利了！",
				myView.getTotalTime() - leftTime).show();
		fragment.stopMusic();
		fragment.stopTimer();
	}

	@Override
	public void GameOver() {
		new MyDialog(fragment.getActivity(), fragment, "失败！",
				myView.getTotalTime() - leftTime).show();
		BoardView.soundPlay.play(Contants.ID_SOUND_LOSE, 0);
		fragment.stopMusic();
		fragment.stopTimer();
	}

}
