package es.unizar.eina.frankenstory;

import android.app.Application;

public class MyApplication extends Application {
    String username;
    String password;
    String stars;
    String coins;
    String notifications;
    String iconUser;

    // USERNAME
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    // PASSSWORD
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // STARS
    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
    // COINS
    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
    // NOTIFICATIONS
    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }
    // ICONUSER
    public String getIconUser() {
        return iconUser;
    }

    public void setIconUser(String iconUser) {
        this.iconUser = iconUser;
    }
}
