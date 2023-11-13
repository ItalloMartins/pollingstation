package br.com.assemb.repository;

import br.com.assemb.model.PautaModel;
import br.com.assemb.model.SessaoModel;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessaoRepository implements PanacheMongoRepository<SessaoModel> {
}
