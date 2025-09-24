package com.expenseTracker.dao;

import com.expenseTracker.util.DatabaseConnection;
import java.util.List;

import com.expenseTracker.model.Category;
import com.expenseTracker.model.Expenses;
import java.sql.*;
import java.util.ArrayList;

public class ExpTrackerDAO {

    private static final String SELECT_ALL_CAT = "SELECT * FROM category";
    private static final String INSERT_CAT = "INSERT INTO category (id,title) VALUES(?,?)";
    private static final String INSERT_EXP = "INSERT INTO expenses (id,description,amount,date,category) VALUES(?,?,?,?,?)";
    private static final String SELECT_ALL_EXP = "SELECT * FROM expenses";

    public int createCat(Category cat) throws SQLException{
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_CAT,Statement.RETURN_GENERATED_KEYS);
        ){ 
            stmt.setInt(1, 0);
            stmt.setString(2, cat.getTitle());

            int rowAffected = stmt.executeUpdate();

            if(rowAffected == 0){
                throw new SQLException("Creating category failed, no row is inserted");
            }

            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
                else{
                    throw new SQLException("Creating category failed, no id obtained");
                }
            }
        }
    }

    private Category getRow(ResultSet rs) throws SQLException {
        Category cat = new Category();
        cat.setId(rs.getInt("id"));
        cat.setTitle(rs.getString("title"));
        return cat;
    }

    public List<Category> getAllCat() throws SQLException {
        List<Category> cats = new ArrayList<>();
        try (Connection cn = DatabaseConnection.getDBConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_CAT);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cats.add(getRow(rs));
            }
        }
        return cats;
    }

    public int createExp (Expenses exp) throws SQLException {
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_EXP,Statement.RETURN_GENERATED_KEYS);
        ){ 
            stmt.setInt(1, 0);
            stmt.setString(2, exp.getDescription());
            stmt.setInt(3, exp.getAmount());
            stmt.setDate(4, Date.valueOf(exp.getDate()));
            stmt.setString(5, exp.getCategory());

            int rowAffected = stmt.executeUpdate();

            if(rowAffected == 0){
                throw new SQLException("Creating expense failed, no row is inserted");
            }

            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
                else{
                    throw new SQLException("Creating expense failed, no id obtained");
                }
            }
        }
    }

    private Expenses getExpRow(ResultSet rs) throws SQLException {
        Expenses exp = new Expenses();
        exp.setId(rs.getInt("id"));
        exp.setDescription(rs.getString("description"));
        exp.setAmount(rs.getInt("amount"));
        exp.setDate(rs.getDate("date").toLocalDate());
        exp.setCategory(rs.getString("category"));
        return exp;
    }

    public List<Expenses> getAllExp() throws SQLException {
        List<Expenses> exps = new ArrayList<>();
        try (Connection cn = DatabaseConnection.getDBConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_EXP);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                exps.add(getExpRow(rs));
            }
        }
        return exps;
    }
}
