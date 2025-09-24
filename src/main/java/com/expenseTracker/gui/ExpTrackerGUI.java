package com.expenseTracker.gui;

import java.time.LocalDate;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.expenseTracker.dao.ExpTrackerDAO;
import com.expenseTracker.model.Category;
import com.expenseTracker.model.Expenses;

public class ExpTrackerGUI extends JFrame{
    
    private JButton catBtn;
    private JButton expBtn;

    public ExpTrackerGUI(){
        intializeComponents();
        setupLayout();
        setupListeners();
    }

    private void intializeComponents(){
        setTitle("Expense Tracker");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        catBtn = new JButton("Category");
        expBtn = new JButton("Expense");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel btnPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        btnPanel.add(catBtn, gbc);
        btnPanel.add(expBtn, gbc);
        add(btnPanel, BorderLayout.CENTER);
    }

    private void setupListeners(){
        catBtn.addActionListener( (e)-> { new CatGUI().setVisible(true); } );
        expBtn.addActionListener( (e)-> { new ExpGUI().setVisible(true); } );
    }

}

class CatGUI extends JFrame {
    
    private ExpTrackerDAO catDAO;

    private JTextField title;
    private JButton addBtn;
    private JButton updateBtn;
    private JButton deleteBtn;
    private JButton refreshBtn;
    private DefaultTableModel catTableModel;
    private JTable catTable;

    public CatGUI(){
        intializeComponents();
        setupLayout();
        setupListeners();
        loadCats();
    }

    private void intializeComponents(){

        setTitle("Category");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        title = new JTextField(20);
        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        refreshBtn = new JButton("Refresh");

        catDAO = new ExpTrackerDAO();

        String[] colNames = {"ID", "Title"};
        catTableModel = new DefaultTableModel(colNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        catTable = new JTable(catTableModel);
        catTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        catTable.getSelectionModel().addListSelectionListener(
             (e) -> {
                if(!e.getValueIsAdjusting()){
                    // loadSeletedCats();
                }
             }
        );

    }

    private void setupLayout(){

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(title, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(btnPanel,BorderLayout.SOUTH);

        add(northPanel,BorderLayout.NORTH);

        add(new JScrollPane(catTable),BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Select a Category to edit or delete"));
        add(statusPanel,BorderLayout.SOUTH);

    }

    private void setupListeners(){
        addBtn.addActionListener((e) -> { addCat(); });
        // updateBtn.addActionListener((e) -> { updateCat(); });
        // deleteBtn.addActionListener((e) -> { deleteCat(); });
        // refreshBtn.addActionListener((e) -> { refreshCat(); });
    }

    private void addCat(){
        String Title = title.getText().trim();
        try{
            Category cat = new Category(Title);
            catDAO.createCat(cat);
            JOptionPane.showMessageDialog(this, "Category added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadCats();
            clearForm();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error adding category: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCats() {
        try{
            List<Category> cats = catDAO.getAllCat();
            updateTable(cats);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error loading categories: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<Category> cats){
        catTableModel.setRowCount(0);
        for(Category cat: cats){
            Object[] rowData = {cat.getId(), cat.getTitle()};
            catTableModel.addRow(rowData);
        }
    }

    private void clearForm(){
        title.setText("");
    }

}

class ExpGUI extends JFrame {
    
    private ExpTrackerDAO expDAO;

    private JTextArea description;
    private JTextField amount;
    private JTextField category;
    private JTextField date;
    private JButton addBtn;
    private JButton updateBtn;
    private JButton deleteBtn;
    private JButton refreshBtn;
    private DefaultTableModel expTableModel;
    private JTable expTable;

    public ExpGUI(){
        intializeComponents();
        setupLayout();
        setupListeners();
        loadExps();
    }

    private void intializeComponents(){

        expDAO = new ExpTrackerDAO();

        setTitle("Expense");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        description = new JTextArea(3, 20);
        amount = new JTextField(20);
        category = new JTextField(20);
        date = new JTextField(10);
        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        refreshBtn = new JButton("Refresh");

        String[] colNames = {"ID", "Description", "Amount", "Date", "Category"};
        expTableModel = new DefaultTableModel(colNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        expTable = new JTable(expTableModel);
        expTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expTable.getSelectionModel().addListSelectionListener(
             (e) -> {
                if(!e.getValueIsAdjusting()){
                    // loadSeletedExps();
                }
             }
        );
    }

    private void setupLayout(){

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(description), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(amount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(category, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Date (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        inputPanel.add(date, gbc);


        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(btnPanel,BorderLayout.SOUTH);

        add(northPanel,BorderLayout.NORTH);

        add(new JScrollPane(expTable),BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Select an Expense to edit or delete"));
        add(statusPanel,BorderLayout.SOUTH);

    }

    private void setupListeners(){
        addBtn.addActionListener((e) -> { addExp(); });
        // updateBtn.addActionListener((e) -> { updateExp(); });
        // deleteBtn.addActionListener((e) -> { deleteExp(); });
        // refreshBtn.addActionListener((e) -> { refreshExp(); });
    }

    private void addExp(){
        String desc = description.getText().trim();
        int amt = Integer.parseInt(amount.getText().trim());  
        String cat = category.getText().trim();  
        String dateText = date.getText().trim();   
        LocalDate date = LocalDate.parse(dateText);

        try{
            Expenses exp = new Expenses();
            exp.setDescription(desc);
            exp.setAmount(amt);
            exp.setCategory(cat);
            exp.setDate(date);
            expDAO.createExp(exp);
            loadExps();
            clearForm();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error adding category: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExps() {
        try{
            List<Expenses> exps = expDAO.getAllExp();
            updateTable(exps);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error loading expenses: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<Expenses> exps){
        expTableModel.setRowCount(0);
        for(Expenses exp: exps){
            Object[] rowData = {exp.getId(), exp.getDescription(), exp.getAmount(), exp.getDate(), exp.getCategory()};
            expTableModel.addRow(rowData);
        }
    }

    private void clearForm() {
        description.setText("");
        amount.setText("");
        category.setText("");
        date.setText("");
    }
    
}
