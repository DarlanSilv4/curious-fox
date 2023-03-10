package com.curiousfox.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.curiousfox.jdbc.ConnectionFactory;
import com.curiousfox.model.Comment;

public class CommentDAO {
	private Connection connection;
	
	public CommentDAO(){
		this.connection = new ConnectionFactory().getConnection();
	}
	
	public void addComment(Comment comment) {
		String sql = "INSERT INTO comments (parent_id, sender_id, receiver_id, comment_text, created_at) values (?,?,?,?,?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setObject(1, comment.getParentId(), Types.OTHER);
			stmt.setObject(2, comment.getSenderId(), Types.OTHER);
			stmt.setObject(3, comment.getReceiverId(), Types.OTHER);
			stmt.setString(4, comment.getText());
			stmt.setTimestamp(5, Timestamp.from(comment.getCreatedAt()));
			
			stmt.execute();
			stmt.close();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}	
	}
	
	public ArrayList<Comment> getAllComments(String userId) {
		String sql = "SELECT * FROM comments WHERE receiver_id = ? AND parent_id is NULL ORDER BY created_at DESC";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setObject(1, userId, Types.OTHER);
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Comment> commentsArr = new ArrayList<Comment>();
			while (rs.next()) {
				String commentId = rs.getString("comment_id");
				String senderId = rs.getString("sender_id");
				String receiverId = rs.getString("receiver_id");
				String text = rs.getString("comment_text");
				Timestamp createdAt = rs.getTimestamp("created_at");
				
				Comment comment = new Comment();
				comment.setId(commentId);
				comment.setSenderId(senderId);
				comment.setReceiverId(receiverId);
				comment.setText(text);
				comment.setCreatedAt(createdAt.toInstant());
				
				commentsArr.add(comment);
			}
			
			stmt.close();
			return commentsArr;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Comment getComment(String commentId) {
		String sql = "SELECT * FROM comments WHERE comment_id = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setObject(1, commentId, Types.OTHER);
			ResultSet rs = stmt.executeQuery();
			
			Comment comment = new Comment();
			while(rs.next()) {
				String parentId = rs.getString("parent_id");
				String senderId = rs.getString("sender_id");
				String receiverId = rs.getString("receiver_id");
				String text = rs.getString("comment_text");
				Timestamp createdAt = rs.getTimestamp("created_at");
				
				comment.setId(commentId);
				comment.setParentId(parentId);
				comment.setSenderId(senderId);
				comment.setReceiverId(receiverId);
				comment.setText(text);
				comment.setCreatedAt(createdAt.toInstant());
			}
			
			rs.close();
			stmt.close();
			return comment;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ArrayList<Comment> getAllReplies(String parentId){
		String sql = "SELECT * FROM comments WHERE parent_id = ? ORDER BY created_at DESC";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setObject(1, parentId, Types.OTHER);
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Comment> repliesArr = new ArrayList<Comment>();
			while (rs.next()) {
				String commentId = rs.getString("comment_id");
				String senderId = rs.getString("sender_id");
				String receiverId = rs.getString("receiver_id");
				String text = rs.getString("comment_text");
				Timestamp createdAt = rs.getTimestamp("created_at");
				
				Comment comment = new Comment();
				comment.setId(commentId);
				comment.setParentId(parentId);
				comment.setSenderId(senderId);
				comment.setReceiverId(receiverId);
				comment.setText(text);
				comment.setCreatedAt(createdAt.toInstant());
				
				repliesArr.add(comment);
			}
			
			stmt.close();
			return repliesArr;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
