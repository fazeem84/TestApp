package testapp.hibernate.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "device_settings")
public class DeviceSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "T1")
    private Integer T1;
    @Column(name = "T2")
    private Integer T2;
    @Column(name = "T3")
    private Integer T3;
    @Column(name = "SL")
    private Integer SL;
    @Column(name = "AM")
    private Integer AM;
    @Column(name = "PH")
    private Integer PH;
    @Column(name = "NI")
    private Integer NI;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    private Date updateDate;

    public DeviceSettings() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getT1() {
        return T1;
    }

    public void setT1(Integer T1) {
        this.T1 = T1;
    }

    public Integer getT2() {
        return T2;
    }

    public void setT2(Integer T2) {
        this.T2 = T2;
    }

    public Integer getT3() {
        return T3;
    }

    public void setT3(Integer T3) {
        this.T3 = T3;
    }

    public Integer getSL() {
        return SL;
    }

    public void setSL(Integer SL) {
        this.SL = SL;
    }

    public Integer getAM() {
        return AM;
    }

    public void setAM(Integer AM) {
        this.AM = AM;
    }

    public Integer getPH() {
        return PH;
    }

    public void setPH(Integer PH) {
        this.PH = PH;
    }

    public Integer getNI() {
        return NI;
    }

    public void setNI(Integer NI) {
        this.NI = NI;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "DeviceSettings{" + "id=" + id + ", T1=" + T1 + ", T2=" + T2 + ", T3=" + T3 + ", SL=" + SL + ", AM=" + AM + ", PH=" + PH + ", NI=" + NI + ", updateDate=" + updateDate + '}';
    }

}
