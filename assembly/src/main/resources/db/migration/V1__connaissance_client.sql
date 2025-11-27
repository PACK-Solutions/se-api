-- Create connaissance_client table
CREATE TABLE IF NOT EXISTS connaissance_client
(
    id_personne              BIGINT       NOT NULL,
    tenant_id                VARCHAR(50)  NOT NULL,
    statut_ppe               JSONB        NULL,
    statut_proche_ppe        JSONB        NULL,
    avec_vigilance_renforcee JSONB        NULL,
    CONSTRAINT vigilance_required_for_ppe_and_proche_ppe CHECK (((statut_ppe IS NULL) AND (statut_proche_ppe IS NULL)) OR (avec_vigilance_renforcee IS NOT NULL)),
    CONSTRAINT connaissance_client_unique_person_tenant UNIQUE (id_personne, tenant_id)
);

-- Create historique table
CREATE TABLE IF NOT EXISTS connaissance_client_historique
(
    id            uuid         NOT NULL,
    id_personne   BIGINT       NOT NULL,
    tenant_id     VARCHAR(50)  NOT NULL,
    audit_user    VARCHAR(100) NOT NULL,
    audit_type    VARCHAR(30)  NOT NULL,
    audit_date    VARCHAR(100) NOT NULL,
    modifications JSONB        NOT NULL
);

-- Ensure uniqueness on historique id
ALTER TABLE connaissance_client_historique
    ADD CONSTRAINT connaissance_client_historique_id_unique UNIQUE (id);

-- Add composite foreign key from historique to connaissance_client (id_personne, tenant_id)
ALTER TABLE connaissance_client_historique
    ADD CONSTRAINT fk_historique_connaissance_client
        FOREIGN KEY (id_personne, tenant_id)
            REFERENCES connaissance_client (id_personne, tenant_id)
            ON DELETE RESTRICT ON UPDATE RESTRICT;
