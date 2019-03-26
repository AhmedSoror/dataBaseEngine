import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {
	public static void main(String[] args) {
		/*
		Properties prop=new Properties();
		try {
			FileInputStream propertiesFile= new FileInputStream("config.properties");
			prop.load(propertiesFile);
			System.out.println(prop.getProperty("MaximumRowsCountinPage"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public int getMaxRowCountinPage() throws IOException {
		Properties prop=new Properties();
		FileInputStream propertiesFile= new FileInputStream("config.properties");
		prop.load(propertiesFile);
		return(Integer.parseInt(prop.getProperty("MaximumRowsCountinPage")));
	}
	
	
}
