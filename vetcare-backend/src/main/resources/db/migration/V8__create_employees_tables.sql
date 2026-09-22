CREATE TABLE employees (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    description VARCHAR(2000),
    profile_image VARCHAR(255),
    user_id UUID UNIQUE,
    CONSTRAINT fk_employee_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE employee_certificates (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    display_order INTEGER,
    CONSTRAINT fk_certificate_employee FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE
);