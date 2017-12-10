package app.users.domain

class RatedUser extends User {

    private float rating

    float getRating() {
        rating
    }

    RatedUser(final User user, final float rating) {
        super(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getRole())
        this.rating = rating
    }

}
