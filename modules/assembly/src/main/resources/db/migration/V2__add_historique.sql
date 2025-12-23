-- Create historique table
CREATE TABLE IF NOT EXISTS historique
(
    id            uuid         NOT NULL,
    tenant_id     VARCHAR(50)  NOT NULL,
    object_type   VARCHAR(50)  NOT NULL,
    object_id     VARCHAR(50)  NOT NULL,
    performed_by  VARCHAR(100) NOT NULL,
    occurred_at   TIMESTAMP    NOT NULL,
    changements   JSONB        NOT NULL
);

CREATE INDEX historique_object_tenant on historique (object_type, object_id, tenant_id)
