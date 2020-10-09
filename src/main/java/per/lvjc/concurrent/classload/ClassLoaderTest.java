package per.lvjc.concurrent.classload;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("xxx");
        Object o = new Object();
        System.out.println(o.getClass());
        //DriverManager.getConnection("");
    }
}
