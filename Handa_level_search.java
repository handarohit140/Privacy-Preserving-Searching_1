/*
Code to perform conjunctive searching by varying the number of clusters. Documents range from 1000 to 10000.
Dummy keywords are 40.
Per query dummy keywords are 10.
d=6.
*/


import java.io.*;
import java.security.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;
import java.net.*;
import java.util.Random;

class Handa_level_search
{
public static void main(String args[])throws Exception
{
	int ik=0;
	CKS_level_search_detail1 ob=new CKS_level_search_detail1();
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_level","root","");
	Statement stmt=con.createStatement();
	String q1="select * from `query_keyword5`";		//change
	ResultSet rs=stmt.executeQuery(q1);
	while(rs.next())
	{
		ik++;
		String keyword=rs.getString("index");
		ob.cluster_search(keyword,ik);
	}

}
}

class CKS_level_search_detail1
{
public long startTime1=0,endTime1=0,xtime=0,count=0;
String bbb="";
public void cluster_search(String keyword_index,int ik)throws Exception
{
	bbb="";
	Class.forName("com.mysql.jdbc.Driver");
	Connection con1=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_level","root","");
	Statement stmt1=con1.createStatement();
	String q1="select * from `cluster_index`";
	int res=0,res1=0;
	String cid="",cluster_index="";
	int count=0;
	ResultSet rs1=stmt1.executeQuery(q1);
	while(rs1.next())
	{
		cid=rs1.getString("cid");	
		cluster_index=rs1.getString("Cluster_Index");
		res=compare_index(keyword_index,cluster_index);
		count++;
		xtime+=endTime1-startTime1;
		if(res==1)
		{
			search(keyword_index,cid);
			//break;	 remove break if the case is hard clustering, i.e., multiple matching clusters are required.
		}
	}
	put_result(bbb,ik);
}

public void search(String keyword_index,String cid)throws Exception
{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con1=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_level","root","");
	Statement stmt1=con1.createStatement();
	Statement stmt2=con1.createStatement();
	Statement stmt21=con1.createStatement();
	Statement stmt3=con1.createStatement();
	Statement stmt4=con1.createStatement();
	String q1="select * from `index_1000_level1` where `cid`='"+cid+"'";
	int res=0,res1=0,res2=0,res3=0,res4=0;
	Double val;
	String did="",index="";
	int count=0;
	ResultSet rs1=stmt1.executeQuery(q1);
	while(rs1.next())
	{
		did=rs1.getString("DID");	
		index=rs1.getString("Doc_Index");
		String q21="select * from `cluster_sim_level1` where `Doc_ID`='"+did+"'";
		ResultSet rs21=stmt21.executeQuery(q21);
		rs21.next();
		val=Double.parseDouble(rs21.getString("Sim"));
		val=(double) Math.round(val * 100.0)/100.0;

		res=compare_index(keyword_index,index);
		count++;
		
		xtime+=endTime1-startTime1;
		if(res==1)
		{
			String q2="select * from `index_1000_level2` where `did`='"+did+"'";
			ResultSet rs2=stmt2.executeQuery(q2);
			rs2.next();
			index=rs2.getString("Doc_Index");
			res1=compare_index(keyword_index,index);
			if(res1==1)
			{
				String q3="select * from `index_1000_level3` where `did`='"+did+"'";
				ResultSet rs3=stmt3.executeQuery(q3);
				rs3.next();
				index=rs3.getString("Doc_Index");
				res2=compare_index(keyword_index,index);
				if(res2==1)
				{
					String q4="select * from `index_1000_level4` where `did`='"+did+"'";
					ResultSet rs4=stmt4.executeQuery(q4);
					rs4.next();
					index=rs4.getString("Doc_Index");
					res3=compare_index(keyword_index,index);
					if(res3==1)
					{
						val=3+val;
						bbb+=did+"("+val+")\t";
					}
					else
					{
						val=2+val;
						bbb+=did+"("+val+")\t";
					}
				}
				else
				{
					val=1+val;
					bbb+=did+"("+val+")\t";
				}
			}
			else
			{
				bbb+=did+"("+val+")\t";
			}

		}
	}
}
public void put_result(String bbb, int iii)throws Exception
{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con2=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_level","root","");
	Statement stmt2=con2.createStatement();
	String queryy="insert into `handa_result_search_k5_1000`(`Sr`,`time`,`DID`) value ('"+iii+"','"+xtime+"','"+bbb+"')";		//change
	stmt2.executeUpdate(queryy);
}
public int compare_index(String input,String index)
{
	int i,like=0,like1=0;
	char array1[]=new char[448];
	char array2[]=new char[448];
	array1=input.toCharArray();
	array2=index.toCharArray();
	startTime1 = System.currentTimeMillis();
	for(i=0;i<448;i++)
	{
		if(array1[i]=='0')
		{
			like++;
			if(array2[i]=='0')
				like1++;
		}
	}
	endTime1 = System.currentTimeMillis();			
	xtime+=endTime1-startTime1;
	
	if(like==like1)
		return 1;
	else
		return 0;
}
}