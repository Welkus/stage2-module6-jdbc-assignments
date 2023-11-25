package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = CustomDataSource.getInstance().getConnection();
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers (firstname,lastname,age) VALUES (?,?,?);";
    private static final String updateUserSQL = "UPDATE myusers SET firstname = ? , lastname = ?, age = ? WHERE id = ?";
    private static final String deleteUser = "DELETE FROM myusers WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers where id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers where firstname = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser(User user) {
        try {
            ps = connection.prepareStatement(createUserSQL,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3,user.getAge());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User findUserById(Long userId) {
        try {
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1,userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new User(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserByName(String userName) {
        try {
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1,userName);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new User(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    public List<User> findAllUser() {
        List<User> uList = new ArrayList<>();

        try {
            ps = connection.prepareStatement(findAllUserSQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                uList.add(new User(rs.getLong(1),rs.getString(2),rs.getString(3),rs.getInt(4) ));
            }
            return uList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User updateUser(User user) {
        try {
            ps = connection.prepareStatement(updateUserSQL);
            ps.setString(1,user.getFirstName());
            ps.setString(2,user.getLastName());
            ps.setInt(3,user.getAge());
            ps.setLong(4,  user.getId());
            ps.executeUpdate();

            return findUserById(user.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteUser(Long userId) {
        try {
            ps = connection.prepareStatement(deleteUser);
            ps.setLong(1,userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}