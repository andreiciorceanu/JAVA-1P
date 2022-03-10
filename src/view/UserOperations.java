package view;

import Utils.Filme;
import controller.UserController;
import model.Rezervare;
import model.User;

import java.io.*;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserOperations {
    private static UserController controller = UserController.getInstance();

    private UserOperations() {
    }

    public static void userOperations() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Te afli in fereastra de logare ");
        while (true) {
            System.out.println("Introduceti comanda");
            String cmd = sc.nextLine();

            switch (cmd) {
                case "logare":
                    System.out.println("Intrduceti username-ul:");
                    String username = sc.nextLine();
                    System.out.println("Introduceti parola");
                    String password = sc.nextLine();

                    if (!testMail(username) || !testPassword(password)) {
                        System.out.println("Mail invalid sau parola invalida");
                        break;
                    }

                    User user = new User(1, username, password);
                    try {
                        controller.addUser(user);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "signInUser":
                    System.out.println("Intrduceti username-ul:");
                    String username1 = sc.nextLine();
                    System.out.println("Introduceti parola");
                    String password1 = sc.nextLine();

                    if (controller.signInUser(username1, password1)) {
                        System.out.println("Corect");
                    } else {
                        System.out.println("Username sau parola gresita");
                        break;
                    }

                    System.out.println("Te afli in fereasta de user");
                    int id = controller.getUserByUsername(username1).get().getId();

                    while (true) {
                        System.out.println("Introduceti comanda");
                        String cmd1 = sc.nextLine();

                        switch (cmd1) {
                            case "rezervare":
                                System.out.println("Va rog introduceti numele filmului, numarul salii si data");
                                System.out.println("Alegeti unul dintre filmele:");
                                System.out.println(Filme.filme);
                                String numeFilm = sc.nextLine();
                                String numarSala = sc.nextLine();
                                String dataRez = sc.nextLine();
                                try {

                                    Date data1 = Date.valueOf(dataRez);
                                    if (data1.before(Date.valueOf(LocalDate.now()))) {
                                        System.out.println("Data introdusa nu e valida");
                                        break;
                                    }

                                    boolean ok = controller.addRezervare(id, username1, numarSala, numeFilm, data1);
                                    if (ok) {
                                        System.out.println("Rezervare facuta cu succes!");
                                    } else {
                                        System.out.println("Rezervarea a esuat!");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case "afisareRezervari":
                                try {
                                    controller.afisareRezervari(id);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "stergeRezervare":
                                System.out.println("Va rugam introduceti numarul salii si data rezervarii");
                                String sala = sc.nextLine();
                                String data2 = sc.nextLine();
                                try {
                                    Date data3 = Date.valueOf(data2);
                                    controller.stergeRezervare(id, sala, data3);
                                    System.out.println("Rezervarea a fost stearsa!");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;


                            case "exit":
                                System.exit(0);
                                break;

                            default:
                                break;
                        }
                    }

                case "signInStaff":
                    System.out.println("Intrduceti username-ul:");
                    String usernameStaff = sc.nextLine();
                    System.out.println("Introduceti parola");
                    String passwordStaff = sc.nextLine();

                    if (controller.signInStaff(usernameStaff, passwordStaff)) {
                        System.out.println("Corect!");
                    } else {
                        System.out.println("Username sau parola gresita");
                        break;
                    }

                    System.out.println("Te afli in fereasta de personal Cinematograf");
                    int idStaff = controller.getUserStaffByUsername(usernameStaff).get().getId();

                    while (true) {
                        System.out.println("Introduceti comanda");
                        String cmd2 = sc.nextLine();

                        switch (cmd2) {
                            case "verificareCapacitate":
                                System.out.println("Introduceti numarul salii si data rezervarii ");
                                String nrSala = sc.nextLine();
                                Date date = Date.valueOf(sc.next());
                                List<User> useri = controller.getAllUsersByDataAndSala(nrSala, date).get();
                                System.out.println(useri);
                                System.out.println("Capacitatea salii este de " + (20 - useri.size()));
                                break;


                            case "stergeRezervare":
                                System.out.println("Va rugam introduceti username-ul user-ului, numarul salii si data rezervarii");
                                String username2 = sc.nextLine();
                                String sala = sc.nextLine();
                                String data4 = sc.nextLine();
                                int idUser = controller.getUserByUsername(username2).get().getId();

                                try {
                                   Date data5 = Date.valueOf(data4);
                                    if (controller.stergeRezervare(idUser, sala, data5)) {
                                        System.out.println("Rezervarea a fost stearsa!");
                                    } else {
                                        System.out.println("S-a produs o eroare!");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case "stergeUtilizator":
                                System.out.println("Va rugam introduceti username-ul utilizatorului");
                                String username3 = sc.nextLine();

                                if (controller.stergeUser(username3)) {
                                    System.out.println("User sters!");
                                } else {
                                    System.out.println("S-a produs o eroare!");
                                }
                                break;

                            case "verificareRezervari":
                                System.out.println("Va rugam introduceti username-ul utilizatorului");
                                String username4 = sc.nextLine();
                                int idUser1 = controller.getUserByUsername(username4).get().getId();
                                if (!controller.verificareRezervari(idUser1)) {
                                    System.out.println("S-a produs o eroare!");
                                }

                                break;

                            case "salvareDB":
                                File file = new File("Users&Reservations");
                                try(BufferedWriter buf = new BufferedWriter(new FileWriter(file, true))){
                                    buf.write("Salvare: " + LocalDate.now());
                                    List<User> Allusers = controller.getUseri().get();
                                    for(User u : Allusers) {
                                        buf.write("User: " + u.getUsername() + "\n");
                                        buf.write("Password " + u.getPassword() + "\n");
                                        buf.write("Rezervari " + "\n");
                                        List<Rezervare> rezervareList = controller.getrezervari(u.getId()).get();
                                        buf.write(String.valueOf(rezervareList));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                break;

                            case "exit":
                                System.exit(0);
                                break;

                            default:
                                break;
                        }


                    }


                case "exit":
                    System.exit(0);
                    break;

            }
        }

    }

    protected static boolean testMail(String email) {
        return email.contains("@") && email.endsWith(".com");
    }

    protected static boolean testPassword(String password) {
        String s = "0123456789";
        String carac = "`~!@#$%^&*(){}_+=-][';:<>,.?|";
        boolean isNumber = false;
        boolean isSpecial = false;
        for(int i =0; i < password.length(); i++) {
            if(s.contains(String.valueOf(password.charAt(i)))) {
                isNumber = true;
            }
            if(carac.contains(String.valueOf(password.charAt(i)))) {
                isSpecial = true;
            }
        }
        return isNumber && isSpecial;
    }
}
