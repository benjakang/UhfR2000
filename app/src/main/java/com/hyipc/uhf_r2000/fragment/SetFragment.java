package com.hyipc.uhf_r2000.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import com.hyipc.uhf_r2000.R;
import com.hyipc.uhf_r2000.hardware.assist.UhfDefaultConfig;
import com.hyipc.uhf_r2000.hardware.assist.UhfMappingRelation;
import com.hyipc.uhf_r2000.hardware.assist.UhfSharedPreferenceUtil;
import com.hyipc.uhf_r2000.hardware.function.UhfReaderInformation;
import com.hyipc.uhf_r2000.hardware.function.UhfSetting;
import com.hyipc.uhf_r2000.model.SeekBarInfo;
import com.hyipc.util.Logger;
import java.util.ArrayList;
import java.util.List;

public class SetFragment extends Fragment implements OnSeekBarChangeListener {
	private TextView mTvVersion, mTvBaud, mTvMinFre, mTvMaxFre, mTvRfPower, mTvScanTime, mTvInfo,mTvAddr,mTvNum;
	private TextView mTvBaudMin, mTvBaudMax, mTvMinFreMin, mTvMinFreMax, mTvMaxFreMin, mTvMaxFreMax, mTvRfPowerMin, mTvRfPowerMax, mTvScanTimeMin, mTvScanTimeMax;
	private SeekBar mSbBaud, mSbMinFre, mSbMaxFre, mSbRfPower, mSbScanTime;
	private Spinner mSpnMem;
	private EditText mEtStartAddr, mEtNum;
	private Button mBtnUpdate, mBtnDefault, mBtnGetReaderInfo;
	private ProgressBar mPbUpdate, mPbDefault;
	private List<SeekBarInfo> mSeekBarInfos;
	private UhfReaderInformation mReaderInformation;
	private UhfMappingRelation mRelation;
	private UhfSetting mSetting;
	private Context mCtx;
	private UhfSharedPreferenceUtil mPreferenceUtil;
	private LinearLayout mLayoutReaderSettting;
	private boolean isGetReaderInfoSucc = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_set, null);
		mCtx = getActivity();

		mTvInfo = (TextView) view.findViewById(R.id.tvInfo);
		mTvVersion = (TextView) view.findViewById(R.id.tvVersion);
		//mTvBaud = (TextView) view.findViewById(R.id.tvBaud);
		mTvMinFre = (TextView) view.findViewById(R.id.tvMinFre);
		mTvMaxFre = (TextView) view.findViewById(R.id.tvMaxFre);
		mTvRfPower = (TextView) view.findViewById(R.id.tvRfPower);
		mTvScanTime = (TextView) view.findViewById(R.id.tvScanTime);

		//mTvBaudMin = (TextView) view.findViewById(R.id.tvBaudMin);
		mTvMinFreMin = (TextView) view.findViewById(R.id.tvMinFreMin);
		mTvMaxFreMin = (TextView) view.findViewById(R.id.tvMaxFreMin);
		mTvRfPowerMin = (TextView) view.findViewById(R.id.tvRfPowerMin);
		mTvScanTimeMin = (TextView) view.findViewById(R.id.tvScanTimeMin);

		//mTvBaudMax = (TextView) view.findViewById(R.id.tvBaudMax);
		mTvMinFreMax = (TextView) view.findViewById(R.id.tvMinFreMax);
		mTvMaxFreMax = (TextView) view.findViewById(R.id.tvMaxFreMax);
		mTvRfPowerMax = (TextView) view.findViewById(R.id.tvRfPowerMax);
		mTvScanTimeMax = (TextView) view.findViewById(R.id.tvScanTimeMax);

		mTvAddr = (TextView) view.findViewById(R.id.tvAddr);
		mTvNum = (TextView) view.findViewById(R.id.tvNum);

		//mSbBaud = (SeekBar) view.findViewById(R.id.sbBaud);
		mSbMinFre = (SeekBar) view.findViewById(R.id.sbMinFre);
		mSbMaxFre = (SeekBar) view.findViewById(R.id.sbMaxFre);
		mSbRfPower = (SeekBar) view.findViewById(R.id.sbRfPower);
		mSbScanTime = (SeekBar) view.findViewById(R.id.sbScanTime);

		mSpnMem = (Spinner) view.findViewById(R.id.spnMem);

		mEtStartAddr = (EditText) view.findViewById(R.id.etAddr);
		mEtNum = (EditText) view.findViewById(R.id.etNum);

		mBtnUpdate = (Button) view.findViewById(R.id.btnUpdate);
		mBtnDefault = (Button) view.findViewById(R.id.btnDefault);
		mBtnGetReaderInfo = (Button) view.findViewById(R.id.btnGetReaderInfo);
		mBtnUpdate.setOnClickListener(onClickListener);
		mBtnDefault.setOnClickListener(onClickListener);
		mBtnGetReaderInfo.setOnClickListener(onClickListener);

		mPbUpdate = (ProgressBar) view.findViewById(R.id.pbUpdate);
		mPbDefault = (ProgressBar) view.findViewById(R.id.pbDefault);

		//mRgVoice = (RadioGroup) view.findViewById(R.id.rgVoice);

		//mSbBaud.setOnSeekBarChangeListener(this);
		mSbMinFre.setOnSeekBarChangeListener(this);
		mSbMaxFre.setOnSeekBarChangeListener(this);
		mSbRfPower.setOnSeekBarChangeListener(this);
		mSbScanTime.setOnSeekBarChangeListener(this);

		mLayoutReaderSettting = (LinearLayout) view.findViewById(R.id.llReaderSetting);
		mReaderInformation = new UhfReaderInformation();
		mRelation = new UhfMappingRelation();
		mSetting = new UhfSetting();
		mPreferenceUtil = UhfSharedPreferenceUtil.getInstance(mCtx);

		initReadView();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		initReadView();
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			initReadView();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		for (int i = 0; i < mSeekBarInfos.size(); i++) {
			SeekBarInfo seekBarInfo = mSeekBarInfos.get(i);
			if (seekBar == seekBarInfo.getSeekBar()) {
				seekBarInfo.onProgressChanged(progress);
			}
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	private void initReadView() {
		// Read setting ========
		final String[] mens = mRelation.getAllMen();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, mens);
		mSpnMem.setAdapter(adapter);
		byte bMem = (byte) mPreferenceUtil.getMem_read();

		mSpnMem.setSelection(bMem);
		mSpnMem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (mens[position].equalsIgnoreCase(getResources().getString(R.string.epc))) {
					/*mTvAddr.setTextColor(Color.GRAY);
					mTvNum.setTextColor(Color.GRAY);
					mEtStartAddr.setEnabled(false);
					mEtNum.setEnabled(false);*/
				}else {
					mTvAddr.setTextColor(Color.BLACK);
					mTvNum.setTextColor(Color.BLACK);
					mEtStartAddr.setEnabled(true);
					mEtNum.setEnabled(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		int iStartAddr = mPreferenceUtil.getStartAddr_read();
		mEtStartAddr.setText(iStartAddr + "");
		int iNum = mPreferenceUtil.getNum_read();
		mEtNum.setText(iNum + "");
	}

	private void initReaderView() {
		// Reader setting =======
		// 读写器信息
		//int iBaud = mPreferenceUtil.getBaud();
		byte iMaxFre = mReaderInformation.getMaxFre();
		byte iMinFre = mReaderInformation.getMinFre();
		iMaxFre = mRelation.reverseMaxFre(iMaxFre);
		iMinFre = mRelation.reverseMinFre(iMinFre);
		int iRfpower = mReaderInformation.getRfPower();

		int iScanTime = mReaderInformation.getScanTime();
		if (iScanTime < 0) {
			iScanTime = iScanTime + 256;
		}

		String currVersion = mReaderInformation.getVersion();
		//String currBaud = mRelation.getBaudValue((byte) iBaud);
		String currMaxFre = mRelation.getFreValue(iMaxFre);
		String currMinFre = mRelation.getFreValue(iMinFre);
		String currRfPower = iRfpower + "";
		String currScanTime = mRelation.getScanTimeValue(iScanTime);
		Logger.D("currScanTime:"+currScanTime);

		// 所有显示的值
		//String[] bauds = mRelation.getAllBaudValue();
		String[] maxFres = mRelation.getAllFreValue();
		String[] minFres = mRelation.getAllFreValue();
		String[] powers = mRelation.getAllRfPowerValue();
		String[] scanTimes = mRelation.getAllScanTimeValue();

		mSeekBarInfos = new ArrayList<SeekBarInfo>();
		//mSeekBarInfos.add(new SeekBarInfo(mSbBaud, mTvBaud, mTvBaudMin, mTvBaudMax, bauds, currBaud));
		mSeekBarInfos.add(new SeekBarInfo(mSbMinFre, mTvMinFre, mTvMinFreMin, mTvMinFreMax, minFres, currMinFre));
		mSeekBarInfos.add(new SeekBarInfo(mSbMaxFre, mTvMaxFre, mTvMaxFreMin, mTvMaxFreMax, maxFres, currMaxFre));
		mSeekBarInfos.add(new SeekBarInfo(mSbRfPower, mTvRfPower, mTvRfPowerMin, mTvRfPowerMax, powers, currRfPower));
		mSeekBarInfos.add(new SeekBarInfo(mSbScanTime, mTvScanTime, mTvScanTimeMin, mTvScanTimeMax, scanTimes, currScanTime));
		// init
		for (int i = 0; i < mSeekBarInfos.size(); i++) {
			mSeekBarInfos.get(i).update();
		}

		mTvVersion.setText(currVersion);
	}

	private boolean isNeedtoInit = false;
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btnUpdate:
					isNeedtoInit = false;
					mTvInfo.setText("");
					String strMem = (String) mSpnMem.getSelectedItem();
					int iMem = mRelation.getMemKey(strMem);//0：保留区、1：EPC、2：TID、3：USER
					mPreferenceUtil.saveMem_read(iMem);

					String strStartAddr = mEtStartAddr.getText().toString();
					if (!TextUtils.isEmpty(strStartAddr)) {
						int iStartAddr = Integer.parseInt(strStartAddr);
						mPreferenceUtil.saveStartAddr_read(iStartAddr);
					} else {
						mTvInfo.setTextColor(Color.RED);
						mTvInfo.setText(getResources().getString(R.string.start_arr));
						return;
					}

					String strNum = mEtNum.getText().toString();
					if (!TextUtils.isEmpty(strNum)) {
						int iNum = Integer.parseInt(strNum);
						mPreferenceUtil.saveNum_read(iNum);
					}else {
						mTvInfo.setTextColor(Color.RED);
						mTvInfo.setText(getResources().getString(R.string.start_arr));
						return;
					}
					if (isGetReaderInfoSucc && mLayoutReaderSettting.getVisibility() == View.VISIBLE) {
						//String strBaud = mTvBaud.getText().toString();
						String strMinFre = mTvMinFre.getText().toString();
						String strMaxFre = mTvMaxFre.getText().toString();
						String strRfPower = mTvRfPower.getText().toString();
						String strScanTime = mTvScanTime.getText().toString();

						//byte bBaud = mRelation.getBaudKey(strBaud);
						byte bMinFre = mRelation.getMinFreKey(strMinFre);
						byte bMaxFre = mRelation.getMaxFreKey(strMaxFre);
						byte bRfPower = Byte.parseByte(strRfPower);
						int iScanTime = mRelation.getScanTimeKey(strScanTime);

						//mSetting.setmBaud(bBaud);
						mSetting.setmMinFre(bMinFre);
						mSetting.setmMaxFre(bMaxFre);
						mSetting.setmRfPower(bRfPower);
						mSetting.setmScanTime(iScanTime);

						new UpdateT().start();
					} else {
						Message.obtain(mHandler, MSG_UPDATE, getResources().getString(R.string.update_read_setting_succ)).sendToTarget();
					}
					break;

				case R.id.btnDefault:
					isNeedtoInit = true;
					mPbDefault.setVisibility(View.VISIBLE);
					mBtnDefault.setClickable(false);
					mTvInfo.setText("");

					//byte bBaud = UhfDefaultConfig.BAUD;
					byte bMinFre = UhfDefaultConfig.MIN_FRE;
					byte bMaxFre = UhfDefaultConfig.MAX_FRE;
					byte bRfPower = UhfDefaultConfig.RF_POWER;
					int iScanTime = UhfDefaultConfig.SCAN_TIME;

					mSetting.setmBaud((byte)5);
					mPreferenceUtil.saveBaud(5);
					mSetting.setmMinFre(bMinFre);
					mSetting.setmMaxFre(bMaxFre);
					mSetting.setmRfPower(bRfPower);
					mSetting.setmScanTime(iScanTime);

					iMem = UhfDefaultConfig.MEM_READ;
					mPreferenceUtil.saveMem_read(iMem);

					int iStartAddr = UhfDefaultConfig.START_ADDR_READ;
					mPreferenceUtil.saveStartAddr_read(iStartAddr);

					int iNum = UhfDefaultConfig.NUM_READ;
					mPreferenceUtil.saveNum_read(iNum);
					mPreferenceUtil.saveVoice(UhfDefaultConfig.VOICE);

					new UpdateT().start();
					break;
				case R.id.btnGetReaderInfo:
					isGetReaderInfoSucc = mReaderInformation.init();
					if (isGetReaderInfoSucc) {
						mLayoutReaderSettting.setVisibility(View.VISIBLE);
						initReaderView();
					} else {
						mTvInfo.setText(getResources().getString(R.string.get_readerinfo_fail));
						mTvInfo.setTextColor(Color.RED);
					}
					break;
				default:
					break;
			}
		}
	};

	private class UpdateT extends Thread {
		@Override
		public synchronized void run() {
			StringBuffer sbf = new StringBuffer();
//			if (!mSetting.updateBaud()) {
//				sbf.append(getResources().getString(R.string.baud_set_fail));
//				sbf.append("\r\n");
//			} else {
//				//mPreferenceUtil.saveBaud(mSetting.getmBaud());
//			}

			if (!mSetting.updateRegion()) {
				sbf.append(getResources().getString(R.string.set_frequency_fail));
				sbf.append("\r\n");
			}

			if (!mSetting.updateRfPower()) {
				sbf.append(getResources().getString(R.string.set_rf_power_fail));
				sbf.append("\r\n");
			}

			if (!mSetting.updateInventoryScanTime()) {
				sbf.append(getResources().getString(R.string.set_inventory_scan_time_fail));
				sbf.append("\r\n");
			}

			if (TextUtils.isEmpty(sbf.toString())) {
				if (isNeedtoInit) {
					sbf.append(getResources().getString(R.string.restore_default_succ));
				} else {
					sbf.append(getResources().getString(R.string.update_succ));
				}
			}

			Message.obtain(mHandler, MSG_UPDATE, sbf.toString()).sendToTarget();
		}
	}

	private final int MSG_UPDATE = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_UPDATE:
					String info = (String) msg.obj;
					mTvInfo.setText(info);
					if (info.contains(getResources().getString(R.string.succ))) {
						mTvInfo.setTextColor(0xFF005500);
					} else {
						mTvInfo.setTextColor(Color.RED);
					}
				/*mBtnUpdate.setClickable(true);
				mPbUpdate.setVisibility(View.INVISIBLE);*/
					mBtnDefault.setClickable(true);
					mPbDefault.setVisibility(View.INVISIBLE);

					if (isNeedtoInit && isGetReaderInfoSucc) {
						initReaderView();
					}
					break;

				default:
					break;
			}
		};
	};
}
