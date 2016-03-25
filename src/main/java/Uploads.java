import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
 
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;

import java.sql.*;
import javax.sql.*;
 
@WebServlet(name = "uploads",urlPatterns = {"/uploads/*"})
@MultipartConfig
public class Uploads extends HttpServlet {
 
 
  private static final long serialVersionUID = 2857847752169838915L;
  int BUFFER_LENGTH = 4096;
  KeyGenerator keyGenerator = null;  
  SecretKey secretKey = null;  
  Cipher cipher = null;  
  String masterPassword = null;  
  public Uploads() {}
  public Uploads(String masterPassword) {  
		this.masterPassword = masterPassword;  
        try {  
             
            keyGenerator = KeyGenerator.getInstance("Blowfish");              
            secretKey = new SecretKeySpec(masterPassword.getBytes(), "Blowfish");  
  
           
            cipher = Cipher.getInstance("Blowfish");  
        } catch (NoSuchPaddingException ex) {  
            System.out.println(ex);  
        } catch (NoSuchAlgorithmException ex) {  
            System.out.println(ex);  
        }  
    } 
 
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
    PrintWriter out = response.getWriter();
    for (Part part : request.getParts()) {
		int a = 0;
        InputStream is = request.getPart(part.getName()).getInputStream();
        String fileName = getFileName(part);
		String directoryPath = System.getenv("OPENSHIFT_DATA_DIR") + fileName;
		String encryptPath = System.getenv("OPENSHIFT_DATA_DIR") + fileName;
		Uploads encryptFile = new Uploads("thisismypassword");
        encryptFile.encrypt(directoryPath, encryptPath, is); 
        //FileOutputStream os = new FileOutputStream(System.getenv("OPENSHIFT_DATA_DIR") + fileName);
        //byte[] bytes = new byte[BUFFER_LENGTH];
        //int read = 0;
        //while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
            //os.write(bytes, 0, read);
        //}
        //os.flush();
        is.close();
        //os.close();
		//Database Connection
		
		HttpSession session = request.getSession(false);
		String name = (String)request.getAttribute("Loguser");
		out.print("a" + name + "a <br/>");
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
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from Images");
			while(rs.next())
			{
				String dbname = rs.getString("UserName");
				String image = rs.getString("ImageName");
				if(dbname.equals(name) && image.equals(fileName))
				{
					a = 1;
				}
			}
			if(a == 0)
			{
				String sql = "insert into Images values('" + name + "', '" + fileName + "')";
				int i = stmt.executeUpdate(sql);
				out.print("Registered Successfully. <a href='index.html'>Go back and Login</a>");
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
		
        out.println(fileName + " was uploaded to " + System.getenv("OPENSHIFT_DATA_DIR"));
    }
  }
 
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
    String filePath = request.getRequestURI().substring(request.getContextPath().length());
	filePath = URLDecoder.decode(filePath, "UTF-8");
	
	//String newfilePath = System.getenv("OPENSHIFT_REPO_DIR") + filePath.replace("/uploads/","/webapps/");
	//request.setAttribute("path", newfilePath);
	//request.getRequestDispatcher("/new.jsp").forward(request, response);
 
    File file = new File(System.getenv("OPENSHIFT_DATA_DIR") + filePath.replace("/uploads/",""));
    InputStream input = new FileInputStream(file);
 
    response.setContentLength((int) file.length());
    response.setContentType(new MimetypesFileTypeMap().getContentType(file));
 
    OutputStream output = response.getOutputStream();
    byte[] bytes = new byte[BUFFER_LENGTH];
    int read = 0;
    while ((read = input.read(bytes, 0, BUFFER_LENGTH)) != -1) {
        output.write(bytes, 0, read);
        output.flush();
    }
 
	input.close();
    output.close();
  }
 
  private String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
          if (cd.trim().startsWith("filename")) {
			String filename = cd.substring(cd.indexOf('=') + 1);
            return filename.substring(filename.lastIndexOf("\\") + 1).trim().replace("\"", "");
          }
        }
        return null;
      }

	private void encrypt(String srcPath, String destPath, InputStream inStream) {  
		File rawFile = new File(srcPath);  
        File encryptedFile = new File(destPath);
        //InputStream inStream = null;  
        OutputStream outStream = null;  
        try {  
            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);  
           
            //inStream = new FileInputStream(rawFile);  
            outStream = new FileOutputStream(encryptedFile);
            byte[] buffer = new byte[BUFFER_LENGTH];  
            int len;  
            while ((len = inStream.read(buffer, 0, BUFFER_LENGTH)) != -1) {
                outStream.write(cipher.update(buffer, 0, len));  
                outStream.flush();  
            }  
            outStream.write(cipher.doFinal());  
            inStream.close();  
            outStream.close();  
        } catch (IllegalBlockSizeException ex) {  
            System.out.println(ex);  
        } catch (BadPaddingException ex) {  
            System.out.println(ex);  
        } catch (InvalidKeyException ex) {  
            System.out.println(ex);  
        } catch (FileNotFoundException ex) {  
            System.out.println(ex);  
        } catch (IOException ex) {  
            System.out.println(ex);  
        }  
    }
}