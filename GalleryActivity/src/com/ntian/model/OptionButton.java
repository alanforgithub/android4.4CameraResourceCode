package com.ntian.model;

public class OptionButton {

	/**
	 * 选项中的名称
	 */
	private String optionName;
	/**
	 * 选项中的button
	 */
	private int drawableId;

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	@Override
	public String toString() {
		return "OptionButton [optionName=" + optionName + ", drawableId="
				+ drawableId + "]";
	}
}
