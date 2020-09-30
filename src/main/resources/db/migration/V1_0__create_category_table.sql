
CREATE TABLE IF NOT EXISTS `category` (
    `id` bigserial PRIMARY KEY,
    `name` varchar(255) NOT NULL,
    `parent_id` long REFERENCES category(id)
);

-- CREATE TABLE IF NOT EXISTS `subcategory` (
--     `id` bigserial PRIMARY KEY,
--     `name` varchar(255) NOT NULL,
--     `category_id` long NOT NULL REFERENCES category(id)
-- );
