package application;

import java.math.BigDecimal;
import java.sql.*;


public class VIP {
	public String cardNo=null;
	public String phoneNum=null;
	public Timestamp startDate=null;
    public BigDecimal amount;
    
    

    public VIP(String cardNo, String phoneNum,Timestamp GHRS,BigDecimal GHFY) {
    	this.cardNo=new String(cardNo);
    	this.phoneNum=new String(phoneNum);
    	this.startDate=GHRS;
    	this.amount=GHFY;
    }
    public VIP() {
    	
    }
    public VIP(ResultSet rsys) {
    	try {
    	    this.cardNo=new String(rsys.getString("CardNo"));
    	    this.phoneNum=new String(rsys.getString("PhoneNum"));
    	    this.startDate=rsys.getTimestamp("StartDate");    	
    	    this.amount=rsys.getBigDecimal("Amount");
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    }    
    public boolean equals(Object o)
    {
    	if(o==null)
    	{
    		return false;
    	}
    	else 
    	{
    		if(o instanceof VIP)
    		{
    			VIP ks=(VIP)o;
    			if(ks.cardNo.equals(this.cardNo))
    				return true;
    		}
    	}
    	return false;
    }
}
