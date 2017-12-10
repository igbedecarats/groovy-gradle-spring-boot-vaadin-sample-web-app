package app.users.usecase

import app.feedbacks.domain.Feedback
import app.feedbacks.domain.FeedbackRepository
import app.users.domain.RatedUser
import app.users.domain.User
import app.users.domain.UserRepository

class UserInteractor {

    private UserRepository userRepository

    private FeedbackRepository feedbackRepository

    UserInteractor(UserRepository userRepository,
                   FeedbackRepository feedbackRepository) {
        this.userRepository = userRepository
        this.feedbackRepository = feedbackRepository
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

    RatedUser calculateUserRating(final User user) {
        List<Feedback> feedbacks = feedbackRepository.findByRecipientId(user.getId())
        float rating = 0f
        for (Feedback feedback : feedbacks) {
            rating += feedback.getRating()
        }
        if (feedbacks.size() > 0) {
            rating = ((float) rating / (float) feedbacks.size()) as float
        }
        new RatedUser(user, rating)
    }
}