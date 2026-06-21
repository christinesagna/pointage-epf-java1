package sn.epf.pointage.service.export;

import net.sf.jasperreports.engine.*;
import sn.epf.pointage.model.RapportMensuel;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class RapportPdfExportService {

    public void exporter(RapportMensuel rapport, Path destination) {
        try (InputStream input = getClass().getResourceAsStream("/reports/rapport_mensuel.jrxml")) {
            if (input == null) {
                throw new IllegalStateException("Template Jasper introuvable: /reports/rapport_mensuel.jrxml");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(input);
            Map<String, Object> params = new HashMap<>();
            params.put("professeur", rapport.getProfesseur().getNomComplet());
            params.put("matricule", rapport.getProfesseur().getMatricule());
            params.put("mois", rapport.getMois());
            params.put("annee", rapport.getAnnee());
            params.put("heures", rapport.getHeuresRealisees().toPlainString());
            params.put("montant", rapport.getMontantXOF().toPlainString());
            params.put("statut", rapport.getStatut().name());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource(1));
            JasperExportManager.exportReportToPdfFile(jasperPrint, destination.toAbsolutePath().toString());
        } catch (Exception e) {
            throw new RuntimeException("Erreur export PDF : " + e.getMessage(), e);
        }
    }
}
