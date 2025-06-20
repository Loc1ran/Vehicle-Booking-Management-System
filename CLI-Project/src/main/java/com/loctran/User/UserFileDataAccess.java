package com.loctran.User;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository("file")
public class UserFileDataAccess implements UserDAO {

    @Override
    public List<User> getUsers(){
        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("users.csv")).getPath());

        List<User> users = new ArrayList<>();

        try{
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                users.add(new User(UUID.fromString(split[0]),split[1]));

            }
            return users;
        }
        catch (IOException e){
            throw new IllegalStateException(e);
        }

    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return getUsers().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public void deleteUser(UUID id) {

    }

    @Override
    public void updateUser(User updatedUser) {

    }

}
