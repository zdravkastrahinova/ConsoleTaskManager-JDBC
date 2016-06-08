package repositories;

import models.BaseModel;

import java.sql.*;
import java.util.ArrayList;

public abstract class BaseRepository<T extends BaseModel> {
    protected Class<T> tClass;
    protected String dbTableName;
    protected Connection conn = null;

    protected BaseRepository() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/TaskManager" + "?verifyServerCertificate=false" + "&useSSL=true" + "&requireSSL=false", "root", "pass");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract void readItem(ResultSet rs, T item);
    protected abstract void insert(T item);
    protected abstract void update(T item);

    public T getById(int id) {
        return this.getAll().stream().filter(item -> item.getId() == id).findFirst().orElse(null);
    }

    public ArrayList<T> getAll() {
        ArrayList<T> items = new ArrayList<T>();

        try {
            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM " + this.dbTableName;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                T item = getInstance();
                readItem(rs, item);

                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void save(T item) {
        if (item.getId() == 0) {
            insert(item);
        } else {
            update(item);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM " + this.dbTableName + " WHERE id=?";

        try {
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setInt(1, id);
            preparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private T getInstance() {
        try {
            return this.tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}