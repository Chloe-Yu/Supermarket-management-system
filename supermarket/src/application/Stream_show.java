
package application;

import javafx.beans.property.SimpleStringProperty;

public class Stream_show
{  
    private final SimpleStringProperty streamNo;  
    private final SimpleStringProperty transTime;  
    private final SimpleStringProperty price;

   
    public Stream_show(String rNum, String pName, String rTime) 
    {  
        this.streamNo = new SimpleStringProperty(rNum);  
        this.transTime = new SimpleStringProperty(pName);  
        this.price = new SimpleStringProperty(rTime);
 
    }  
   
    public String getStreamNo() {  
        return streamNo.get();  
    }  
    public void setStreamNo(String fName) {  
    	streamNo.set(fName);  
    }  
          
    public String getTransTime() {  
        return transTime.get();  
    }  
    public void setTransTime(String fName) {  
    	transTime.set(fName);  
    }  
      
    public String getPrice() {  
        return price.get();  
    }  
    public void setPrice(String fName) {  
    	price.set(fName);  
    }
    

}
          