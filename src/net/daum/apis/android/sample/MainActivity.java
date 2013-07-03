package net.daum.apis.android.sample;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnSearchAPI;
		btnSearchAPI = (Button) findViewById(R.id.btnSearchAPI);

		Button[] btns = { btnSearchAPI};
		for (Button btn : btns) {
			btn.setOnClickListener(this);
		}
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.btnSearchAPI : {
			Intent intent = new Intent(this, SearchAPIActivity.class);
			startActivity(intent);
		}
		break;
		default:
			break;
		}
		
	}


}
