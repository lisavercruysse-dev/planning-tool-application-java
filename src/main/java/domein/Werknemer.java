package domein;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="werknemers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class Werknemer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="firstName")
    private String voornaam;

    @Column(name="lastName")
    private String achternaam;

    @Enumerated(EnumType.STRING)
    private JobTitel jobTitel;
    //private String jobTitel;

    private String email;

    @Column(name="password_hash")
    private String wachtwoord;

    @ManyToOne
    @JoinColumn(name = "teamId")
    Team team;

    public Werknemer(String voornaam, String achternaam, JobTitel jobtitel, String wachtwoord, Team team) {
        setVoornaam(voornaam);
        setAchternaam(achternaam);
        setJobTitel(jobtitel);
        setWachtwoord(wachtwoord);
        this.email = String.format("%s.%s@example.com", voornaam, achternaam.replaceAll("\\s+", ""));
        this.team = team;
    }

    public static Set<String> validate(String voornaam, String achternaam, String jobtitel, String wachtwoord){
        Set<String> errors = new LinkedHashSet<>();

      if (voornaam == null || voornaam.length() < 2 || !voornaam.matches("^[A-Za-z]+$")) {
            errors.add("Voornaam: minimaal 2 letters, geen speciale tekens");
        }
        if (achternaam == null || achternaam.replaceAll("\\s+", "").length() < 2 || !achternaam.matches("^[A-Za-z ]+$")) {
            errors.add("Achternaam: minimaal 2 letters, geen speciale tekens");
        }
        if (wachtwoord == null || wachtwoord.length() < 8) {
            errors.add("Wachtwoord moet minstens 8 tekens lang zijn");
        }
          
        if (jobtitel == null || jobtitel.isBlank()) {
            errors.add("JobTitel moet gekozen zijn");
        } else {
            try {
                JobTitel.valueOf(jobtitel.toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.add("Ongeldige JobTitel: " + jobtitel);
            }
        }
        return errors;

    }

}
