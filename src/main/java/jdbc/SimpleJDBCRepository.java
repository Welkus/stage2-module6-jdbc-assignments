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

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myuser (first_name,last_name,age) VALUES ('John','Butch',30)";
    private static final String updateUserSQL = "UPDATE myuser age=20 where first_name = 'John' AND last_name = 'Butch'";
    private static final String deleteUser = "DELETE FROM myuser WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myuser WHERE id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myuser WHERE userName = ?";
    private static final String findAllUserSQL = "SELECT * FROM myuser";

    public Long createUser() {

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();


            return rs.getLong(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserById(Long userId) {
        long id = 0L;
        String firstName = "";
        String lastName = "";
        int age = 0;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1,userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                id = rs.getLong("id");
                firstName = rs.getString("firstname");
                lastName = rs.getString("lastname");
                age = rs.getInt("age");
            }
            return new User(id,firstName,lastName,age);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserByName(String userName) {
        long id = 0L;
        String firstName = "";
        String lastName = "";
        int age = 0;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1,userName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                id = rs.getLong("id");
                firstName = rs.getString("firstname");
                lastName = rs.getString("lastname");
                age = rs.getInt("age");
            }
            return new User(id,firstName,lastName,age);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAllUser() {
        long id = 0L;
        String firstName = "";
        String lastName = "";
        int age = 0;
        List<User> usList = new ArrayList<>();
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findAllUserSQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                id = rs.getLong("id");
                firstName = rs.getString("firstname");
                lastName = rs.getString("lastname");
                age = rs.getInt("age");

                usList.add(new User(id,firstName,lastName,age));
            }
            return usList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User updateUser() {
        long id = 0L;
        String firstName = "";
        String lastName = "";
        int age = 0;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(updateUserSQL);
            ps.executeUpdate();
            String s = "SELECT * FROM myuser where first_name = ?";
            PreparedStatement pp = connection.prepareStatement(s);
            pp.setString(1,"John");
            ResultSet rs = pp.executeQuery();

            while (rs.next()){
                id = rs.getLong("id");
                firstName = rs.getString("firstname");
                lastName = rs.getString("lastname");
                age = rs.getInt("age");
            }
            return new User(id,firstName,lastName,age);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteUser(Long userId) {

        try {
            connection = CustomDataSource.getInstance().getConnection();
           ps = connection.prepareStatement(deleteUser);
           ps.setLong(1,userId);

           ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
