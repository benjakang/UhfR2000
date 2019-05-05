package com.halio;

public class r2000 {
	public static native boolean doTest();

	public static native void ModulePowerOn();
	/*
    功能描述：该函数用于打开模块电源。
    参数：无
    输出参数：无
     */
	public static native void ModulePowerOff();
	/*
	功能描述：该函数用于给模块电源断电。
参数：无
输出参数：无
	 */
	public static native boolean ConnectReader(byte[] ComAdr,byte baud);
	/*
	功能描述：该函数用于建立串口与读写模块之间的连接。接通读写模块的电源之后，必须执行该函数，才能使用读写模块的其它功能。(其实就是打开串口)
参数：ComAdr：模块的地址，以广播地址（0xFF）调用此函数，ComAdr将返回读写器的实际地址，以其它地址调用此函数，将由ComAdr地址指定的读写器执行此函数命令。
baud：输入变量，用该值设置或更改串口通讯控件的波特率(默认波特率是
      57600)。
	 */
	public static native boolean DisConnectReader();
	/*
	功能描述：该函数用于断开读写模块和串口的连接，释放相应资源。
参数：无
输出参数：如果成功，返回true；如果失败，返回false；
	 */
	
	public static native boolean SetAddress(byte[] ComAdr, byte ComAdrData,int[] Errorcode);
	/*
	功能描述：执行该命令后，读写器将会把读写器地址改为用户给定的值，并把这个值写入EEPROM保存。出厂时默认值是0x00。允许用户的修改范围是0x00~0xfe。当用户写入的值是0xff时，读写器将会自动恢复成默认值0x00。
参数：ComAdr : 输入变量，原先的读写器地址
ComAdrData：输入变量，一个字节,待写入的读写器地址.
errorcode：输出变量，一个字节，返回错误代码。-1表示成功。
输出参数：如果成功，返回true；如果失败，返回false；
	 */
	public static native boolean SetInventoryScanTime(byte[] ComAdr, byte ScanTime,int[] Errorcode);
	public static native boolean SetRfPower (byte[] ComAdr,byte powerDbm,int[] Errorcode);
	public static native boolean SetRegion(byte[] ComAdr, byte dmaxfre, byte dminfre,int[] Errorcode);
	public static native boolean SetBaudRate (byte[] ComAdr, byte baud,int[] Errorcode);
	public static native boolean SetCheckAnt(byte[] ComAdr, byte CheckAnt,int[] Errorcode);
	public static native boolean GetReaderInformation(byte[] ComAdr,byte[] VersionInfo,byte[] ReaderType, byte[] TrType,
			byte[] dmaxfre, byte[] dminfre,byte[] powerdBm, byte[] ScanTime,byte[] Ant,byte[] BeepEn,byte[] OutputRep, byte[] CheckAnt,int[] Errorcode);
	public static native boolean InventoryG2(byte[] ComAdr,byte QValue,byte Session,byte MaskMem,byte[] MaskAdr,byte MaskLen,byte[] MaskData,byte MaskFlag, byte AdrTID, byte LenTID, byte TIDFlag,byte[] EPClenandEPC, byte[] Ant, int[] Totallen, int[] CardNum,int[] Errorcode);
	public static native boolean ReadDataG2 (byte[] ComAdr,byte[] EPC, byte Enum,byte Mem, byte WordPtr, byte Num,byte[] Password,byte MaskMem,byte[] MaskAdr,byte MaskLen, byte[] MaskData, byte[] Data ,int[] errorcode);
	public static native boolean WriteDataG2(byte[] ComAdr, byte[] EPC,byte Wnum, byte Enum,byte Mem,byte WordPtr,byte[] Writedata,byte[] Password, byte  MaskMem,byte[] MaskAdr,byte MaskLen,byte[] MaskData,int[] errorcode);
	public static native boolean BlockEraseG2 (byte[] ComAdr, byte[] EPC, byte Enum,byte Mem, byte WordPtr, byte Num, byte[] Password,byte MaskMem,byte[] MaskAdr, byte MaskLen,byte[] MaskData, int[] errorcode);
	public static native boolean LockG2 (byte[] ComAdr, byte[] EPC,byte Enum, byte select,byte setprotect, byte[] Password,byte MaskMem,byte[] MaskAdr, byte  MaskLen,byte[] MaskData, int[] errorcode);
	public static native boolean KillTagG2 (byte[] ComAdr,byte[]EPC, byte Enum,byte[] Password,byte MaskMem,byte[] MaskAdr, byte MaskLen,byte[] MaskData, int[] errorcode);
	public static native boolean SetPrivacyByEPCG2 (byte[] ComAdr,byte[] EPC, byte Enum,byte[] Password, byte MaskMem,byte[] MaskAdr,byte MaskLen,byte[] MaskData,int[] errorcode);
	public static native boolean SetPrivacyWithoutEPCG2 (byte[] ComAdr,byte[] Password, int[] errorcode);
	public static native boolean ResetPrivacyG2(byte[] ComAdr, byte[] Password, int[] errorcode);
	public static native boolean CheckPrivacyG2 (byte[] ComAdr, byte[] Readpro ,int[] errorcode);
	public static native boolean EASConfigureG2 (byte[] ComAdr,byte[] EPC, byte Enum,byte[] Password,byte EAS,byte MaskMem,byte[] MaskAdr,byte MaskLen,byte[] MaskData, int[] errorcode);
	public static native boolean EASAlarmG2 (byte[] ComAdr, int[] errorcode);
	public static native boolean BlockLockG2 (byte[] ComAdr,byte[] EPC, byte Enum,byte[] Password,byte WrdPointer,byte MaskMem,byte[] MaskAdr,byte MaskLen,byte[] MaskData,int[] errorcode);
	public static native boolean WriteEPCG2(byte[] ComAdr,byte[] Password,byte[] WriteEPC,byte ENum,int[] Errorcode);
	
	static {
		System.loadLibrary("hyio_gpio_api");
		System.loadLibrary("hyio_uart_api");
		System.loadLibrary("halio_uhf_r2000"); 
	}
}
