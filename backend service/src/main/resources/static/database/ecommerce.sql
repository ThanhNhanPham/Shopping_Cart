-- --------------------------------------------------------
-- Máy chủ:                      127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Phiên bản:           12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for ecommerce_tb
CREATE DATABASE IF NOT EXISTS `ecommerce_tb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `ecommerce_tb`;

-- Dumping structure for table ecommerce_tb.cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3d704slv66tw6x5hmbm6p2x3u` (`product_id`),
  KEY `FK9x4wn098i53ikun1ynxet2ynj` (`user_id`),
  CONSTRAINT `FK3d704slv66tw6x5hmbm6p2x3u` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK9x4wn098i53ikun1ynxet2ynj` FOREIGN KEY (`user_id`) REFERENCES `user_dtls` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ecommerce_tb.cart: ~2 rows (approximately)
INSERT INTO `cart` (`id`, `quantity`, `product_id`, `user_id`) VALUES
	(20, 1, 10, 7),
	(26, 1, 11, 22),
	(27, 1, 8, 22);

-- Dumping structure for table ecommerce_tb.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image_name` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ecommerce_tb.category: ~6 rows (approximately)
INSERT INTO `category` (`id`, `image_name`, `is_active`, `name`) VALUES
	(7, 'laptop1.png', b'1', 'Latop'),
	(9, 'shirt.png', b'1', 'Clothes'),
	(13, 'oppoa3.jpg', b'1', 'Mobile Phone'),
	(14, 'nike.jpg', b'1', 'Shoes'),
	(15, 'watches1.jpg', b'1', 'Watches'),
	(16, 'guccy1.jpg', b'1', 'Asseccory');

-- Dumping structure for table ecommerce_tb.order_address
CREATE TABLE IF NOT EXISTS `order_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mobile_no` varchar(255) DEFAULT NULL,
  `pincode` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ecommerce_tb.order_address: ~10 rows (approximately)
INSERT INTO `order_address` (`id`, `address`, `city`, `email`, `first_name`, `last_name`, `mobile_no`, `pincode`, `state`) VALUES
	(5, 'Hoà Hải', 'Đà Nẵng', 'huynhdangquoc2004@gmail.com', 'Huynh Dang', 'Quoc', '0905923427', '12345', 'NHS'),
	(6, 'Tam Ky', 'Quang Nam', 'demo@gmail.com', 'Ho Gia', 'Huy', '0905923427', '1223', 'NHS'),
	(7, 'Tổ 42, Trần Đại Nghĩa', 'Đà Nẵng', 'huynhdangquoc2004@gmail.com', 'Huynh Dang', 'Quoc', '0905923427', '5544', 'NHS'),
	(8, 'Tổ 42, Trần Đại Nghĩa', 'Đà Nẵng', 'huynhdangquoc2004@gmail.com', 'Huynh Dang', 'Quoc', '0905923427', '5544', 'NHS'),
	(23, 'Tran Dai Nghia 2', 'Đà Nẵng', 'bichthiho1989@gmail.com', 'Ho Thi ', 'Bich', '0905923427', '22345', 'NHS'),
	(24, 'Tran Dai Nghia 2', 'Đà Nẵng', 'bichthiho1989@gmail.com', 'Ho Thi ', 'Bich', '0905923427', '22345', 'NHS'),
	(25, 'Tran Dai Nghia 2', 'Da Nang 2', 'demo@gmail.com', 'Huynh Dang', 'Quoc', '123455667657', '22345', 'NHS'),
	(26, 'Tran Dai Nghia 2', 'Da Nang 2', 'demo@gmail.com', 'Huynh Dang', 'Quoc', '123455667657', '22345', 'NHS'),
	(27, 'Tran Dai Nghia 2', 'Da Nang 2', 'demo@gmail.com', 'Huynh Dang', 'Quoc', '123455667657', '22345', 'NHS'),
	(28, 'Tran Dai Nghia 2', 'Da Nang 2', 'demo@gmail.com', 'Huynh Dang', 'Quoc', '123455667657', '22345', 'NHS');

-- Dumping structure for table ecommerce_tb.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(5000) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `stock` int(11) NOT NULL,
  `title` varchar(500) DEFAULT NULL,
  `discount` int(11) NOT NULL,
  `discount_price` double NOT NULL,
  `is_active` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ecommerce_tb.product: ~8 rows (approximately)
INSERT INTO `product` (`id`, `category`, `description`, `image`, `price`, `stock`, `title`, `discount`, `discount_price`, `is_active`) VALUES
	(5, 'Clothes', '– Loại: Cổ Tròn – Polo – Hoodie – Raglan\r\n\r\n– Form: OVER SIZE – BASIC\r\n\r\n– Vải: 100% cotton 2 chiều (250gsm)\r\n\r\n– Thành phần sợi: 95% Cotton 5% Spandex\r\n\r\n– In: PET – Decal – DTG – Lụa – In Nổi …', 'aothun.jpg', 115000, 50, 'ÁO THUN XANH ĐẬM', 0, 115000, b'1'),
	(8, 'Mobile Phone', 'Vi xử lý mạnh mẽ nhất Galaxy - Snapdragon 8 Gen 1 (4 nm)-\r\nCamera mắt thần bóng đêm Nightography - Chụp đêm cực đỉnh-\r\nS Pen đầu tiên trên Galaxy S - Độ trễ thấp, dễ thao tác-\r\nDung lượng pin bất chấp ngày đêm - Viên pin 5000mAh, sạc nhanh 45W', 'samsungs22ultra.jpg', 950000, 10, 'Samsung S22 Ultra', 10, 855000, b'1'),
	(9, 'Clothes', '– Loại: Cổ Tròn – Polo – Hoodie – Raglan\r\n\r\n– Form: OVER SIZE – BASIC\r\n\r\n– Vải: 100% cotton 2 chiều (250gsm)\r\n\r\n– Thành phần sợi: 95% Cotton 5% Spandex\r\n\r\n– In: PET – Decal – DTG – Lụa – In Nổi …', 'aothunkemnau.jpg', 200000, 0, 'ÁO THUN ĐEN LOCAL', 0, 200000, b'1'),
	(10, 'Mobile Phone', ' - Thiết kế tiệm cận tràn viền mãn nhãn bậc nhất nhờ viền màn hình siêu mỏng\r\n\r\n - Chất liệu Titanium gia công 14 tiếng cho độ cứng cáp hàng đầu\r\n\r\n - Apple “chơi lớn” với màu Blue Titanium và Natural Titanium lần đầu ra mắt\r\n\r\n - Khai tử cổng Lightning thay bằng USB-C tăng tốc độ truyền dữ liệu 10 gigabit/giây\r\n\r\n - Nút Action Button mới thú vị hơn, đỉnh cao hơn\r\n\r\n - Chụp Chân dung nâng cấp và cải thiện chế độ quay video 4K .', 'iphone15.jpg', 300000, 23, 'iPhone 15 Pro Max ', 20, 240000, b'1'),
	(11, 'Latop', 'BỘ VI XỬ LÝ :\r\n\r\n_Intel Core i3-1115G4 3.0 up to 4.1 GHz\r\n\r\n_Windows 10 Home 64bit\r\n\r\nRAM\r\n\r\n_8GB DDR4 3200 MHz\r\n\r\nỔ CỨNG\r\n\r\n_128GB PCIe NVMe M.2 SSD storage\r\n\r\nMÀN HÌNH\r\n\r\n_14-inch Full HD LED display (1920 x 1080)', 'laptop1.png', 900000, 10, 'Laptop Asus Vivobook', 0, 900000, b'1'),
	(12, 'Shoes', 'Giày Sneaker SX02 trắng tự hào là một sản phẩm được tạo ra từ chất liệu da siêu đẹp, phong cách thời trang hot trend. Mặt ngoài của giày được làm từ 100% da bò cao cấp nhập khẩu, đảm bảo tính bền đẹp và dễ dàng vệ sinh. Không chỉ vậy, da mềm mại mang lại cảm giác êm ái và thoải mái cho đôi chân của bạn trong mọi hoạt động di chuyển.', 'nike.jpg', 499000, 10, 'Giày Sneaker Trắng', 10, 449100, b'1'),
	(13, 'Watches', 'Đồng hồ thông minh Apple Watch Ultra 2tích hợp hơn 20 môn thể thao và đo sức khỏe nhịp tim, huyết áp, SPO2 màn hình IPS sắc nét', 'watches3.jpg', 800000, 10, 'Apple Watch Ultra 2', 15, 680000, b'1'),
	(14, 'Watches', ' Thêm tính năng Always-On Retina display (Màn hình Retina luôn bật)\r\n- Hỗ trợ tập luyện hoạt động được nâng cao.\r\n- Hỗ trợ nghe nhạc ngay cả khi không có điện thoại bên cạnh.\r\n- Dòng máy tương thích : iPhone\r\n- Hỗ trợ định vị GPS và bộ vi xử lí GLONASS\r\n- Xử lý/ Bộ nhớ : Apple S5\r\n- Dung lượng Pin/Thời gian sử dụng : Lên đến 18h\r\n- Thông báo : Tin nhắn, cuộc gọi,…\r\n- Cảm biến : con quay hồi chuyển thế hệ 2 , cảm biến nhịp tim, gia tốc, và cảm biến ánh sáng môi trường xung quanh công nghệ mới\r\n- Hệ điều hành : WatchOS 6', 'watches4.jpg', 880000, 10, 'Apple Watch Series 5', 0, 880000, b'1');

-- Dumping structure for table ecommerce_tb.product_order
CREATE TABLE IF NOT EXISTS `product_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_date` date DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `order_address_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqcdbxaeuc7c5gahwh0dutg04r` (`order_address_id`),
  KEY `FKh73acsd9s5wp6l0e55td6jr1m` (`product_id`),
  KEY `FK4f2ycr32kigtux5ag3tv0xu5m` (`user_id`),
  CONSTRAINT `FK4f2ycr32kigtux5ag3tv0xu5m` FOREIGN KEY (`user_id`) REFERENCES `user_dtls` (`id`),
  CONSTRAINT `FK8frxalwc79tpxo7hgqp3hsjck` FOREIGN KEY (`order_address_id`) REFERENCES `order_address` (`id`),
  CONSTRAINT `FKh73acsd9s5wp6l0e55td6jr1m` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ecommerce_tb.product_order: ~6 rows (approximately)
INSERT INTO `product_order` (`id`, `order_date`, `order_id`, `payment_type`, `price`, `quantity`, `status`, `product_id`, `user_id`, `order_address_id`) VALUES
	(5, '2024-12-03', '002700fe-0155-4531-b0d6-31e5efbe68de', 'COD', 900000, 1, 'Delivered', 11, 5, 5),
	(6, '2024-12-04', '643e2b95-bd14-4443-83e8-b9c72442f412', 'COD', 855000, 1, 'Cancelled', 8, 5, 6),
	(7, '2024-12-06', '160481c2-683a-4d41-978c-e974ba63a702', 'COD', 855000, 2, 'In Progress', 8, 7, 7),
	(8, '2024-12-06', '278e2650-312e-4353-bcff-489f8ebc9332', 'COD', 115000, 1, 'Order Received', 5, 7, 8),
	(25, '2025-05-09', '47492a9a-d8b1-4f80-a371-76caf5d40bed', 'COD', 900000, 1, 'In Progress', 11, 22, 25),
	(27, '2025-05-09', '30e90fd0-21fd-4cb0-8467-1f4b82626503', 'COD', 855000, 1, 'In Progress', 8, 22, 27);

-- Dumping structure for table ecommerce_tb.user_dtls
CREATE TABLE IF NOT EXISTS `user_dtls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `pincode` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `is_enable` bit(1) DEFAULT NULL,
  `account_non_locked` bit(1) DEFAULT NULL,
  `failed_attempt` int(11) NOT NULL,
  `lock_time` datetime(6) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ecommerce_tb.user_dtls: ~5 rows (approximately)
INSERT INTO `user_dtls` (`id`, `address`, `city`, `email`, `mobile_number`, `name`, `password`, `pincode`, `profile_image`, `state`, `role`, `is_enable`, `account_non_locked`, `failed_attempt`, `lock_time`, `reset_token`) VALUES
	(5, 'Tran Dai Nghia', 'Da Nang 23', 'demo@gmail.com', '0905923427', 'Quocne', '$2a$10$z7SY0AjnrepFeuTwmQjvEexoH6pPZlmTNnutW.8zKzOpNUrjJHgMS', '22345', 'watches3.jpg', 'Alone', 'ROLE_USER', b'1', b'1', 1, NULL, '89292ffc-d2a5-4a8d-bb4c-f83c86461812'),
	(6, 'Tran Dai Nghia', 'Quang Nam', 'admin@gmail.com', '123456789', 'Admin', '$2a$12$ehGPJ6vKc0KqzcHqmuQGX.8QLqOGt0jHzkX9iaiLgIcE7DfbSAGGK', '12345', 'laptop4.png', 'Alone', 'ROLE_ADMIN', b'1', b'1', 2, NULL, NULL),
	(7, 'Hoà Hải', 'Đà Nẵng', 'quoc@gmail.com', '0905923427', 'Huỳnh Đăng Quốc', '$2a$10$sx2dv4T2XSmEAMhAy.1jRO4ctaYMLKZK48gCZiUxo6LBectvTlhOi', '455', 'quoc.jpg', 'NHS', 'ROLE_USER', b'1', b'1', 0, NULL, NULL),
	(8, 'Tran Dai Nghia 2', 'Quang Nam 2', 'admin2@gmail.com', '123456789', 'Admin MC', '$2a$10$.FqNaqVewVt.e7mT9jDhX.e5x4Nj2g1.0KnzqUXBXpQOmqM/Dqkz.', '22345', 'laptop.jpg', 'NHS', 'ROLE_ADMIN', b'1', b'1', 0, NULL, NULL),
	(22, 'Tran Dai Nghia', 'Da Nang 23', 'demo2@gmail.com', '0905923427', 'Tuan Tu', '$2a$10$Qend/640QPSAbVckQ3agP.ocqynQX9Vj8kB6kwUQJYC88EamQpGWq', '22345', 'room2.jpg', 'NHS', 'ROLE_USER', b'1', b'1', 0, NULL, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
