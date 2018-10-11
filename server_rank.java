import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class server_rank
{
String str,bbb="";
int sr=1;

public void rank()throws Exception
{
	Class.forName("com.mysql.jdbc.Driver");
	int iii=1,count=0;
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_level","root","");
	Statement stmt=con.createStatement();
	String queryy="select * from `orencik_result_search_k1_1000`";
	stmt.executeQuery(queryy);
	ResultSet rs3=stmt.executeQuery(queryy);
	while(rs3.next())
	{	
		String dlist=rs3.getString("DID");
		dlist=dlist.replace("(",",");
		dlist=dlist.replace(")",",");
		String ddlist[]=dlist.split(",");
		count=(ddlist.length)/2;
		System.out.println(count);
		String sort_array[][]=new String[count][2];

		for(int k=0,index=0;index<count;k++,index++)
		{
			sort_array[index][0]=ddlist[k];
			k++;
			sort_array[index][1]=ddlist[k];
		}

		for(int xx=0;xx<count;xx++)
		{
			for(int yy=0;yy<count-xx-1;yy++)
			{
				if(Double.parseDouble(sort_array[yy][1])<Double.parseDouble(sort_array[yy+1][1]))
				{
					String temp=sort_array[yy][0];
					String vv=sort_array[yy][1];
					sort_array[yy][0]=sort_array[yy+1][0];
					sort_array[yy][1]=sort_array[yy+1][1];
					sort_array[yy+1][0]=temp;
					sort_array[yy+1][1]=vv;
				}
			}
		}
		String result="";
		for(int xx=0;xx<count;xx++)
		{	
			result=result+","+sort_array[xx][0]+","+sort_array[xx][1];
		}

		Class.forName("com.mysql.jdbc.Driver");
		Connection con1=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_level","root","");
		Statement stmt1=con1.createStatement();
		String query2="insert into `orencik_server_result_k1_1000`(`Sr`,`Doc_Id`) value ('"+sr+"','"+result+"')";
		sr++;
		stmt1.executeUpdate(query2);
	}
}

public static void main(String args[])throws Exception
{
	String doc_result="";
	server_rank ob=new server_rank();
	ob.rank();
}
}