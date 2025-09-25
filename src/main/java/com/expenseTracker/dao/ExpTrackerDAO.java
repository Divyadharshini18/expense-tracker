package com.expenseTracker.dao;

import com.expenseTracker.util.DatabaseConnection;
import java.util.List;

import com.expenseTracker.model.Category;
import com.expenseTracker.model.Expenses;
import java.sql.*;
import java.util.ArrayList;

public class ExpTrackerDAO {

    private static final String SELECT_ALL_CAT = "SELECT * FROM category";
    private static final String SELECT_ALL_EXP = "SELECT * FROM expenses";
    private static final String INSERT_CAT = "INSERT INTO category (id,title) VALUES(?,?)";
    private static final String INSERT_EXP = "INSERT INTO expenses (eid, description, amount, category, date) VALUES(?,?,?,?,?)";
    private static final String DELETE_CAT = "DELETE FROM category WHERE id = ?";
    private static final String DELETE_EXP = "DELETE FROM expenses WHERE eid = ?";
    private static final String UPDATE_CAT = "UPDATE category SET title = ? WHERE id = ?";
    private static final String UPDATE_EXP = "UPDATE expenses SET description = ?, amount = ?, date = ?, category = ? WHERE eid = ?";
    private static final String SELECT_CAT_ID = "SELECT * FROM category WHERE id = ?";
    private static final String SELECT_EXP_ID = "SELECT * FROM expenses WHERE eid = ?";
    private static final String FILTER_EXP = "SELECT * FROM expenses WHERE category = ?";


    public int createCat(Category cat) throws SQLException{
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_CAT,Statement.RETURN_GENERATED_KEYS);
        ){
            stmt.setInt(1, cat.getId());
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

    public Category getCatByID (int catId) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_CAT_ID);
        ) {
            stmt.setInt(1, catId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getRow(rs);
                }
            }
        }
        return null; // no category found
    }

    public boolean updateCat(Category cat) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CAT)
        ) {
            stmt.setString(1, cat.getTitle());  
            stmt.setInt(2, cat.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCat(Category cat) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_CAT)
        ) {
            stmt.setInt(1, cat.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
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
            stmt.setInt(1, exp.getId());
            stmt.setString(2, exp.getDescription());
            stmt.setInt(3, exp.getAmount());
            stmt.setString(4, exp.getCategory());
            stmt.setDate(5, Date.valueOf(exp.getDate()));


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

    public Expenses getExpByID (int expId) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_EXP_ID);
        ) {
            stmt.setInt(1, expId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getExpRow(rs);
                }
            }
        }
        return null; // no expenses found
    }

    public boolean updateExp(Expenses exp) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_EXP)
        ) {
            stmt.setString(1, exp.getDescription());
            stmt.setInt(2, exp.getAmount());
            stmt.setDate(3, Date.valueOf(exp.getDate()));
            stmt.setString(4, exp.getCategory());
            stmt.setInt(5, exp.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteExp(Expenses exp) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_EXP)
        ) {
            stmt.setInt(1, exp.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Expenses> filterExp(String category) throws SQLException{
        List<Expenses> exps=new ArrayList<>();
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(FILTER_EXP);
        ){
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                exps.add(getExpRow(rs));
            }
        }
        return exps;
    }

    private Expenses getExpRow(ResultSet rs) throws SQLException {
        Expenses exp = new Expenses();
        exp.setId(rs.getInt("eid"));
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
