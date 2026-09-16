
ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS fk_user_roles_user;

ALTER TABLE user_roles
    ALTER COLUMN user_id TYPE UUID USING gen_random_uuid();