package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.sql.*;


public class Main extends Application {
	//数据库连接信息
    String strCon = "jdbc:mysql://localhost:3306/supermarket?useSSL=false&serverTimezone=Asia/Shanghai";
	String strUserName = "root"; 	// 数据库的用户名称
	String strPWD = "yyx416"; // 数据库的密码
	public java.sql.Connection con =null;	
	public short brys=0; 		//等于0时无人登录，等于1时为收银员登录，等于2为系统管理员登录
	public String dl_account=null;
	public Timestamp login_time=null;
	//病人基本信息
	
	//public Cashier cash=null;  	//登录的收银员信息
	public Stage primaryStage=null;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage=primaryStage;
			//连接数据库
			Class.forName("com.mysql.cj.jdbc.Driver");
            con = java.sql.DriverManager.getConnection(strCon, strUserName, strPWD);
            if(con!=null ) System.out.println("已顺利连接到数据库。");		
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);//隔离级别设置为串行化
			//装载登录界面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
			Parent root = (Parent)loader.load();
			LoginController controller=(LoginController) loader.getController();
	        controller.setUp(this);
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
