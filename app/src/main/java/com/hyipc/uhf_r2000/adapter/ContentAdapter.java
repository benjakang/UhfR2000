package com.hyipc.uhf_r2000.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyipc.uhf_r2000.R;
import com.hyipc.uhf_r2000.model.PojoCard;

import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends BaseAdapter{
	public List<PojoCard> cards;
	private LayoutInflater inflater;
	
	public ContentAdapter(Context ctx,List<PojoCard> cards) {
		if (cards == null) {
			cards = new ArrayList<PojoCard>();
		}
		this.cards = cards;
		if (ctx == null) {
			return;
		}
		inflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cards.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cards.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_card, null);
			viewHolder.tvNo = (TextView)convertView.findViewById(R.id.tvNo);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			viewHolder.tvCount = (TextView)convertView.findViewById(R.id.tvCount);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		PojoCard pojoCard = cards.get(position);
		viewHolder.tvNo.setText((position+1)+"");
		viewHolder.tvContent.setText(pojoCard.getContent());
		viewHolder.tvCount.setText(pojoCard.getCount()+"");

		if (position % 2 == 0) {
			convertView.setBackgroundColor(Color.LTGRAY);
		}else {
			convertView.setBackgroundColor(Color.WHITE);
		}
		
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tvNo;
		private TextView tvContent;
		private TextView tvCount;
	}

}
