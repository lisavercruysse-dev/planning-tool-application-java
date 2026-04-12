package domein;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="sites")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String locatie;

    private int capaciteit;

    private String operationeleStatus;

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
