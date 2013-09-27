package tk.sdxuyan.game;

import android.R.integer;
import android.os.AsyncTask;

public class RefreshGameView extends AsyncTask<Object, Object, integer> {

	private MyView myView;
	private int leftTime;
	private int state;

	@Override
	protected integer doInBackground(Object... params) {
		myView = (MyView) params[0];
		state = (Integer) params[1];
		leftTime = (Integer) params[2];
		return null;
	}

	@Override
	protected void onPostExecute(integer result) {
		super.onPostExecute(result);
	}

}
