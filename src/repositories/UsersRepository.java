package repositories;

import models.User;

import java.sql.*;

public class UsersRepository extends BaseRepository<User> {

    public UsersRepository() {
        super();
        this.dbTableName = "Users";
        this.tClass = User.class;
    }

    protected void readItem(ResultSet rs, User item) {
        try {
            item.setId(rs.getInt("id"));
            item.setUsername(rs.getString("username"));
            item.setPassword(rs.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void insert(User user) {
        String sql = "INSERT INTO " + this.dbTableName + " (`username`, `password`) VALUES (?,?)";

        try {
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setString(1, user.getUsername());
            preparedStmt.setString(2, user.getPassword());

            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void update(User user) {
        String sql = "UPDATE " + this.dbTableName + " SET `username`=?,`password`=? WHERE id=?";

        try {
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setString(1, user.getUsername());
            preparedStmt.setString(2, user.getPassword());
            preparedStmt.setInt(3, user.getId());

            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkIfUserExists(User user) {
        User verifyUser = this.getAll().stream().filter(u -> u.getUsername().equals(user.getUsername())).findFirst().orElse(null);
        if (verifyUser == null) {
            return false;
        }

        return true;
    }

    public User getByUsernameAndPassword(String username, String password) {
        return this.getAll().stream().filter((User) -> User.getUsername().equals(username) && User.getPassword().equals(password)).findFirst().orElse(null);
    }
}