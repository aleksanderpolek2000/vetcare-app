CREATE TABLE clinic_services_category (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    slug VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    display_order INTEGER UNIQUE,
    created_by UUID NOT NULL,
    active BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_category_user FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE clinic_services (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    image_path VARCHAR(255),
    slug VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    display_order INTEGER UNIQUE,
    created_by UUID NOT NULL,
    active BOOLEAN NOT NULL DEFAULT FALSE,
    category_id UUID,
    CONSTRAINT fk_service_user FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT fk_service_category FOREIGN KEY (category_id) REFERENCES clinic_services_category (id)
);