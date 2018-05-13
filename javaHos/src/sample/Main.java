package sample;
import java.sql.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
public class Main extends Application{
    private ArrayList<String> SFZJ=new ArrayList<>();//保存号种类型：是否为专家号
    private ArrayList<String> DoctorType=new ArrayList<>();//保存医生类型：是否为专家
    private ArrayList<String> GHFY=new ArrayList<>();//挂号费用
    private ObservableList<data> da;
    private TableView table;
    private Stage mainStage;
    private Popup popWindow=new Popup();
    private Scene regScene;
    private registrationController reg;
    private docLoginController doc;
    private connectToDbController db;
    public static Connection con;
    private String curDocType=" ";//当前的医生类型
    private String curHzType=" ";//当前的号种类型
    private String curHzNum=" ";//当前的号种编号
    private String curYsNum=" ";//当前的医生编号
    private String curBrNUm=" ";//当前的病人编号
    private String curKsNum="";//当前的科室编号
    private String ysBh;//医生登录自己的编号
    private String YCJE;//预存金额
    private String balance;//病人预存的余额
    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage=primaryStage;
        gotoConnectToDb();
    }
    /*连接数据库*/
    public boolean connectToMysql(String url,String user,String passWord,String driver){
        String jdbc=url+"?useSSL=false";
        try{
            Class.forName(driver);
            con=DriverManager.getConnection
                    (jdbc,user,passWord);
            System.out.println("Success connect Mysql server!");
            return true;
        }
        catch (Exception e){
                return false;
        }
    }
    /*设置医生自己的编号*/
   public void setYsBh(String value){
        ysBh=value;
   }
   public String getYsBh(){
       return ysBh;
   }
    /*获取挂号编号*/
    public int getRegNum(){
        int num=0;
        try {
            Statement st=con.createStatement();
            ResultSet res=st.executeQuery("select count(*) from t_ghxx");
            if(res.next())
                num=res.getInt(1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return num+1;
    }
    public void setCurBrNUm(String value){
        curBrNUm=value;
    }
    public void gotoLogin(){
        try{
            mainStage.hide();
            Controller login=(Controller)replaceSceneContent("sample.fxml");
            login.setApp(this);
        }catch (Exception ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainStage.show();
    }
    public void gotoRegistration(){
        try{
            reg=(registrationController)replaceSceneContent("registration.fxml");
            reg.setApp(this);
            mainStage.show();
        }catch (Exception ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void gotoDocLogin(){
        try{
            doc=(docLoginController)replaceSceneContent("DoctorLogin.fxml");
            doc.setApp(this);
            mainStage.show();
        }
        catch(Exception e)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,e);
        }
    }
    public void gotoConnectToDb(){
        try {
            db=(connectToDbController)replaceSceneContent("ConnectToDataBase.fxml");
            db.setApp(this);
            mainStage.show();
        }catch (Exception e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    private Initializable replaceSceneContent(String fxml)throws Exception{
        FXMLLoader loader=new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        Parent root;
        try{
           root=loader.load(in);
        }finally{
            in.close();
        }
        regScene=new Scene(root,650,500);
        mainStage.setScene(regScene);
        return loader.getController();
    }
    /*弹出提示窗口*/
    public void showPopWindow(double x,double y,String type){
        table=new TableView();
        da=FXCollections.observableArrayList();
        table.setMaxHeight(200);
        popWindow.setAutoFix(true);
        popWindow.setAutoHide(true);
        TableColumn number = new TableColumn("编号");
        TableColumn name = new TableColumn("名称");
        TableColumn pinyin = new TableColumn("拼音字母");
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        pinyin.setCellValueFactory(new PropertyValueFactory<>("pinyin"));
        getDbInfo(type);//获取对应的数据
        table.setItems(da);
        table.getColumns().addAll(number,name,pinyin);
        popWindow.getContent().addAll(table);
        popWindow.show(regScene.getWindow(),x,y);
        ListChangeListener<data> indicesListener=new ListChangeListener<data>(){
            public void onChanged(Change<? extends data> c){
                if(c.next()){
                    int row=table.getSelectionModel().getSelectedIndex();
                    data d=(data)table.getSelectionModel().getSelectedItem();
                    if(d==null)
                        return;
                    if(type.equals("KSXX")){
                        curKsNum=d.getNumber();
                    }
                    if(type.equals("KSYS"))
                        curYsNum=d.getNumber();
                    if(type.equals("HZMC"))
                        curHzNum=d.getNumber();
                    Platform.runLater(()->{
                        reg.setContent(d.getName(),type);
                        checkInfo(type,row);
                    });
            }
            }
        };
        table.getSelectionModel().getSelectedItems().addListener(indicesListener);
    }
    /*获取数据库信息，返回数据存到info中*/
    public void getDbInfo(String type){
        if(SFZJ.size()>0)
            SFZJ.clear();
        if(GHFY.size()>0)
            GHFY.clear();
        if(DoctorType.size()>0)
            DoctorType.clear();
        da.clear();
        String sql="";
        if(type.equals("KSXX"))//如果是科室信息
            sql="select * from t_ksxx";
        else if(type.equals("KSYS"))
            sql="select YSBH,YSMC,PYZS,SFZJ from t_ksys where ksbh='"+curKsNum+"'";
        else if(type.equals("HZMC"))
            sql="select HZBH,HZMC,PYZS,SFZJ,GHFY from t_hzxx where ksbh='"+curKsNum+"'";
        else;
        try{
            Statement st=con.createStatement();
            ResultSet res=st.executeQuery(sql);
            while(res.next()){
                da.add(new data(res.getString(1),
                        res.getString(2), res.getString(3)));
                if(type.equals("HZMC"))//添加号种类型
                {
                    SFZJ.add(res.getString( 4));
                    GHFY.add(res.getString(5));
                }
                else if(type.equals("KSYS"))//添加医生类型
                    DoctorType.add(res.getString(4));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void refreshTable(String textContent,String type){
        getDbInfo(type);
        for(int i=0;i<da.size();i++)
        {
            /*在arrayList中搜索*/
            if(da.get(i).getPinyin().indexOf(textContent,0)==-1)
            {
                da.remove(i);//删除不满足条件的行
                i--;
            }
        }
    }
    /*插入一条新的挂号信息*/
    /*并返回不同的int值*/
    /*-1表示该号种已经挂满了*/
    /*0表示系统繁忙，即出现多人争夺一个号的情况*/
    /*1表示挂号成功*/
    public int insertRegInfo(){
        try{
            Statement st=con.createStatement();
            Statement st1=con.createStatement();
            String regNum=reg.getRegNum();//挂号的编号
            String cost=reg.getCOST();//实际花费
            int hzNum=getHzNum();//获取号种人次
            if(hzNum==-1)//如果超过了挂号人数
                return -1;
            String sql="insert into t_ghxx values ('"+String.valueOf(regNum)+"','"+curHzNum+"','"+curYsNum+"'," +
                    "'"+curBrNUm+"','"+hzNum+"',false,'"+cost+"','"+LocalDateTime.now()+"')";
            st.execute(sql);
            if(Double.valueOf(balance)>=0) {
                sql = "update t_brxx set ycje='" + balance + "'where brbh='" + curBrNUm + "'";
                st1.execute(sql);//修改病人的预存金额
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
    /*检查输入的号种类型和医生类型是否匹配*/
    /*检查该号种时候还有挂号人次剩余*/
    public void checkInfo(String type,int row){
        if(type.equals("KSYS"))
            curDocType=DoctorType.get(row);
        if(type.equals("HZMC")){
            curHzType=SFZJ.get(row);
        }
        if(type.equals("HZMC")||type.equals("KSYS"))
        {
            /*若号种类型和医生类型不匹配*/
            if(curHzType.equals("1")&&curDocType.equals("0"))
            {
                Alert error = new Alert(Alert.AlertType.ERROR,"非专家不能挂专家号！");
                error.showAndWait();
                return;
            }
            if(type.equals("HZMC"))
            {
                reg.setHZLB(SFZJ.get(row));//设置号种类别
                reg.setYcje(YCJE);//设置预存金额
                reg.setCOSET(GHFY.get(row));//设置号种应缴纳的费用
                balance=String.valueOf(Double.valueOf(YCJE)-Double.valueOf(GHFY.get(row)));
            }
        }
    }
    /*对于每一个病人计算自己是该号种的第几个人*/
    public int getHzNum(){
        int count=0;//号种人次
        try{
            Statement st=con.createStatement();
            /*获取当前号种的挂号人次*/
            ResultSet res=st.executeQuery("select count(*) from t_ghxx where hzbh='"+curHzNum+"'");
            while(res.next()){
                count=Integer.valueOf(res.getString(1));
            }
            res=st.executeQuery("select GHRS from t_hzxx where hzbh='"+curHzNum+"'");
            while(res.next())//检查该号种是否已经挂满了
            {
                if(count>=res.getInt(1))
                    return -1;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return count+1;
    }
    public void setYcje(String value){
        YCJE=value;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
