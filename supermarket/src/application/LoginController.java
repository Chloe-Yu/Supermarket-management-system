package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.stage.WindowEvent;


public class LoginController  implements Initializable {
	private Main myApp;			//用于和主控程序关联
	//定义界面中的输入栏目
	@FXML
	private ComboBox<String> yhlb;
	@FXML 
    private TextField yhzh;
    @FXML 
    private TextField yhkl;
    @FXML
    private Button qdan;
    @FXML
    private Button tcan;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	yhlb.getItems().addAll( "收银员登录", "管理员登录");
    	yhlb.getSelectionModel().select(0);
    	
    	qdan.setOnAction(e->onqueding(e));
    	tcan.setOnAction(e->quit(e));
    }   
    
      
   
    @FXML
    private void quit(ActionEvent e)  {
    	Event.fireEvent(myApp.primaryStage,
    			new WindowEvent(myApp.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    
    
    
    @FXML
    private void onqueding(ActionEvent event)  {
    	//加载主控界面
		//装载登录界面
    	if(yhzh.getText()==null)
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
           			"账号错误！", "警告", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	
    	String account=yhzh.getText();
    	String pass=yhkl.getText();
    	
    	
    	
    	if(yhlb.getSelectionModel().getSelectedIndex()==0)//收银员
    	{
    		try
    		{
    			Statement stat = myApp.con.createStatement();
    			String sql="SELECT * FROM STAFF WHERE ACCOUNT="+"'"+account+"'";
        		ResultSet res = stat.executeQuery(sql);
        		Boolean b=false;
        		String result=null;
        		
        		if(res.next())
        		{
        			myApp.dl_account=res.getString("account");
        			result=res.getString("password");
        			b=res.getBoolean("privilege");
        			res.close();
        			stat.close();
        		}
        		else
        		{
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	              			"账号错误！", "警告", JOptionPane.WARNING_MESSAGE);
        			res.close();
        			stat.close();
        			return;
        		}
        		
        		if(result.equals(pass))
        		{	
        			if(b==false)
    			{
    				myApp.login_time=new Timestamp(System.currentTimeMillis());
    			}
    			else
    			{
    				JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	              			"不是收银员！", "警告", JOptionPane.WARNING_MESSAGE);
    				return;
    			}
        			
        		
        		}
        		else
        		{
        			
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), "密码错误！", "警告", JOptionPane.WARNING_MESSAGE);
        			return;
        		}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    			return;
    		}
    		
    		yhkl.clear();
    		System.out.println("登录成功。");
    		
    		myApp.brys=1;
    		myApp.login_time=new Timestamp(System.currentTimeMillis());
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Cashier.fxml"));
			Parent root =null;
			try {
			    root = (Parent)loader.load();
			}
		    catch(Exception e) {
			    e.printStackTrace();
			    return;
		    }
			CashierController controller=(CashierController) loader.getController();
			controller.setUp(myApp);
			Scene scene = new Scene(root,600,680);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			myApp.primaryStage.setScene(scene);  //登录后，主控界面可以利用登录界面的Stage，主控界面打开菜单弹出的新界面可以创建新的Stage
			myApp.primaryStage.show();    	
    	}
    	else if(yhlb.getSelectionModel().getSelectedIndex()==1)//doctor
    	{
    		try
    		{
    			Statement stat = myApp.con.createStatement();
    			String sql="SELECT * FROM STAFF WHERE ACCOUNT="+"'"+account+"'";
        		ResultSet res = stat.executeQuery(sql);
        		Boolean b=false;
        		String result=null;
        		if(res.next())
        		{
        			myApp.dl_account=res.getString("account");
        			result=res.getString("password");
        			b=res.getBoolean("privilege");
        			res.close();
        			stat.close();
        		}
        		else
        		{
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	              			"账号错误！", "警告", JOptionPane.WARNING_MESSAGE);
        			res.close();
        			stat.close();
        			return;
        		}
        		
        		if(result.equals(pass))
        		{	
        			if(b==true)
        			{
        				myApp.login_time=new Timestamp(System.currentTimeMillis());
        			}
        			else
        			{
        				JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	              			"无管理员权限！", "警告", JOptionPane.WARNING_MESSAGE);
        				return;
        			}
        		
        		}
        		else
        		{
        			
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), "密码错误！", "警告", JOptionPane.WARNING_MESSAGE);
        			return;
        		}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    			return;
    		}
    		
    		yhkl.clear();
    		System.out.println("登录成功。");
    		
    		myApp.brys=2;
    		myApp.login_time=new Timestamp(System.currentTimeMillis());
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Manager.fxml"));
			Parent root =null;
			try {
			    root = (Parent)loader.load();
			}
		    catch(Exception e) {
			    e.printStackTrace();
			    return;
		    }
			ManagerController controller=(ManagerController ) loader.getController();
			controller.setUp(myApp);
			Scene scene = new Scene(root,600,625);
			
			myApp.primaryStage.setScene(scene);  //登录后，主控界面可以利用登录界面的Stage，主控界面打开菜单弹出的新界面可以创建新的Stage
			myApp.primaryStage.show();    	
    	}
    	else
    	{
    		return;
    	}
		
       
		
	
    } 
    
    public void setUp(Main application) {
        myApp = application;

    }    
}
