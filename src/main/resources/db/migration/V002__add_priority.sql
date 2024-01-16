ALTER TABLE todos
    ADD priority INTEGER;
ALTER TABLE todos
    ADD CONSTRAINT todos_uniq_owner_priority
        UNIQUE (owner, priority)
            DEFERRABLE INITIALLY DEFERRED;

CREATE OR REPLACE FUNCTION find_max_priority() RETURNS TRIGGER
AS
$$
BEGIN
    NEW.priority := (SELECT COALESCE(max(priority), 0) + 1 FROM todos WHERE owner = NEW.owner);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER priority_on_insert
    BEFORE insert
    ON todos
    FOR EACH ROW
EXECUTE PROCEDURE find_max_priority();