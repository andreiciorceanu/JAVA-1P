package dao;

import model.Rezervare;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private Connection con;

    private PreparedStatement addUser;
    private PreparedStatement getUserByUsername;
    private PreparedStatement getFilmByNameSalaData;
    private PreparedStatement addRezervare;
    private PreparedStatement afisareRezervari;
    private PreparedStatement stergeRezervare;
    private PreparedStatement getUserStaffByUsername;
    private PreparedStatement deleteUser;
    private PreparedStatement verificareRezervari;
    private PreparedStatement getAllUsers;
    private PreparedStatement allUsersFromDatabase;
    private PreparedStatement getAllRezervations;

    public UserDao(Connection con){
        this.con = con;

        try {
            addUser = con.prepareStatement("INSERT INTO useri VALUES (null, ?, ?)");
            getUserByUsername = con.prepareStatement("SELECT * FROM useri WHERE username = ?");
            getFilmByNameSalaData = con.prepareStatement("SELECT COUNT(*) FROM rezervariuseri WHERE film = ? AND sala = ? AND data = ?");
            addRezervare = con.prepareStatement("INSERT INTO rezervariuseri VALUES (null, ?, ?, ?, ?, ?)");
            afisareRezervari = con.prepareStatement("SELECT * FROM rezervariuseri WHERE idUser = ?");
            stergeRezervare = con.prepareStatement("DELETE FROM rezervariuseri WHERE idUser = ? AND sala = ? AND data = ?");

            getUserStaffByUsername = con.prepareStatement("SELECT * FROM personalcinematograf WHERE username = ?");
            deleteUser = con.prepareStatement("DELETE FROM useri WHERE username = ?");
            verificareRezervari = con.prepareStatement("SELECT * FROM rezervariuseri WHERE idUser = ?");
            getAllUsers = con.prepareStatement("SELECT * FROM useri INNER JOIN rezervariuseri ON rezervariuseri.idUser=useri.id WHERE rezervariuseri.data=? AND rezervariuseri.sala=? GROUP BY useri.id");
            allUsersFromDatabase = con.prepareStatement("SELECT * from useri");
            getAllRezervations = con.prepareStatement("SELECT * FROM rezervariuseri WHERE idUser = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addUser(User user) throws Exception {
        try {
            Optional<User> optionalUser = getUserByUsername(user.getUsername());
            if(optionalUser.isPresent()){
                throw new Exception("Username este deja folosit.");
            }

            addUser.setString(1, user.getUsername());
            addUser.setString(2, user.getPassword());
            return addUser.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Optional<User> getUserByUsername(String username){
        try {
            getUserByUsername.setString(1, username);
            ResultSet rs = getUserByUsername.executeQuery();
            if(rs.next()){
                User user = new User(rs.getInt(1),
                        rs.getString("username"),
                        rs.getString("password")) ;

                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean singInUser(String username, String password) {
        Optional<User> optionalUser = getUserByUsername(username);
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            return true;
        }
        return  false;
    }

    public boolean addRezervare(int userID, String numeFilm, String sala, String numeRezervare, Date date) {
        try {
            getFilmByNameSalaData.setString(1, numeFilm);
            getFilmByNameSalaData.setString(2, sala);
            getFilmByNameSalaData.setDate(3, (java.sql.Date) date);

            ResultSet rs = getFilmByNameSalaData.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 20 ) {
                    System.out.println("Sala plina!");
                    return false;
                }
                addRezervare.setInt(1, userID);
                addRezervare.setString(2, numeRezervare);
                addRezervare.setString(3, sala);
                addRezervare.setString(4, numeFilm);
                addRezervare.setDate(5, (java.sql.Date) date);
                return addRezervare.executeUpdate() != 0;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public boolean afisareRezervari(int userId) {
        try {
            afisareRezervari.setInt(1, userId);
            ResultSet rs = afisareRezervari.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getString(3) + " " + rs.getString(4)
                        + " " + rs.getString(5)+ " " + rs.getDate(6));

            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public boolean stergeRezervare(int userId, String sala, Date date) {
        try {
            stergeRezervare.setInt(1, userId);
            stergeRezervare.setString(2, sala);
            stergeRezervare.setDate(3, (java.sql.Date) date);
            return stergeRezervare.executeUpdate() != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Optional<User> getUserStaffByUsername(String username){
        try {
            getUserStaffByUsername.setString(1, username);
            ResultSet rs = getUserStaffByUsername.executeQuery();
            if(rs.next()){
                User user = new User(rs.getInt(1),
                        rs.getString("username"),
                        rs.getString("password")) ;

                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean singInStaff(String username, String password) {
        Optional<User> optionalUser = getUserStaffByUsername(username);
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            return true;
        }
        return  false;
    }

    public boolean stergeUser(String username) {
        try {
            deleteUser.setString(1, username);
            return deleteUser.executeUpdate() != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public boolean verificareRezervari(int userId) {
        try {
            verificareRezervari.setInt(1, userId);
            ResultSet rs = verificareRezervari.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getString(3) + " " + rs.getString(4)
                        + " " + rs.getString(5)+ " " + rs.getDate(6));

            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public Optional<List<User>> getAllUsersByDataAndSala(String sala, Date date){
        List<User> useri = new ArrayList<>();
        try {
            getAllUsers.setString(2,sala);
            getAllUsers.setDate(1, (java.sql.Date) date);
            ResultSet rs = getAllUsers.executeQuery();
            while(rs.next()){
                User user = new User(rs.getInt(1),
                        rs.getString("username"),
                        rs.getString("password")) ;
                useri.add(user);
            }
            return Optional.of(useri);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<List<User>> getAllUsersFromDb(){
        List<User> useri = new ArrayList<>();
        try {
            ResultSet rs = allUsersFromDatabase.executeQuery();
            while(rs.next()){
                User user = new User(rs.getInt(1),
                        rs.getString("username"),
                        rs.getString("password")) ;
                useri.add(user);
            }
            return Optional.of(useri);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<List<Rezervare>> getRezervari(Integer idUser) {
        List<Rezervare> rezervari = new ArrayList<>();
        try {
            getAllRezervations.setInt(1,idUser);
            ResultSet rs = getAllRezervations.executeQuery();
            while(rs.next()){
               Rezervare rezervare = new Rezervare
                       (rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),
                               rs.getString(5),rs.getDate(6));
               rezervari.add(rezervare);
            }
            return Optional.of(rezervari);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
