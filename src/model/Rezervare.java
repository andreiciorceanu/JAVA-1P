package model;

import java.sql.Date;

public class Rezervare {
    private int id;
    private int idUser;
    private String numeRezervare;
    private String sala;
    private String film;
    private Date dataProgramare;

    public Rezervare(int id, int idUser, String numeRezervare, String sala, String film, Date dataProgramare) {
        this.id = id;
        this.idUser = idUser;
        this.numeRezervare = numeRezervare;
        this.sala = sala;
        this.film = film;
        this.dataProgramare = dataProgramare;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNumeRezervare() {
        return numeRezervare;
    }

    public void setNumeRezervare(String numeRezervare) {
        this.numeRezervare = numeRezervare;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public Date getDataProgramare() {
        return dataProgramare;
    }

    public void setDataProgramare(Date dataProgramare) {
        this.dataProgramare = dataProgramare;
    }

    @Override
    public String toString() {
        return "Programari{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", numeRezervare='" + numeRezervare + '\'' +
                ", sala='" + sala + '\'' +
                ", film='" + film + '\'' +
                ", dataProgramare=" + dataProgramare +
                '}';
    }
}
