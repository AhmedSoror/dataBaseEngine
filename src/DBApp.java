import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.omg.CORBA.OBJ_ADAPTER;

public class DBApp {
	Vector<Table> vecTables;
	
//----------------------------------------------------------------( Initialization )----------------------------------------------------------------
	public void init() {
		vecTables = new Vector<Table>();
	}
// ----------------------------------------------------------------( Create Table )----------------------------------------------------------------

	public void createTable(String strTableName, String strClusteringKeyColumn,
		Hashtable<String, String> htblColNameType) throws DBAppException {
		Hashtable<String, String> htblColNameTypeClone = (Hashtable<String, String>) htblColNameType.clone();
		htblColNameTypeClone.put("TouchDate", "java.util.Date");
		Table newTable = new Table(strTableName, strClusteringKeyColumn, htblColNameTypeClone);
																							
		writeTable(newTable);
		vecTables.add(newTable);
	}

//	----------------------------------------------------------------( Create Bitmapped Index )----------------------------------------------------------------
	public void createBitmapIndex(String strTableName, String strColName) throws DBAppException {
		Table table = loadTable(strTableName);
		table.createBitmapIndex(strColName);
		writeTable(table);
	}

//	----------------------------------------------------------------( Insert into Table )----------------------------------------------------------------
	public void insertIntoTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		if (!check(strTableName, htblColNameValue))
			throw new DBAppException("Type mismatch");
		Hashtable<String, Object> htblColNameValueClone = (Hashtable<String, Object>) htblColNameValue.clone();
		Table table = loadTable(strTableName);
		table.insert(htblColNameValueClone);
		table.incRecordsCount();
		writeTable(table);
	}
	
//	----------------------------------------------------------------( Update Table )----------------------------------------------------------------
	
	public boolean check(String strTableName, Hashtable<String, Object> htblInput) throws DBAppException {
		Hashtable<String, String> htblColType = new Hashtable<>();
		try {
			String currentLine = "";
			String columnName;
			String columnType;
			boolean found = false;
			FileReader fileReader = new FileReader("data/"+strTableName+"metadata.csv");
			BufferedReader br = new BufferedReader(fileReader);
			br.readLine();
			while ((currentLine = br.readLine()) != null) {
				String[] line = currentLine.split(",");
				String tableName = line[0];
				if (tableName.equals(strTableName)) {
					found = true;
					columnName = line[1];
					columnType = line[2];
					htblColType.put(columnName, columnType);

				}
				if (found && !tableName.equals(strTableName))
					break;

			}
			Set<String> colNames = htblInput.keySet();
			for (String s : colNames) {
				Object value = htblInput.get(s);
				if (!htblColType.get(s).equalsIgnoreCase(value.getClass().getName()))
					return false;
			}
			return true;
		} catch (Exception e) {
			throw new DBAppException("column not found");
		}
	}

	
	public static Hashtable<String, Object> update_helper(Hashtable<String, Object> htblRecord, Hashtable<String, Object> htblUpdate) {
		Set<String> colNames = htblUpdate.keySet();
		for (String s : colNames) {
			htblRecord.put(s, htblUpdate.get(s));
		}
		return htblRecord;
	}

	
	public void updateTable(String strTableName, String strKey, Hashtable<String, Object> htblColNameValue)	throws DBAppException {
		if (!check(strTableName, htblColNameValue))
			throw new DBAppException("Type mismatch");
		Hashtable<String, Object> htblColNameValueClone = (Hashtable<String, Object>) htblColNameValue.clone();
		Table table = loadTable(strTableName);
		if(htblColNameValueClone.get(table.getStrClusteringKeyColumn())!=null) {
			Hashtable<String, Object> htblRecordPrimary=new Hashtable<>();
			htblRecordPrimary.put(table.getStrClusteringKeyColumn(),strKey);
			Hashtable<String , Object> htblOld = table.get(htblRecordPrimary);
			if(htblOld==null)return;
			table.delete(htblOld);
			htblRecordPrimary = update_helper(htblOld, htblColNameValueClone);
			table.insert(htblRecordPrimary);
			
		}
		else {
			table.update(strKey, htblColNameValueClone);
			writeTable(table);
		}
	}
//	----------------------------------------------------------------( Delete from Table )----------------------------------------------------------------
	public void deleteFromTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		if (!check(strTableName, htblColNameValue))
			throw new DBAppException("Type mismatch");
		Hashtable<String, Object> htblColNameValueClone = (Hashtable<String, Object>) htblColNameValue.clone();
		Table table = loadTable(strTableName);
		table.delete(htblColNameValueClone);
		table.decRecordsCount();
		writeTable(table);
	}
//	----------------------------------------------------------------( Iterator )----------------------------------------------------------------
	/*
	 * public Iterator selectFromTable(SQLTerm[] arrSQLTerms,String[] strarrOperators)// throws DBAppException { return null; }
	 */
	
//	-----------------------------------------------------( Write & Read table )----------------------------------------------------------------
	
	public boolean writeTable(Table table) { // ******** */
		try {
			FileOutputStream fileOut = new FileOutputStream("data/" + table.getStrTableName() + ".class");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(table);
			objectOut.close();
			return true;

		} catch (Exception ex) {
			return false;
		}
	}
	
	public Table loadTable(String strTableName) { // ********//
		try {
			FileInputStream filein = new FileInputStream("data/" + strTableName + ".class");
			ObjectInputStream objectin = new ObjectInputStream(filein);
			Table table = (Table) objectin.readObject();
			objectin.close();
			return table;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
