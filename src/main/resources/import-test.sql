ALTER SEQUENCE Etat_SEQ INCREMENT 1;
ALTER SEQUENCE Pont_SEQ INCREMENT 1;
ALTER SEQUENCE Region_SEQ INCREMENT 1;
ALTER SEQUENCE HistoriqueEtatPont_SEQ INCREMENT 1;
ALTER SEQUENCE UniteAnalogique_SEQ INCREMENT 1;
ALTER SEQUENCE UniteNumerique_SEQ INCREMENT 1;
ALTER SEQUENCE Capteur_SEQ INCREMENT 1;
ALTER SEQUENCE Releve_SEQ INCREMENT 1;

INSERT INTO Etat(id, nom)
VALUES 
	(nextval('Etat_SEQ'), 'En maintenance'),
 	(nextval('Etat_SEQ'), 'Actif');

INSERT INTO Region(id,nom,codeRegion)
VALUES
	(nextval('Region_SEQ'), 'Île-de-France', 11),
	(nextval('Region_SEQ'), 'Auvergne-Rhône-Alpes', 84),
    (nextval('Region_SEQ'), 'Bourgogne-Franche-Comté', 27),
    (nextval('Region_SEQ'), 'Bretagne', 53),
    (nextval('Region_SEQ'), 'Centre-Val de Loire', 24),
    (nextval('Region_SEQ'), 'Corse', 94),
    (nextval('Region_SEQ'), 'Grand Est', 44),
    (nextval('Region_SEQ'), 'Hauts-de-France', 32),
    (nextval('Region_SEQ'), 'Normandie', 28),
    (nextval('Region_SEQ'), 'Nouvelle-Aquitaine', 75),
    (nextval('Region_SEQ'), 'Occitanie', 76),
    (nextval('Region_SEQ'), 'Pays de la Loire', 52),
    (nextval('Region_SEQ'), 'Provence-Alpes-Côte d''Azur', 93);

INSERT INTO UniteAnalogique(id, nom, symbole)
VALUES 
	(nextval('UniteAnalogique_SEQ'), 'Celcius', '°C'),
	(nextval('UniteAnalogique_SEQ'), 'Pascal', 'Pa'),
	(nextval('UniteAnalogique_SEQ'), 'humidity', '%');

INSERT INTO UniteNumerique(id, etathaut, etatbas)
VALUES (nextval('UniteNumerique_SEQ'), 'Present', 'Non present');


INSERT INTO Pont(id,nom,longueur,largeur,latitude,longitude,dateCreation,region_id, etat_id)
VALUES
    (nextval('Pont_SEQ'),'Pont de Rézy', 25.7, 4, '48.8298', '2.9036', '1950-01-01',1, 1),
    (nextval('Pont_SEQ'),'Pont de la Grande Rue', 41.4, 9, '48.8255', '2.9553', '1975-01-01',1, 1),

	(nextval('Pont_SEQ'),'Pont de Secheras sur le Peage', 15, 3.5, '45.1424', '4.8060', '1950-01-01',2, 1),
    (nextval('Pont_SEQ'),'Pont de Bobon', 3.2, 4.4, '45.1033', '4.7951', '1950-01-01',2, 1),

	(nextval('Pont_SEQ'),'Pont de Revignon', 4, 6.5, '46.9383', '5.2108', '1975-01-01',3, 1),
    (nextval('Pont_SEQ'),'Pont du Port sur le Meuzin', 8.4, 6.7, '46.9631', '5.0226', '1975-01-01',3, 1),

	(nextval('Pont_SEQ'),'Pont de la Renaudie', 4, 5.2, '48.3070', '-2.1570', '1950-01-01',4, 1),
    (nextval('Pont_SEQ'),'Pont Beaumont', 70.4, 4.2, '48.3016', '-2.0875', '1975-01-01',4, 1),

	(nextval('Pont_SEQ'),'Pont de Boissereau', 18.6, 3.5, '46.8384', '2.3357', '1950-01-01',5, 1),
    (nextval('Pont_SEQ'),'Pont des Thureaux', 38.2, 5, '47.1228', '2.0565', '1975-01-01',5, 1),

	(nextval('Pont_SEQ'),'Pont du Fango', 39, 3.4, '42.3754', '8.7792', '1950-01-01',6, 1),
    (nextval('Pont_SEQ'),'Ponte Vecchiu', 14, 3.1, '42.3968', '8.7145', '1950-01-01',6, 1),

	(nextval('Pont_SEQ'),'Pont rue du moulin 2', 9.3, 6, '48.1890', '7.4912', '1950-01-01',7, 1),
    (nextval('Pont_SEQ'),'Pont rue principale', 3.5, 8, '48.1878', '7.5754', '1975-01-01',7, 1),

	(nextval('Pont_SEQ'),'Pont rue Coquette franchissant La Somme', 33, 6.4, '50.0418', '1.9461', '1950-01-01',8, 1),
    (nextval('Pont_SEQ'),'Pont Les Patures', 11.7, 4.5, '50.16', '1.6877', '1950-01-01',8, 1),

	(nextval('Pont_SEQ'),'Pont rue du Thil francissant L Yères', 4, 16, '49.9966', '1.3370', '1950-01-01',9, 1),
    (nextval('Pont_SEQ'),'Pont voute de la Reboursiere', 11.5, 6.6, '48.7051', '0.1768', '1950-01-01',9, 1),

	(nextval('Pont_SEQ'),'Pont Devanne / La Tude', 18.2, 40.1, '45.2278', '0.0348', '1950-01-01',10, 1),
    (nextval('Pont_SEQ'),'Pont de chez Vrignaud', 37.5, 4.4, '45.2073', '0.0148', '1950-01-01',10, 1),

	(nextval('Pont_SEQ'),'Pont Thomas-La Bataillouze', 7.5, 4.8, '43.4093', '0.4040', '1975-01-01',11, 1),
    (nextval('Pont_SEQ'),'Pont de Saint-Jaymes', 3, 5.9, '43.4280', '0.3698', '1950-01-01',11, 1),

	(nextval('Pont_SEQ'),'Pont des Chenêts', 8, 5.4, '47.8277', '-0.9720', '1975-01-01',12, 1),
    (nextval('Pont_SEQ'),'Pont voûte Rue M Grimault aval', 4, 10.9, '47.8439', '-1.0461', '1950-01-01',12, 1),

	(nextval('Pont_SEQ'),'Pont de la Montagne', 8.5, 5.2, '44.4570', '5.6678', '1975-01-01',13, 1),
    (nextval('Pont_SEQ'),'Pont de la Poste', 30.8, 8.8, '44.7828', '6.8707', '1975-01-01',13, 1);


INSERT INTO CapteurAnalogique(id, nom, description, uniteanalogique_id, pont_id)
VALUES 
	(nextval('Capteur_SEQ'),'CapteurAnalogique1', 'Capteur de température', 1, 1),
	(nextval('Capteur_SEQ'),'CapteurAnalogique2', 'Capteur de pression', 2, 1),
	(nextval('Capteur_SEQ'),'CapteurAnalogique3', 'Capteur de humidité', 3, 1),

	(nextval('Capteur_SEQ'),'CapteurAnalogique4', 'Capteur de température', 1, 2),
	(nextval('Capteur_SEQ'),'CapteurAnalogique5', 'Capteur de pression', 2, 2),
	(nextval('Capteur_SEQ'),'CapteurAnalogique6', 'Capteur de humidité', 3, 2),

	(nextval('Capteur_SEQ'),'CapteurAnalogique7', 'Capteur de température', 1, 3),
	(nextval('Capteur_SEQ'),'CapteurAnalogique8', 'Capteur de pression', 2, 3),
	(nextval('Capteur_SEQ'),'CapteurAnalogique9', 'Capteur de humidité', 3, 3),

	(nextval('Capteur_SEQ'),'CapteurAnalogique10', 'Capteur de température', 1, 4),
	(nextval('Capteur_SEQ'),'CapteurAnalogique11', 'Capteur de pression', 2, 4),
	(nextval('Capteur_SEQ'),'CapteurAnalogique12', 'Capteur de humidité', 3, 4),

	(nextval('Capteur_SEQ'),'CapteurAnalogique13', 'Capteur de température', 1, 5),
	(nextval('Capteur_SEQ'),'CapteurAnalogique14', 'Capteur de pression', 2, 5),
	(nextval('Capteur_SEQ'),'CapteurAnalogique15', 'Capteur de humidité', 3, 5),

	(nextval('Capteur_SEQ'),'CapteurAnalogique16', 'Capteur de température', 1, 6),
	(nextval('Capteur_SEQ'),'CapteurAnalogique17', 'Capteur de pression', 2, 6),
	(nextval('Capteur_SEQ'),'CapteurAnalogique18', 'Capteur de humidité', 3, 6),

	(nextval('Capteur_SEQ'),'CapteurAnalogique19', 'Capteur de température', 1, 7),
	(nextval('Capteur_SEQ'),'CapteurAnalogique20', 'Capteur de pression', 2, 7),
	(nextval('Capteur_SEQ'),'CapteurAnalogique21', 'Capteur de humidité', 3, 7),

	(nextval('Capteur_SEQ'),'CapteurAnalogique22', 'Capteur de température', 1, 8),
	(nextval('Capteur_SEQ'),'CapteurAnalogique23', 'Capteur de pression', 2, 8),
	(nextval('Capteur_SEQ'),'CapteurAnalogique24', 'Capteur de humidité', 3, 8),

	(nextval('Capteur_SEQ'),'CapteurAnalogique25', 'Capteur de température', 1, 9),
	(nextval('Capteur_SEQ'),'CapteurAnalogique26', 'Capteur de pression', 2, 9),
	(nextval('Capteur_SEQ'),'CapteurAnalogique27', 'Capteur de humidité', 3, 9),

	(nextval('Capteur_SEQ'),'CapteurAnalogique28', 'Capteur de température', 1, 10),
	(nextval('Capteur_SEQ'),'CapteurAnalogique29', 'Capteur de pression', 2, 10),
	(nextval('Capteur_SEQ'),'CapteurAnalogique30', 'Capteur de humidité', 3, 10),

	(nextval('Capteur_SEQ'),'CapteurAnalogique31', 'Capteur de température', 1, 11),
	(nextval('Capteur_SEQ'),'CapteurAnalogique32', 'Capteur de pression', 2, 11),
	(nextval('Capteur_SEQ'),'CapteurAnalogique33', 'Capteur de humidité', 3, 11),

	(nextval('Capteur_SEQ'),'CapteurAnalogique34', 'Capteur de température', 1, 12),
	(nextval('Capteur_SEQ'),'CapteurAnalogique35', 'Capteur de pression', 2, 12),
	(nextval('Capteur_SEQ'),'CapteurAnalogique36', 'Capteur de humidité', 3, 12),

	(nextval('Capteur_SEQ'),'CapteurAnalogique37', 'Capteur de température', 1, 13),
	(nextval('Capteur_SEQ'),'CapteurAnalogique38', 'Capteur de pression', 2, 13),
	(nextval('Capteur_SEQ'),'CapteurAnalogique39', 'Capteur de humidité', 3, 13),

	(nextval('Capteur_SEQ'),'CapteurAnalogique40', 'Capteur de température', 1, 14),
	(nextval('Capteur_SEQ'),'CapteurAnalogique41', 'Capteur de pression', 2, 14),
	(nextval('Capteur_SEQ'),'CapteurAnalogique42', 'Capteur de humidité', 3, 14),

	(nextval('Capteur_SEQ'),'CapteurAnalogique43', 'Capteur de température', 1, 15),
	(nextval('Capteur_SEQ'),'CapteurAnalogique44', 'Capteur de pression', 2, 15),
	(nextval('Capteur_SEQ'),'CapteurAnalogique45', 'Capteur de humidité', 3, 15),

	(nextval('Capteur_SEQ'),'CapteurAnalogique46', 'Capteur de température', 1, 16),
	(nextval('Capteur_SEQ'),'CapteurAnalogique47', 'Capteur de pression', 2, 16),
	(nextval('Capteur_SEQ'),'CapteurAnalogique48', 'Capteur de humidité', 3, 16),

	(nextval('Capteur_SEQ'),'CapteurAnalogique49', 'Capteur de température', 1, 17),
	(nextval('Capteur_SEQ'),'CapteurAnalogique50', 'Capteur de pression', 2, 17),
	(nextval('Capteur_SEQ'),'CapteurAnalogique51', 'Capteur de humidité', 3, 17),

	(nextval('Capteur_SEQ'),'CapteurAnalogique52', 'Capteur de température', 1, 18),
	(nextval('Capteur_SEQ'),'CapteurAnalogique53', 'Capteur de pression', 2, 18),
	(nextval('Capteur_SEQ'),'CapteurAnalogique54', 'Capteur de humidité', 3, 18),

	(nextval('Capteur_SEQ'),'CapteurAnalogique55', 'Capteur de température', 1, 19),
	(nextval('Capteur_SEQ'),'CapteurAnalogique56', 'Capteur de pression', 2, 19),
	(nextval('Capteur_SEQ'),'CapteurAnalogique57', 'Capteur de humidité', 3, 19),

	(nextval('Capteur_SEQ'),'CapteurAnalogique58', 'Capteur de température', 1, 20),
	(nextval('Capteur_SEQ'),'CapteurAnalogique59', 'Capteur de pression', 2, 20),
	(nextval('Capteur_SEQ'),'CapteurAnalogique60', 'Capteur de humidité', 3, 20),

	(nextval('Capteur_SEQ'),'CapteurAnalogique61', 'Capteur de température', 1, 21),
	(nextval('Capteur_SEQ'),'CapteurAnalogique62', 'Capteur de pression', 2, 21),
	(nextval('Capteur_SEQ'),'CapteurAnalogique63', 'Capteur de humidité', 3, 21),

	(nextval('Capteur_SEQ'),'CapteurAnalogique64', 'Capteur de température', 1, 22),
	(nextval('Capteur_SEQ'),'CapteurAnalogique65', 'Capteur de pression', 2, 22),
	(nextval('Capteur_SEQ'),'CapteurAnalogique66', 'Capteur de humidité', 3, 22),

	(nextval('Capteur_SEQ'),'CapteurAnalogique67', 'Capteur de température', 1, 23),
	(nextval('Capteur_SEQ'),'CapteurAnalogique68', 'Capteur de pression', 2, 23),
	(nextval('Capteur_SEQ'),'CapteurAnalogique69', 'Capteur de humidité', 3, 23),

	(nextval('Capteur_SEQ'),'CapteurAnalogique70', 'Capteur de température', 1, 24),
	(nextval('Capteur_SEQ'),'CapteurAnalogique71', 'Capteur de pression', 2, 24),
	(nextval('Capteur_SEQ'),'CapteurAnalogique72', 'Capteur de humidité', 3, 24),

	(nextval('Capteur_SEQ'),'CapteurAnalogique73', 'Capteur de température', 1, 25),
	(nextval('Capteur_SEQ'),'CapteurAnalogique74', 'Capteur de pression', 2, 25),
	(nextval('Capteur_SEQ'),'CapteurAnalogique75', 'Capteur de humidité', 3, 25),

	(nextval('Capteur_SEQ'),'CapteurAnalogique76', 'Capteur de température', 1, 26),
	(nextval('Capteur_SEQ'),'CapteurAnalogique77', 'Capteur de pression', 2, 26),
	(nextval('Capteur_SEQ'),'CapteurAnalogique78', 'Capteur de humidité', 3, 26);

	


INSERT INTO CapteurNumerique(id, nom, description, unitenumerique_id, pont_id)
VALUES 
	(nextval('Capteur_SEQ'), 'CapteurNumerique1', 'Capteur de présence', 1, 1),
	(nextval('Capteur_SEQ'), 'CapteurNumerique2', 'Capteur de présence', 1, 2),
	(nextval('Capteur_SEQ'), 'CapteurNumerique3', 'Capteur de présence', 1, 3),
	(nextval('Capteur_SEQ'), 'CapteurNumerique4', 'Capteur de présence', 1, 4),
	(nextval('Capteur_SEQ'), 'CapteurNumerique5', 'Capteur de présence', 1, 5),
	(nextval('Capteur_SEQ'), 'CapteurNumerique6', 'Capteur de présence', 1, 6),
	(nextval('Capteur_SEQ'), 'CapteurNumerique7', 'Capteur de présence', 1, 7),
	(nextval('Capteur_SEQ'), 'CapteurNumerique8', 'Capteur de présence', 1, 8),
	(nextval('Capteur_SEQ'), 'CapteurNumerique9', 'Capteur de présence', 1, 9),
	(nextval('Capteur_SEQ'), 'CapteurNumerique10', 'Capteur de présence', 1, 10),
	(nextval('Capteur_SEQ'), 'CapteurNumerique11', 'Capteur de présence', 1, 11),
	(nextval('Capteur_SEQ'), 'CapteurNumerique12', 'Capteur de présence', 1, 12),
	(nextval('Capteur_SEQ'), 'CapteurNumerique13', 'Capteur de présence', 1, 13),
	(nextval('Capteur_SEQ'), 'CapteurNumerique14', 'Capteur de présence', 1, 14),
	(nextval('Capteur_SEQ'), 'CapteurNumerique15', 'Capteur de présence', 1, 15),
	(nextval('Capteur_SEQ'), 'CapteurNumerique16', 'Capteur de présence', 1, 16),
	(nextval('Capteur_SEQ'), 'CapteurNumerique17', 'Capteur de présence', 1, 17),
	(nextval('Capteur_SEQ'), 'CapteurNumerique18', 'Capteur de présence', 1, 18),
	(nextval('Capteur_SEQ'), 'CapteurNumerique19', 'Capteur de présence', 1, 19),
	(nextval('Capteur_SEQ'), 'CapteurNumerique20', 'Capteur de présence', 1, 20),
	(nextval('Capteur_SEQ'), 'CapteurNumerique21', 'Capteur de présence', 1, 21),
	(nextval('Capteur_SEQ'), 'CapteurNumerique22', 'Capteur de présence', 1, 22),
	(nextval('Capteur_SEQ'), 'CapteurNumerique23', 'Capteur de présence', 1, 23),
	(nextval('Capteur_SEQ'), 'CapteurNumerique24', 'Capteur de présence', 1, 24),
	(nextval('Capteur_SEQ'), 'CapteurNumerique25', 'Capteur de présence', 1, 25),
	(nextval('Capteur_SEQ'), 'CapteurNumerique26', 'Capteur de présence', 1, 26);


INSERT INTO ReleveAnalogique(id, valeur, capteuranalogique_id)
VALUES 
	(nextval('Releve_SEQ'), 25.4, 1),
	(nextval('Releve_SEQ'), 0.7, 1);

INSERT INTO ReleveNumerique(id, valeur, capteurnumerique_id)
VALUES 
	(nextval('Releve_SEQ'), TRUE, 79),
 	(nextval('Releve_SEQ'), FALSE, 80);
