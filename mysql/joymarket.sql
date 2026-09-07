-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 19, 2025 at 01:12 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `joymarket`
--
CREATE DATABASE IF NOT EXISTS `joymarket` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `joymarket`;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `idAdmin` varchar(50) NOT NULL,
  `emergencyContact` varchar(13) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `admin`:
--   `idAdmin`
--       `user` -> `idUser`
--

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`idAdmin`, `emergencyContact`) VALUES
('AD001', '0215345833'),
('AD002', '0215345833'),
('AD003', '0215345833');

-- --------------------------------------------------------

--
-- Table structure for table `cartitem`
--

DROP TABLE IF EXISTS `cartitem`;
CREATE TABLE `cartitem` (
  `idCustomer` varchar(50) NOT NULL,
  `idProduct` varchar(50) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `cartitem`:
--   `idCustomer`
--       `customer` -> `idCustomer`
--   `idProduct`
--       `product` -> `idProduct`
--

--
-- Dumping data for table `cartitem`
--

INSERT INTO `cartitem` (`idCustomer`, `idProduct`, `count`) VALUES
('CU001', 'PD001', 2),
('CU001', 'PD005', 1),
('CU001', 'PD009', 3),
('CU002', 'PD010', 2),
('CU002', 'PD013', 1),
('CU002', 'PD016', 1),
('CU003', 'PD018', 2),
('CU003', 'PD020', 1),
('CU004', 'PD001', 1),
('CU004', 'PD002', 1),
('CU004', 'PD003', 1),
('CU004', 'PD004', 1),
('CU004', 'PD005', 1),
('CU004', 'PD006', 1),
('CU004', 'PD007', 1),
('CU004', 'PD008', 1),
('CU004', 'PD009', 1),
('CU004', 'PD010', 1),
('CU004', 'PD011', 1),
('CU004', 'PD012', 1),
('CU004', 'PD013', 1),
('CU004', 'PD014', 1),
('CU004', 'PD015', 1),
('CU004', 'PD016', 1),
('CU004', 'PD017', 1),
('CU004', 'PD018', 1),
('CU004', 'PD019', 1),
('CU004', 'PD020', 1);

-- --------------------------------------------------------

--
-- Table structure for table `courier`
--

DROP TABLE IF EXISTS `courier`;
CREATE TABLE `courier` (
  `idCourier` varchar(50) NOT NULL,
  `vehicleType` varchar(50) NOT NULL,
  `vehiclePlate` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `courier`:
--   `idCourier`
--       `user` -> `idUser`
--

--
-- Dumping data for table `courier`
--

INSERT INTO `courier` (`idCourier`, `vehicleType`, `vehiclePlate`) VALUES
('CO001', 'Car', 'B 1234 ABC'),
('CO002', 'Motorcycle', 'B 5678 DEF'),
('CO003', 'Car', 'B 9101 GHI');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `idCustomer` varchar(50) NOT NULL,
  `balance` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `customer`:
--   `idCustomer`
--       `user` -> `idUser`
--

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`idCustomer`, `balance`) VALUES
('CU001', 1000000),
('CU002', 750000),
('CU003', 500000),
('CU004', 1000000);

-- --------------------------------------------------------

--
-- Table structure for table `delivery`
--

DROP TABLE IF EXISTS `delivery`;
CREATE TABLE `delivery` (
  `idOrder` varchar(50) NOT NULL,
  `idCourier` varchar(50) NOT NULL,
  `status` enum('Pending','In Progress','Delivered') DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `delivery`:
--   `idOrder`
--       `orderheader` -> `idOrder`
--   `idCourier`
--       `courier` -> `idCourier`
--

--
-- Dumping data for table `delivery`
--

INSERT INTO `delivery` (`idOrder`, `idCourier`, `status`) VALUES
('OR001', 'CO001', 'Delivered'),
('OR002', 'CO002', 'In Progress'),
('OR004', 'CO002', 'Delivered'),
('OR006', 'CO002', 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `orderdetail`
--

DROP TABLE IF EXISTS `orderdetail`;
CREATE TABLE `orderdetail` (
  `idOrder` varchar(50) NOT NULL,
  `idProduct` varchar(50) NOT NULL,
  `qty` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `orderdetail`:
--   `idOrder`
--       `orderheader` -> `idOrder`
--   `idProduct`
--       `product` -> `idProduct`
--

--
-- Dumping data for table `orderdetail`
--

INSERT INTO `orderdetail` (`idOrder`, `idProduct`, `qty`) VALUES
('OR001', 'PD001', 3),
('OR001', 'PD005', 2),
('OR001', 'PD009', 1),
('OR002', 'PD010', 2),
('OR002', 'PD013', 3),
('OR003', 'PD002', 2),
('OR003', 'PD006', 2),
('OR004', 'PD012', 1),
('OR004', 'PD017', 2),
('OR004', 'PD018', 1),
('OR005', 'PD007', 2),
('OR005', 'PD011', 2),
('OR006', 'PD001', 1),
('OR006', 'PD002', 1),
('OR006', 'PD003', 1),
('OR006', 'PD004', 1),
('OR006', 'PD005', 1),
('OR006', 'PD006', 1),
('OR006', 'PD007', 1),
('OR006', 'PD008', 1),
('OR006', 'PD009', 1),
('OR006', 'PD010', 1),
('OR006', 'PD011', 1),
('OR006', 'PD012', 1),
('OR006', 'PD013', 1),
('OR006', 'PD014', 1),
('OR006', 'PD015', 1),
('OR006', 'PD016', 1),
('OR006', 'PD017', 1),
('OR006', 'PD018', 1),
('OR006', 'PD019', 1),
('OR006', 'PD020', 1);

-- --------------------------------------------------------

--
-- Table structure for table `orderheader`
--

DROP TABLE IF EXISTS `orderheader`;
CREATE TABLE `orderheader` (
  `idOrder` varchar(50) NOT NULL,
  `idCustomer` varchar(50) NOT NULL,
  `idPromo` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Pending',
  `orderedAt` datetime DEFAULT current_timestamp(),
  `totalAmount` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `orderheader`:
--   `idCustomer`
--       `customer` -> `idCustomer`
--   `idPromo`
--       `promo` -> `idPromo`
--

--
-- Dumping data for table `orderheader`
--

INSERT INTO `orderheader` (`idOrder`, `idCustomer`, `idPromo`, `status`, `orderedAt`, `totalAmount`) VALUES
('OR001', 'CU001', 'PM001', 'Delivered', '2025-12-01 10:30:00', 135000),
('OR002', 'CU001', NULL, 'In Progress', '2025-12-03 14:20:00', 75000),
('OR003', 'CU002', 'PM002', 'Pending', '2025-12-04 09:15:00', 160000),
('OR004', 'CU003', NULL, 'Delivered', '2025-11-28 16:45:00', 250000),
('OR005', 'CU003', 'PM001', 'Pending', '2025-12-05 11:00:00', 180000),
('OR006', 'CU004', 'PM004', 'Delivered', '2025-12-19 05:17:32', 637500);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `idProduct` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` double NOT NULL,
  `stock` int(11) NOT NULL,
  `category` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `product`:
--

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`idProduct`, `name`, `price`, `stock`, `category`) VALUES
('PD001', 'Milk 1L', 15000, 100, 'Dairy'),
('PD002', 'Yogurt 500g', 25000, 29, 'Dairy'),
('PD003', 'Cheese 200g', 45000, 19, 'Dairy'),
('PD004', 'Butter 250g', 30000, 24, 'Dairy'),
('PD005', 'Chicken Breast 500g', 35000, 29, 'Meat'),
('PD006', 'Beef Steak 300g', 75000, 14, 'Meat'),
('PD007', 'Pork Chop 400g', 55000, 19, 'Meat'),
('PD008', 'Lamb Chop 500g', 95000, 9, 'Meat'),
('PD009', 'Apple 1kg', 45000, 39, 'Fruits'),
('PD010', 'Banana 1kg', 20000, 49, 'Fruits'),
('PD011', 'Orange 1kg', 35000, 34, 'Fruits'),
('PD012', 'Strawberry 500g', 55000, 24, 'Fruits'),
('PD013', 'Carrot 500g', 12000, 44, 'Vegetables'),
('PD014', 'Broccoli 300g', 18000, 29, 'Vegetables'),
('PD015', 'Spinach 250g', 10000, 39, 'Vegetables'),
('PD016', 'Tomato 500g', 15000, 49, 'Vegetables'),
('PD017', 'Salmon Fillet 300g', 85000, 14, 'Seafood'),
('PD018', 'Shrimp 500g', 65000, 19, 'Seafood'),
('PD019', 'Tuna Fillet 250g', 70000, 17, 'Seafood'),
('PD020', 'Squid 400g', 45000, 24, 'Seafood');

-- --------------------------------------------------------

--
-- Table structure for table `promo`
--

DROP TABLE IF EXISTS `promo`;
CREATE TABLE `promo` (
  `idPromo` varchar(50) NOT NULL,
  `code` varchar(20) NOT NULL,
  `headline` varchar(100) DEFAULT NULL,
  `discountPercentage` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `promo`:
--

--
-- Dumping data for table `promo`
--

INSERT INTO `promo` (`idPromo`, `code`, `headline`, `discountPercentage`) VALUES
('PM001', 'DISC10', '10% Discount for All Items', 0.1),
('PM002', 'DISC20', '20% Discount Special Promo', 0.2),
('PM003', 'DISC15', '15% Weekend Discount', 0.15),
('PM004', 'NEWUSER', '25% New User Discount', 0.25);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `idUser` varchar(50) NOT NULL,
  `fullName` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone` varchar(13) NOT NULL,
  `address` text NOT NULL,
  `role` enum('Customer','Admin','Courier') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELATIONSHIPS FOR TABLE `user`:
--

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`idUser`, `fullName`, `email`, `password`, `phone`, `address`, `role`) VALUES
('AD001', 'Aditya Eka Pratama', 'adit.admin@gmail.com', 'joymarketadmin001', '0215345830', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Admin'),
('AD002', 'Jonathan Alvindo Fernandi', 'jonathan.admin@gmail.com', 'joymarketadmin002', '0215345831', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Admin'),
('AD003', 'Evan Julian Fonbeth', 'evan.admin@gmail.com', 'joymarketadmin003', '0215345832', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Admin'),
('CO001', 'Aditya Eka Pratama', 'adit.courier@gmail.com', 'Courier@Adit', '0215345830', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Courier'),
('CO002', 'Jonathan Alvindo Fernandi', 'jonathan.courier@gmail.com', 'Courier@Jonathan', '0215345831', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Courier'),
('CO003', 'Evan Julian Fonbeth', 'evan.courier@gmail.com', 'Courier@Evan', '0215345832', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Courier'),
('CU001', 'Aditya Eka Pratama', 'aditya.pratama@gmail.com', 'Adityapratama!', '0215345830', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Customer'),
('CU002', 'Jonathan Alvindo Fernandi', 'jonathan.fernandi@gmail.com', 'Jonathanfernandi!', '0215345831', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Customer'),
('CU003', 'Evan Julian Fonbeth', 'evan.fonbeth@gmail.com', 'Evanfonbeth!', '0215345832', 'Jl. K. H. Syahdan No.9, RT 006/RW 012, Kel Palmerah, Kecamatan Palmerah, Jakarta Barat, Provinsi DKI Jakarta, Indonesia', 'Customer'),
('CU004', 'Evan Jonathan Aditya', 'evan.aditya@gmail.com', 'Evanaditya!', '0812345678', 'Jl. Kebon Jeruk No.27, RT 001/RW 009, Kel Kemanggisan, Kecamatan Palmerah, Jakarta Barat, Provinsi Daerah Khusus Ibukota Jakarta, 11530, Indonesia', 'Customer');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`idAdmin`);

--
-- Indexes for table `cartitem`
--
ALTER TABLE `cartitem`
  ADD PRIMARY KEY (`idCustomer`,`idProduct`),
  ADD KEY `idProduct` (`idProduct`);

--
-- Indexes for table `courier`
--
ALTER TABLE `courier`
  ADD PRIMARY KEY (`idCourier`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`idCustomer`);

--
-- Indexes for table `delivery`
--
ALTER TABLE `delivery`
  ADD PRIMARY KEY (`idOrder`,`idCourier`),
  ADD KEY `idCourier` (`idCourier`);

--
-- Indexes for table `orderdetail`
--
ALTER TABLE `orderdetail`
  ADD PRIMARY KEY (`idOrder`,`idProduct`),
  ADD KEY `idProduct` (`idProduct`);

--
-- Indexes for table `orderheader`
--
ALTER TABLE `orderheader`
  ADD PRIMARY KEY (`idOrder`),
  ADD KEY `idCustomer` (`idCustomer`),
  ADD KEY `idPromo` (`idPromo`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`idProduct`);

--
-- Indexes for table `promo`
--
ALTER TABLE `promo`
  ADD PRIMARY KEY (`idPromo`),
  ADD UNIQUE KEY `code` (`code`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`idAdmin`) REFERENCES `user` (`idUser`) ON DELETE CASCADE;

--
-- Constraints for table `cartitem`
--
ALTER TABLE `cartitem`
  ADD CONSTRAINT `cartitem_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`idCustomer`) ON DELETE CASCADE,
  ADD CONSTRAINT `cartitem_ibfk_2` FOREIGN KEY (`idProduct`) REFERENCES `product` (`idProduct`) ON DELETE CASCADE;

--
-- Constraints for table `courier`
--
ALTER TABLE `courier`
  ADD CONSTRAINT `courier_ibfk_1` FOREIGN KEY (`idCourier`) REFERENCES `user` (`idUser`) ON DELETE CASCADE;

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `user` (`idUser`) ON DELETE CASCADE;

--
-- Constraints for table `delivery`
--
ALTER TABLE `delivery`
  ADD CONSTRAINT `delivery_ibfk_1` FOREIGN KEY (`idOrder`) REFERENCES `orderheader` (`idOrder`),
  ADD CONSTRAINT `delivery_ibfk_2` FOREIGN KEY (`idCourier`) REFERENCES `courier` (`idCourier`);

--
-- Constraints for table `orderdetail`
--
ALTER TABLE `orderdetail`
  ADD CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`idOrder`) REFERENCES `orderheader` (`idOrder`) ON DELETE CASCADE,
  ADD CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`idProduct`) REFERENCES `product` (`idProduct`);

--
-- Constraints for table `orderheader`
--
ALTER TABLE `orderheader`
  ADD CONSTRAINT `orderheader_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`idCustomer`),
  ADD CONSTRAINT `orderheader_ibfk_2` FOREIGN KEY (`idPromo`) REFERENCES `promo` (`idPromo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
