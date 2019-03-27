
public class SQLTerm {
	String _strTableName;
	String _strColumnName;
	String _strOperator;
	Object _objValue;
	String _evaluation="";
	
	public SQLTerm(String strTableName,String strColumnName,String strOperator,	Object objValue) throws DBAppException {
		strOperator=strOperator.trim();
		// Operator Inside SQLTerm can either be >, >=, <, <=, != or =
		if(!(strOperator.equals(">")||strOperator.equals(">=")||strOperator.equals("<")||strOperator.equals("<=")||strOperator.equals("!=")||strOperator.equals("="))) {
			throw new DBAppException("invalid operator");
		}
		//java.lang.Integer, java.lang.String,java.lang.Double, java.lang.Boolean and java.util.Date
		if(!(objValue.getClass().equals("java.lang.Integer")||objValue.getClass().equals("java.lang.String")||objValue.getClass().equals("java.lang.Double")||objValue.getClass().equals("java.lang.Boolean")||objValue.getClass().equals("java.util.Date"))) {
			throw new DBAppException("invalid data type");
		}

		this._strTableName=strTableName;
		this._strColumnName=strColumnName;
		this._strOperator=strOperator;
		this._objValue=objValue;
	}

	public SQLTerm() {
		// TODO Auto-generated constructor stub
	}
	
}
