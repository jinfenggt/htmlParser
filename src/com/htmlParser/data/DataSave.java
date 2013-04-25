package com.htmlParser.data;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.htmlParser.product.ProductReview;

public class DataSave {
	
	private QueryRunner runner = new QueryRunner();
	private Connection conn = ConnectionManager.getConnection();
	
	public boolean insert(ProductReview review){
		
		
		String sql = "insert into productreview(asin, use_id, rating, comments, comments_no," +
				" ishelpful_1, ishelpful_2) values(?,?,?,?,?,?,?)";
		Object[] objs = {review.getAsin(), review.getUser_id(), review.getRating(),
				review.getContent(), review.getComment_no(), review.getIsHelpful1(), review.getIsHelpful2()
		};
		try {
			runner.update(conn, sql, objs);
			return true;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}

}
