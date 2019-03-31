package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class SQL {
	private static String dbName = "GAMEUSERS";
	
	public static void main(String[] args) throws Exception {
		try {
			MysqlDataSource mysqlDS = new MysqlDataSource();

			mysqlDS.setURL("jdbc:mysql://localhost:3306/superheroes");
			mysqlDS.setUser("root");
			mysqlDS.setPassword("");

			Connection conn = mysqlDS.getConnection();
			Statement myStmt = conn.createStatement();
			String query = "select * from superhero_table";
			ResultSet rs = myStmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString("name");
				String realFirstName = rs.getString("real_first_name");
				String realSurname = rs.getString("real_surname");
				String dob = rs.getString("dob");
				String powers = rs.getString("powers");
				// System.out.println("Name = " + name);
				// System.out.println("Real First Name = " + realFirstName);
				// System.out.println("Real Surname = " + realSurname);
				// System.out.println("Date of Birth = " + dob);
				// System.out.println("Powers = " + powers);

				System.out.println(name + ", " + realFirstName + ", " + dob + ", " + powers);

			}
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}
	}
}
