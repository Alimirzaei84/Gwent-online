package client;

import client.view.AppMenu;

public class User {

    private static User user = null ;
    private String username;
    private AppMenu appMenu ;

    public User(){
        username = "unknown";
    }

    public String getUsername() {
        return username;
    }

    public static User getInsetance(){
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
}
