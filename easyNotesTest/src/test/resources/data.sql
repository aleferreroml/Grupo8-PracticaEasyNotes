INSERT INTO `user`
    (`id`,`first_name`,`last_name`)
VALUES
    (1, 'user1','last1'),
    (2, 'user2','last2'),
    (3, 'user3','last3'),
    (4, 'user4','last4'),
    (5, 'user5','last5'),
    (6, 'user6','last6'),
    (7, 'user7','last7'),
    (8, 'user8','last8');
    (9, 'user9','last9');


INSERT INTO `note`
    (`id`,`content`,`created_at`,`title`,`updated_at`,`author_id`)
VALUES
    (0,'Si el tiempo no se me pasa más cuando se corta la luz1','2019-12-06','Que hacemos1?','2021-12-06',1),
    (1,'Si el tiempo no se me pasa más cuando se corta la luz2','2020-12-06','Que hacemos2?','2021-12-06',1),
    (14,'Si el tiempo no se me pasa más cuando se corta la luz3','2020-12-06','Que hacemos3?','2021-12-06',1),
    (29,'Si el tiempo no se me pasa más cuando se corta la luz4','2020-12-06','Que hacemos4?','2021-12-06',1),
    (30,'Si el tiempo no se me pasa más cuando se corta la luz4','2020-12-06','Que hacemos4?','2021-12-06',1),
    (37,'Si el tiempo no se me pasa más cuando se corta la luz5','2021-12-06','Que hacemos5?','2021-12-06',1),
    (40,'Si el tiempo 2','2021-12-19','Que hacemos4?','2021-12-19',2),
    (41,'Si el tiempo 3','2021-12-18','Que hacemos4?','2021-12-18',2),
    (42,'Si el tiempo 4','2021-12-17','Que hacemos5?','2021-12-17',2),
    (50,'Si el 1','2021-12-17','Que hacemos4?','2021-12-19',3),
    (51,'Si el 3','2021-12-10','Que hacemos4?','2021-12-18',3),
    (52,'Si el 4','2021-12-03','Que hacemos5?','2021-12-17',3);
    /*getCategory - Diario*/
    (90,'Si el tiempo no se me pasa más cuando se corta la luz91','2021-12-19','Que hacemos1?','2021-12-20',9),
    (91,'Si el tiempo no se me pasa más cuando se corta la luz92','2021-12-18','Que hacemos2?','2021-12-20',9),
    (914,'Si el tiempo no se me pasa más cuando se corta la luz93','2021-12-17','Que hacemos3?','2021-12-20',9),
    (901,'Si el tiempo no se me pasa más cuando se corta la luz91','2021-12-20','Que hacemos1?','2021-12-20',9),



INSERT INTO `thank`
    (`note_id`,`user_id`,`created_at`)
VALUES
    (0,2,'2019-12-16'),
    (0,3,'2019-12-26'),
    (0,4,'2019-12-05'),
    (0,5,'2019-12-07'),
    (0,6,'2019-12-08'),
    (0,7,'2019-12-09'),
    (0,8,'2019-12-20'),

    (1,2,'2020-12-06'),
    (1,3,'2020-12-06'),
    (1,4,'2020-12-06'),
    (1,5,'2020-12-06'),
    (1,6,'2020-12-06'),

    (1,7,'2021-12-06'),

    (14,2,'2020-12-06'),
    (14,3,'2020-12-06'),
    (14,4,'2020-12-06'),

    (14,5,'2021-12-06'),

    (29,2,'2020-12-06'),
    (29,4,'2020-12-06'),
    (29,3,'2020-12-06'),
    (29,5,'2020-12-06'),

    (29,6,'2021-12-06'),

    (30,2,'2020-12-06'),
    (30,3,'2020-12-06'),

    (30,4,'2021-12-06'),


    (37,2,'2021-12-06'),
    (37,3,'2021-12-06'),
    (37,4,'2021-12-06'),
    (37,5,'2021-12-06'),
    (37,6,'2021-12-06'),
    (37,7,'2021-12-06');

