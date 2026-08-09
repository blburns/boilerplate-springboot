-- Seed permissions and roles (users seeded at runtime with BCrypt)
INSERT INTO permissions (code, description) VALUES
  ('admin:access', 'Access admin shell'),
  ('items:read', 'View items'),
  ('items:write', 'Create/update/delete items'),
  ('users:read', 'View users'),
  ('users:write', 'Create/update users'),
  ('roles:read', 'View roles'),
  ('roles:write', 'Manage role assignments');

INSERT INTO roles (name, description) VALUES
  ('ADMIN', 'Full administrative access'),
  ('USER', 'Standard operator access to Items');

-- ADMIN gets every permission
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p WHERE r.name = 'ADMIN';

-- USER: admin shell entry + items
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.code IN ('admin:access', 'items:read', 'items:write')
WHERE r.name = 'USER';
