import java.util.Hashtable;

public class test {
	public static void main(String[] args) {
		
		Hashtable<String,Integer> htbl = new Hashtable<>();
		htbl.put("id", 1);
		htbl.put("age", 1);
		htbl.put("size", 1);
		System.out.println(htbl.keySet());
		
		
		Hashtable <String, Object> htblColNameValue = new Hashtable  <String, Object>( );
		htblColNameValue.put("id", new Integer( 2343432 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		
		// System.out.println(htblColNameValue.get("id"));
		
		System.out.println("hello world");
		String className="java.lang.Integer";
		try {
			Class c=Class.forName(className);
			c.newInstance();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
	}
}
