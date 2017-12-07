package app.users.usecase

import app.users.domain.User
import app.users.domain.UserRepository

class UserInteractor {

    private UserRepository userRepository

    UserInteractor(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    List<User> findAll() {
        (List<User>) userRepository.findAll()
    }

    void delete(User user) {
        userRepository.delete(user)
    }

    User save(User user) {
        userRepository.save(user)
    }
}