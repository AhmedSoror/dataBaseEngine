import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.omg.CORBA.OBJ_ADAPTER;

public class DBApp {
	Vector<Table> vecTables;
	
	
	// this does whatever initialization you would like or leave it empty if there is no code you want to execute at application startup
	public void init() {
		vecTables=new Vector<Table>();
		try{
		FileWriter fileWriter=new FileWriter("data/metadata.csv",true);
		fileWriter.write("");
		}
		catch (Exception e){

		}
	}
	
	public void createTable(String strTableName,String strClusteringKeyColumn,Hashtable<String,String> htblColNameType ) 	throws DBAppException
	{	
		Hashtable<String,String> htblColNameTypeClone=(Hashtable<String, String>)htblColNameType.clone(); 
		htblColNameTypeClone.put("TouchDate", "java.util.Date");
		Table newTable=new Table(strTableName, strClusteringKeyColumn, htblColNameTypeClone);				//unique table names????????????????
		writeTable(newTable);
		vecTables.add(newTable);		
		System.out.println("created table \t"+this);
	}

	public void createBitmapIndex(String strTableName,String strColName) throws DBAppException
	{
		
	}
	public void insertIntoTable(String strTableName,Hashtable<String,Object>htblColNameValue)throws DBAppException
	{	
		if(!check(strTableName, htblColNameValue)) throw new DBAppException("Type mismatch"); 
		Hashtable<String,Object>htblColNameValueClone=(Hashtable<String, Object>) htblColNameValue.clone();
		// for(Table table : vecTables) {
		// 	if(table.getStrTableName().equals(strTableName)) {
		// 		table.insert(htblColNameValueClone);
				
		// 		break;
		// 	}
		Table table =loadTable(strTableName);
		table.insert(htblColNameValueClone);
		writeTable(table);
		// }
//		System.out.println("inserted into table \t"+this);
	}
	public void updateTable(String strTableName,String strKey,Hashtable<String,Object> htblColNameValue	) 	throws DBAppException
	{		
		if(!check(strTableName, htblColNameValue)) throw new DBAppException("Type mismatch"); 
		Hashtable<String,Object>htblColNameValueClone=(Hashtable<String, Object>) htblColNameValue.clone();
		// for(Table table : vecTables) {
		// 	if(table.getStrTableName().equals(strTableName)) {
		// 		table.update(strKey, htblColNameValue);
		// 		break;
		// 	}
		// }
		Table table =loadTable(strTableName);
		table.update(strKey, htblColNameValue);
		writeTable(table);
	}



	public void deleteFromTable(String strTableName,Hashtable<String,Object> htblColNameValue)throws DBAppException
	{		
		if(!check(strTableName, htblColNameValue)) throw new DBAppException("Type mismatch"); 
		Hashtable<String,Object>htblColNameValueClone=(Hashtable<String, Object>) htblColNameValue.clone();
		// for(Table table : vecTables) {
		// 	if(table.getStrTableName().equals(strTableName)) {
		// 		table.delete(htblColNameValueClone);
		// 		break;
		// 	}
		// }
		Table table =loadTable(strTableName);
		table.delete(htblColNameValueClone);
		writeTable(table);
	}
	
	
	/*
	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,String[] strarrOperators)//	throws DBAppException
	{
		return null;
	}
	*/
	public  boolean check (String strTableName,Hashtable<String,Object>htblInput) throws DBAppException{
		Hashtable<String , String> htblColType  = new Hashtable<>();
		try{
			String currentLine="";
			String columnName;
			String columnType;
			boolean found = false;
			FileReader fileReader= new FileReader("data/metadata.csv");
			BufferedReader br = new BufferedReader(fileReader);
			while ((currentLine = br.readLine()) != null) {
				String[] line =currentLine.split(",");
				String tableName = line[0];
				if(tableName.equals(strTableName)) {
					found=true;
					columnName = line[1];
					columnType = line[2];
					htblColType.put(columnName, columnType);
					
				}
				if(found && !tableName.equals(strTableName))break;

			}
				Set<String> colNames=htblInput.keySet();
				for(String s :colNames) {
					Object value = htblInput.get(s);
					if(!htblColType.get(s).equalsIgnoreCase(value.getClass().getName()))	return false;
				}
				return true;
		}
			catch (Exception e){
				throw new DBAppException(e.getMessage());
			}
	}
	

// dont forget to not clear meta data file in init	***********************************************

public boolean writeTable(Table table){					//******** */
	try {
		FileOutputStream fileOut = new FileOutputStream("data/"+table.getStrTableName()+".ser");
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		objectOut.writeObject(table);
		objectOut.close();
		return true;

	} catch (Exception ex) {
		return false;
	}
}
public Table loadTable(String strTableName){								//********//
	try {
		FileInputStream filein = new FileInputStream("data/"+strTableName+".ser");
		ObjectInputStream objectin = new ObjectInputStream(filein);
		Table table =(Table)objectin.readObject();
		objectin.close();
		return table;

	} catch (Exception ex) {
		return null;
	}
}






	public static void main(String[] args) throws Exception{
		DBApp test=new DBApp();
		test.init();
		
//		create a new table 
		String strTableName = "Student";
		Hashtable <String, String> htblColNameType = new Hashtable<String, String>( );
		Hashtable <String, Object> htblColNameValue = new Hashtable  <String, Object>( );
		/*
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		test.createTable( strTableName, "id", htblColNameType );
		//System.out.println("created \t"+test);
		
		
		// insert into table
		htblColNameValue.put("id", new Integer(10));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double(3.14) );
		test.insertIntoTable( strTableName , htblColNameValue );
	 	
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 20));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		test.insertIntoTable( strTableName , htblColNameValue );

	
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 30));
		htblColNameValue.put("name", new String("Dalia Noor" ) );
		htblColNameValue.put("gpa", new Double( 1.25 ) );
		test.insertIntoTable( strTableName , htblColNameValue );
	
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 40 ));
		htblColNameValue.put("name", new String("John Noor" ) );
		htblColNameValue.put("gpa", new Double( 1.5 ) );
		test.insertIntoTable( strTableName , htblColNameValue );
		
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 50 ));
		htblColNameValue.put("name", new String("Zaky Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.88 ) );
		test.insertIntoTable( strTableName , htblColNameValue );

		System.out.println("inserted into table \t"+test);
		*/

/*
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 5 ));
		test.deleteFromTable(strTableName, htblColNameValue);

		htblColNameValue.clear( );
		htblColNameValue.put("name", new String( "BO-G" ) );
		test.updateTable(strTableName,"5", htblColNameValue);

		System.out.println("updated into table \t"+test);
*/
		/*
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer(20));
		test.deleteFromTable(strTableName, htblColNameValue);
		 */
		
		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer( 20));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		test.insertIntoTable( strTableName , htblColNameValue );
		System.out.println(test);
	}
	
	public String toString() {
		return loadTable("Student").toString();
	}
	
}

