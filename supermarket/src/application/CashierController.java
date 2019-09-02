package application;

import java.math.BigDecimal;
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
import javafx.event.ActionEvent;
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

public class CashierController implements Initializable{
	private Main myApp;			//���ں����س������
	private String sql=null;
    static Statement stmt = null;
    
	//�ÿ�
	private String cosCard=null;
	private Boolean hasCard=false;
    private	Boolean valid_commodity=false;
    private	Boolean reg_ready=false;
    
    private VIP card=new VIP();//�쿨

    private String commodity_no=null;
    private String commodity_name=null;
    private String commodity_price=null;
    private String commodity_stock=null;
    private String buy_quantity;
    private BigDecimal money=BigDecimal.ZERO;
    private BigDecimal off=new BigDecimal("1.0");
    private BigDecimal str_money=BigDecimal.ZERO;
    
    ObservableList<Goods_show> cart = FXCollections.observableArrayList(); 
    ObservableList<Goods_show> unreg_list = FXCollections.observableArrayList();
    ObservableList<Stream_show> stream_list = FXCollections.observableArrayList(); 
    @FXML
    private TableView<Goods_show> table_patreg;
    @FXML
    private TableView<Stream_show> table_stream;
	    
	    @FXML
	    private Button btn_ok,btn_ok1,btn_clear,btn_exit,btn_exit1,btn_exit2,btn_unreg,btn_card,btn_commit,btn_reg;
	 
	    @FXML
	    private ComboBox<String> pay,recall;
	    @FXML
	    private TextField cost,phone,cardno,checkinfo,comno,quantity,pay1,change,regnum,stream_sum;
	    
	    @FXML
	    private TableColumn<Goods_show,String>SPBH,SPMC,SPDJ,GMSL,JG,KCL;
	    @FXML
	    private Tab tab_reg,tab_unreg,tab_str;
	    @FXML
	    private TableColumn<Stream_show,String>LSBH,JYSJ,LSJE;
	    
	    @Override
	    public void initialize(URL url, ResourceBundle rb) {
    	SPBH.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("merchNo"));  
    	SPMC.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("merchName"));  
    	SPDJ.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("price")); 
    	GMSL.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("num"));
    	JG.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("tPrice"));  
    	KCL.setCellValueFactory(new PropertyValueFactory<Goods_show,String>("Stock")); 
    	LSBH.setCellValueFactory(new PropertyValueFactory<Stream_show,String>("streamNo"));  
    	JYSJ.setCellValueFactory(new PropertyValueFactory<Stream_show,String>("transTime"));  
    	LSJE.setCellValueFactory(new PropertyValueFactory<Stream_show,String>("price")); 
    	
    	table_patreg.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	pay.getItems().addAll( "��50Ԫ", "���");
    	pay.getSelectionModel().select(0);
    	recall.getItems().addAll( "����", "�ֻ���");
    	recall.getSelectionModel().select(0);
    	table_patreg.setItems(cart);
    	table_stream.setItems(stream_list);

    }  


 

    @FXML
    private void btn_card_okay(MouseEvent event)

    {
    	String strno=null;
    	int ind=pay.getSelectionModel().getSelectedIndex();
    	String phone_num=phone.getText();
    	Boolean flag=false;
    	if((phone_num!=null)&&(phone_num.length()==11))
    	{
    		
    		try//�Ȳ���û������ֻ���
			{
				stmt=myApp.con.createStatement();
				sql="SELECT * FROM vip where PhoneNum ='"+phone_num+"'";
    			ResultSet rs=stmt.executeQuery(sql);
    			if(rs.next())
    			{
    				card.cardNo= rs.getString("CardNo");
    				flag=true;
    			}
    			else
    			{

    				sql="SELECT COUNT(*) AS COUNT FROM VIP ";
    				ResultSet rss=stmt.executeQuery(sql);
    				if(rss.next())
    				{
    					card.cardNo=String.format("%06d", rss.getInt("count")+1);
    				}
    				rss.close();
    			}
    			
    			card.amount=new BigDecimal(0.0);
	    		card.phoneNum=phone_num;
	    		card.startDate=new Timestamp(System.currentTimeMillis());
	    	
	    		stmt=myApp.con.createStatement();
	    		sql=flag?("UPDATE VIP SET startdate='"+card.startDate+"',amount="+card.amount+",valid=1 where cardno='"+card.cardNo+"'"):("INSERT INTO VIP VALUES('" + card.cardNo + "','" + card.phoneNum+"','"+card.startDate+"',"+card.amount+",1)");
	    		stmt.executeUpdate(sql);
    			stmt.close();
			}
    		catch(Exception e)
			{
				e.printStackTrace();
				
				return;
			}
    		
			
    		
    		if(ind==0)//��Ǯ�쿨Ҫ������ˮ
    		{
    			
    			try
    			{
    				stmt=myApp.con.createStatement();
    				sql="SELECT COUNT(*) AS COUNT FROM stream ";
        			ResultSet rs=stmt.executeQuery(sql);
        			if(rs.next())
        			{
        				strno=String.format("%06d", rs.getInt("count")+1);
        			}
        			rs.close();
        			stmt.close();
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    				
    				return;
    			}
    			try
        		{
    				stmt=myApp.con.createStatement();
    				sql="INSERT INTO stream VALUES('" + strno+ "','" + card.startDate+"','"+myApp.dl_account+"',"+new BigDecimal(50.0)+",NULL)";
        			stmt.executeUpdate(sql);
        	        stmt.close();
        		}
        		catch(Exception err){
        			err.printStackTrace();
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), "��ˮ����ʧ�ܣ�����ϵ����Ա", "����", JOptionPane.WARNING_MESSAGE);
        			return;
        		}
    			stream_list.add(new Stream_show(strno,String.valueOf(card.startDate),"50.0"));
          		str_money=str_money.add(new BigDecimal(50.0));
          		stream_sum.setText(String.valueOf(str_money));
    		}
    		
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "�쿨�ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);

            phone.clear();

        	cardno.setText(card.cardNo);
          
            card=new VIP();
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "���벻�Ϸ���", "����", JOptionPane.WARNING_MESSAGE);
    	}
    }
  
    
    @FXML
    private void btn_vip_okay(MouseEvent event)
    {//��ѡ����ʾ�˿���Ϣ
    	int ind=recall.getSelectionModel().getSelectedIndex();
    	String phone_num=checkinfo.getText();
    	checkinfo.clear();
    	if((ind==1)&&(phone_num!=null)&&(phone_num.length()==11))
    	{//�ֻ���
    			try
    			{
    				stmt=myApp.con.createStatement();
    				sql="SELECT * FROM vip where PhoneNum ='"+phone_num+"'";
        			ResultSet rs=stmt.executeQuery(sql);
        			if(rs.next())
        			{
        				hasCard=rs.getBoolean("valid");
        				cosCard= rs.getString("CardNo");
        			}
        			else
        			{
        				JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	              			"�绰����", "����", JOptionPane.WARNING_MESSAGE);
        				rs.close();
        				stmt.close();
        				return;
        			}
        			rs.close();
        			stmt.close();
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    				return;
    			}
    			
    	}
    	else if((ind==0)&&(phone_num!=null)&&(phone_num.length()==6)){//���ŵ�¼
    		
			try
			{
				stmt=myApp.con.createStatement();
				sql="SELECT * FROM vip where CardNo ='"+phone_num+"'";
    			ResultSet rs=stmt.executeQuery(sql);
    			if(rs.next())
    			{
    				cosCard= rs.getString("CardNo");
    				hasCard=rs.getBoolean("valid");
    			}
    			else
    			{
    				JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	              			"���Ŵ���", "����", JOptionPane.WARNING_MESSAGE);
    				rs.close();
    				stmt.close();
    				return;
    			}
    			rs.close();
    			stmt.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return;
			}
			
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "���벻�Ϸ���", "����", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	if(hasCard==true)
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "���Ż��ʸ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
    		 off=new BigDecimal("0.9");
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "���ѽ��δ����Ҫ�󣬲��ܼ��������Ż�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
    	}
    }
  

    @FXML
    private void comno_okay(ActionEvent event)
    {//��ѡ����ʾ�˿���Ϣ
    	
    	commodity_no=comno.getText();
    			try
    			{
    				stmt=myApp.con.createStatement();
    				sql="SELECT * FROM goods where MerchNo ='"+commodity_no+"'";
        			ResultSet rs=stmt.executeQuery(sql);
        			if(rs.next())
        			{
        				commodity_price= rs.getString("price");
        				commodity_name=rs.getString("Name");
        				commodity_stock=rs.getString("Stocknum");
        			}
        			else
        			{
        				JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	              			"��Ʒ��Ŵ���", "����", JOptionPane.WARNING_MESSAGE);
        				commodity_no=null;
        				comno.clear();
        				rs.close();
        				stmt.close();
        				return;
        			}
        			valid_commodity=true;
        			rs.close();
        			stmt.close();
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    				return;
    			}

    }
    @FXML
    private void quantity_okay(ActionEvent event) {
    	buy_quantity=quantity.getText();
    	BigDecimal mon=new BigDecimal(commodity_price).multiply(new BigDecimal(buy_quantity));
    	if(valid_commodity) {
    		cart.add(new Goods_show(commodity_no,commodity_name,commodity_price,buy_quantity,String.valueOf(mon),commodity_stock));
    		 comno.clear();
    		 quantity.clear();
    		 commodity_no=null;
    		 commodity_name=null;
    		 commodity_price=null;
    		 commodity_stock=null;
    		 money=money.add(mon.multiply(off));
    	     cost.setText(String.valueOf(money));
    	}
    	
    }
    
    @FXML
    private void btn_clear_clicked(MouseEvent event) {
    	cart.clear();
  	    money=BigDecimal.ZERO;
  	    cost.clear();
  	    pay1.clear();
  	    change.clear();
    }
    

    @FXML
    private void btn_exit_clicked(MouseEvent event)
    {
    	Event.fireEvent(myApp.primaryStage,
    		new WindowEvent(myApp.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    
    
  @FXML
    private void btn_unreg_clicked(MouseEvent event)
    {
    	
    	unreg_list = table_patreg.getSelectionModel().getSelectedItems();
    	Goods_show patreg;
    	
    	for(int i=0;i<unreg_list.size();i++)
    	{
    		patreg = unreg_list.get(i);
    		money=money.subtract((new BigDecimal(patreg.getTPrice())).multiply(off));
    		cart.remove(patreg);
    	}
    	cost.setText(String.valueOf(money));
    }
  
  @FXML
  private void pay_okay(ActionEvent e) {
  	BigDecimal tmp=(new BigDecimal(pay1.getText())).subtract(money);
  	
  	if(tmp.compareTo(BigDecimal.ZERO)>=0)
  	{
  		change.setText(tmp.toString());
  		reg_ready=true;
  	}
  	else
  	{
  		change.setText("Ǯ���������½�Ǯ");
  		reg_ready=false;
  	}
  }
  
  @FXML
  private void commit_clicked(MouseEvent event)
  {
	  
  	 Goods_show patreg;
  	 String strno;
  	 Timestamp strtime=null;
  	 if(reg_ready==false)
  	 {
  		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "��δ����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
  		return;
  	 }
  		try
  		{	
  			//����п�������¿���������
  			//���޿�����Ҫ��ʾ����
  			//���� rollback:����������Ʒ���  ������ˮ��Ϣ (�۸���default�� ��������record  commit

  			 String sql,cardno;
  			
  			 myApp.con.setAutoCommit(false);//��������
  	  	     stmt = myApp.con.createStatement();	      
  	  	    sql="SELECT COUNT(*) AS COUNT FROM stream ";
			ResultSet rs=stmt.executeQuery(sql);
			if(rs.next())
			{//�õ���ˮ��
				strno=String.format("%06d", rs.getInt("count")+1);
			}
			else
			{
				JOptionPane.showMessageDialog(new JFrame().getContentPane(), "��������", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			//������ˮ��¼
			cardno=hasCard?("'"+cosCard+"'"):"null";
			strtime=new Timestamp(System.currentTimeMillis());
  	  	    sql = "insert into stream values('"+strno+"','"+strtime+"','"+myApp.dl_account+"',"+String.valueOf(money)+","+cardno+")";
  	  	    stmt.executeUpdate(sql);
  	  	    
  	  		for(int i=0;i<cart.size();i++)
  	    	{//����������Ʒ���  ����record
  	    		patreg = cart.get(i);
  	    		
  	    		 sql = "update goods set stocknum=stocknum-"+patreg.getNum()+" where merchno = '"+patreg.getMerchNo()+"'";
    	         stmt.executeUpdate(sql);

    	         
    	         sql = "insert into record values('"+strno+"','"+patreg.getMerchNo()+"',"+patreg.getNum()+")";
    	         stmt.executeUpdate(sql);
  	    	}
  	  		
  	  		//�����û����ѽ��
  	
  	  		if(hasCard) {
  	  			sql = "update vip set amount=amount+"+String.valueOf(money)+"where cardNo='"+cosCard+"'";
  	  			stmt.executeUpdate(sql);
  	  		}
  	  			stmt.close();    
  	  			myApp.con.commit();
  	  		    myApp.con.setAutoCommit(true);
  	  			
    	     }catch(SQLException se){ 
    	    	 
    	    	 try {
    	    		 	myApp.con.rollback();
    	    		 	 myApp.con.setAutoCommit(true);
    	    		 } catch (SQLException e1) {
    	    		             e1.printStackTrace();
    	    		 }
    	    	 JOptionPane.showMessageDialog(new JFrame().getContentPane(), "��Ʒ��Ϣ����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
    	    	 se.printStackTrace();
    	         return;
    	     }
  	
  		   
  		   if(hasCard==false && (money.compareTo(new BigDecimal("1000")))>=0 )
  		   {
  			 JOptionPane.showMessageDialog(new JFrame().getContentPane(), "������1000Ԫ������Ѱ쿨", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
 
  		   }
  		   else
  		   {
  			 JOptionPane.showMessageDialog(new JFrame().getContentPane(), "����ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
  		   }
  		stream_list.add(new Stream_show(strno,String.valueOf(strtime),String.valueOf(money)));
  		str_money=str_money.add(money);
  		stream_sum.setText(String.valueOf(str_money));
  		cosCard=null;
  		hasCard=false;
  	    valid_commodity=false;
  	    reg_ready=false;
  	    
  	    card=new VIP();//�쿨
  	
  	    commodity_no=null;
  	    commodity_name=null;
  	    commodity_price=null;
  	    commodity_stock=null;
  	    money=BigDecimal.ZERO;
  	    off=new BigDecimal("1.0");
  	    cart.clear();
  	    cost.clear();
  	    pay1.clear();
  	    change.clear();
  	    regnum.setText(strno);

  }
  
 //�̴�
  
  @FXML
  private void register_clicked(MouseEvent event)
  {
	  try
		{	

			 String sql,mon;
	  	     stmt = myApp.con.createStatement();	      
	  	     Timestamp end_time=new Timestamp(System.currentTimeMillis());
	  	    sql="SELECT SUM(cost) AS mon FROM stream where account='"+myApp.dl_account+"' and time between '"+myApp.login_time+"' and '"+end_time+"'";
			ResultSet rs=stmt.executeQuery(sql);
			if(rs.next())
			{//�õ��̴���
				mon=rs.getString("mon");
			}
			else
			{
				JOptionPane.showMessageDialog(new JFrame().getContentPane(), "�̴�ͳ�Ƴ���", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			//�����̴��¼
			mon=(mon==null)?"0":mon;
	  	    sql = "insert into register values('"+myApp.login_time+"','"+end_time+"','"+myApp.dl_account+"',"+mon+")";
	  	    stmt.executeUpdate(sql);
	  	    
  	     }catch(SQLException se){
  	         // ���� JDBC ����
  	         se.printStackTrace();
  	         return;
  	     }
	  
		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "�̴�ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
  }
	 public void setUp(Main application) {
	        myApp = application;

	    }    
}
