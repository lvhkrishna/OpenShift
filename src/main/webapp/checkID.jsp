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
	String uid = request.getParameter("UID");
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String dbhost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
	String dbport = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
	String username = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
	String password = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
	String url = "jdbc:mysql://" + dbhost + ":" + dbport + "/imagestorage";
	request.getSession(false);
	String user = (String)session.getAttribute("Loguser");
	try
	{	
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, username, password);
		stmt = conn.createStatement();
		String sql = "select * from Users";
		rs = stmt.executeQuery(sql);
		while(rs.next())
		{
			String dbuser = rs.getString("UserName");
			String dbid = rs.getString("ID");
			if(dbuser.equals(user) && uid == dbid)
			{
				a = 1;
				response.sendRedirect("uploaded.jsp");
			}
		}
		if(a == 0)
		{
			out.print("Invalid ID entered. <a href='enterID.html'>Try again</a>");
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