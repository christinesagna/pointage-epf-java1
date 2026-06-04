package sn.epf.pointage.util;

import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;

public class MatriculeGenerator {
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);

    public static String generate(String nom, String prenom) {
        String initiales = (nom.substring(0, 1) + prenom.substring(0, 1)).toUpperCase();
        int annee = Year.now().getValue();
        int seq = SEQUENCE.getAndIncrement();
        return String.format("EPF-%d-%s-%03d", annee, initiales, seq);
    }
}
