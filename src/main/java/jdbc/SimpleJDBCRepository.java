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

    private Connection connection;

    {
        try {
            connection = CustomDataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers(firstname, lastname, age) VALUES(?, ?, ?)";;
    private static final String updateUserSQL = "UPDATE myusers SET firstname = ?, lastname = ?, age = ? WHERE id = ?";;
    private static final String deleteUser = "DELETE FROM myusers WHERE id = ?";;
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?";;
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname = ?";;
    private static final String findAllUserSQL = "SELECT * FROM myusers";;

    public Long createUser(User user) {
        Long id = null;
        try (PreparedStatement statement = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, user.getFirstName());
            statement.setObject(2, user.getLastName());
            statement.setObject(3, user.getAge());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public User findUserById(Long userId) {
        User user = null;
        try (
             PreparedStatement statement = connection.prepareStatement(findUserByIdSQL)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getAllUserParametersFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User findUserByName(String userName) {
        User user = null;
        try (
             PreparedStatement statement = connection.prepareStatement(findUserByNameSQL)) {
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getAllUserParametersFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> findAllUser() {

        List<User> users = new ArrayList<>();

        try (
             PreparedStatement statement = connection.prepareStatement(findAllUserSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(getAllUserParametersFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User updateUser(User user) {
        try (
             PreparedStatement statement = connection.prepareStatement(updateUserSQL)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setInt(3, user.getAge());
            statement.setLong(4, user.getId());
            if (statement.executeUpdate() != 0) {
                return findUserById(user.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUser(Long userId) {
        try (
             PreparedStatement statement = connection.prepareStatement(deleteUser)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User getAllUserParametersFromResultSet(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("firstname"))
                .lastName(rs.getString("lastname"))
                .age(rs.getInt("age"))
                .build();

    }
}
