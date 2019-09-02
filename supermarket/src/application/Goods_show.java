
package application;

import javafx.beans.property.SimpleStringProperty;

public class Goods_show
{  
    private final SimpleStringProperty merchNo;  
    private final SimpleStringProperty merchName;  
    private final SimpleStringProperty price;
    private final SimpleStringProperty num;
    private final SimpleStringProperty tPrice;
    private final SimpleStringProperty Stock;
   
    public Goods_show(String rNum, String pName, String rTime,String rType,String val,String Stock) 
    {  
        this.merchNo = new SimpleStringProperty(rNum);  
        this.merchName = new SimpleStringProperty(pName);  
        this.price = new SimpleStringProperty(rTime);
        this.num = new SimpleStringProperty(rType);
        this.tPrice = new SimpleStringProperty(val);
        this.Stock= new SimpleStringProperty(Stock);
    }  
   
    public String getMerchNo() {  
        return merchNo.get();  
    }  
    public void setMerchNo(String fName) {  
    	merchNo.set(fName);  
    }  
          
    public String getMerchName() {  
        return merchName.get();  
    }  
    public void setMerchName(String fName) {  
    	merchName.set(fName);  
    }  
      
    public String getPrice() {  
        return price.get();  
    }  
    public void setPrice(String fName) {  
    	price.set(fName);  
    }
    
    public String getNum() {  
        return num.get();  
    }  
    public void setNum(String fName) {  
    	num.set(fName);  
    }
    public String getTPrice()
    {
    	return tPrice.get();
    }
    public void setTPrice(String val)
    {
    	tPrice.set(val);
    }
    public String getStock()
    {
    	return Stock.get();
    }
    public void setStock(String val)
    {
    	Stock.set(val);
    }
}
          