package org.miage.intervenantservice.boundary;

import org.miage.intervenantservice.entity.Intervenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervenantRepository extends JpaRepository<Intervenant, String> {
    
    // List<Intervenant> findByCodepostal(String codepostal);
}
