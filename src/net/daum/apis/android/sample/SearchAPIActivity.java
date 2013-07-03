package net.daum.apis.android.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import net.daum.apis.android.common.DaumOpenApiCommon;
import net.daum.apis.android.common.DaumOpenApiCommon.OutputType;
import net.daum.apis.android.common.DaumOpenApiCommon.SortType;
import net.daum.apis.android.common.DaumOpenApiSDKException;
import net.daum.apis.android.conn.RequestListener;
import net.daum.apis.android.conn.ResponseData;
import net.daum.apis.android.search.Search;
import net.daum.apis.android.search.SearchBoard;
import net.daum.apis.android.search.datamodel.BoardResult.BoardData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchAPIActivity extends Activity implements OnClickListener{
	
	Button btnSearchSync, btnSearchAsync, btnAsyncTest;
	TextView tvResult;
	Spinner spnSearch;
	
	ArrayAdapter<CharSequence>  adapter;
	int searchWhat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_api);

		initUI();
		
		
		
		DaumOpenApiCommon.setApikey("DAUM_SEARCH_DEMO_APIKEY");
	}

	/**
	 *  UI 초기화 및 리스너 설정
	 */
	private void initUI() {
		tvResult = (TextView)findViewById(R.id.tvResult);
		btnSearchSync = (Button) findViewById(R.id.btnSearchSync);
		btnSearchAsync = (Button) findViewById(R.id.btnSearchAsync);
		btnAsyncTest = (Button) findViewById(R.id.btnAsyncTest);
		
		spnSearch = (Spinner) findViewById(R.id.spnSearch);
		spnSearch.setPrompt("선택하세요");	
		adapter = ArrayAdapter.createFromResource(this, R.array.search, android.R.layout.simple_spinner_dropdown_item);
		spnSearch.setAdapter(adapter);
		spnSearch.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				clearResult();
				Toast.makeText(SearchAPIActivity.this, adapter.getItem(position) + "을/를 선택 했습니다.", Toast.LENGTH_SHORT).show();				
				btnSearchSync.setText(adapter.getItem(position) + " 동기");
				btnSearchAsync.setText(adapter.getItem(position) + " 비동기");
				searchWhat = position;
			}
			
			public void onNothingSelected(AdapterView<?> parent)
			{
				clearResult();
				Toast.makeText(SearchAPIActivity.this, "API를 선택해주세요", Toast.LENGTH_SHORT).show();	
			}

			
		});
			

		Button[] btns = { btnSearchSync, btnSearchAsync, btnAsyncTest};
		for (Button btn : btns) {
			btn.setOnClickListener(this);
		}
	}
	
	public void clearResult() {
		setResponseResult("");
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		Toast toast;
		switch (id) {
		case R.id.btnSearchSync: 
			clearResult();
			try {
				if(searchWhat == Search.BOARD)
					searchBoardSync();
				else {
					toast = Toast.makeText(this,  "구현되지않음", Toast.LENGTH_SHORT);
					toast.show();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			break;
		case R.id.btnSearchAsync: 
			try {
				clearResult();
				try {
					if(searchWhat == Search.BOARD)
						searchBoardAsync();
					else {
						toast = Toast.makeText(this,  "구현되지않음", Toast.LENGTH_SHORT);
						toast.show();
					}
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
			toast = Toast.makeText(this,  "test", Toast.LENGTH_SHORT);
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
		Search board = new SearchBoard("daum", 10, 1, SortType.ACCU, OutputType.JSON);
		BoardData result = (BoardData)board.run();	
		setResponseResult("[[Sync]]\n" +  result.toString());
	}
	
	private void searchBoardAsync() throws IOException, InterruptedException, ExecutionException, DaumOpenApiSDKException {
		Search board = new SearchBoard("daum");
		board.runAsync(requestListener);	
	}

	RequestListener requestListener = new RequestListener(){

		@Override
		public void onComplete(ResponseData reponseData) {
			setResponseResult(reponseData.toString());
		}

		@Override
		public void onDaumOpnApiSDKException(DaumOpenApiSDKException e) {
			setResponseResult(e.toString());	
		}

		
	}; 
		
}
