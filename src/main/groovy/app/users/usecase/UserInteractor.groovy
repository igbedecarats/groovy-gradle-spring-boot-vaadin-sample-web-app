package app.users.usecase

import app.feedbacks.domain.Feedback
import app.feedbacks.usecase.FeedbackInteractor
import app.users.domain.RatedUser
import app.users.domain.User
import app.users.domain.UserRepository
import org.jsoup.helper.Validate

class UserInteractor {

    private UserRepository userRepository

    private FeedbackInteractor feedbackInteractor

    UserInteractor(final UserRepository userRepository,
                   final FeedbackInteractor feedbackInteractor) {
        this.userRepository = userRepository
        this.feedbackInteractor = feedbackInteractor
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
        Validate.notNull(user, "The User cannot be null")
        List<Feedback> feedbacks = feedbackInteractor.getFeedbacksCreatedByClient(user)
        float rating = feedbackInteractor.calculateRatingFrom(feedbacks)
        new RatedUser(user, rating)
    }
}