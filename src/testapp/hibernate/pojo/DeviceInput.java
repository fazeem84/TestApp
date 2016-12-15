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
@Table(name = "device_input")
public class DeviceInput implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "T1")
    private Double T1;
    @Column(name = "T2")
    private Double T2;
    @Column(name = "T3")
    private Double T3;
    @Column(name = "SL")
    private Double SL;
    @Column(name = "AM")
    private Double AM;
    @Column(name = "PH")
    private Double PH;
    @Column(name = "NI")
    private Double NI;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    public DeviceInput() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getT1() {
        return T1;
    }

    public void setT1(Double T1) {
        this.T1 = T1;
    }

    public Double getT2() {
        return T2;
    }

    public void setT2(Double T2) {
        this.T2 = T2;
    }

    public Double getT3() {
        return T3;
    }

    public void setT3(Double T3) {
        this.T3 = T3;
    }

    public Double getSL() {
        return SL;
    }

    public void setSL(Double SL) {
        this.SL = SL;
    }

    public Double getAM() {
        return AM;
    }

    public void setAM(Double AM) {
        this.AM = AM;
    }

    public Double getPH() {
        return PH;
    }

    public void setPH(Double PH) {
        this.PH = PH;
    }

    public Double getNI() {
        return NI;
    }

    public void setNI(Double NI) {
        this.NI = NI;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "DeviceInput{" + "id=" + id + ", T1=" + T1 + ", T2=" + T2 + ", T3=" + T3 + ", SL=" + SL + ", AM=" + AM + ", PH=" + PH + ", NI=" + NI + ", created=" + created + '}';
    }
    

   

}
