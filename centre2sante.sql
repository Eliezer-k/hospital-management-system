-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : dim. 19 avr. 2026 à 22:16
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `centre2sante`
--

-- --------------------------------------------------------

--
-- Structure de la table `accouchements`
--

CREATE TABLE `accouchements` (
  `ID` int(11) NOT NULL,
  `Patient_ID` int(11) DEFAULT NULL,
  `Date_accouchement` date DEFAULT NULL,
  `Heure_accouchement` time DEFAULT NULL,
  `Type_accouchement` varchar(255) DEFAULT NULL,
  `Complications` tinyint(1) DEFAULT NULL,
  `Details_complications` text DEFAULT NULL,
  `Nom_medecin` varchar(255) DEFAULT NULL,
  `Nom_sage_femme` varchar(255) DEFAULT NULL,
  `Poids_bebe` decimal(5,2) DEFAULT NULL,
  `Taille_bebe` decimal(5,2) DEFAULT NULL,
  `Sexe_bebe` char(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `accouchements`
--

INSERT INTO `accouchements` (`ID`, `Patient_ID`, `Date_accouchement`, `Heure_accouchement`, `Type_accouchement`, `Complications`, `Details_complications`, `Nom_medecin`, `Nom_sage_femme`, `Poids_bebe`, `Taille_bebe`, `Sexe_bebe`) VALUES
(2, 10, '2024-05-26', '00:16:11', 'Naturel', 0, 'NULL', 'Assy', 'Diambra', 20.00, 10.00, 'Feminin'),
(3, 11, '2024-05-26', '10:46:13', 'Césarienne', 1, 'Quelques troubles et fatigue', 'Eliezer', 'Diambra', 10.00, 30.00, 'Masculin'),
(4, 15, '2024-05-26', '21:38:06', 'Naturel', 0, 'NULL', 'Djadou', 'Koffi', 22.00, 15.00, 'Feminin'),
(5, 16, '2024-05-26', '22:15:22', 'Césarienne', 0, 'NULL', 'Assy', 'Correa', 25.00, 30.00, 'Masculin'),
(8, 7, '2024-05-30', '03:33:03', 'Naturel', 0, 'NULL', 'Eliezer', 'Diambra', 30.00, 25.00, 'Feminin'),
(9, 18, '2024-05-30', '13:47:00', 'Naturel', 0, 'NULL', 'Assy', 'Jessica', 20.00, 25.00, 'Feminin');

-- --------------------------------------------------------

--
-- Structure de la table `activites_personnel`
--

CREATE TABLE `activites_personnel` (
  `id_activite` int(11) NOT NULL,
  `nom_personnel` varchar(255) NOT NULL,
  `service` varchar(255) NOT NULL,
  `type_activite` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `date_activite` date NOT NULL,
  `heure_activite` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `activites_personnel`
--

INSERT INTO `activites_personnel` (`id_activite`, `nom_personnel`, `service`, `type_activite`, `description`, `date_activite`, `heure_activite`) VALUES
(1, 'Koffi', 'Injection', 'Injection de type: Médicament', NULL, '2024-05-30', '03:12:19'),
(2, 'Lazard', 'Prélèvement', 'Type de prélèvement: URINAIRE', NULL, '2024-05-30', '03:12:43'),
(3, 'Eliezer', 'Consultation', ' Pour: ok, rhume, fatigue', NULL, '2024-05-30', '03:23:27'),
(4, 'Eliezer et Diambra', 'Accouchement', 'Accouchement de type: Naturel', NULL, '2024-05-30', '03:33:03'),
(5, 'Assy', 'Consultation', ' Pour: diarrhé		', NULL, '2024-05-30', '13:43:36'),
(6, 'Assy et Jessica', 'Accouchement', 'Accouchement de type: Naturel', NULL, '2024-05-30', '13:47:00'),
(7, 'Assy', 'Consultation', ' Pour: fievre	', NULL, '2024-05-30', '15:08:09');

-- --------------------------------------------------------

--
-- Structure de la table `chambre`
--

CREATE TABLE `chambre` (
  `id_chambre` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `disponibilite` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `chambre`
--

INSERT INTO `chambre` (`id_chambre`, `nom`, `disponibilite`) VALUES
(1, 'Chambre 1', 1),
(2, 'Chambre 2', 1),
(3, 'Chambre 3', 1),
(4, 'Chambre 4', 1),
(5, 'Chambre 5', 1);

-- --------------------------------------------------------

--
-- Structure de la table `constante`
--

CREATE TABLE `constante` (
  `id` int(11) NOT NULL,
  `id_patient` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `temperature` decimal(5,2) DEFAULT NULL,
  `poids` decimal(5,2) DEFAULT NULL,
  `pression_arterielle` decimal(5,2) DEFAULT NULL,
  `frequence_cardiaque` int(11) DEFAULT NULL,
  `frequence_respiratoire` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `constante`
--

INSERT INTO `constante` (`id`, `id_patient`, `date`, `temperature`, `poids`, `pression_arterielle`, `frequence_cardiaque`, `frequence_respiratoire`) VALUES
(1, 7, '2024-05-06', 20.00, 50.00, 10.00, 21, 30),
(2, 9, '2024-05-06', 21.00, 60.00, 45.00, 31, 41),
(3, 10, '2024-05-07', 39.00, 50.00, 21.00, 22, 22),
(4, 11, '2024-05-08', 35.00, 63.00, 15.00, 24, 20),
(5, 13, '2024-05-14', 41.00, 85.00, 55.00, 145, 141),
(6, 17, '2024-05-30', 10.00, 50.00, 45.00, 20, 11),
(7, 18, '2024-05-30', 20.00, 12.00, 125.00, 123, 10),
(8, 20, '2024-05-30', 12.00, 120.00, 122.00, 12, 12);

-- --------------------------------------------------------

--
-- Structure de la table `consultation`
--

CREATE TABLE `consultation` (
  `id_consultation` int(11) NOT NULL,
  `id_patient` int(11) DEFAULT NULL,
  `id_personnel` int(11) DEFAULT NULL,
  `date_consultation` date DEFAULT NULL,
  `symptome` text NOT NULL,
  `examensRequis` text NOT NULL,
  `diagnostic` text DEFAULT NULL,
  `traitement_prescrit` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `consultation`
--

INSERT INTO `consultation` (`id_consultation`, `id_patient`, `id_personnel`, `date_consultation`, `symptome`, `examensRequis`, `diagnostic`, `traitement_prescrit`) VALUES
(12, 7, 12, '2024-05-01', 'Rhume', 'Anti-tetanos', 'Paludisme', 'Doliprane, vaccin'),
(13, 9, 10, '2024-05-01', 'pipi au lit', 'Anti tchepo', 'Kouachorkor', '2 pregnons'),
(14, 7, 10, '2024-05-02', 'demangaisons', 'vaccins', 'Variselle', 'Doliprane'),
(15, 10, 10, '2024-05-07', 'ok, toux, vomissement', 'null', 'Paludisme', 'Artefan , sirop, doliprane'),
(16, 11, 12, '2024-05-08', 'ok,toux,vomissements, forte fièvres, Diarrhée', 'Aucun', 'Fièvre jaune', 'Paracetamol au moi 2x 1M 1S\n, Artefan  1M 1S, Sirop, repos et \nbonne alimentation'),
(17, 13, 10, '2024-05-14', 'niill', 'null\n', 'null', 'null'),
(18, 13, 12, '2024-05-27', 'Diarrhée, toux , rhume', 'null', 'Covid-19', 'Vaccin'),
(19, 9, 18, '2024-05-27', 'toux , nausés', 'Examen de sang', 'Fatigue', 'Doliprane et bain froid'),
(20, 17, 18, '2024-05-30', 'ok, rhume, fatigue', 'Null', 'Ebola', 'Paracetamol, plus de repos, bain chaud et bonne alimentation'),
(21, 18, 12, '2024-05-30', 'diarrhé		', 'null', 'diarrhe excessif', 'liather'),
(22, 20, 12, '2024-05-30', 'fievre	', 'null', 'palu', 'artefan');

--
-- Déclencheurs `consultation`
--
DELIMITER $$
CREATE TRIGGER `insert_resultat` AFTER INSERT ON `consultation` FOR EACH ROW BEGIN
    DECLARE medecin_nom VARCHAR(255);
    DECLARE patient_nom VARCHAR(255);

   
    SELECT nom INTO medecin_nom FROM personnel WHERE id_personnel = NEW.id_personnel;

  
    SELECT nom INTO patient_nom FROM patient WHERE id_patient = NEW.id_patient;

    
    INSERT INTO resultat (id_resultat, nom_patient, nom_medecin, date_consultation, traitement_requis)
    VALUES (NULL, patient_nom, medecin_nom, NEW.date_consultation, NEW.traitement_prescrit);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `examen`
--

CREATE TABLE `examen` (
  `id_examen` int(11) NOT NULL,
  `id_patient` int(11) DEFAULT NULL,
  `id_personnel` int(11) DEFAULT NULL,
  `type_examen` varchar(255) DEFAULT NULL,
  `date_examen` date DEFAULT NULL,
  `resultat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `hospitalisation`
--

CREATE TABLE `hospitalisation` (
  `id_hospitalisation` int(11) NOT NULL,
  `id_patient` int(11) DEFAULT NULL,
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `motif` text DEFAULT NULL,
  `Chambre_occupé` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `hospitalisation`
--

INSERT INTO `hospitalisation` (`id_hospitalisation`, `id_patient`, `date_debut`, `date_fin`, `motif`, `Chambre_occupé`) VALUES
(8, 10, '2024-05-27', '2024-05-29', 'Accouchement', 'Chambre 2'),
(10, 11, '2024-05-27', '2024-05-29', 'Accouchement', 'Chambre 4'),
(11, 16, '2024-05-27', '2024-05-29', 'Accouchement', 'Chambre 3'),
(13, 15, '2024-05-27', '2024-05-28', 'Accouchement', 'Chambre 1'),
(14, 7, '2024-05-30', '2024-05-31', 'Accouchement', 'Chambre 5');

--
-- Déclencheurs `hospitalisation`
--
DELIMITER $$
CREATE TRIGGER `before_insert_hospitalisation` BEFORE INSERT ON `hospitalisation` FOR EACH ROW BEGIN
    IF LOWER(NEW.motif) = 'accouchement' AND DATEDIFF(NEW.date_fin, NEW.date_debut) > 2 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La durée de l''hospitalisation pour un accouchement ne peut pas dépasser 2 jours.';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_update_hospitalisation` BEFORE UPDATE ON `hospitalisation` FOR EACH ROW BEGIN
    IF LOWER(NEW.motif) = 'accouchement' AND DATEDIFF(NEW.date_fin, NEW.date_debut) > 2 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La durée de l''hospitalisation pour un accouchement ne peut pas dépasser 2 jours.';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `injections`
--

CREATE TABLE `injections` (
  `id` int(11) NOT NULL,
  `nom_patient` varchar(255) DEFAULT NULL,
  `heure` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `type_injection` enum('Médicament','Vaccin','Autre') DEFAULT NULL,
  `remarques` text DEFAULT NULL,
  `infirmier_en_charge` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `injections`
--

INSERT INTO `injections` (`id`, `nom_patient`, `heure`, `date`, `type_injection`, `remarques`, `infirmier_en_charge`) VALUES
(3, 'Moyet', '03:12:19', '2024-05-30', 'Médicament', 'null', 'Koffi');

-- --------------------------------------------------------

--
-- Structure de la table `patient`
--

CREATE TABLE `patient` (
  `id_patient` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `sexe` enum('Homme','Femme') DEFAULT NULL,
  `telephone` varchar(255) NOT NULL,
  `Numero` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `patient`
--

INSERT INTO `patient` (`id_patient`, `nom`, `prenom`, `date_naissance`, `sexe`, `telephone`, `Numero`) VALUES
(7, 'Moyet', ' eliezer', '2002-12-03', 'Homme', '+225 0749593327', '010122211100000'),
(9, 'critie', 'bi boti', '1999-05-13', 'Homme', '+225 4545444478', '0121214400000'),
(10, 'Beyaki', 'deborah', '2010-05-09', 'Femme', '+225 0140424240', '0121545555'),
(11, 'Kouassi', 'Djolo Yves Aurel', '1999-05-21', 'Homme', '+225 0749593327', '01212450000000'),
(13, 'paul', 'george', '2019-05-16', 'Homme', '+225 0779097174', '225000'),
(15, 'Minikan', 'Jessica', '2005-04-25', 'Femme', '+225 0312457800', '1400000555550'),
(16, 'Akafou', 'mariam', '2000-05-19', 'Femme', '+225 0140424250', '010000252000000'),
(17, 'Jean', 'francois', '1999-05-14', 'Homme', '+225 0022441010', '01212133300004'),
(18, 'joe', 'jo', '2018-05-17', 'Homme', '+225 0789620106', '12010201'),
(20, 'dadjou', 'joseph', '2016-05-19', 'Homme', '+225 0102040506', '41255555');

-- --------------------------------------------------------

--
-- Structure de la table `personnel`
--

CREATE TABLE `personnel` (
  `id_personnel` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `sexe` enum('Homme','Femme') DEFAULT NULL,
  `specialite` enum('Medecin','Infirmier','Sage-femme') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `personnel`
--

INSERT INTO `personnel` (`id_personnel`, `nom`, `prenom`, `sexe`, `specialite`) VALUES
(10, 'Djadou', 'cauphy', 'Homme', 'Medecin'),
(12, 'Assy', 'Vianney', 'Homme', 'Medecin'),
(16, 'Diambra', 'Ange-emmanuelle', 'Femme', 'Sage-femme'),
(18, 'Eliezer', 'chamack', 'Homme', 'Medecin'),
(24, 'lazack', 'roger', 'Homme', 'Infirmier'),
(25, 'Djolo', 'Aurel', 'Homme', 'Infirmier'),
(26, 'Jessica', 'Carmel', 'Femme', 'Sage-femme'),
(27, 'Kanga', 'long', 'Homme', 'Medecin'),
(28, 'fille', 'go', 'Femme', 'Sage-femme'),
(29, 'LLAA', 'Ivey', 'Femme', 'Sage-femme');

-- --------------------------------------------------------

--
-- Structure de la table `prelevement_sanguin`
--

CREATE TABLE `prelevement_sanguin` (
  `id_prelevement` int(11) NOT NULL,
  `nom_patient` varchar(255) DEFAULT NULL,
  `heure_prelevement` varchar(255) DEFAULT NULL,
  `date_prelevement` date DEFAULT NULL,
  `type_prelevement` enum('SANGUIN','URINAIRE','SALIVAIRE','AUTRE') DEFAULT NULL,
  `resultat_prelevement` text DEFAULT NULL,
  `commentaire` text DEFAULT NULL,
  `nom_infirmier` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `prelevement_sanguin`
--

INSERT INTO `prelevement_sanguin` (`id_prelevement`, `nom_patient`, `heure_prelevement`, `date_prelevement`, `type_prelevement`, `resultat_prelevement`, `commentaire`, `nom_infirmier`) VALUES
(3, 'critie', '03:12:43', '2024-05-30', 'URINAIRE', 'null', 'okok', 'Lazard');

-- --------------------------------------------------------

--
-- Structure de la table `rendez_vous`
--

CREATE TABLE `rendez_vous` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `date_rdv` date NOT NULL,
  `heure_rdv` time NOT NULL,
  `motif` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `rendez_vous`
--

INSERT INTO `rendez_vous` (`id`, `nom`, `date_rdv`, `heure_rdv`, `motif`) VALUES
(4, 'Kanga', '2024-05-18', '10:00:00', 'Bilan de sante'),
(5, 'critie', '2024-05-12', '10:00:00', 'Bilan de sante'),
(7, 'paul', '2024-06-08', '14:00:00', 'Controle de sante'),
(8, 'Kouassi', '2024-05-28', '08:00:00', 'Echographie'),
(9, 'joe', '2024-06-08', '16:00:00', 'Bilan');

-- --------------------------------------------------------

--
-- Structure de la table `resultat`
--

CREATE TABLE `resultat` (
  `id_resultat` int(11) NOT NULL,
  `date_consultation` date DEFAULT NULL,
  `traitement_requis` text DEFAULT NULL,
  `nom_patient` varchar(255) DEFAULT NULL,
  `nom_medecin` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `resultat`
--

INSERT INTO `resultat` (`id_resultat`, `date_consultation`, `traitement_requis`, `nom_patient`, `nom_medecin`) VALUES
(8, '2024-05-01', 'Doliprane, vaccin', 'Kanga', 'Assy'),
(9, '2024-05-01', '2 pregnons', 'critie', 'Djadou'),
(10, '2024-05-02', 'Doliprane', 'Moyet', 'Djadou'),
(11, '2024-05-07', 'Artefan , sirop, doliprane', 'Beyaki', 'Djadou'),
(12, '2024-05-08', 'Paracetamol au moi 2x 1M 1S\n, Artefan  1M 1S, Sirop, repos et \nbonne alimentation', 'Kouassi', 'Assy'),
(14, '2024-05-27', 'Vaccin', 'paul', 'Assy'),
(15, '2024-05-27', 'Doliprane et bain froid', 'critie', 'Eliezer'),
(16, '2024-05-30', 'Paracetamol, plus de repos, bain chaud et bonne alimentation', 'Jean', 'Eliezer'),
(17, '2024-05-30', 'liather', 'joe', 'Assy'),
(18, '2024-05-30', 'artefan', 'dadjou', 'Assy');

-- --------------------------------------------------------

--
-- Structure de la table `suivi_patient`
--

CREATE TABLE `suivi_patient` (
  `id_suivi` int(11) NOT NULL,
  `id_patient` int(11) DEFAULT NULL,
  `date_interaction` date DEFAULT NULL,
  `type_interaction` varchar(50) DEFAULT NULL,
  `details_interaction` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Déchargement des données de la table `suivi_patient`
--

INSERT INTO `suivi_patient` (`id_suivi`, `id_patient`, `date_interaction`, `type_interaction`, `details_interaction`) VALUES
(2, 11, '2024-05-27', 'Hospitalisation', 'Hospitalisation de Kouassi pour Accouchementen Chambre 4'),
(3, 16, '2024-05-27', 'Hospitalisation', 'Hospitalisation de Akafou pour Accouchementen Chambre 3'),
(5, 7, '2024-05-27', 'Hospitalisation', 'Hospitalisation de Moyet pour Accouchementen Chambre 5'),
(6, 15, '2024-05-27', 'Hospitalisation', 'Hospitalisation de Minikan pour Accouchement en Chambre 1'),
(7, 9, '2024-05-27', 'Consultation', 'Consultation de critie pour toux , nausés'),
(8, 17, '2024-05-30', 'Consultation', 'Consultation de Jean pour ok, rhume, fatigue'),
(9, 7, '2024-05-30', 'Hospitalisation', 'Hospitalisation de Moyet pour Accouchement en Chambre 5'),
(10, 18, '2024-05-30', 'Consultation', 'Consultation de joe pour diarrhé		'),
(11, 20, '2024-05-30', 'Consultation', 'Consultation de dadjou pour fievre	');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `accouchements`
--
ALTER TABLE `accouchements`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Patient_ID` (`Patient_ID`);

--
-- Index pour la table `activites_personnel`
--
ALTER TABLE `activites_personnel`
  ADD PRIMARY KEY (`id_activite`);

--
-- Index pour la table `chambre`
--
ALTER TABLE `chambre`
  ADD PRIMARY KEY (`id_chambre`);

--
-- Index pour la table `constante`
--
ALTER TABLE `constante`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_patient` (`id_patient`);

--
-- Index pour la table `consultation`
--
ALTER TABLE `consultation`
  ADD PRIMARY KEY (`id_consultation`),
  ADD KEY `consultation_ibfk_1` (`id_patient`),
  ADD KEY `consultation_ibfk_2` (`id_personnel`);

--
-- Index pour la table `examen`
--
ALTER TABLE `examen`
  ADD PRIMARY KEY (`id_examen`),
  ADD KEY `id_patient` (`id_patient`),
  ADD KEY `id_personnel` (`id_personnel`);

--
-- Index pour la table `hospitalisation`
--
ALTER TABLE `hospitalisation`
  ADD PRIMARY KEY (`id_hospitalisation`),
  ADD KEY `id_patient` (`id_patient`);

--
-- Index pour la table `injections`
--
ALTER TABLE `injections`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`id_patient`);

--
-- Index pour la table `personnel`
--
ALTER TABLE `personnel`
  ADD PRIMARY KEY (`id_personnel`);

--
-- Index pour la table `prelevement_sanguin`
--
ALTER TABLE `prelevement_sanguin`
  ADD PRIMARY KEY (`id_prelevement`);

--
-- Index pour la table `rendez_vous`
--
ALTER TABLE `rendez_vous`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `resultat`
--
ALTER TABLE `resultat`
  ADD PRIMARY KEY (`id_resultat`);

--
-- Index pour la table `suivi_patient`
--
ALTER TABLE `suivi_patient`
  ADD PRIMARY KEY (`id_suivi`),
  ADD KEY `id_patient` (`id_patient`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `accouchements`
--
ALTER TABLE `accouchements`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `activites_personnel`
--
ALTER TABLE `activites_personnel`
  MODIFY `id_activite` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `chambre`
--
ALTER TABLE `chambre`
  MODIFY `id_chambre` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `constante`
--
ALTER TABLE `constante`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `consultation`
--
ALTER TABLE `consultation`
  MODIFY `id_consultation` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `hospitalisation`
--
ALTER TABLE `hospitalisation`
  MODIFY `id_hospitalisation` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT pour la table `injections`
--
ALTER TABLE `injections`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `patient`
--
ALTER TABLE `patient`
  MODIFY `id_patient` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT pour la table `personnel`
--
ALTER TABLE `personnel`
  MODIFY `id_personnel` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT pour la table `prelevement_sanguin`
--
ALTER TABLE `prelevement_sanguin`
  MODIFY `id_prelevement` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `rendez_vous`
--
ALTER TABLE `rendez_vous`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `resultat`
--
ALTER TABLE `resultat`
  MODIFY `id_resultat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT pour la table `suivi_patient`
--
ALTER TABLE `suivi_patient`
  MODIFY `id_suivi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `accouchements`
--
ALTER TABLE `accouchements`
  ADD CONSTRAINT `accouchements_ibfk_1` FOREIGN KEY (`Patient_ID`) REFERENCES `patient` (`id_patient`);

--
-- Contraintes pour la table `constante`
--
ALTER TABLE `constante`
  ADD CONSTRAINT `constante_ibfk_1` FOREIGN KEY (`id_patient`) REFERENCES `patient` (`id_patient`);

--
-- Contraintes pour la table `consultation`
--
ALTER TABLE `consultation`
  ADD CONSTRAINT `consultation_ibfk_1` FOREIGN KEY (`id_patient`) REFERENCES `patient` (`id_patient`) ON DELETE CASCADE,
  ADD CONSTRAINT `consultation_ibfk_2` FOREIGN KEY (`id_personnel`) REFERENCES `personnel` (`id_personnel`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_personnel_id` FOREIGN KEY (`id_personnel`) REFERENCES `personnel` (`id_personnel`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `examen`
--
ALTER TABLE `examen`
  ADD CONSTRAINT `examen_ibfk_1` FOREIGN KEY (`id_patient`) REFERENCES `patient` (`id_patient`),
  ADD CONSTRAINT `examen_ibfk_2` FOREIGN KEY (`id_personnel`) REFERENCES `personnel` (`id_personnel`);

--
-- Contraintes pour la table `hospitalisation`
--
ALTER TABLE `hospitalisation`
  ADD CONSTRAINT `hospitalisation_ibfk_1` FOREIGN KEY (`id_patient`) REFERENCES `patient` (`id_patient`);

--
-- Contraintes pour la table `suivi_patient`
--
ALTER TABLE `suivi_patient`
  ADD CONSTRAINT `suivi_patient_ibfk_1` FOREIGN KEY (`id_patient`) REFERENCES `patient` (`id_patient`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
