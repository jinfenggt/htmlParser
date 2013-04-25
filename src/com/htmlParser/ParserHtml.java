package com.htmlParser;

import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.htmlParser.data.DataSave;
import com.htmlParser.product.ProductReview;

public class ParserHtml {
	
	public Map<Integer, String> pMap = new HashMap<>();
	public Map<Integer, String> reviewMap = new HashMap<>();
	public Map<Integer, String> pageMap = new HashMap<>();
	String reviewUrl;
	private DataSave data = new DataSave();
	
	public void getPage(){
		NodeFilter filter = new NodeFilter() {
			
			@Override
			public boolean accept(Node arg0) {
				if (arg0.toHtml().startsWith("<li class=\"zg_page"))
					return true;
				return false;
			}
		};
		
		Parser p = new Parser();
		try {
			p.setURL("http://www.amazon.com/gp/bestsellers/watches/ref=sr_bs_1");
			NodeList list = p.extractAllNodesThatMatch(filter);
			for (int i = 0; i <list.size(); ++ i){
				Node node = list.elementAt(i);
				NodeList nodes = node.getChildren();
				for (int j = 0; j < nodes.size(); ++ j){
					Node chil = nodes.elementAt(j);
					if (chil.getClass() == LinkTag.class){
						LinkTag link = (LinkTag)chil;
						pageMap.put(i, link.extractLink());
					}
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getPMap(String url) {

		NodeFilter filter = new NodeFilter() {
			
			@Override
			public boolean accept(Node arg0) {
				
				if (arg0.getClass() == Div.class){
					Div node = (Div)arg0;
					if (node.getAttribute("class") != null){
						if (node.getAttribute("class").equalsIgnoreCase("zg_title"))
							return true;
					}
				}
				return false;
			}
		};
		
		Parser parser = new Parser();
		try {
			parser.setURL(url);
			NodeList list = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < list.size(); ++ i){
				Node node = list.elementAt(i);
				NodeList chils = node.getChildren();
				for (int j = 0; j < chils.size(); ++ j){
					if (chils.elementAt(j).getClass() == LinkTag.class){
						LinkTag link = (LinkTag)chils.elementAt(j);
						pMap.put(i, link.extractLink());
					}
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getReviewPage(String url){
		reviewUrl = new String();
		NodeFilter filter = new NodeFilter() {
			
			@Override
			public boolean accept(Node arg0) {
//				if (arg0.getClass() == LinkTag.class){
//					LinkTag link = (LinkTag)arg0;
//					if (link.getAttribute("id") != null){
//						if (link.getAttribute("id").equalsIgnoreCase("seeAllReviewsUrl")){
//							reviewUrl = link.extractLink();
////							System.out.println(reviewUrl);
//							return true;
//						}
//					}
//				}
				return true;
			}
		};
//		System.out.println();
//		System.out.println(reviewUrl);
		
		Parser p = new Parser();
		try {
			p.setURL(url);
			NodeList list = p.extractAllNodesThatMatch(filter);
			for (int i = 0; i < list.size(); ++ i){
				Node arg0 = list.elementAt(i);
				if (arg0.getClass() == LinkTag.class){
					LinkTag link = (LinkTag)arg0;
					if (link.getAttribute("id") != null){
						if (link.getAttribute("id").equalsIgnoreCase("seeAllReviewsUrl")){
							reviewUrl = link.extractLink();
//							System.out.println(reviewUrl);
						}
					}
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("1 " + reviewUrl);
		return reviewUrl;
	}
	
	public String getNextPage(String url){
		reviewUrl = new String();
		NodeFilter filter = new NodeFilter() {
			
			@Override
			public boolean accept(Node arg0) {
				if (arg0.getClass() == LinkTag.class){
//					System.out.println("in");
					LinkTag link = (LinkTag)arg0;
					
					if (link.toPlainTextString().startsWith("Next")){
						
						reviewUrl = link.extractLink();
						return true;
					}
				}
				return false;
			}
		};
		
		Parser p = new Parser();
		try {
			p.setURL(url);
			p.extractAllNodesThatMatch(filter);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("0 " + reviewUrl);
		return reviewUrl;
		
	}
	
	public void getReview(String url){
		StringBuffer content;
		ProductReview review = new ProductReview();
		String asin = url.substring(url.lastIndexOf("/") + 1);
		if (asin.contains("?")){
			asin = asin.substring(0, asin.lastIndexOf("?")-1);
		}
		review.setAsin(asin);
		NodeFilter filter = new NodeFilter() {
			
			@Override
			public boolean accept(Node arg0) {
				if (arg0.getClass() == TableTag.class){
					TableTag table = (TableTag)arg0;
					if (table.getAttribute("id") != null){
						if (table.getAttribute("id").equalsIgnoreCase("productReviews")){
							return true;
						}
					}
				}
				return false;
			}
		};
		
		Parser p = new Parser();
		try {
			p.setURL(url);
			NodeList list = p.extractAllNodesThatMatch(filter);
			for (int i = 0; i < list.size(); ++ i){
				TableTag table = (TableTag) list.elementAt(i);
				NodeList childs = table.getRows()[0].getColumns()[0].getChildren();
				for (int j = 0; j < childs.size(); ++ j){
					Node node = childs.elementAt(j);
					if (node.getClass() == Div.class){
						NodeList nodes = node.getChildren();
//						System.out.println(nodes.size());
						content = new StringBuffer();
						for (int k = 0; k < nodes.size(); ++ k){
							Node child = nodes.elementAt(k);
//							System.out.println(child.getClass());
							String s = child.toPlainTextString().trim();
							if (s.contains("people found the following review helpful")){
								String[] str = s.split(" ");
								review.setIsHelpful1(Integer.parseInt(str[0]));
								review.setIsHelpful2(Integer.parseInt(str[2]));
							}
							if (s.contains("out of 5 stars")){
								String[] str = s.split(" ");
								review.setRating(Float.parseFloat(str[0]));
//								System.out.println(str[0]);
							}
							if (s.contains("See all my reviews")){
								review.setUser_id(s.substring(s.indexOf(";") + 1, s.lastIndexOf("-")-1));
//								System.out.println(s.substring(s.indexOf(";") + 1, s.indexOf("-")-1));
							}
							if (s.contains("PermalinkComment")){
								int index = s.lastIndexOf("(");
								if (index != -1){
									review.setComment_no(Integer.parseInt(s.substring(index + 1, index + 2)));
//									System.out.println(s.substring(index + 1, index + 2));
								}
							}
							if (child.getClass() == TextNode.class){
								if (s.length() != 0){
									content.append(s);
								}
							}
						}
						review.setContent(content.toString());
						data.insert(review);
//						System.out.println(review.getContent());
						
//						System.out.println();
					}
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		ParserHtml p = new ParserHtml();
		p.getPage();
//		for (int i = 4; i < p.pageMap.size(); ++ i){
			p.getPMap(p.pageMap.get(2));
			for (int j = 11; j < p.pMap.size(); ++ j){
				String s = p.getReviewPage(p.pMap.get(j));
				System.out.println("2 " + s);
				if (p.reviewUrl.length() != 0){
					p.getReview(s);
					String page = p.getNextPage(s);
					while (page.length() != 0){
//						System.out.println("3 "+p.reviewUrl);
						p.getReview(page);
						page = p.getNextPage(page);
					}
				}
			}
//		}
//		p.getPMap();
//		p.getPage();
//		p.getReviewPage("http://www.amazon.com/Timex-Womens-T5J151-Sports-Digital/dp/B000H6AQ0Q/ref=zg_bs_watches_8");
//		p.getReview("http://www.amazon.com/Timex-Womens-T5J151-Sports-Digital/product-reviews/B000H6AQ0Q");
	}

}
