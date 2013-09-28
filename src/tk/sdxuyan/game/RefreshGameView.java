package tk.sdxuyan.game;

import tk.sdxuyan.fragment.GameFragment;
import tk.sdxuyan.tool.Contants;
import android.R.integer;
import android.os.AsyncTask;
import android.widget.Toast;

public class RefreshGameView extends AsyncTask<Object, Object, integer> {

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
		if (myView.win()) {
			fragment.setGameState(Contants.game_win);
			Toast.makeText(fragment.getActivity(),
					"胜利了！！！" + (myView.getTotalTime() - leftTime),
					Toast.LENGTH_SHORT).show();
		}
	}

}
