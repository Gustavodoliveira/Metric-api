ALTER TABLE enterprise
    ALTER COLUMN cnpj TYPE VARCHAR(14);

-- Preserve an existing constraint, or attach an existing standalone unique index.
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conrelid = 'enterprise'::regclass
          AND conname = 'uk_enterprise_cnpj'
          AND contype = 'u'
    ) THEN
        IF EXISTS (
            SELECT 1
            FROM pg_index i
            JOIN pg_class idx ON idx.oid = i.indexrelid
            WHERE i.indrelid = 'enterprise'::regclass
              AND idx.relname = 'uk_enterprise_cnpj'
              AND i.indisunique
        ) THEN
            ALTER TABLE enterprise
                ADD CONSTRAINT uk_enterprise_cnpj
                UNIQUE USING INDEX uk_enterprise_cnpj;
        ELSE
            ALTER TABLE enterprise
                ADD CONSTRAINT uk_enterprise_cnpj UNIQUE (cnpj);
        END IF;
    END IF;
END;
$$;

ALTER TABLE enterprise
    ALTER COLUMN plano SET DEFAULT 'START',
    ALTER COLUMN status SET DEFAULT 'ACTIVE';

ALTER TABLE enterprise
    DROP CONSTRAINT IF EXISTS chk_enterprise_status,
    DROP CONSTRAINT IF EXISTS chk_enterprise_plano;

ALTER TABLE enterprise
    ADD CONSTRAINT chk_enterprise_status
        CHECK (status IN ('ACTIVE', 'TRIAL', 'CANCELED', 'EXPIRED', 'SUSPENDED')),
    ADD CONSTRAINT chk_enterprise_plano
        CHECK (plano IN ('START', 'PRO', 'BUSINESS'));
