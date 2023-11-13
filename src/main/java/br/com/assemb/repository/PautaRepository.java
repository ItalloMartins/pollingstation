package br.com.assemb.repository;

import br.com.assemb.model.PautaModel;
import com.mongodb.client.model.Filters;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.conversions.Bson;

@ApplicationScoped
public class PautaRepository implements PanacheMongoRepository<PautaModel> {
}
