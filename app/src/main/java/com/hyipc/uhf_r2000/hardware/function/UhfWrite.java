package com.hyipc.uhf_r2000.hardware.function;

import android.util.Log;

import com.hyipc.uhf_r2000.hardware.assist.UhfUtil;

public class UhfWrite {
	// === 成员变量 start ====
	/** 起始地址 */
	private byte mWordPtr;
	/** 读取的存储区 */
	private byte mMem;
	/** 写入的内容 */
	private String mContent;
	// === 成员变量 end ====

	// === InventoryG2 的参数 start ====
	private byte QValue = 0;// how many cards scan for one time
	private byte Session = 0;
	private byte MaskMem = 1;// te:must be 1
	private byte[] MaskAdr = { 0, 0 };
	private byte MaskLen = 8;
	private byte[] MaskData = { 0 };
	private byte MaskFlag = 0;
	private byte AdrTID = 0;
	private byte LenTID = 15;//*******************
	private byte TIDFlag = 0;// te:must be zero
	private byte[] pEPCList = new byte[256];
	private byte[] Ant1 = new byte[1];
	private int[] Totallen = new int[1];
	private int[] CardNum = new int[1];
	// === InventoryG2 的参数 end ====

	// === WriteDataG2 的参数 start ===
	private int totalLength;
	private byte[] pepList;
	private int dataLength;
	private byte[] Data;
	private byte[] EPC2;
	private byte Enum2 = (byte) 6; // EPC字长度
	private byte Wnum2;
	private byte[] Writedata2;
	private byte[] Password2 = { (byte) 0, (byte) 0, (byte) 0, (byte) 0 };// 4字节访问密码
	private byte MaskMem2 = (byte) 1; // 掩码区,0x01：EPC存储区；0x02：TID存储区；0x03：用户存储区。
	private byte[] MaskAdr2 = { (byte) 0, (byte) 0 }; // 掩码的起始位地址
	private byte MaskLen2 = (byte) 8; // 掩码的位长度
	private byte[] MaskData2 = { (byte) 0 }; // 掩码数据。MaskData数据字节长度是MaskLen/8。
	private int[] errorcode2 = { -3 };

	// === WriteDataG2 的参数 end ===

	public UhfWrite() {
	}

	public boolean doWrite() {
		if (!com.halio.r2000.InventoryG2(UhfComm.sAddr, QValue, Session, MaskMem, MaskAdr, MaskLen, MaskData, MaskFlag, AdrTID, LenTID, TIDFlag, pEPCList, Ant1, Totallen, CardNum,errorcode2)) {
			Log.e("Tag","不符合电子标签协议！");
			return false;
		} else {
			if (Totallen[0] != 0) {
				totalLength = Totallen[0];
				pepList = new byte[totalLength - 2];
				for (int i = 0; i < totalLength - 2; i++) {
					pepList[i] = pEPCList[i + 1];
				}

				if (mContent == null) {
					return false;
				}

				Enum2 = (byte)(pEPCList[0] / 2);
				Log.e("Tag","写Enum2    "+Enum2);
				Writedata2 = UhfUtil.hexStringToBytes(mContent);
				EPC2 = pepList;
				Wnum2 = (byte) (Writedata2.length / 2); // 写入字个数

				if (!com.halio.r2000.WriteDataG2(UhfComm.sAddr, EPC2, Wnum2, Enum2, mMem, mWordPtr, Writedata2, Password2, MaskMem2, MaskAdr2, MaskLen2, MaskData2, errorcode2)) {
					Log.e("Tag ","写数据失败的错误码值： "+errorcode2[0]+"");
					return false;
				} else {
					Log.e("Tag ","写数据成功的错误码值： "+Integer.toHexString(errorcode2[0]));
					return true;
				}
			}
			return false;
		}
	}

	public byte getmWordPtr() {
		return mWordPtr;
	}

	public void setmWordPtr(byte mWordPtr) {
		this.mWordPtr = mWordPtr;
	}

	public byte getmMem() {
		return mMem;
	}

	public void setmMem(byte mMem) {
		this.mMem = mMem;
	}

	public String getmContent() {
		return mContent;
	}

	public void setmContent(String mContent) {
		this.mContent = mContent;
	}

}
