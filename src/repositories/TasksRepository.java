package repositories;

import enums.TaskStatusEnum;
import models.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TasksRepository extends BaseRepository<Task> {

    public TasksRepository() {
        super();
        this.dbTableName = "Tasks";
        this.tClass = Task.class;
    }

    protected void readItem(ResultSet rs, Task item) {
        try {
            item.setId(rs.getInt("id"));
            item.setUserId(rs.getInt("userId"));
            item.setTitle(rs.getString("title"));
            item.setContent(rs.getString("content"));
            item.setTaskStatus(TaskStatusEnum.values()[rs.getInt("status")]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void insert(Task task) {
        String sql = "INSERT INTO " + this.dbTableName + " (`userId`, `title`, `content`, `status`) VALUES (?,?,?,?)";

        try {
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setInt(1, task.getUserId());
            preparedStmt.setString(2, task.getTitle());
            preparedStmt.setString(3, task.getContent());
            preparedStmt.setInt(4, task.getTaskStatus().ordinal());

            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void update(Task task) {
        String sql = "UPDATE " + this.dbTableName + " SET `title`= ?,`content`= ?,`status`= ? WHERE `id`=?";

        try {
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setString(1, task.getTitle());
            preparedStmt.setString(2, task.getContent());
            preparedStmt.setInt(3, task.getTaskStatus().ordinal());
            preparedStmt.setInt(4, task.getId());

            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getByUserId(int userId) {
        return this.getAll().stream().filter(t -> t.getUserId() == userId).collect(Collectors.toCollection(ArrayList<Task>::new));
    }
}
