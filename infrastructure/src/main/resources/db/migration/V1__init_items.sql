-- Sample Item table for the boilerplate CRUD feature
CREATE TABLE items (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(200)  NOT NULL,
  description VARCHAR(2000),
  created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_items_name ON items (name);
