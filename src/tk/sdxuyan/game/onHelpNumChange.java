package tk.sdxuyan.game;

import android.os.AsyncTask;
import android.widget.TextView;

public class onHelpNumChange extends AsyncTask<Object, Object, Integer> {

	private TextView textView;
	private int count;
	private int flag;

	@Override
	protected Integer doInBackground(Object... params) {
		textView = (TextView) params[0];
		count = (Integer) params[1];
		flag = (Integer) params[2];
		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (flag == 1) {
			textView.setText("Lv:$");
			textView.setText(textView.getText().toString()
					.replace("$", String.valueOf(count)));
		} else {
			textView.setText("" + count);
		}

	}
}
