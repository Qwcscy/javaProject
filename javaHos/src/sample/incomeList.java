package sample;

import javafx.beans.property.SimpleStringProperty;

public class incomeList implements Cloneable{
    private final SimpleStringProperty ksmc;
    private final SimpleStringProperty ysbh;
    private final SimpleStringProperty ysmc;
    private final SimpleStringProperty hzlb;
    private final SimpleStringProperty ghrc;
    private final SimpleStringProperty sumIncome;
    incomeList(String m_ksmc,String m_ysbh,String m_ysmc,String m_hzlb,String m_ghrc,String m_sumIncmoe)
    {
        ksmc= new SimpleStringProperty(m_ksmc);
        ysbh= new SimpleStringProperty(m_ysbh);
        ysmc= new SimpleStringProperty(m_ysmc);
        hzlb= new SimpleStringProperty(m_hzlb);
        ghrc= new SimpleStringProperty(m_ghrc);
        sumIncome=new SimpleStringProperty(m_sumIncmoe);
    }
    public String getKsmc(){
        return ksmc.get();
    }
    public String getYsbh(){
        return ysbh.get();
    }
    public String getYsmc(){
        return ysmc.get();
    }
    public String getHzlb(){
        return hzlb.get();
    }
    public String getGhrc(){
        return ghrc.get();
    }
    public String getSumIncome(){
        return sumIncome.get();
    }
}
