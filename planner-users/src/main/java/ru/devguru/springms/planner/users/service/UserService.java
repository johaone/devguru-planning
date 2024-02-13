package ru.devguru.springms.planner.users.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.devguru.springms.planner.entity.UserData;
import ru.devguru.springms.planner.users.repository.UserRepository;

import java.util.Optional;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserData add(UserData user) {
        return userRepository.save(user);
    }

    public UserData update(UserData user){
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserData> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserData findByEmailOrUsername(String email, String username) {
        return userRepository.findByEmailOrUsername(email, username);
    }

    public void deleteByEmail(String email){
        userRepository.deleteByEmail(email);
    }

    public Page<UserData> findByParam(String email, String username, Long id, PageRequest paging) {
        return userRepository.findByParam(email, username, id, paging);
    }
}
