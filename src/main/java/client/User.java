package client;

import client.view.AppMenu;

public class User {

    private static User user = null ;
    private boolean isPlaying;
    private String username;
    private AppMenu appMenu ;

    public User(){
        isPlaying = false;
        username = "unknown";
    }

    public String getUsername() {
        return username;
    }

    public static User getInstance(){
        if (user ==null){
            user = new User();
        }
        return user;
    }

    public AppMenu getAppMenu(){
        return appMenu;
    }

    public void setAppMenu(AppMenu appMenu) {
        this.appMenu = appMenu;
    }

    public void setUsername(String text) {
        username = text;
    }
}