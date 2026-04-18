package domein;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        this.verantwoordelijke = verantwoordelijke;
        this.site = site;
        this.naam = naam;
        verantwoordelijke.getTeams().add(this);
        werknemers.add(verantwoordelijke);
    }

    public static Set<String> validate(Werknemer verantwoordelijke, String naam, Site site) {
        Set<String> errors = new HashSet<>();

        if (verantwoordelijke == null) {
            errors.add("Verantwoordelijke moet ingevuld zijn");
        }
        if (naam == null || naam.isBlank() || naam.trim().length() < 2) {
            errors.add("Naam moet minstens 2 karakters zijn");
        }
        if (site == null) {
            errors.add("Site moet ingevuld zijn");
        }
        return errors;
    }
}
