package gr.aueb.cf.agronitor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@Table(name = "greenhouses")
public class Greenhouse {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "greenhouse_name", length = 50, nullable = false)
    private String greenhouseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
    private List<Temperature> temperatureList;

    @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
    private List<Humidity> humidityList;

    @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
    private List<SoilHydration> soilHydrationList;

    @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
    private List<UVRadiation> uvRadiationList;

//    Overloaded constructors
    public Greenhouse(Long id, String greenhouseName, User user) {
        this.id = id;
        this.greenhouseName = greenhouseName;
        this.user = user;
    }

    public Greenhouse(Long id, String greenhouseName) {
        this.id = id;
        this.greenhouseName = greenhouseName;
    }

    public Greenhouse(String greenhouseName, Long userId) {
        this.greenhouseName = greenhouseName;
//        this.user = new User();
        this.user.setId(userId);
    }

    public Greenhouse(String greenhouseName, User user) {
        this.greenhouseName = greenhouseName;
        this.user = user;
        this.user.addGreenhouse(this);
    }

//    Convenient Method
    public void addTemperature(Temperature temperature) {
        this.temperatureList.add(temperature);
        temperature.setGreenhouse(this);
    }

    public void addHumidity(Humidity humidity) {
        this.humidityList.add(humidity);
        humidity.setGreenhouse(this);
    }

    public void addSoilHydration(SoilHydration soilHydration) {
        this.soilHydrationList.add(soilHydration);
        soilHydration.setGreenhouse(this);
    }

    public void addUVRadiation(UVRadiation uvRadiation) {
        this.uvRadiationList.add(uvRadiation);
        uvRadiation.setGreenhouse(this);
    }
}
