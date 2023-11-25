package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;
    //  code

    private CustomDataSource(String driver, String url, String password, String name) {
    this.driver = driver;
    this.url = url;
    this.password = password;
    this.name = name;

    }


    public static CustomDataSource getInstance() {

        if(instance == null){
            try(InputStream inp = CustomDataSource.class.getClassLoader().getResourceAsStream("app.properties")
                    ){
                Properties props = new Properties();
                props.load(inp);

              return instance = new CustomDataSource(
                        props.getProperty("postgres.driver"),
                        props.getProperty("postgres.url"),
                        props.getProperty("postgres.password"),
                        props.getProperty("postgres.name")
                );
            } catch (IOException e){
                e.printStackTrace();
            }

        }
        return instance;
    }

    @Override
    public Connection getConnection()  {
        try {
            return DriverManager.getConnection(url,name,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}