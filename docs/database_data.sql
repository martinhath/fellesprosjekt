-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: mysql.stud.ntnu.no
-- Generation Time: Mar 12, 2015 at 12:36 PM
-- Server version: 5.5.41
-- PHP Version: 5.3.10-1ubuntu3.16

--SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
--SET time_zone = "+00:00";


--/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
--/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
--/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
--/*!40101 SET NAMES utf8 */;


INSERT INTO `Room` (`capacity`, `room_num`, `available`) VALUES
(5, 'Grupperom 204', 1),
(40, 'Festsal 101', 1),
(3, 'Kontor 314', 1);



INSERT INTO `User` (`userid`, `username`, `name`, `email`, `password`, `salt`, `create_time`, `update_time`) VALUES
(1, 'Viktor', 'Viktor Andersen', 'viktorfa@stud.ntnu.no', 'b8d4c62d9bee042e87e683074797a6033771173a555597006676c6831918ec2b', '99b4be447c706372f32f989349ad6ba6', '2015-03-12 11:39:10', '2015-03-12 10:39:10'),
(2, 'Phrida', 'Phrida Norrhall', 'phridanorrhall@gmail.com', '5deaf4d5360780bb9cbe410a4ab744f3cbc43657cc3f61ea46516a31ca9f1aaf', 'd34d40fcb112de57cdbb4258b7ced743', '2015-03-12 11:41:41', '2015-03-12 10:41:41'),
(3, 'Martin', 'Martin Thoresen', 'martinhath@gmail.com', 'ace8198b7e8302a061a12cf4176c2c5bada0798ddf9355cce26351dac572e110', '49fa875509794513f1a71bd9fa145224', '2015-03-12 11:43:28', '2015-03-12 10:43:28'),
(4, 'Herman', 'Herman Karlsen', 'hermanmk@stud.ntnu.no', 'ee52be1a170782d005515d63c9ac4f7787c91d87699d386db271d433fdb3fee7', '5e9c45f0ba61803c993975f8723c3fa8', '2015-03-12 11:47:58', '2015-03-12 10:47:58'),
(5, 'Ingrid', 'Ingrid Vold', 'none', '5ca437c5e1734f2634b88079de6752fd7ed0062917743d299738e0808e7f4723', '63324c6e82d6f43a00ef47c7575f12e3', '2015-03-12 12:01:21', '2015-03-12 11:01:21');



INSERT INTO `User_group` (`groupid`, `name`, `owner_id`, `create_time`, `update_time`) VALUES
(1, 'The Gang', 1, '2015-03-12 11:56:03', '2015-03-12 10:56:03'),
(2, 'Gruppe 24', 3, '2015-03-12 11:58:43', '2015-03-12 10:58:43');



INSERT INTO `Meeting` (`meetingid`, `name`, `description`, `start_time`, `end_time`, `Room_roomid`, `location`, `owner_id`, `Group_groupid`, `create_time`, `update_time`) VALUES
(1, 'Fredagspils', 'På fredag skal vi drikke en god del pils, og så klart litt sambuca.', '2015-05-08 18:00:00', '2015-05-09 00:00:00', 2, 'Trondheim Torg', 4, 1, '2015-03-12 12:22:40', '2015-03-12 11:31:32'),
(2, 'Gruppemøte', 'Obligatorisk møte for alle gruppemedlemmer!', '2015-04-26 12:00:00', '2015-04-26 15:30:00', 1, 'Gløshaugen', 3, 2, '2015-03-12 12:30:36', '2015-03-12 11:30:36'),
(3, 'Fresh fest', 'Sick/10 fest der vi skal chillen, høre på smud musikk, og spise Grandis (med stor G!). Obligatorisk oppmøte i Bergljots Gate 5.', '2015-03-19 15:00:00', '2015-03-19 19:30:00', 2, 'Bergljots', 3, 2, '2015-03-12 12:30:36', '2015-03-12 11:30:36'),
(4, 'Grill&Chill', 'Vi tar livet med ro, og spiser grillmat i høyskoleparken.', '2015-03-20 12:00:00', '2015-03-20 16:00:00', 2, 'Høyskoleparken', 3, 2, '2015-03-12 12:30:36', '2015-03-12 11:30:36'),
(5, 'lel', 'top lel top lel top lel top lel', '2015-03-21 10:00:00', '2015-03-20 21:00:00', 2, 'lel lellesen', 3, 2, '2015-03-12 12:30:36', '2015-03-12 11:30:36');

--
-- Dumping data for table `User_group_has_User`
--

INSERT INTO `User_group_has_User` (`User_userid`, `User_group_groupid`, `notification_message`, `notification_read`, `confirmed`) VALUES
(1, 1, 'Du lagde denne gruppen.', 1, 1),
(1, 2, 'Du har blitt invitert til gruppen ''Gruppe 24''.', 0, 0),
(2, 2, 'Du har blitt invitert til gruppen ''Gruppe 24''.', 0, 0),
(3, 2, 'Du lagde denne gruppen.', 1, 1),
(4, 1, 'Du har blitt invitert til gruppen ''The Gang''.', 0, 0),
(4, 2, 'Du har blitt invitert til gruppen ''Gruppe 24''.', 0, 0),
(5, 2, 'Du har blitt invitert til gruppen ''Gruppe 24''.', 0, 0);

--
-- Dumping data for table `User_invited_to_meeting`
--

INSERT INTO `User_invited_to_meeting` (`User_userid`, `Meeting_meetingid`, `notification_message`, `notification_read`, `confirmed`, `hide`, `alarm_time`) VALUES
(1, 1, 'Du har blitt invitert til møtet ''Fredagspils''.', 0, 0, 0, NULL),
(4, 1, 'Du lagde dette møtet.', 1, 1, 0, NULL),
(1, 2, 'Du har blitt invitert til møtet ''Gruppemøte''.', 1, 0, 0, NULL),
(2, 2, 'Du har blitt invitert til møtet ''Gruppemøte''.', 1, 0, 0, NULL),
(3, 2, 'Du lagde dette møtet.', 0, 0, 0, NULL),
(4, 2, 'Du har blitt invitert til møtet ''Gruppemøte''.', 0, 0, 0, NULL),
(5, 2, 'Du har blitt invitert til møtet ''Gruppemøte''.', 1, 0, 0, NULL);

--/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
--/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS 0*/;
--/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
