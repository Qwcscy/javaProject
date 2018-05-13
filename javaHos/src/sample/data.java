package sample;

import javafx.beans.property.SimpleStringProperty;

public class data implements Cloneable {
    private final SimpleStringProperty number;
    private final SimpleStringProperty name;
    private final SimpleStringProperty pinyin;

    data(String m_number, String m_name, String m_pinyin) {
        number = new SimpleStringProperty(m_number);
        name = new SimpleStringProperty(m_name);
        pinyin = new SimpleStringProperty(m_pinyin);
    }
    public void setNumber(String num) {
        number.set(num);
    }
    public void setName(String na) {
        number.set(na);
    }
    public void setPinyin(String pin) {
        number.set(pin);
    }
    public String getName() {
        return name.get();
    }
    public String getPinyin() {
        return pinyin.get();
    }
    public String getNumber() {
        return number.get();
    }
}
