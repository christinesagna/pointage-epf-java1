USE pointage_epf;

INSERT INTO professeurs (matricule, nom, prenom, email, telephone, typeContrat, tauxHoraireXOF, dateEmbauche, photo, actif)
VALUES
    ('EPF-2026-ND-001', 'Ndiaye', 'Aminata', 'aminata.ndiaye@epf.sn', '770000001', 'PERMANENT', 12000.00, '2024-10-01', NULL, true),
    ('EPF-2026-FD-002', 'Fall', 'Demba', 'demba.fall@epf.sn', '770000002', 'VACATAIRE', 15000.00, '2025-01-15', NULL, true);

INSERT INTO utilisateurs (login, mot_de_passe_hash, role, professeur_id)
VALUES
    ('admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiW1R3n8A0x1F2Y3Z4A5B6C7D8E9F0G', 'ADMIN', NULL),
    ('scolarite', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiW1R3n8A0x1F2Y3Z4A5B6C7D8E9F0G', 'SCOLARITE', NULL);
