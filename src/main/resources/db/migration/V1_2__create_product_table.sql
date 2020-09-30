
CREATE TABLE IF NOT EXISTS `product` (

    `id` bigserial PRIMARY KEY,
    `name` varchar(255) NOT NULL,
--     `subcategory_id` long NOT NULL REFERENCES subcategory(id),
    `category_id` long NOT NULL REFERENCES category(id),
    `quantity` int NOT NULL

)
