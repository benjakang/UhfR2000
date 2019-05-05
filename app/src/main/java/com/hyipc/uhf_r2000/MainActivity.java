package com.hyipc.uhf_r2000;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hyipc.uhf_r2000.adapter.FragmentAdapter;
import com.hyipc.uhf_r2000.fragment.ReadFragment;
import com.hyipc.uhf_r2000.fragment.SetFragment;
import com.hyipc.uhf_r2000.fragment.UploadFragment;
import com.hyipc.uhf_r2000.fragment.WriteFragment;
import com.hyipc.uhf_r2000.hardware.function.UhfComm;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnPageChangeListener, OnCheckedChangeListener {
	private ViewPager mViewPager;
	private FragmentAdapter mAdapter;
	private List<Fragment> mFragments;
	private RadioGroup mRadioGroup;
	private UhfComm mUhfComm;
	private ActivityManager mActivityManager;
	private static final String BARCODE_PROCESS = "com.hyipc.service.barcode";
	private boolean isHasBarcodeService = false;
	private List<RadioButton> mRadioButtons = null;
	private RadioButton mRbRead,mRbWrite,mRbSetting,mRbUpload;
	private TextView mTvVersion;
	
	private void init() {
		mUhfComm = new UhfComm();
		
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mRbRead = (RadioButton) findViewById(R.id.rbRead);
		mRbWrite = (RadioButton) findViewById(R.id.rbWrite);
		mRbSetting = (RadioButton) findViewById(R.id.rbSet);
		mRbUpload = (RadioButton) findViewById(R.id.rbUpload);

		mRadioButtons = new ArrayList<RadioButton>();
		mRadioButtons.add(mRbRead);
		mRadioButtons.add(mRbWrite);
		mRadioButtons.add(mRbSetting);
		mRadioButtons.add(mRbUpload);

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new ReadFragment());
		mFragments.add(new WriteFragment());
		mFragments.add(new SetFragment());
		mFragments.add(new UploadFragment());

		mAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
		mViewPager.setAdapter(mAdapter);
		//mViewPager.setOffscreenPageLimit(0);
		mViewPager.setCurrentItem(1);
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager.addOnPageChangeListener(this);
		
		Thread.setDefaultUncaughtExceptionHandler(new unCaughtExceptionHandler());
		mTvVersion = (TextView) findViewById(R.id.tvVersion);
		String version = getCurrentVersionName();
		mTvVersion.setText("version:"+version);
				
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		mActivityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> services = mActivityManager.getRunningServices(100);
		for (int i = 0; i < services.size(); i++) {
			ActivityManager.RunningServiceInfo serviceInfo = services.get(i);
			String process = serviceInfo.process;
			if (process.contentEquals(BARCODE_PROCESS)) {
				isHasBarcodeService = true;
				IntentFilter filter = new IntentFilter();
				filter.addAction(ACTION_CLOSE_SUCC);
				registerReceiver(portReceive, filter);
				this.sendBroadcast(new Intent("portOff"));
				return;
			}
		}
		boolean succ = mUhfComm.init(MainActivity.this);
	}
	
	public String getCurrentVersionName() {
		try {
			String str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			return str;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			localNameNotFoundException.printStackTrace();
		}
		return "1.0";
	}
	

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbRead:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.rbWrite:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.rbSet:
			mViewPager.setCurrentItem(2);
			break;
		case R.id.rbUpload:
			mViewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onPageSelected(int arg0) {
		mRadioButtons.get(arg0).setChecked(true);
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	private final long INVER_TIME = 1000;
	private long lastTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long currTime = System.currentTimeMillis();
			if (currTime - lastTime < INVER_TIME) {
				this.finish();
			}else {
				Toast.makeText(this, getResources().getString(R.string.again_to_exit), Toast.LENGTH_LONG).show();
				lastTime = currTime;
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mUhfComm.unInit();
		if (isHasBarcodeService) {
			this.sendBroadcast(new Intent("portOn"));
			unregisterReceiver(portReceive);
		}
		portReceive = null;
		System.exit(0);
	}
	
	private final String ACTION_CLOSE_SUCC = "portCloseSucc";  
	BroadcastReceiver portReceive = new BroadcastReceiver() {  
        @Override  
        public void onReceive(final Context context, final Intent intent) {  
            Log.e("Tag", "onReceive");
            String action = intent.getAction(); 
            if (TextUtils.isEmpty(action)) {
				return;
			}
  
            if (ACTION_CLOSE_SUCC.equalsIgnoreCase(action)) {
            	boolean succ = mUhfComm.init(MainActivity.this);
        		if (!succ) {
        			Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.uhf_init_fail), Toast.LENGTH_LONG).show();
					return;
        		}
            } 
        }  
    };  
    
    public class unCaughtExceptionHandler implements UncaughtExceptionHandler{

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			/*ExceptionHandler.getInstance().doHandle(MainActivity.this, (Exception)ex);
			ExceptionHandler.getInstance().doCheckForUpload(MainActivity.this);*/
		}
    }

}
