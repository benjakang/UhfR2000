package com.hyipc.uhf_r2000.model;

import com.hyipc.util.TimeUtil;

public class PojoCard {
	private String content;
	private int count;
	private String time;

	@Override
	public String toString() {
		return "PojoCard [content=" + content + ", count=" + count + "]";
	}
	public PojoCard() {
		super();
	}

	public PojoCard(String content, String time,int count) {
		super();
		this.content = content;
		this.time = time;
		this.count = count;
	}

	public PojoCard(String content, int count) {
		super();
		this.content = content;
		this.count = count;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public String getTime() {
		return TimeUtil.getCurrentTime2();
	}

	public void setTime(String time) {
		this.time = time;
	}
}
