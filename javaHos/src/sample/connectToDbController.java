package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class connectToDbController implements Initializable{
    @FXML private TextField URL;
    @FXML private TextField USER;
    @FXML private TextField PW;
    @FXML private TextField DRIVER;
    @FXML private Button SURE;
    private Main application;
    public void setApp(Main application){
        this.application=application;
//        URL.setText("jdbc:mysql://222.20.104.86:3306/mydb");
//        USER.setText("user");
//        PW.setText("1234");
//        DRIVER.setText("com.mysql.jdbc.Driver");
        SURE.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(URL.getText().isEmpty()||USER.getText().isEmpty()||PW.getText().isEmpty()||
                        DRIVER.getText().isEmpty())
                {
                    Alert error = new Alert(Alert.AlertType.ERROR,"输入不能为空！");
                    error.showAndWait();
                    return;
                }
                if(!application.connectToMysql(URL.getText(),USER.getText(),PW.getText(),DRIVER.getText()))
                {
                    Alert error = new Alert(Alert.AlertType.ERROR,"数据库连接失败！请检查信息是否正确！");
                    error.showAndWait();
                    return;
                }
                application.gotoLogin();
            }
        });
    }
    public void initialize(URL url, ResourceBundle rb){
    }
}
