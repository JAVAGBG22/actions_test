package com.example.unitTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    // ATT TESTA
    //createUser Method:
    // se til att ett giltigt user object skickas med och att det sparas korrekt
    // verifiera att det returnerade user objectet har ett ID

    //getUserById Method:
    // positivt scenario: när en user med ett id existerar, så ska metoden returnera usern
    // negativt scenario: när en user inte hittas, ska den kasta ett fel

    /**
     * Creates a new user by saving it to the repository.
     *
     * @param user The user to create.
     * @return The saved user with an assigned ID.
     */
    public User createUser(User user){
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The found user.
     * @throws RuntimeException if no user is found with the given ID.
     */
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }


}
