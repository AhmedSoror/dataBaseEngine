import java.util.Hashtable;

public class DBAppTest {

	public static void main(String[] args) throws Exception {
		DBApp test = new DBApp();
		test.init();

//
		String strTableName1 = "Student";
		Hashtable<String, String> htblColNameType1 = new Hashtable<String, String>();
		Hashtable<String, Object> htblColNameValue1 = new Hashtable<String, Object>();
///*
 
//		create a new table 
		
		htblColNameType1.put("id", "java.lang.Integer");
		htblColNameType1.put("name", "java.lang.String");
		htblColNameType1.put("gpa", "java.lang.double");
		test.createTable(strTableName1, "id", htblColNameType1);
//		System.out.println("created \t"+test);

//		insert data		
		
		htblColNameValue1.put("id", new Integer(10));
		htblColNameValue1.put("name", new String("Ahmed Noor"));
		htblColNameValue1.put("gpa", new Double(3.14));
		test.insertIntoTable(strTableName1, htblColNameValue1);

		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(20));
		htblColNameValue1.put("name", new String("Ahmed Noor"));
		htblColNameValue1.put("gpa", new Double(0.95));
		test.insertIntoTable(strTableName1, htblColNameValue1);

		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(30));
		htblColNameValue1.put("name", new String("Dalia Noor"));
		htblColNameValue1.put("gpa", new Double(1.25));
		test.insertIntoTable(strTableName1, htblColNameValue1);

		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(40));
		htblColNameValue1.put("name", new String("John Noor"));
		htblColNameValue1.put("gpa", new Double(1.5));
		test.insertIntoTable(strTableName1, htblColNameValue1);

		htblColNameValue1.clear();
		htblColNameValue1.put("id", new Integer(50));
		htblColNameValue1.put("name", new String("Zaky Noor"));
		htblColNameValue1.put("gpa", new Double(0.88));
		test.insertIntoTable(strTableName1, htblColNameValue1);
////*/
//		
//		
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
		
		htblColNameValue.put("id", new Integer(10));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", new Double(3.14));
		test.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(20));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", new Double(0.95));
		test.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(30));
		htblColNameValue.put("name", new String("John Noor"));
		htblColNameValue.put("gpa", new Double(1.25));
		test.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(40));
		htblColNameValue.put("name", new String("John Noor"));
		htblColNameValue.put("gpa", new Double(1.5));
		test.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(50));
		htblColNameValue.put("name", new String("Zaky Noor"));
		htblColNameValue.put("gpa", new Double(0.88));
		test.insertIntoTable(strTableName, htblColNameValue);
//*/
//		delete 
		
		htblColNameValue.clear();
		htblColNameValue.put("name", new String("John Noor"));
		test.deleteFromTable(strTableName, htblColNameValue);
		System.out.println("delete:  " + test);

//		update
		
		htblColNameValue.clear();
		htblColNameValue.put("name", new String("new Name"));
		test.updateTable(strTableName, "10", htblColNameValue);
		System.out.println("updated into table \t" + test);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(40));
		test.updateTable(strTableName, "100", htblColNameValue);
		System.out.println("updated into table \t" + test);
		
		test.createBitmapIndex(strTableName1, "id");
	}

}




