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
    private Werknemer verantwoordelijke;

    //site

    @OneToMany(mappedBy = "team")
    private List<Werknemer> werknemers;

    public Team(Werknemer verantwoordelijke) {
        this.verantwoordelijke = verantwoordelijke;
        verantwoordelijke.setTeam(this);
    }
}
