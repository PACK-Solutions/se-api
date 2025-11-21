-- Create connaissance_client table
CREATE TABLE IF NOT EXISTS connaissance_client (
    id_personne BIGINT NOT NULL,
    statut_ppe JSONB NULL,
    statut_proche_ppe JSONB NULL,
    avec_vigilance_renforcee JSONB NULL,
    CONSTRAINT vigilance_required_for_ppe_and_proche_ppe CHECK (((statut_ppe IS NULL) AND (statut_proche_ppe IS NULL)) OR (avec_vigilance_renforcee IS NOT NULL))
);

-- Ensure uniqueness on id_personne
ALTER TABLE connaissance_client
    ADD CONSTRAINT connaissance_client_id_personne_unique UNIQUE (id_personne);

-- Create historique table
CREATE TABLE IF NOT EXISTS connaissance_client_historique (
    id uuid NOT NULL,
    id_personne BIGINT NOT NULL,
    audit_user VARCHAR(100) NOT NULL,
    audit_type VARCHAR(30) NOT NULL,
    audit_date VARCHAR(100) NOT NULL,
    modifications JSONB NOT NULL,
    CONSTRAINT fk_connaissance_client_historique_id_personne FOREIGN KEY (id_personne)
        REFERENCES connaissance_client(id_personne) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Ensure uniqueness on historique id
ALTER TABLE connaissance_client_historique
    ADD CONSTRAINT connaissance_client_historique_id_unique UNIQUE (id);
