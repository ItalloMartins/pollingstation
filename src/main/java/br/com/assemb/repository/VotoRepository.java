package br.com.assemb.repository;

import br.com.assemb.model.votos.VotosModel;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VotoRepository implements PanacheMongoRepository<VotosModel> {
}
