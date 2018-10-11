import java.io.*;
import java.security.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;
import java.net.*;

class PTSearch
{
public static void main(String args[])throws Exception
{
Search ob=new Search();
ob.find();
}
}

class Search
{
public void find()throws Exception
{
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse","root","");
	Connection con2=DriverManager.getConnection("jdbc:mysql://localhost:3306/ptsearch","root","");
	Statement stmt=con.createStatement();
	Statement stmt1=con.createStatement();
	String query="select * from `keyword5`";
	ResultSet rs=stmt.executeQuery(query);
	int k=0;
	while(rs.next())
	{
		String fres="";
		String db_did="";
		String keyword1=rs.getString("Keyword1");
		String keyword2=rs.getString("Keyword2");
		String keyword3=rs.getString("Keyword3");
		String keyword4=rs.getString("Keyword4");
		String keyword5=rs.getString("Keyword5");
		String query1="select * from `keywords_10000`";
		ResultSet rs1=stmt1.executeQuery(query1);
		while(rs1.next())
		{	
			db_did=rs1.getString("Doc");
			k=0;
			for(int i=1;i<=100;i++)
			{	
				String check="Key"+i;
				String db_keyword=rs1.getString(check);	
				
				if(keyword1.equals(db_keyword)||keyword2.equals(db_keyword)||keyword3.equals(db_keyword)||keyword4.equals(db_keyword)||keyword5.equals(db_keyword))
				//if(keyword1.equals(db_keyword)||keyword2.equals(db_keyword)||keyword3.equals(db_keyword)||keyword4.equals(db_keyword))
				//if(keyword1.equals(db_keyword)||keyword2.equals(db_keyword)||keyword3.equals(db_keyword))
				//if(keyword1.equals(db_keyword)||keyword2.equals(db_keyword))
				//if(keyword1.equals(db_keyword))
					k++;
			}
			if(k>3)
				fres=fres+"\t"+db_did;
		
		}
	
			//System.out.println(fres);
			String query2="insert into `ptsearch_result_search_k5_10000`(`DID`,`time`) values('"+fres+"','0');";
			Statement stmt2=con2.createStatement();
			stmt2.executeUpdate(query2);
	}
}
}
