package domein;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "verantwoordelijkeId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Werknemer verantwoordelijke;

    //site

    @ManyToMany(mappedBy = "teams")
    private List<Werknemer> werknemers;

    public Team(Werknemer verantwoordelijke) {
        if (verantwoordelijke == null) {
            throw new IllegalArgumentException("Verantwoordelijke moet ingevuld zijn");
        }
        this.verantwoordelijke = verantwoordelijke;
        verantwoordelijke.getTeams().add(this);
    }
}
