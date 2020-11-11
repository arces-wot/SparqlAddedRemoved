package support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import addedremoved.UpdateExtractedData;

public class Cloner {

	
	 public static Object deepCopy(Object object) {
		   try {
		     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		     ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
		     outputStrm.writeObject(object);
		     ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		     ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
		     return objInputStream.readObject();
		   }
		   catch (Exception e) {
		     e.printStackTrace();
		     return null;
		   }
	}
	 
	 public static ArrayList<UpdateExtractedData> deepCopy(ArrayList<UpdateExtractedData>  toClone){
		 ArrayList<UpdateExtractedData> ris = new ArrayList<UpdateExtractedData>();
		 for (UpdateExtractedData updateConstruct : toClone) {
			 ris.add(updateConstruct.clone());
		 }
		 return ris;
	 }
}
