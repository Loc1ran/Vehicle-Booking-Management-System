package com.loctran.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class UserFileDataAccess implements UserDAO {

    @Override
    public User[] getUsers(){
        File file = new File("src.com.local/users.csv");

        User[] users = new User[4];
        

        try{
            int i = 0;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                users[i++] = new User(UUID.fromString(split[0]),split[1]);

            }
            return users;
        }
        catch (IOException e){
            throw new IllegalStateException(e);
        }

    }
}
