package gr.aueb.cf.agronitor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "temperatures")
public class Temperature {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_greenhouse", referencedColumnName = "id")
    private Greenhouse greenhouse;

    public Temperature(Date timestamp, String value, Long greenhouseId) {
        this.timestamp = timestamp;
        this.value = value;
//        this.greenhouse = new Greenhouse();
        this.greenhouse.setId(greenhouseId);
        this.greenhouse.addTemperature(this);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Greenhouse getGreenhouse() {
        return greenhouse;
    }
    public void setGreenhouse(Greenhouse greenhouse) {
        this.greenhouse = greenhouse;
    }
}
