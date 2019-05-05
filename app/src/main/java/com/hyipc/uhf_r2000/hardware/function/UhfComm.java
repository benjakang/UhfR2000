package com.hyipc.uhf_r2000.hardware.function;

import android.content.Context;
import android.util.Log;

import com.halio.r2000;
import com.hyipc.uhf_r2000.hardware.assist.UhfSharedPreferenceUtil;

public class UhfComm {
	/**	读写器地址 */
	public static byte[] sAddr = {(byte)0xFF};
	
	public boolean init(Context ctx){
		r2000.ModulePowerOn();
		int baud = UhfSharedPreferenceUtil.getInstance(ctx).getBaud();
		Log.e("Tag","连接的波特率： "+(byte)baud);
		return r2000.ConnectReader(sAddr, (byte)baud);
	}
	
	public boolean unInit(){
		//r2000.ModulePowerOff();
		Log.e("Tag","unInit ");
		if (r2000.DisConnectReader()) {
			r2000.ModulePowerOff();
			return true;
		}
		return false;
	}
}
