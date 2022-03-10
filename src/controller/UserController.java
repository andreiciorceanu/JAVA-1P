package controller;

import dao.UserDao;
import model.Rezervare;
import model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserController {
    private UserDao userDao;

    private UserController(){
        userDao = new UserDao(ConnectionController.getInstance().getConnection());
    }

    private static final class SingletonHolder{
        private static final UserController instance = new UserController();
    }

    public static final UserController getInstance(){
        return SingletonHolder.instance;
    }

    public boolean addUser(User user) throws Exception {
        return userDao.addUser(user);
    }

    public Optional<User> getUserByUsername(String username){
        return userDao.getUserByUsername(username);
    }

    public boolean signInUser(String username, String password) {
        return userDao.singInUser(username, password);
    }

    public boolean addRezervare(int userId, String numeRezervare, String sala, String numeFilm, Date date) {
        return userDao.addRezervare(userId, numeRezervare, sala, numeFilm, date);
    }

    public boolean afisareRezervari(int userId) {
        return userDao.afisareRezervari(userId);
    }

    public boolean stergeRezervare(int userId, String sala, Date date) {
        return  userDao.stergeRezervare(userId, sala, date);
    }

    public Optional<User> getUserStaffByUsername(String username){
        return userDao.getUserStaffByUsername(username);
    }

    public boolean signInStaff(String username, String password) {
        return userDao.singInStaff(username, password);
    }

    public boolean stergeUser(String username) {
        return userDao.stergeUser(username);
    }

    public boolean verificareRezervari(int userId) {
        return userDao.verificareRezervari(userId);
    }

    public Optional<List<User>> getAllUsersByDataAndSala(String sala, Date date){return userDao.getAllUsersByDataAndSala(sala,date);}

    public Optional<List<User>> getUseri(){return userDao.getAllUsersFromDb();}

    public Optional<List<Rezervare>> getrezervari(int idUser){return userDao.getRezervari(idUser);}


}
