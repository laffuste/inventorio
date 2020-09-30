-- INSERT INTO category(name)
-- VALUES ('Clothes'),     -- 1
--        ('Food'),        -- 2
--        ('Electronics'), -- 3
--        ('Toys & Games') -- 4
-- ;

-- INSERT INTO subcategory(name, category_id)
-- VALUES ('Clothing', 1),       -- 1
--        ('Shoes', 1),          -- 2
--        ('Watches', 1),        -- 3
--
--        ('Snacks', 2),         -- 4
--        ('Dairy', 2),          -- 5
--        ('Groceries', 2),      -- 6
--
--        ('Camera & Photo', 3), -- 7
--        ('Headphones', 3),     -- 8
--        ('Home Audio', 3),     -- 9
--
--        ('Boardgames', 4),        -- 10
--        ('Collectible Toys', 4) -- 11
-- ;

INSERT INTO category(name, parent_id)
VALUES ('Clothes', null),     -- 1
       ('Food', null),        -- 2
       ('Electronics', null), -- 3
       ('Toys & Games', null), -- 4

       ('Clothing', 1),       -- 5
       ('Shoes', 1),          -- 6
       ('Watches', 1),        -- 7

       ('Snacks', 2),         -- 8
       ('Dairy', 2),          -- 9
       ('Groceries', 2),      -- 10

       ('Camera & Photo', 3), -- 11
       ('Headphones', 3),     -- 12
       ('Home Audio', 3),     -- 13

       ('Boardgames', 4),        -- 14
       ('Collectible Toys', 4) -- 15
;