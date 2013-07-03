package net.daum.apis.android.sample;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.daum.apis.android.common.DaumOpenApiCommon;
import net.daum.apis.android.common.DaumOpenApiSDKException;
import net.daum.apis.android.conn.RequestListener;
import net.daum.apis.android.search.Search;
import net.daum.apis.android.search.SearchBoard;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchAPIActivity extends Activity implements OnClickListener{
	
	Button btnSearchBoardSync, btnSearchBoardAsync, btnAsyncTest;
	TextView tvResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_api);

		initUI();
		DaumOpenApiCommon.setApikey("DAUM_SEARCH_DEMO_APIKEY1");
	}

	/***
	 *  UI 초기화 및 리스너 설정
	 */
	private void initUI() {
		
		tvResult = (TextView)findViewById(R.id.tvResult);
		btnSearchBoardSync = (Button) findViewById(R.id.btnSearchBoardSync);
		btnSearchBoardAsync = (Button) findViewById(R.id.btnSearchBoardAsync);
		btnAsyncTest = (Button) findViewById(R.id.btnAsyncTest);

		Button[] btns = { btnSearchBoardSync, btnSearchBoardAsync, btnAsyncTest};
		for (Button btn : btns) {
			btn.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.btnSearchBoardSync: 
			setResponseResult("");
			try {
				setResponseResult("");
				searchBoardSync();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			break;
		case R.id.btnSearchBoardAsync: 
			try {
				setResponseResult("");
				try {
					searchBoardAsync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (DaumOpenApiSDKException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case R.id.btnAsyncTest:
			Toast toast = Toast.makeText(this,  "test", Toast.LENGTH_SHORT);
			toast.show();
			break;
		default:
			break;
		}
	}
	
	public void setResponseResult(String string)
	{
		tvResult.setText(string);
	}
	
	private void searchBoardSync() throws InterruptedException, ExecutionException, IOException {
		Search board = new SearchBoard("daum");
		String result =	board.run().toString();	
		setResponseResult("[[Sync]]\n" +  result);
	}
	
	private void searchBoardAsync() throws IOException, InterruptedException, ExecutionException, DaumOpenApiSDKException {
		Search board = new SearchBoard("daum");
		board.runAsync(requestListener);	
	}

	RequestListener requestListener = new RequestListener(){

		@Override
		public void onComplete(Object object) {
			setResponseResult(object.toString());
		}

		@Override
		public void onDaumOpnApiSDKException(DaumOpenApiSDKException e) {
			setResponseResult(e.toString());	
		}
		
	}; 
		
}
