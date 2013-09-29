package tk.sdxuyan.game;

import android.os.AsyncTask;
import android.widget.TextView;

public class onHelpNumChange extends AsyncTask<Object, Object, Integer> {

	private TextView textView;
	private int count;

	@Override
	protected Integer doInBackground(Object... params) {
		textView = (TextView) params[0];
		count = (Integer) params[1];
		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		textView.setText("" + count);
	}

}
