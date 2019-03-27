import java.util.Hashtable;
import java.util.Iterator;

public class DBAppTest {

	public static void main(String[] args) throws Exception {
		DBApp test = new DBApp();
		test.init();

		String strTableName1 = "Student";
		Hashtable<String, String> htblColNameType1 = new Hashtable<String, String>();
		Hashtable<String, Object> htblColNameValue1 = new Hashtable<String, Object>();
 
		
		htblColNameType1.put("id", "java.lang.Integer");
		htblColNameType1.put("name", "java.lang.String");
		htblColNameType1.put("gpa", "java.lang.double");
		test.createTable(strTableName1, "id", htblColNameType1);

//--1		
		htblColNameValue1.put("id", new Integer(10));
		htblColNameValue1.put("name", new String("Ali Noor"));
		htblColNameValue1.put("gpa", new Double(3.14));
		test.insertIntoTable(strTableName1, htblColNameValue1);
//--2
		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(20));
		htblColNameValue1.put("name", new String("Dalia Noor"));
		htblColNameValue1.put("gpa", new Double(0.95));
		test.insertIntoTable(strTableName1, htblColNameValue1);
//--3
		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(30));
		htblColNameValue1.put("name", new String("Ahmed Noor"));
		htblColNameValue1.put("gpa", new Double(1.25));
		test.insertIntoTable(strTableName1, htblColNameValue1);
//--4
		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(40));
		htblColNameValue1.put("name", new String("Ahmed Noor"));
		htblColNameValue1.put("gpa", new Double(1.5));
		test.insertIntoTable(strTableName1, htblColNameValue1);
//--5
		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(60));
		htblColNameValue1.put("name", new String("John Noor"));
		htblColNameValue1.put("gpa", new Double(0.98));
		test.insertIntoTable(strTableName1, htblColNameValue1);

//--6		
		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(150));
		htblColNameValue1.put("name", new String("Zaky Noor"));
		htblColNameValue1.put("gpa", new Double(0.88));
		test.insertIntoTable(strTableName1, htblColNameValue1);
		

		htblColNameValue1.clear();
		htblColNameValue1.put("name", new String("Ali Noor"));
		test.deleteFromTable(strTableName1, htblColNameValue1);

		test.createBitmapIndex(strTableName1, "name");

		String strTableName = "Employee";
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
//		
//		create table 
//		/*
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		test.createTable(strTableName, "id", htblColNameType);

//		insert data
//--1		
		htblColNameValue.put("id", new Integer(10));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", new Double(3.14));
		test.insertIntoTable(strTableName, htblColNameValue);
		//--2
		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(20));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", new Double(0.95));
		test.insertIntoTable(strTableName, htblColNameValue);
		//--3
		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(30));
		htblColNameValue.put("name", new String("John Noor"));
		htblColNameValue.put("gpa", new Double(1.25));
		test.insertIntoTable(strTableName, htblColNameValue);
		//--4
		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(40));
		htblColNameValue.put("name", new String("John Noor"));
		htblColNameValue.put("gpa", new Double(1.5));
		test.insertIntoTable(strTableName, htblColNameValue);
		//--5
		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(50));
		htblColNameValue.put("name", new String("Zaky Noor"));
		htblColNameValue.put("gpa", new Double(0.88));
		test.insertIntoTable(strTableName, htblColNameValue);
//*/
//		delete 
		//--4
//		htblColNameValue.clear();
//		htblColNameValue.put("name", new String("John Noor"));
//		test.deleteFromTable(strTableName, htblColNameValue);
//
////		update
//		//--4
//		htblColNameValue.clear();
//		htblColNameValue.put("name", new String("new Name"));
//		test.updateTable(strTableName, "10", htblColNameValue);
//
//		htblColNameValue.clear();
//		htblColNameValue.put("id", new Integer(40));
//		test.updateTable(strTableName, "100", htblColNameValue);
//		
		
		SQLTerm[] arrSQLTerms;
		arrSQLTerms = new SQLTerm[2];
		arrSQLTerms[0]=new SQLTerm();
		arrSQLTerms[1]=new SQLTerm();
		arrSQLTerms[0]._strTableName = "Student";
		arrSQLTerms[0]._strColumnName="name";
		arrSQLTerms[0]._strOperator ="=";
		arrSQLTerms[0]._objValue="John Noor";
		
		arrSQLTerms[1]._strTableName = "Student";
		arrSQLTerms[1]._strColumnName="gpa";
		arrSQLTerms[1]._strOperator ="=";
		arrSQLTerms[1]._objValue=new Double( 1.5 );
		String[]strarrOperators = new String[1];
		strarrOperators[0] = "OR";
		Iterator resultSet = test.selectFromTable(arrSQLTerms , strarrOperators);
		
		while(resultSet.hasNext()) {
			System.out.println(resultSet.next());
		}
	}

}




