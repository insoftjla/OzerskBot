package sd.insoft.service;

import sd.insoft.model.TelegramUser;
import sd.insoft.repo.TelegramUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class TelegramUserService {
    private final TelegramUserRepository userRepository;

    public TelegramUserService(TelegramUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public TelegramUser findByChatId(long id){
        return userRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findAll(){
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(TelegramUser user){
        userRepository.save(user);
    }
}
