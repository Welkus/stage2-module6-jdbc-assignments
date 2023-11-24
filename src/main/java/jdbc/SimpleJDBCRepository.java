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

    private static final String createUserSQL = "";
    private static final String updateUserSQL = "";
    private static final String deleteUser = "";
    private static final String findUserByIdSQL = "";
    private static final String findUserByNameSQL = "";
    private static final String findAllUserSQL = "";

    public Long createUser() {

       return null;
    }

    public User findUserById(Long userId) {
        long id = 0L;
        String firstName = "";
        String lastName = "";
        int age = 0;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM myuser WHERE id = "+userId);

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
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM myuser WHERE userName = "+userName);

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
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM myuser");

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
        return null;
    }

    private void deleteUser(Long userId) {

        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
           st.execute("SELECT * FROM myuser WHERE id = "+userId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
