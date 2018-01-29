package boot.storage;

import boot.model.User;

public class UserStorage {

    private static ThreadLocal<User> userStorage = new ThreadLocal<>();

    public static User getUser(){
        return userStorage.get();
    }

    public static void setUser(User user){
        userStorage.set(user);
    }

    public static void removeUser(){
        userStorage.remove();
    }
}
