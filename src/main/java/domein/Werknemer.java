package domein;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="werknemers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    private String email;

    @Column(name="password")
    private String wachtwoord;

    @Setter
    @ManyToOne
    @JoinTable(name = "team_werknemers",
        joinColumns = @JoinColumn(name = "werknemerId"),
        inverseJoinColumns = @JoinColumn(name = "teamId")
    )
    Team team;

    public Werknemer(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        setVoornaam(voornaam);
        setAchternaam(achternaam);
        setJobTitel(jobtitel);
        setWachtwoord(wachtwoord);
        this.email = String.format("%s.%s@example.com", voornaam, achternaam.replaceAll("\\s+", ""));
        this.team = team;
    }

    private void setVoornaam(String voornaam) {
        if (voornaam != null && voornaam.length() >= 2 && voornaam.matches("^[A-Za-z]+$")) {
            this.voornaam = voornaam;
        }
        else throw new IllegalArgumentException("Voornaam is niet correct");
    }

    private void setAchternaam(String achternaam) {
        if(achternaam != null && achternaam.length() >= 2 && achternaam.replaceAll("\\s+", "").length() >= 2 && achternaam.matches("^[A-Za-z ]+$")) {
            this.achternaam = achternaam;
        }
        else throw new IllegalArgumentException("Achternaam is niet correct");
    }

    private void setJobTitel(String jobtitel) {
        if (jobtitel == null || jobtitel.isBlank()) {
            throw new IllegalArgumentException("JobTitel moet gekozen zijn");
        }

        try {
            this.jobTitel = JobTitel.valueOf(jobtitel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ongeldige jobtitel: " + jobtitel);
        }
    }

    private void setWachtwoord(String wachtwoord) {
        if (wachtwoord.length() >= 8) {
            this.wachtwoord = wachtwoord;
        }
        else throw new IllegalArgumentException("Wachtwoord is niet correct");
    }
}
