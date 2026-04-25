package domein;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.SiteElement;

import java.util.*;

@Entity
@Table(name="sites")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    private Site(Builder builder){
        this.name = builder.name;
        this.locatie = builder.locatie;
        this.capaciteit = builder.capaciteit;
        this.operationeleStatus = builder.operationeleStatus;
        this.productieStatus = builder.productieStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private Map<SiteElement, String> errors;

        private String name;
        private String locatie;
        private int capaciteit;
        private String operationeleStatus;
        private String productieStatus;

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder locatie(String locatie){
            this.locatie = locatie;
            return this;
        }

        public Builder capaciteit(Integer capaciteit){
            this.capaciteit = capaciteit;
            return this;
        }

        public Builder operationeleStatus(String operationeleStatus){
            this.operationeleStatus = operationeleStatus;
            return this;
        }

        public Builder productieStatus(String productieStatus){
            this.productieStatus = productieStatus;
            return this;
        }

        public Site build(){

            return new Site(this);
        }
    }

    /*
    public Site(String name, String locatie, int capaciteit, String operationeleStatus, String productieStatus) {
        this.name = name;
        this.locatie = locatie;
        this.capaciteit = capaciteit;
        this.operationeleStatus = operationeleStatus;
        this.productieStatus = productieStatus;
    } */
}
