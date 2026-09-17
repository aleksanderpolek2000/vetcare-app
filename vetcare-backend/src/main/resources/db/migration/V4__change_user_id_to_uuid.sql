ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS fk_user_roles_user;

ALTER TABLE users ALTER COLUMN id DROP DEFAULT;

ALTER TABLE users
    ALTER COLUMN id TYPE UUID USING gen_random_uuid(),
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_user
    FOREIGN KEY (user_id) REFERENCES users(id);