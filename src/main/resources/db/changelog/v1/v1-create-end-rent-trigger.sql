--create-end-rent-trigger

CREATE OR REPLACE FUNCTION end_rent_and_update_location()
RETURNS trigger AS $$
DECLARE
active_rent_id BIGINT;
BEGIN
    IF OLD.rent_id IS NOT NULL AND NEW.rent_id IS NULL THEN
        SELECT id INTO active_rent_id
        FROM rents
        WHERE auto_id = OLD.id AND status = 'ACTIVE'
        LIMIT 1;

        IF active_rent_id IS NOT NULL THEN
            UPDATE rents
            SET status = 'FINISHED',
            end_rental = now()
            WHERE id = active_rent_id;
        END IF;

        IF NEW.status IS DISTINCT FROM 'FREE' THEN
            NEW.status := 'FREE';
        END IF;
    END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_end_rent_and_update_location
    BEFORE UPDATE ON automobiles
    FOR EACH ROW
    WHEN (
        OLD.rent_id IS NOT NULL AND NEW.rent_id IS NULL
        )
    EXECUTE FUNCTION end_rent_and_update_location();