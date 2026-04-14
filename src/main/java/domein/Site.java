package domein;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="sites")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name="locatie")
    private String locatie;

    @Column(name="capaciteit")
    private int capaciteit;

    @Column(name="operationeleStatus")
    private String operationeleStatus;

    @Column(name = "productieStatus")
    private String productieStatus;

    @OneToMany(mappedBy = "site")
    private List<Team> teams;

    public Site(String name, String locatie, int capaciteit, String operationeleStatus, String productieStatus) {
        this.name = name;
        this.locatie = locatie;
        this.capaciteit = capaciteit;
        this.operationeleStatus = operationeleStatus;
        this.productieStatus = productieStatus;
    }
}
