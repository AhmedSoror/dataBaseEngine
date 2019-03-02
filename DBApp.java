import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.OBJ_ADAPTER;

public class DBApp {
	Vector<Table> vecTables;
	
	
	// this does whatever initialization you would like or leave it empty if there is no code you want to execute at application startup
	public void init(){
		vecTables=new Vector<Table>();
	}
	
	public void createTable(String strTableName,String strClusteringKeyColumn,Hashtable<String,String> htblColNameType ) //	throws DBAppException
	{	
		Hashtable<String,String> htblColNameTypeClone=(Hashtable<String, String>)htblColNameType.clone(); 
		htblColNameTypeClone.put("TouchDate", "java.util.Date");
		Table newTable=new Table(strTableName, strClusteringKeyColumn, htblColNameTypeClone);				//unique table names????????????????
		vecTables.add(newTable);		
		System.out.println("created table \t"+this);
	}

	public void createBitmapIndex(String strTableName,String strColName)// throws DBAppException
	{
		
	}
	public void insertIntoTable(String strTableName,Hashtable<String,Object>htblColNameValue)//throws DBAppException
	{	
		Hashtable<String,Object>htblColNameValueClone=(Hashtable<String, Object>) htblColNameValue.clone();
		for(Table table : vecTables) {
			if(table.getStrTableName().equals(strTableName)) {
				table.insert(htblColNameValueClone);
				break;
			}
		}
//		System.out.println("inserted into table \t"+this);
	}
	public void updateTable(String strTableName,String strKey,Hashtable<String,Object> htblColNameValue	) throws Exception//	throws DBAppException
	{		Hashtable<String,Object>htblColNameValueClone=(Hashtable<String, Object>) htblColNameValue.clone();
		for(Table table : vecTables) {
			if(table.getStrTableName().equals(strTableName)) {
				table.update(strKey, htblColNameValue);
				break;
			}
		}


	}



	public void deleteFromTable(String strTableName,Hashtable<String,Object> htblColNameValue)//throws DBAppException
	{		Hashtable<String,Object>htblColNameValueClone=(Hashtable<String, Object>) htblColNameValue.clone();
		for(Table table : vecTables) {
			if(table.getStrTableName().equals(strTableName)) {
				table.delete(htblColNameValueClone);
				break;
			}
		}
	}
	
	/*
	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,String[] strarrOperators)//	throws DBAppException
	{
		return null;
	}
	*/
	public static void main(String[] args) throws Exception{
		DBApp test=new DBApp();
		test.init();
		
//		create a new table 
		
		String strTableName = "Student";
		Hashtable <String, String> htblColNameType = new Hashtable<String, String>( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		test.createTable( strTableName, "id", htblColNameType );
		
		
//		insert into table
		Hashtable <String, Object> htblColNameValue = new Hashtable  <String, Object>( );
		htblColNameValue.put("id", new Integer( 5));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		test.insertIntoTable( strTableName , htblColNameValue );
		
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 3));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		test.insertIntoTable( strTableName , htblColNameValue );

	
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 4 ));
		htblColNameValue.put("name", new String("Dalia Noor" ) );
		htblColNameValue.put("gpa", new Double( 1.25 ) );
		test.insertIntoTable( strTableName , htblColNameValue );
	
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 1 ));
		htblColNameValue.put("name", new String("John Noor" ) );
		htblColNameValue.put("gpa", new Double( 1.5 ) );
		test.insertIntoTable( strTableName , htblColNameValue );
		
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 3 ));
		htblColNameValue.put("name", new String("Zaky Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.88 ) );
		test.insertIntoTable( strTableName , htblColNameValue );

		System.out.println("inserted into table \t"+test);


		// htblColNameValue.clear( );
		// htblColNameValue.put("id", new Integer( 5 ) );
		// test.deleteFromTable(strTableName, htblColNameValue);


		
		htblColNameValue.clear( );
		htblColNameValue.put("name", new String( "BO-G" ) );
		test.updateTable(strTableName,"5", htblColNameValue);

		System.out.println("updated into table \t"+test);

	}
	
	public String toString() {
		return vecTables.get(0).toString();
	}
	
}



