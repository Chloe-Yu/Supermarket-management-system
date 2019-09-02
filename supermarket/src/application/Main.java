package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.sql.*;


public class Main extends Application {
	//���ݿ�������Ϣ
    String strCon = "jdbc:mysql://localhost:3306/supermarket?useSSL=false&serverTimezone=Asia/Shanghai";
	String strUserName = "root"; 	// ���ݿ���û�����
	String strPWD = "yyx416"; // ���ݿ������
	public java.sql.Connection con =null;	
	public short brys=0; 		//����0ʱ���˵�¼������1ʱΪ����Ա��¼������2Ϊϵͳ����Ա��¼
	public String dl_account=null;
	public Timestamp login_time=null;
	//���˻�����Ϣ
	
	//public Cashier cash=null;  	//��¼������Ա��Ϣ
	public Stage primaryStage=null;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage=primaryStage;
			//�������ݿ�
			Class.forName("com.mysql.cj.jdbc.Driver");
            con = java.sql.DriverManager.getConnection(strCon, strUserName, strPWD);
            if(con!=null ) System.out.println("��˳�����ӵ����ݿ⡣");		
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);//���뼶������Ϊ���л�
			//װ�ص�¼����
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
