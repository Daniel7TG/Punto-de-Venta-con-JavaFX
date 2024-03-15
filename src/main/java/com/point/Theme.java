package com.point;

import javafx.scene.paint.Color;

public enum Theme {

//	BLUE("#4a5989", "#9cb6dd",
//			"#e7f0f8", "#f2f7fb", "#6374ae", "#6374ae",
//			"#6374ae", "#FFFFFF",
//			"#414e6e");
	DARK("#6d6d6d", "#888888",
			 "#6d6d6d", "#5d5d5d", "#000000", "#FFFFFF",
			 "#000000", "#5d5d5d",
			 "#d1d1d1");
	
	
	private String backgroundA, backgroundB, 
	buttonBg, buttonHover, buttonBorder, buttonLabel,
	labelOut, labelIn,
	side;
	
	/**
	 * @param backgroundA
	 * @param backgroundB
	 * @param buttonBg
	 * @param buttonHover
	 * @param buttonBorder
	 * @param buttonLabel
	 * @param labelOut
	 * @param labelIn
	 * @param side
	 */
	private Theme(String backgroundA, String backgroundB, String buttonBg, String buttonHover, String buttonBorder,
			String buttonLabel, String labelOut, String labelIn, String side) {
		this.backgroundA = backgroundA;
		this.backgroundB = backgroundB;
		this.buttonBg = buttonBg;
		this.buttonHover = buttonHover;
		this.buttonBorder = buttonBorder;
		this.buttonLabel = buttonLabel;
		this.labelOut = labelOut;
		this.labelIn = labelIn;
		this.side = side;
	}

	public String getBackgroundA() {
		return backgroundA;
	}

	public String getBackgroundB() {
		return backgroundB;
	}

	public String getButtonBg() {
		return buttonBg;
	}

	public String getButtonHover() {
		return buttonHover;
	}

	public String getButtonBorder() {
		return buttonBorder;
	}

	public String getButtonLabel() {
		return buttonLabel;
	}

	public String getLabelOut() {
		return labelOut;
	}

	public String getLabelIn() {
		return labelIn;
	}

	public String getSide() {
		return side;
	}	

}
