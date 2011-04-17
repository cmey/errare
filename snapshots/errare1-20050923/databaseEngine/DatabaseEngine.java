package databaseEngine;
import java.sql.*;

import javax.sql.ConnectionEvent;

public class DatabaseEngine {
	
	private Connection co;
	
	public DatabaseEngine() {
		this.co = connectToDatabase();
		
	}
	
	private Connection connectToDatabase() {
//		 Register the Mckoi JDBC Driver
		try {
			Class.forName("com.mckoi.JDBCDriver").newInstance();
		}
		catch (Exception e) {
			System.out.println(
					"Unable to register the JDBC Driver.\n" +
					"Make sure the JDBC driver is in the\n" +
			"classpath.\n");
			System.exit(1);
		}
		
		// This URL specifies we are connecting with a local database
		// within the file system.  './db.conf' is the path of the
		// configuration file of the database to embed.
		String url = "jdbc:mckoi:local://./db.conf";
		
		// The username / password to connect under.
		String username = "admin_user";
		String password = "aupass00";
		
		// Make a connection with the local database.
		Connection connection;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
		}
		catch (SQLException e) {
			System.out.println(
					"Unable to make a connection to the database.\n" +
					"The reason: " + e.getMessage());
			return null;
		}
		
		return connection;
	}
	
	private void createDatabase() {
		try {
			sendRequest("create table develteam (" +
					"name CHAR(10)," +
					"age INT(2)," +
					"PRIMARY KEY (name)," +
					"CHECK(age IN(18, 19, 20))" +
			");");
			sendRequest("create table task (" +
					"goal CHAR(25)," +
					"difficulty CHAR(10)," +
					"PRIMARY KEY (goal)" +
			");");
			sendRequest("create table workson (" +
					"goal CHAR(25)," +
					"name CHAR(10)," +
					"PRIMARY KEY (goal, name)," +
					"FOREIGN KEY (name) REFERENCES develteam(name)," +
					"FOREIGN KEY (goal) REFERENCES task(goal)" +
			");");
			
			
			
			
		} catch (SQLException e) {
			System.out.println("Database already created");
		}
	}
	
	private void fillDatabase() {
		try {
			sendRequest("insert into develteam values('martin', 18)");
			sendRequest("insert into develteam values('arnaud', 18)");
			sendRequest("insert into develteam values('antoine', 19)");
			sendRequest("insert into develteam values('christophe', 20)");
			sendRequest("insert into develteam values('guillaume', 20)");
			
			sendRequest("insert into task values('database', 'easy')");
			sendRequest("insert into task values('picking', 'impossible')");
			sendRequest("insert into task values('gui', 'normal')");
			sendRequest("insert into task values('gameengine', 'funny')");
			sendRequest("insert into task values('graphics', 'hard')");
			
			sendRequest("insert into workson values('database', 'martin')");
			sendRequest("insert into workson values('gui', 'arnaud')");
			sendRequest("insert into workson values('graphics', 'christophe')");
			sendRequest("insert into workson values('picking', 'antoine')");
			sendRequest("insert into workson values('gameengine', 'guillaume')");
			sendRequest("insert into workson values('graphics', 'antoine')");
			
			
			
			
		} catch (SQLException e) {
			System.out.println("Database already filled");
		}
	}
	
	private void destroyDatabase() {
		
		try {
			sendRequest("drop table workson");
		} catch (SQLException e) {
			System.out.println("Cant destroy database : "+e.getMessage());
		}	
		try {
			sendRequest("drop table develteam");
		} catch (SQLException e) {
			System.out.println("Cant destroy database : "+e.getMessage());
		}
		try {
			sendRequest("drop table task");
		} catch (SQLException e) {
			System.out.println("Cant destroy database : "+e.getMessage());
		}
		
	}
	
	
	
	public ResultSet sendRequest(String req) throws SQLException {
		return co.createStatement().executeQuery(req);
	}
	
	public void closeDatabase() {
		try {
			co.close();
		}catch(SQLException e) {
			System.out.println("Impossible to close the databse : "+e.getMessage());
		}
	}
	
	public void printDatabase() {
		try {
			ResultSet tableNames;
			
			tableNames = sendRequest("show tables");
			
		
			while(tableNames.next()) {
				String name = tableNames.getString(1);
				System.out.println(name);
				
				ResultSet res = sendRequest("select * from "+name);
				ResultSetMetaData colums = res.getMetaData();
				int numColums = colums.getColumnCount();
				for(int i=1; i<=numColums; i++) {
					System.out.print(colums.getColumnLabel(i)+" ");
					System.out.print(colums.getColumnClassName(i)+"    ");
				}
				
				System.out.println();
				
				while(res.next()) {
					for(int i=1; i<=numColums; i++)
						System.out.print(res.getObject(i)+" ");
					System.out.println();
				}
			}
			
		} catch (SQLException e) {
			System.out.println("Error when printing the database : "+e.getMessage());
		}
	}
	
	private void printConstraints() {
		
		try {
			ResultSet res = sendRequest("select * from SYS_INFO.sUSRUniqueInfo");
			ResultSetMetaData colums = res.getMetaData();
			int numColums = colums.getColumnCount();
			for(int i=1; i<=numColums; i++) {
				System.out.print(colums.getColumnLabel(i)+" ");
				System.out.print(colums.getColumnClassName(i)+"    ");
			}
			
			System.out.println();
			
			while(res.next()) {
				for(int i=1; i<=numColums; i++)
					System.out.print(res.getObject(i)+" ");
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Error when printing the constraints : "+e.getMessage());
		}
	}
	
	public static void main(String[] args) throws SQLException {
		DatabaseEngine de = new DatabaseEngine();
		de.destroyDatabase();
		de.createDatabase();
		de.fillDatabase();
		
		de.printDatabase();
		de.closeDatabase();
		
		
		//ResultSet res = de.sendRequest("select * from SYS_INFO.sUSRTableColumns");
		//ResultSet res = de.sendRequest("select * from SYS_INFO.sUSRUniqueInfo");
	}
	
}

