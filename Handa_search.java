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

class Handa_search
{
public static void main(String args[])throws Exception
{
int ik=0;
	CKS_search_detail1 ob=new CKS_search_detail1();
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_soft","root","111111");
	Statement stmt=con.createStatement();
	String q1="select * from `query_keyword5`";
	ResultSet rs=stmt.executeQuery(q1);
	while(rs.next())
	{
		ik++;
		String keyword=rs.getString("index");
		ob.cluster_search(keyword,ik);
	}
con.close();
}
}

class CKS_search_detail1
{
public long startTime1=0,endTime1=0,xtime=0,count=0;
String bbb="";
public void cluster_search(String keyword_index,int ik)throws Exception
{
	bbb="";
	Class.forName("com.mysql.jdbc.Driver");
	Connection con1=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_soft","root","111111");
	Statement stmt1=con1.createStatement();
	String q1="select * from `uniform_cluster_index` order by `cid`";
	int res=0,res1=0;
	String cid="",cluster_index="",cid1="";
	int count=0;
	ResultSet rs1=stmt1.executeQuery(q1);
	while(rs1.next())
	{
		cid=rs1.getString("cid");	
		if(cid1.equals(cid))continue;
		cluster_index=rs1.getString("Cluster_Index");
		res=compare_index(keyword_index,cluster_index);
		count++;
		//if(res==1)
		//{
		//	search(keyword_index,cid);
			//break;	remove break if the case is hard clustering, i.e., multiple matching clusters are required.
		//	cid1=String.valueOf(cid);
		//}
	}
	put_result(bbb,ik);
con1.close();
}

public void search(String keyword_index,String cid)throws Exception
{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con1=DriverManager.getConnection("jdbc:mysql://localhost:3306/cbse_soft","root","111111");
	Statement stmt1=con1.createStatement();
	String q1="select * from `uniform_cbse_index_6000` where `cid`='"+cid+"'";
	int res=0;
	String did="",index="";
	int count=0;
	ResultSet rs1=stmt1.executeQuery(q1);
	while(rs1.next())
	{
		
		did=rs1.getString("DID");	
		index=rs1.getString("Doc_Index");
		res=compare_index(keyword_index,index);
		count++;
		xtime+=endTime1-startTime1;
		if(res==1)
		{
			bbb+=did+"\t";
		}
	}
con1.close();
}
public void put_result(String bbb, int iii)throws Exception
{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con2=DriverManager.getConnection("jdbc:mysql://localhost:3306/uniform","root","111111");
	Statement stmt2=con2.createStatement();
	String queryy="insert into `handa_result_search_k5_6000`(`Sr`,`time`,`DID`) value ('"+iii+"','"+xtime+"','"+bbb+"')";
	stmt2.executeUpdate(queryy);
	if(iii>=200)
	System.out.println(xtime);
con2.close();
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
