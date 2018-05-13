package sample;
import javafx.beans.property.SimpleStringProperty;
import java.util.Date;
public class patientData implements Cloneable{
    private final SimpleStringProperty number;
    private final SimpleStringProperty name;
    private final SimpleStringProperty time;
    private final SimpleStringProperty type;
    patientData(String m_number, String m_name,String m_time,String m_type){
        number = new SimpleStringProperty(m_number);
        name=new SimpleStringProperty(m_name);
        time=new SimpleStringProperty(m_time);
        type=new SimpleStringProperty(m_type);
    }
    public String getNumber(){
        return number.get();
    }
    public String getName() {
        return name.get();
    }
    public String getTime(){
        return time.get();
    }
    public String getType(){
        return type.get();
    }
}
