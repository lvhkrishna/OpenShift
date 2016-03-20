<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
	<%@ page import = "java.sql.*" %>
	<%@ page import = "javax.sql.*" %>
	<%

	int a = 0;
	String name = request.getParameter("user");
	String pass = request.getParameter("password");
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String dbhost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
	String dbport = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
	String username = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
	String password = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
	String url = "jdbc:mysql://" + dbhost + ":" + dbport + "/imagestorage";
	try
	{	
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, username, password);
		if(conn == null)
			out.print("NULL");
		stmt = conn.createStatement();
		String sql = "select * from Users";
		rs = stmt.executeQuery(sql);
		while(rs.next())
		{
			String dbname = rs.getString("UserName");
			String dbpwd = rs.getString("Password");
			if(dbname.equals(name) && dbpwd.equals(pass))
			{
				a = 1;
				response.sendRedirect("upload.html");
			}
		}
		if(a == 0)
		{
			out.print("Invalid");
		}
	}
	catch(ClassNotFoundException ce){ce.printStackTrace();}
	catch(SQLException se){se.printStackTrace();}
	catch(Exception e){e.printStackTrace();}
	finally
	{
		try
		{
			stmt.close();
			rs.close();
			conn.close();
		}
		catch(SQLException e){e.printStackTrace();}
		catch(Exception e){e.printStackTrace();}
	}
	%>
</body>
</html>