package sn.epf.pointage;

import sn.epf.pointage.config.HibernateUtil;

public class App {
    public static void main(String[] args) {
        System.out.println("Projet Pointage EPF - Backend OK");
        HibernateUtil.getSessionFactory();
        System.out.println("Hibernate initialisé avec succès.");
    }
}
