package org.miage.intervenantservice.entity;

import java.io.Serial;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Intervenant implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 8765432345678L;

    @Id
    private String id;

    private String nom;
    private String prenom;
    private String commune;
    private String codepostal;
    
}
