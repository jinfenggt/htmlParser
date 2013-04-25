package com.htmlParser.product;

public class ProductReview {
	
	private String user_id;
	private String asin;
	private String content;
	private float rating;
	private int comment_no;
	private int isHelpful1;
	private int isHelpful2;
	
	public ProductReview(){
		
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public int getComment_no() {
		return comment_no;
	}

	public void setComment_no(int comment_no) {
		this.comment_no = comment_no;
	}

	public int getIsHelpful1() {
		return isHelpful1;
	}

	public void setIsHelpful1(int isHelpful1) {
		this.isHelpful1 = isHelpful1;
	}

	public int getIsHelpful2() {
		return isHelpful2;
	}

	public void setIsHelpful2(int isHelpful2) {
		this.isHelpful2 = isHelpful2;
	}
	
	

}
