package boot.repository;

import boot.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends CrudRepository <Player, Integer>{
    Player findByName(String name);
}
