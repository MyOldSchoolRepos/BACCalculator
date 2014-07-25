package brillianceanimationstudio.brandonward.baccalculator.service;

/**
 * Created by BrandonWard on 7/21/2014.
 */

import brillianceanimationstudio.brandonward.baccalculator.domain.*;

import java.io.*;
import java.util.*;

public class userInfoSIOImpl {
    private userInfo userInfo = null;//Cache of data (of our SIO of userInfo)
    private File file = new File("userInfo.sio");

    public userInfoSIOImpl() {//Constructor to initialize our list.
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            userInfo = (userInfo) in.readObject();
            in.close();
        } catch (Exception e) {

        } finally {
            if (userInfo == null) {
                userInfo = new userInfo();
            }
        }
    }

    public userInfo getUserInfo() {
        return userInfo;
    }

    public userInfo updateUserInfo(userInfo userInfo) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(userInfo);
            out.flush();
            out.close();
        } catch (Exception e) {

        }

        return userInfo;
    }
}

