package sample;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import jdk.nashorn.internal.runtime.regexp.joni.constants.CCSTATE;

public class Controller implements Initializable{
    @FXML private Button D_LOGIN;
    @FXML private Button P_LOGIN;
    @FXML private TextField userName;
    @FXML private PasswordField passWord;
    private String YCJE="";//预存金额
    private Main application;
    public void setApp(Main application){
        this.application=application;
        P_LOGIN.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                if(checkPassWord(false)){
                    application.setCurBrNUm(userName.getText());//设置病人编号
                    application.setYcje(YCJE);
                    setLoginTimeP();
                    application.gotoRegistration();
                }
                else{
                    Alert error = new Alert(Alert.AlertType.ERROR,"用户名或密码错误！");
                    error.showAndWait();
                }
            }
        });
        D_LOGIN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                if(checkPassWord(true))
                {
                    application.setYsBh(userName.getText());
                    application.gotoDocLogin();
                }
                else{
                    Alert error = new Alert(Alert.AlertType.ERROR,"用户名或密码错误！");
                    error.showAndWait();
                }
            }
        });
    }
    /*登录信息验证，type指明登录者身份类型*/
    /*0表示患者登陆，1表示医生登录*/
    public boolean checkPassWord(boolean type){
        String account=userName.getText();
        String password=passWord.getText();
        try{
            Statement st=Main.con.createStatement();
            String sql=type?"select YSBH,DLKL from t_ksys":"select BRBH,DLKL,YCJE from T_BRXX";
            ResultSet res=st.executeQuery(sql);
            while(res.next())
            {
                if(account.equals(res.getString(1))&&
                        password.equals(res.getString(2))){
                    if(!type)
                        YCJE=res.getString(3);
                    return true;
                }
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public void setLoginTimeP(){
        String sql="update t_brxx set DLRQ='"+LocalDateTime.now()+"'where brbh='"+userName.getText()+"'";
        try {
            Statement st=Main.con.createStatement();
            st.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setLoginTimeD(){
        String sql="update t_ksys set DLRQ='"+LocalDateTime.now()+"'where ysbh='"+userName.getText()+"'";
        try {
            Statement st=Main.con.createStatement();
            st.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void initialize(URL url, ResourceBundle rb){
    }
}
