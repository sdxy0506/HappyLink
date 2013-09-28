package tk.sdxuyan.game;

import tk.sdxuyan.fragment.GameFragment;
import tk.sdxuyan.tool.Contants;
import tk.sdxuyan.tool.GameDone;
import android.R.integer;
import android.os.AsyncTask;
import android.widget.Toast;

public class RefreshGameState extends AsyncTask<Object, Object, integer>
		implements GameDone {

	private MyView myView;
	private int leftTime;
	private int state;
	private GameFragment fragment;

	@Override
	protected integer doInBackground(Object... params) {
		fragment = (GameFragment) params[0];
		myView = (MyView) params[1];
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
		Toast.makeText(fragment.getActivity(),
				"胜利了！！！" + (myView.getTotalTime() - leftTime),
				Toast.LENGTH_SHORT).show();
		fragment.stopTimer();
	}

	@Override
	public void GameOver() {
		Toast.makeText(fragment.getActivity(),
				"失败了！！！" + (myView.getTotalTime() - leftTime),
				Toast.LENGTH_SHORT).show();
		fragment.stopTimer();
	}

}
