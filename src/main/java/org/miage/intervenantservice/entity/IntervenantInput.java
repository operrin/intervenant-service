package org.miage.intervenantservice.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntervenantInput {
    
    @NotNull
    @NotBlank
    private String nom;
    @NotNull
    @Size(min = 2)
    private String prenom;
    @NotNull
    @Size(min = 3)
    private String commune;
    @NotNull
    @Size(min = 5, max = 5)
    @Pattern(regexp = "[0-9]+")
    private String codepostal;

}
