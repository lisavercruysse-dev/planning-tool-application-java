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
@Setter
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

    public Werknemer(String voornaam, String achternaam, String jobtitel, String wachtwoord) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.jobTitel = JobTitel.valueOf(jobtitel.toUpperCase());
        this.wachtwoord = wachtwoord;
        this.email = String.format("%s.%s@example.com", voornaam, achternaam.replaceAll("\\s+", ""));
    }


}
