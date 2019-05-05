package com.hyipc.uhf_r2000.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hyipc.uhf_r2000.R;
import com.hyipc.uhf_r2000.adapter.ContentAdapter;
import com.hyipc.uhf_r2000.hardware.assist.UhfReadListener;
import com.hyipc.uhf_r2000.hardware.assist.UhfSharedPreferenceUtil;
import com.hyipc.uhf_r2000.hardware.function.UhfRead;
import com.hyipc.uhf_r2000.model.PojoCard;
import com.hyipc.util.FileUtilsPi;
import com.hyipc.util.ToneUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class ReadFragment extends Fragment {
    private ListView mLvContent;
    private TextView mTvCardCount;
    private Button mBtnScan, mBtnClear, mBtnSave, mBtnUpload;
    private UhfRead mUhfRead;
    private Context mCtx;
    private LinkedHashMap<String, Integer> mMapContent;
    private ContentAdapter mAdapter;
    private List<PojoCard> mArrCard;
    private int mVoice = UhfSharedPreferenceUtil.VOICE_SYSTEM;
    public static final String DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    public static final String ApplicationFilePath = DCIM_PATH + "/超高频数据";
    public static final String logFile = ApplicationFilePath + "/uhf.txt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCtx = getActivity();
        View view = inflater.inflate(R.layout.fragment_read, null);
        mLvContent = (ListView) view.findViewById(R.id.lvContent);
        mTvCardCount = (TextView) view.findViewById(R.id.tvCardCount);
        mBtnScan = (Button) view.findViewById(R.id.btnScan);
        mBtnClear = (Button) view.findViewById(R.id.btnClear);
        mBtnSave = (Button) view.findViewById(R.id.btnSave);
        mBtnScan.setOnClickListener(listener);
        mBtnClear.setOnClickListener(listener);
        mBtnSave.setOnClickListener(listener);

        if (mUhfRead == null) {
            mUhfRead = new UhfRead(new UhfReadListener() {
                @Override
                public void onErrorCaughted(String error) {
                }

                @Override
                public void onContentCaughted(Object[] obj) {
                    Message.obtain(mHandler, MSG_CONTENT, obj).sendToTarget();
                }
            }, mCtx);
        }

        if (UhfSharedPreferenceUtil.getInstance(mCtx).getMem_read() == 1) {
            int data = UhfSharedPreferenceUtil.getInstance(mCtx).getStartAddr_read();
            mUhfRead.setmMem((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getMem_read());
            mUhfRead.setmWordPtr((byte) (data + 2));
            mUhfRead.setmNum((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getNum_read());
        } else {
            mUhfRead.setmMem((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getMem_read());
            mUhfRead.setmWordPtr((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getStartAddr_read());
            mUhfRead.setmNum((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getNum_read());
        }

        mVoice = UhfSharedPreferenceUtil.getInstance(mCtx).getVoice();
        mMapContent = new LinkedHashMap<String, Integer>();
        mArrCard = new ArrayList<PojoCard>();
        mAdapter = new ContentAdapter(mCtx, mArrCard);
        mLvContent.setAdapter(mAdapter);
        FileUtilsPi.createFolder2(DCIM_PATH);
        FileUtilsPi.createFolder2(ApplicationFilePath);
        FileUtilsPi.createFile(logFile);
        return view;
    }

    private String content;
    private final int MSG_CONTENT = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONTENT:
                    String[] contents = (String[]) (msg.obj);
                    synchronized (mMapContent) {
                        for (int i = 0; i < contents.length; i++) {
                            content = contents[i];
                            Log.e("Tag", "读取的内容： " + content);
                            if (mMapContent.containsKey(content)) {
                                int count = Integer.parseInt(mMapContent.get(content).toString());
                                count++;
                                mMapContent.put(content, count);
                            } else {
                                mMapContent.put(content, 1);
                            }
                        }
                        updatemArrCard(mMapContent);
                        mAdapter.notifyDataSetChanged();
                    }
                    mTvCardCount.setText(mArrCard.size() + "");
                    playTone();
                    break;

                default:
                    break;
            }
        }


    };

    private void playTone() {
        switch (mVoice) {
            case UhfSharedPreferenceUtil.VOICE_SYSTEM:
                ToneUtil.getInstace(mCtx).play(ToneUtil.TYPE_CUSTOM);
                break;
            case UhfSharedPreferenceUtil.VOICE_CUSTOM:
                ToneUtil.getInstace(mCtx).play(ToneUtil.TYPE_CUSTOM);
                break;

            default:
                break;
        }
    }

    private boolean isStart = false;
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnScan:
                    String text = mBtnScan.getText().toString().trim();
                    if (text.equalsIgnoreCase(getResources().getString(R.string.scan))) {
                        mBtnScan.setText(getResources().getString(R.string.stop));
                        if (!isStart) {
                            mUhfRead.start();
                            isStart = true;
                        } else {
                            mUhfRead.reStart();
                        }
                    } else {
                        mBtnScan.setText(getResources().getString(R.string.scan));
                        mUhfRead.pause();
                    }
                    break;
                case R.id.btnClear:
                    clear();
                    break;
                case R.id.btnSave:
                    save();
                    break;
                default:
                    break;
            }
        }
    };



    private void save() {
        try {
            StringBuffer sb = new StringBuffer();
            Set<PojoCard> adData = readFromSD(logFile);
            for (PojoCard item : mAdapter.cards) {
                boolean hasAdd = true;
                for (PojoCard temp : adData) {
                    if (temp.getContent().equals(item.getContent())) {
                        hasAdd = false;
                        break;
                    }
                }
                if (hasAdd) {
                    adData.add(item);
                }
            }
            for (PojoCard list : adData) {
                if (new PojoCard(list.getContent(), "\t" + list.getTime(), 1).equals(adData)) {
                    return;
                } else {
                    sb.append(list.getContent());
                    sb.append("\t");
                    sb.append(list.getTime());
                    sb.append("\r\n");
                }

            }
            FileUtilsPi.saveStringFile(logFile, sb.toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<PojoCard> readFromSD(String filename) throws IOException {
        Set<PojoCard> setList = new HashSet<PojoCard>();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            fileReader = new FileReader(filename);
            bufferedReader = new BufferedReader(fileReader);
            String read = null;
            for (PojoCard t : setList) {
                while ((read = bufferedReader.readLine()) != null) {
                    PojoCard data = new PojoCard(read, "\t" + t.getTime(), 12);
                    setList.add(data);
                }
            }

        }
        return setList;
    }

    private void clear() {
        if (mMapContent == null || mArrCard == null || mAdapter == null || mTvCardCount == null) {
            return;
        }

        synchronized (mMapContent) {
            mMapContent.clear();
            mArrCard.clear();
            mAdapter.notifyDataSetChanged();
        }
        mTvCardCount.setText(mArrCard.size() + "");
    }

    /**
     * @param map
     * @todo 更新 mArrCard 内容
     */
    private void updatemArrCard(LinkedHashMap<String, Integer> map) {
        if (map == null) {
            return;
        }
        mArrCard.clear();
        for (Entry entry : map.entrySet()) {
            if (entry == null || entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            String content = entry.getKey().toString();
            int count = Integer.parseInt(entry.getValue().toString());
            mArrCard.add(new PojoCard(content, count));
        }
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        ToneUtil.getInstace(mCtx).release();
        if (mUhfRead != null) {
            mUhfRead.destroy();
            mUhfRead = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (mCtx == null) {
            mCtx = getActivity();
        }
        if (isVisibleToUser) {
            if (mUhfRead == null) {
                mUhfRead = new UhfRead(new UhfReadListener() {
                    @Override
                    public void onErrorCaughted(String error) {
                    }

                    @Override
                    public void onContentCaughted(Object[] obj) {
                        Message.obtain(mHandler, MSG_CONTENT, obj).sendToTarget();
                    }
                }, mCtx);
            }

            if (mCtx == null) {
                mCtx = getActivity();
            }
            if (UhfSharedPreferenceUtil.getInstance(mCtx).getMem_read() == 1) {
                int data = UhfSharedPreferenceUtil.getInstance(mCtx).getStartAddr_read();
                mUhfRead.setmMem((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getMem_read());
                mUhfRead.setmWordPtr((byte) (data + 2));
                mUhfRead.setmNum((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getNum_read());
            } else {
                mUhfRead.setmMem((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getMem_read());
                mUhfRead.setmWordPtr((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getStartAddr_read());
                mUhfRead.setmNum((byte) UhfSharedPreferenceUtil.getInstance(mCtx).getNum_read());
            }

            mVoice = UhfSharedPreferenceUtil.getInstance(mCtx).getVoice();
            isStart = false;
            clear();
            if (mBtnScan != null) {
                mBtnScan.setText(getResources().getString(R.string.scan));
            }

        } else {
            if (mUhfRead != null) {
                mUhfRead.destroy();
                mUhfRead = null;
            }
        }
    }

}
