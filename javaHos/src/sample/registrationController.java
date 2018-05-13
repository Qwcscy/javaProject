package sample;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
public class registrationController implements Initializable {
    @FXML private Button LOGOUT;
    @FXML private Button SURE;
    @FXML private Button CLEAR;
    @FXML private TextField KSMC;
    @FXML private TextField KSYS;
    @FXML private TextField HZMC;
    @FXML private TextField HZLB;
    @FXML private TextField COST;
    @FXML private TextField MONEY;
    @FXML private TextField CHANGE;
    @FXML private TextField REGNUM;
    private String YCJE;
    private int flag=0;
    private Main application;
    public void setApp(Main application){
        this.application = application;
        CHANGE.setEditable(false);
        REGNUM.setEditable(false);
        setREGNUM();
        SURE.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                if(KSMC.getText().isEmpty()||KSYS.getText().isEmpty()||
                        HZLB.getText().isEmpty()||MONEY.getText().isEmpty())
                {
                    if(MONEY.getText().isEmpty()&&flag==1);
                    else{
                        Alert error = new Alert(Alert.AlertType.ERROR,"输入不能为空！");
                        error.showAndWait();
                        return;
                    }
                }
                if(flag==0)
                {
                    if(Double.parseDouble(MONEY.getText())<0){
                        Alert error = new Alert(Alert.AlertType.ERROR,"金额不足！");
                        error.showAndWait();
                        return ;
                    }
                }
                int flag=application.insertRegInfo();
                if(flag==-1)
                {
                    Alert error = new Alert(Alert.AlertType.ERROR,"该号种已挂满！");
                    error.showAndWait();
                    return ;
                }
                else if(flag==0)
                {
                    Alert error = new Alert(Alert.AlertType.ERROR,"系统繁忙，请稍后再提交申请...");
                    error.showAndWait();
                    return ;
                }
                else{
                    Alert error = new Alert(Alert.AlertType.CONFIRMATION,"挂号成功！");
                    error.showAndWait();
                    application.gotoLogin();
                }
            }
        });
        CLEAR.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                KSMC.clear();KSYS.clear();HZMC.clear();
                HZLB.clear();COST.clear();MONEY.clear();CHANGE.clear();
            }
        });
        LOGOUT.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                application.gotoLogin();
            }
        });
        /*科室名称*/
        KSMC.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                double x = KSMC.getScene().getWindow().getX() +
                        KSMC.getScene().getX() + KSMC.localToScene(0, 0).getX();
                double y = KSMC.getScene().getWindow().getY() + KSMC.getScene().getY() +
                        KSMC.localToScene(0, 0).getY() + KSMC.getHeight();
                application.showPopWindow(x,y,"KSXX");
            }
        });
        /*科室医生*/
        KSYS.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                double x = KSYS.getScene().getWindow().getX() +
                        KSYS.getScene().getX() + KSYS.localToScene(0, 0).getX();
                double y =  KSYS.getScene().getWindow().getY() +  KSYS.getScene().getY() +
                        KSYS.localToScene(0, 0).getY() + KSYS.getHeight();
                application.showPopWindow(x,y,"KSYS");
            }
        });
        /*号种名称*/
        HZMC.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                double x =HZMC.getScene().getWindow().getX() +
                        HZMC.getScene().getX() + HZMC.localToScene(0, 0).getX();
                double y =  HZMC.getScene().getWindow().getY() +  HZMC.getScene().getY() +
                        HZMC.localToScene(0, 0).getY() + HZMC.getHeight();
                application.showPopWindow(x,y,"HZMC");
            }
        });
        /*监听textField内值的变化*/
        KSMC.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                application.refreshTable(KSMC.getText(),"KSXX");
            }
        });
        KSYS.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                application.refreshTable(KSYS.getText(),"KSYS");
            }
        });
        HZMC.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                application.refreshTable(HZMC.getText(),"HZMC");
            }
        });
        MONEY.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                  if(!MONEY.getText().isEmpty()&&!COST.getText().isEmpty())
                  {
                      double cost=Double.parseDouble(COST.getText());
                      double money=Double.parseDouble(MONEY.getText());
                      if(money-cost>=0)
                         CHANGE.setText(String.valueOf(money-cost));
                      else
                          CHANGE.setText("金额不足");
                  }
            }
        });
    }
    public void initialize(URL url,ResourceBundle rb){
    }
    public void setContent(String content,String type){
        if(type.equals("KSXX"))
            setKSMC(content);
        else if(type.equals("KSYS"))
            setKSYS(content);
        else setHZMC(content);
    }
    public void setKSMC(String value){
        KSMC.setText(value);
    }
    public void setKSYS(String value){
        KSYS.setText(value);
    }
    public void setHZMC(String value){
        HZMC.setText(value);
    }
    public void setREGNUM(){
        REGNUM.setText(String.valueOf(application.getRegNum()));
    }
    public void setHZLB(String value){
        if(value.equals("1")) HZLB.setText("专家号");
        else HZLB.setText("普通号");
    }
    /*根据号种类型设置挂号费用*/
    public void setCOSET(String value){
        COST.setText(value);
        if(Double.valueOf(value)<=Double.valueOf(YCJE)) {
            MONEY.setEditable(false);
            flag = 1;
        }
    }
    public String getRegNum(){
        return REGNUM.getText();
    }
    public String getCOST(){
        return COST.getText();
    }
    public void setYcje(String value)
    {
        YCJE=value;
    }
}


