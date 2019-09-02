package application;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

public class ManagerController implements Initializable {
	private Main myApp;			//用于和主控程序关联
    static Statement stmt = null;
    
    
    ObservableList<Goods_show> storage = FXCollections.observableArrayList(); 
    ObservableList<Goods_show> unreg_list = FXCollections.observableArrayList();
    @FXML
    private TableView<Goods_show> table_patreg;
    @FXML
    private Button btn_ok,btn_ok1,btn_clear,btn_exit,btn_exit1,btn_off;
    @FXML
    private ComboBox<String> op_good,op_staff,priv;
    @FXML
    private TextField comno,comname,quantity,price,account,pass,name,id;
    @FXML
    private TableColumn<Goods_show,String>SPBH,SPMC,SPDJ,KCL;
    @FXML
    private Tab tab_good,tab_staff;
	   @Override
	    public void initialize(URL url, ResourceBundle rb) {
		    SPBH.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("merchNo"));  
	    	SPMC.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("merchName"));  
	    	SPDJ.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("price")); 
	    	KCL.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("Stock"));
	    	table_patreg.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    	
	    	op_good.getItems().addAll( "调价", "进货","上架","改名");
	    	op_good.getSelectionModel().select(0);
	    	op_staff.getItems().addAll( "入职","修改密码","修改权限");
	    	op_staff.getSelectionModel().select(0);
	    	priv.getItems().addAll( "无管理员权限", "有管理员权限");
	    	priv.getSelectionModel().select(0);
	    	table_patreg.setItems(storage);
	   }
	   @FXML
	    private void btn_unreg_clicked(MouseEvent event)
	    {
	    	
	    	unreg_list = table_patreg.getSelectionModel().getSelectedItems();
	    	Goods_show patreg;
	    	
	    	for(int i=0;i<unreg_list.size();i++)
	    	{
	    		patreg = unreg_list.get(i);
	    	
	    		try
	    		{
	      	      
	    			 String sql;
	    	  	     stmt = myApp.con.createStatement();	         
	      	         sql = "delete from goods where merchno = '"+patreg.getMerchNo()+"'";
	      	         stmt.executeUpdate(sql);
	      	         //删除不插入记录
	      	         stmt.close();
	      	     }catch(SQLException se){
	      	         // 处理 JDBC 错误
	      	         se.printStackTrace();
	      	         return;
	      	     }
	    	}
	    	
	    	JOptionPane.showMessageDialog(new JFrame().getContentPane(), "下架成功", "提示", JOptionPane.INFORMATION_MESSAGE);
	    	tab_changed(new Event(null));//refresh the gui
	    }

	   
	   @FXML
	    private void tab_changed(Event event)
	    {
	    	String spbh,spmc,spdj,kcl;
	    	if(tab_good.selectedProperty().get())
	    	{
			try
			{
	 	        
	 	        stmt = myApp.con.createStatement();
	  	        String sql;
	  	        ResultSet rs=null;
  			
	     		 
	     		storage.clear();
	     	    sql = "select * from goods";
	     	   	rs = stmt.executeQuery(sql);
	     	   	while(rs.next())
	     	   	    {
	     	   	        	 spbh = rs.getString("MerchNo");
	     	   	             spmc = rs.getString("Name"); 
	     	   	             spdj= rs.getString("price");
	     	   	             kcl = rs.getString("Stocknum");
	     	   	        
	     	   	             storage.add(new Goods_show(spbh,spmc,spdj,"0","0",kcl));
	     	   	     }	    
	     	    rs.close();
	  	        stmt.close();
	  	     }catch(SQLException se){
	  	         // 处理 JDBC 错误
	  	         se.printStackTrace();
	  	     }
	    	}
	    }
	   @FXML
	    private void btn_ok_clicked(MouseEvent event)
	    {
		   int ind=op_good.getSelectionModel().getSelectedIndex();
		   String s1=null,s2=null,s3=null,s4=null;
		   try {
			     myApp.con.setAutoCommit(false);
			 	stmt = myApp.con.createStatement();
	  	        String sql;
		   switch(ind)
		   {
		   case(0):
		   {//set price
			   s1=comno.getText();
			   s4=price.getText();
			   if(s1.length()==6 && s4.length()!=0)
			   {
				   
				  sql="update goods set price="+s4+" where MerchNo='"+s1+"'";
				  stmt.executeUpdate(sql);
				  sql="insert into managegoods values('"+s1+"','"+myApp.dl_account+"','"+new Timestamp(System.currentTimeMillis())+"',0)";
				  stmt.executeUpdate(sql);
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "输入不合法", "提示", JOptionPane.INFORMATION_MESSAGE);
				   return;
			   }
			   break;
		   }
		   case(1):
		   {//进货
			   s1=comno.getText();
			   s3=quantity.getText();

			   if(s1.length()==6 && s3.length()!=0)
			   {

				  sql="update goods set stocknum=stocknum+("+s3+") where MerchNo='"+s1+"'";
				  stmt.executeUpdate(sql);
				  sql="insert into managegoods values('"+s1+"','"+myApp.dl_account+"','"+new Timestamp(System.currentTimeMillis())+"',1)";
				  stmt.executeUpdate(sql);
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "输入不合法", "提示", JOptionPane.INFORMATION_MESSAGE);
				   return;
			   }
			   break;
		   }
		   case(2):
		   {//上架
			   s1=comno.getText();
			   s2=comname.getText();
			   s3=quantity.getText();
			   s4=price.getText();
			   if(s1.length()==6 && (s2.length()*s3.length()*s4.length())!=0)
			   {
				   
				  sql="insert into goods values('"+s1+"','"+s2+"',"+s3+","+s4+")";
				  stmt.executeUpdate(sql);
				  sql="insert into managegoods values('"+s1+"','"+myApp.dl_account+"','"+new Timestamp(System.currentTimeMillis())+"',2)";
				  stmt.executeUpdate(sql);
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "输入不合法", "提示", JOptionPane.INFORMATION_MESSAGE);
				   return;
			   }
			   break;
		   }
		   case(3):
		   {//改名
			   s1=comno.getText();
			   s2=comname.getText();
			
			   if(s1.length()==6 && s2.length()!=0)
			   {
				   
				   sql="update goods set name='"+s2+"' where MerchNo='"+s1+"'";
				  stmt.executeUpdate(sql);
				  sql="insert into managegoods values('"+s1+"','"+myApp.dl_account+"','"+new Timestamp(System.currentTimeMillis())+"',3)";
				  stmt.executeUpdate(sql);
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "输入不合法", "提示", JOptionPane.INFORMATION_MESSAGE);
				   return;
			   }
			   break;
		   }
		   default:
			   return;
		   }
				myApp.con.commit();
	  		    myApp.con.setAutoCommit(true);
		   }
		   catch(SQLException se){
	  	         // 处理 JDBC 错误
			   try {
	    		 	myApp.con.rollback();
	    		 	 myApp.con.setAutoCommit(true);
	    		 	 se.printStackTrace();
	    		 } catch (SQLException e1) {
	    		             e1.printStackTrace();
	    		 }
	  	       JOptionPane.showMessageDialog(new JFrame().getContentPane(), "操作失败", "提示", JOptionPane.INFORMATION_MESSAGE);
	  	     return;
		   }
		   tab_changed(new Event(null));
		   
		   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "操作成功", "提示", JOptionPane.INFORMATION_MESSAGE);
		   comno.clear();
		   comname.clear();
		   quantity.clear();
		   price.clear();
	    }
	   @FXML
	    private void btn_ok1_clicked(MouseEvent event)
	    {
		   int ind=op_staff.getSelectionModel().getSelectedIndex();
		   String s1=null,s2=null,s3=null,s4=null;
		   int p;
		   try {
			     myApp.con.setAutoCommit(false);
			 	stmt = myApp.con.createStatement();
	  	        String sql;
		   switch(ind)
		   {
		   case(1):
		   {//修改密码
			   s1=account.getText();
			   s2=pass.getText();
			 
			   if(s1.length()<=6 && (s1.length()*s2.length())!=0)
			   {
				   
				  sql="update staff set password='"+s2+"' where account='"+s1+"'";
				  stmt.executeUpdate(sql);
				  sql="insert into managestaff values('"+myApp.dl_account+"','"+s1+"','"+new Timestamp(System.currentTimeMillis())+"',1)";
				  stmt.executeUpdate(sql);
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "输入不合法", "提示", JOptionPane.INFORMATION_MESSAGE);
				   myApp.con.setAutoCommit(true);
				   return;
			   }
			   break;
		   }
		
		   case(0):
		   {//入职
			   s1=account.getText();
			   s2=pass.getText();
			   s3=name.getText();
			   s4=id.getText();
			   p=priv.getSelectionModel().getSelectedIndex();
			   if(s1.length()<=6 && (s1.length()*s2.length()*s3.length()*s4.length())!=0 &&s4.length()==18)
			   {
				   
				  sql="insert into staff values('"+s1+"','"+s2+"','"+s3+"','"+s4+"',"+String.valueOf(p)+")";
				  stmt.executeUpdate(sql);
				  sql="insert into managestaff values('"+myApp.dl_account+"','"+s1+"','"+new Timestamp(System.currentTimeMillis())+"',0)";
				  stmt.executeUpdate(sql);
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "输入不合法", "提示", JOptionPane.INFORMATION_MESSAGE);
				   myApp.con.setAutoCommit(true);
				   return;
			   }
			   break;
		   }
		   case(2):
		   {//修改权限
			   s1=account.getText();
			   p=priv.getSelectionModel().getSelectedIndex();
			   
			   if(s1.length()<=6 && s1.length()!=0)
			   {
				   
				  sql="update staff set privilege="+String.valueOf(p)+" where account='"+s1+"'";
				  stmt.executeUpdate(sql);
				  sql="insert into managestaff values('"+myApp.dl_account+"','"+s1+"','"+new Timestamp(System.currentTimeMillis())+"',2)";
				  stmt.executeUpdate(sql);
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "输入不合法", "提示", JOptionPane.INFORMATION_MESSAGE);
				   myApp.con.setAutoCommit(true);
				   return;
			   }
			   break;
		   }
		   default:
			   return;
		   }
				myApp.con.commit();
	  		    myApp.con.setAutoCommit(true);
		   }
		   catch(SQLException se){
	  	         // 处理 JDBC 错误
			   try {
	    		 	myApp.con.rollback();
	    		 	 myApp.con.setAutoCommit(true);
	    		 	 se.printStackTrace();
	    		 } catch (SQLException e1) {
	    		             e1.printStackTrace();
	    		 }
	  	       JOptionPane.showMessageDialog(new JFrame().getContentPane(), "操作失败", "提示", JOptionPane.INFORMATION_MESSAGE);
	  	     return;
		   }
		   JOptionPane.showMessageDialog(new JFrame().getContentPane(), "操作成功", "提示", JOptionPane.INFORMATION_MESSAGE);
		   account.clear();
		   name.clear();
		   pass.clear();
		   id.clear();
	    }
	    
	   
	    
	    @FXML
	    private void btn_exit_clicked(MouseEvent event)
	    {
	    	Event.fireEvent(myApp.primaryStage,
	    		new WindowEvent(myApp.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST ));
	    }
	    
	
	 public void setUp(Main application) {
	        myApp = application;

	    }    
}
