package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class docLoginController implements Initializable {
    private Main application;
    private ObservableList<patientData> pData;
    private ObservableList<incomeList> incomeData;
    private Map<String,String> m=new HashMap<String,String>();
    @FXML TableView pListTable;
    @FXML TableView incomeTable;
    @FXML Tab INCOME;
    @FXML Tab LOGOUT;
    @FXML Tab PATIENT;
    @FXML TextField START;
    @FXML TextField END;
    @FXML Button FIND;
    private String ysBh;
    public void setApp(Main application){
        this.application=application;
        getYsBh();
        getKsmc();
        getPatientList();
        INCOME.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                getIncomeList();
            }
        });
        PATIENT.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                getPatientList();
            }
        });
        LOGOUT.setOnSelectionChanged(new EventHandler<Event>(){
            @Override
            public void handle(Event event) {
                application.gotoLogin();
            }
        });
        FIND.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getIncomeList();
            }
        });
    }
    /*获取病人列表信息*/
    public void getPatientList(){
        pData=FXCollections.observableArrayList();
        String sql="select GHBH,BRBH,GHFY,RQSJ,HZBH from t_ghxx where YSBH='"+ysBh+"'";
        String type="";
        try{
            Statement st=Main.con.createStatement();
            Statement st1=Main.con.createStatement();
            ResultSet res=st.executeQuery(sql);
            while(res.next())
            {
                if(getHzType(res.getString(5)))
                    type="专家号";
                else type="普通号";
                sql="select BRMC from t_brxx where BRBH="+res.getString(2)+"";
                ResultSet re=st1.executeQuery(sql);
                while(re.next())
                {
                    System.out.println(res.getString(1));
                    pData.add(new patientData(res.getString(1),re.getString(1), res.getString(4),type));
                    //System.out.print(res.getString(1)+" "+re.getString(1)+" "+res.getString(4)+"\n");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        showPatientList();
    }
    /*获取收入信息*/
    public void getIncomeList(){
        incomeData=FXCollections.observableArrayList();
        String curYsbh="";
        String curHzbh="";
        String curType="";
        String curKsmc="";
        String start=START.getText().isEmpty()?getTime():START.getText();
        String end=END.getText().isEmpty()?LocalDateTime.now().toString():END.getText();
        String sql="select YSBH,HZBH,sum(GHFY),count(*) from t_ghxx where RQSJ between '"+start+"'and '"+end+"' group by HZBH,YSBH";
        try{
            Statement st=Main.con.createStatement();
            Statement st1=Main.con.createStatement();
            ResultSet res=st.executeQuery(sql);
            while(res.next()){
                curHzbh=res.getString(2);
                curYsbh=res.getString(1);
                sql="select t_ksys.ysmc,t_ksys.ksbh,t_hzxx.sfzj from t_hzxx,t_ksys where hzbh='"+curHzbh+"'and ysbh='"+curYsbh+"'";
                ResultSet re=st1.executeQuery(sql);
                if(re.next()){
                    {
                        curType=re.getString(3).equals("1")?"专家号":"普通号";
                        curKsmc=m.get(re.getString(2));
                        incomeData.add(new incomeList(curKsmc,curYsbh,re.getString(1),
                                curType,res.getString(4),res.getString(3)));
                        System.out.println(incomeData.get(0).getKsmc()+" "+re.getString(1));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        showIncomeList();
    }
    /*显示收入列表*/
    public void showIncomeList(){
        System.out.println(incomeTable.getColumns().size());
        if(incomeTable.getColumns().size()>0)
            incomeTable.getColumns().remove(0,incomeTable.getColumns().size());
        System.out.println("showIncomeList");
        TableColumn ksmc=new TableColumn("科室名称");
        TableColumn ysbh=new TableColumn("医生编号");
        TableColumn ysmc=new TableColumn("医生名称");
        TableColumn hzlb=new TableColumn("号种类别");
        TableColumn ghrc=new TableColumn("挂号人次");
        TableColumn sumIncome=new TableColumn("收入合计");
        ksmc.setCellValueFactory(new PropertyValueFactory<>("ksmc"));
        ysbh.setCellValueFactory(new PropertyValueFactory<>("ysbh"));
        ysmc.setCellValueFactory(new PropertyValueFactory<>("ysmc"));
        hzlb.setCellValueFactory(new PropertyValueFactory<>("hzlb"));
        ghrc.setCellValueFactory(new PropertyValueFactory<>("ghrc"));
        sumIncome.setCellValueFactory(new PropertyValueFactory<>("sumIncome"));
        incomeTable.setItems(incomeData);
        incomeTable.getColumns().addAll(ksmc,ysbh,ysmc,hzlb,ghrc,sumIncome);
    }
    /*显示该医生病人列表*/
    public void showPatientList(){
        System.out.println("showPatientList");
        System.out.println(pListTable.getColumns().size());
        if(pListTable.getColumns().size()>0)
            pListTable.getColumns().remove(0,pListTable.getColumns().size());
        TableColumn number = new TableColumn("挂号编号");
        TableColumn name = new TableColumn("病人名称");
        TableColumn time= new TableColumn("挂号日期");
        TableColumn type=new TableColumn("号种类别");
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        pListTable.setItems(pData);
        pListTable.getColumns().addAll(number,name,time,type);
    }
    public void getYsBh(){
        ysBh=application.getYsBh();
    }
    /*获取科室信息*/
    public void getKsmc(){
        String sql="select ksbh,ksmc from t_ksxx";
        try {
            Statement st=Main.con.createStatement();
            ResultSet res=st.executeQuery(sql);
            while(res.next()){
                m.put(res.getString(1),res.getString(2));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean getHzType(String value){
        String sql="select SFZJ from t_hzxx where hzbh='"+value+"'";
        try {
            Statement st=Main.con.createStatement();
            ResultSet res=st.executeQuery(sql);
            while(res.next())
            {
                if(res.getString(1).equals("1"))
                    return true;
                else return false;
            }
        }catch (Exception e)
        {
            e.printStackTrace();

        }
        return false;
    }
    public void initialize(URL url,ResourceBundle rb){
    }
    public String getTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return format.format(calendar.getTime());
    }
}
