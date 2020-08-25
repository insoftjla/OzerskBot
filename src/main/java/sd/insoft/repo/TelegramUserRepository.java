package sd.insoft.repo;

import sd.insoft.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Integer> {

    TelegramUser findByChatId(long id);

}
