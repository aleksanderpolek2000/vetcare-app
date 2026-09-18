CREATE TABLE pets (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    species VARCHAR(255) NOT NULL,
    breed VARCHAR(255),
    date_of_birth TIMESTAMP NOT NULL,
    owner_id UUID NOT NULL,
    created_at TIMESTAMP,
    modified_at TIMESTAMP,
    CONSTRAINT fk_pets_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_pets_owner_id ON pets (owner_id);