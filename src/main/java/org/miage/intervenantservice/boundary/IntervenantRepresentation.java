package org.miage.intervenantservice.boundary;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import org.miage.intervenantservice.control.IntervenantAssembler;
import org.miage.intervenantservice.entity.Intervenant;
import org.miage.intervenantservice.entity.IntervenantInput;
import org.miage.intervenantservice.entity.IntervenantValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@ResponseBody
@RequestMapping(value = "/intervenants", produces = MediaType.APPLICATION_JSON_VALUE)
public class IntervenantRepresentation {

    private final IntervenantResource ir;
    private final IntervenantAssembler ia;
    private final IntervenantValidator iv;

    public IntervenantRepresentation(IntervenantResource ir, IntervenantValidator iv, IntervenantAssembler ia) {
        this.ir = ir;
        this.ia = ia;
        this.iv = iv;
    }

    // GET all
    @GetMapping
    public ResponseEntity<?> getAllIntervenants() {
        return ResponseEntity.ok(ia.toCollectionModel(ir.findAll()));
        //return ResponseEntity.ok(ir.findAll());
    }

    // GET one intervenant
    @GetMapping(value = "/{intervenantId}")
    public ResponseEntity<?> getIntervenant(@PathVariable("intervenantId") String id) {
        return Optional.of(ir.findById(id))
                .filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(ia.toModel(i.get())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> save(@RequestBody @Valid IntervenantInput intervenant) {
        
        Intervenant toSave = new Intervenant(UUID.randomUUID().toString(),
                intervenant.getNom(),
                intervenant.getPrenom(),
                intervenant.getCommune(),
                intervenant.getCodepostal());
        Intervenant saved = ir.save(toSave);
        URI location = linkTo(IntervenantRepresentation.class).slash(saved.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{intervenantId}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable("intervenantId") String id,
            @RequestBody @Valid IntervenantInput newIntervenant) {
        if (!ir.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Intervenant toSave = new Intervenant(id,
                newIntervenant.getNom(),
                newIntervenant.getPrenom(),
                newIntervenant.getCommune(),
                newIntervenant.getCodepostal());
        ir.save(toSave);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{intervenantId}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("intervenantId") String id) {
        Optional<Intervenant> intervenant = ir.findById(id);
        intervenant.ifPresent(ir::delete);
        return ResponseEntity.noContent().build();
    }

    // PATCH
    @PatchMapping(value = "/{intervenantId}")
    @Transactional
    public ResponseEntity<Object> partialUpdate(@PathVariable("intervenantId") String id,
            @RequestBody IntervenantInput newIntervenant) {
        Optional<Intervenant> body = ir.findById(id);
        if (body.isPresent()) {
            Intervenant existingIntervenant = body.get();
            if (StringUtils.hasLength(newIntervenant.getNom())) {
                existingIntervenant.setNom(newIntervenant.getNom());
            }
            if (StringUtils.hasLength(newIntervenant.getPrenom())) {
                existingIntervenant.setPrenom(newIntervenant.getPrenom());
            }
            if (StringUtils.hasLength(newIntervenant.getCommune())) {
                existingIntervenant.setCommune(newIntervenant.getCommune());
            }
            if (StringUtils.hasLength(newIntervenant.getCodepostal())) {
                existingIntervenant.setCodepostal(newIntervenant.getCodepostal());
            }
            iv.validate(new IntervenantInput(existingIntervenant.getNom(),
                    existingIntervenant.getPrenom(),
                    existingIntervenant.getCommune(),
                    existingIntervenant.getCodepostal()));
            ir.save(existingIntervenant);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
