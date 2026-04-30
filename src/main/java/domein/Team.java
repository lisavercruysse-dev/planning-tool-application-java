package domein;

import exception.TeamInformationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import util.TeamElement;

import java.util.*;

@Entity
@Table(name="teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAAM")
    private String naam;

    @ManyToOne
    @JoinColumn(name = "verantwoordelijkeId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Werknemer verantwoordelijke;

    //site

    @ManyToMany(mappedBy = "teams")
    private List<Werknemer> werknemers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "SITE_ID")
    private Site site;

    public Team(Werknemer verantwoordelijke, String naam, Site site) {
        Map<TeamElement, String> errors = new HashMap<>();

        if (verantwoordelijke == null) {
            errors.put(TeamElement.VERANTWOORDELIJKE, "Verantwoordelijke is verplicht");
        } else this.verantwoordelijke = verantwoordelijke;
        if (site == null) {
            errors.put(TeamElement.SITE, "Site is verplicht");
        }else this.site = site;
        if (naam == null || naam.isBlank()) {
            errors.put(TeamElement.NAAM, "Naam is verplicht");
        }
        else if (naam.trim().length() < 2){
            errors.put(TeamElement.NAAM, "Naam moet minstens 2 karakters zijn");
        } else this.naam = naam;

        if (!errors.isEmpty()) {
            throw new TeamInformationException(errors);
        }
    }

}
