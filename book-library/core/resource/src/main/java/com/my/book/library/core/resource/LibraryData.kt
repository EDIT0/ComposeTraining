package com.my.book.library.core.resource

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

object LibraryData {
    // 1. 도서관 코드 (별도 API 조회 필요)
    data class Library(
        val code: String,
        val name: String
    )

    // 2. 성별 코드
    data class Gender(
        val code: Int,
        @StringRes val nameRes: Int
    )

    val genderList = listOf(
        Gender(0, R.string.gender_male),
        Gender(1, R.string.gender_female),
        Gender(2, R.string.gender_unknown)
    )

    // 3. 연령 코드
    data class Age(
        val code: Int,
        @StringRes val nameRes: Int,
        @StringRes val descriptionRes: Int
    )

    val ageList = listOf(
        Age(0,  R.string.age_0_5,     R.string.age_desc_0_5),
        Age(6,  R.string.age_6_7,     R.string.age_desc_6_7),
        Age(8,  R.string.age_8_13,    R.string.age_desc_8_13),
        Age(14, R.string.age_14_19,   R.string.age_desc_14_19),
        Age(20, R.string.age_20,      R.string.age_desc_20),
        Age(30, R.string.age_30,      R.string.age_desc_30),
        Age(40, R.string.age_40,      R.string.age_desc_40),
        Age(50, R.string.age_50,      R.string.age_desc_50),
        Age(60, R.string.age_60,      R.string.age_desc_60),
        Age(-1, R.string.age_unknown, R.string.age_desc_unknown)
    )

    // 4. ISBN 부가기호 코드
    data class AdditionSymbol(
        val code: Int,
        @StringRes val nameRes: Int
    )

    val additionSymbolList = listOf(
        AdditionSymbol(0, R.string.addition_general),
        AdditionSymbol(1, R.string.addition_practical),
        AdditionSymbol(2, R.string.addition_women),
        AdditionSymbol(4, R.string.addition_teen),
        AdditionSymbol(5, R.string.addition_reference_middle_high),
        AdditionSymbol(6, R.string.addition_reference_elementary),
        AdditionSymbol(7, R.string.addition_child),
        AdditionSymbol(9, R.string.addition_professional)
    )

    // 5. 대주제 코드
    data class MainSubject(
        val code: Int,
        @StringRes val nameRes: Int
    )

    val mainSubjectList = listOf(
        MainSubject(0, R.string.main_0),
        MainSubject(1, R.string.main_1),
        MainSubject(2, R.string.main_2),
        MainSubject(3, R.string.main_3),
        MainSubject(4, R.string.main_4),
        MainSubject(5, R.string.main_5),
        MainSubject(6, R.string.main_6),
        MainSubject(7, R.string.main_7),
        MainSubject(8, R.string.main_8),
        MainSubject(9, R.string.main_9)
    )

    // 6. 세부주제 코드
    data class DetailSubject(
        val code: Int,
        @StringRes val nameRes: Int
    )

    val detailSubjectList = listOf(
        // 총류 (00-09)
        DetailSubject(0, R.string.detail_subject_0),
        DetailSubject(1, R.string.detail_subject_1),
        DetailSubject(2, R.string.detail_subject_2),
        DetailSubject(3, R.string.detail_subject_3),
        DetailSubject(4, R.string.detail_subject_4),
        DetailSubject(5, R.string.detail_subject_5),
        DetailSubject(6, R.string.detail_subject_6),
        DetailSubject(7, R.string.detail_subject_7),
        DetailSubject(8, R.string.detail_subject_8),
        DetailSubject(9, R.string.detail_subject_9),
        // 철학 (10-19)
        DetailSubject(10, R.string.detail_subject_10),
        DetailSubject(11, R.string.detail_subject_11),
        DetailSubject(12, R.string.detail_subject_12),
        DetailSubject(13, R.string.detail_subject_13),
        DetailSubject(14, R.string.detail_subject_14),
        DetailSubject(15, R.string.detail_subject_15),
        DetailSubject(16, R.string.detail_subject_16),
        DetailSubject(17, R.string.detail_subject_17),
        DetailSubject(18, R.string.detail_subject_18),
        DetailSubject(19, R.string.detail_subject_19),
        // 종교 (20-29)
        DetailSubject(20, R.string.detail_subject_20),
        DetailSubject(21, R.string.detail_subject_21),
        DetailSubject(22, R.string.detail_subject_22),
        DetailSubject(23, R.string.detail_subject_23),
        DetailSubject(24, R.string.detail_subject_24),
        DetailSubject(25, R.string.detail_subject_25),
        DetailSubject(26, R.string.detail_subject_26),
        DetailSubject(27, R.string.detail_subject_27),
        DetailSubject(28, R.string.detail_subject_28),
        DetailSubject(29, R.string.detail_subject_29),
        // 사회과학 (30-39)
        DetailSubject(30, R.string.detail_subject_30),
        DetailSubject(31, R.string.detail_subject_31),
        DetailSubject(32, R.string.detail_subject_32),
        DetailSubject(33, R.string.detail_subject_33),
        DetailSubject(34, R.string.detail_subject_34),
        DetailSubject(35, R.string.detail_subject_35),
        DetailSubject(36, R.string.detail_subject_36),
        DetailSubject(37, R.string.detail_subject_37),
        DetailSubject(38, R.string.detail_subject_38),
        DetailSubject(39, R.string.detail_subject_39),
        // 자연과학 (40-49)
        DetailSubject(40, R.string.detail_subject_40),
        DetailSubject(41, R.string.detail_subject_41),
        DetailSubject(42, R.string.detail_subject_42),
        DetailSubject(43, R.string.detail_subject_43),
        DetailSubject(44, R.string.detail_subject_44),
        DetailSubject(45, R.string.detail_subject_45),
        DetailSubject(46, R.string.detail_subject_46),
        DetailSubject(47, R.string.detail_subject_47),
        DetailSubject(48, R.string.detail_subject_48),
        DetailSubject(49, R.string.detail_subject_49),
        // 기술과학 (50-59)
        DetailSubject(50, R.string.detail_subject_50),
        DetailSubject(51, R.string.detail_subject_51),
        DetailSubject(52, R.string.detail_subject_52),
        DetailSubject(53, R.string.detail_subject_53),
        DetailSubject(54, R.string.detail_subject_54),
        DetailSubject(55, R.string.detail_subject_55),
        DetailSubject(56, R.string.detail_subject_56),
        DetailSubject(57, R.string.detail_subject_57),
        DetailSubject(58, R.string.detail_subject_58),
        DetailSubject(59, R.string.detail_subject_59),
        // 예술 (60-69)
        DetailSubject(60, R.string.detail_subject_60),
        DetailSubject(61, R.string.detail_subject_61),
        DetailSubject(62, R.string.detail_subject_62),
        DetailSubject(63, R.string.detail_subject_63),
        DetailSubject(64, R.string.detail_subject_64),
        DetailSubject(65, R.string.detail_subject_65),
        DetailSubject(66, R.string.detail_subject_66),
        DetailSubject(67, R.string.detail_subject_67),
        DetailSubject(68, R.string.detail_subject_68),
        DetailSubject(69, R.string.detail_subject_69),
        // 언어 (70-79)
        DetailSubject(70, R.string.detail_subject_70),
        DetailSubject(71, R.string.detail_subject_71),
        DetailSubject(72, R.string.detail_subject_72),
        DetailSubject(73, R.string.detail_subject_73),
        DetailSubject(74, R.string.detail_subject_74),
        DetailSubject(75, R.string.detail_subject_75),
        DetailSubject(76, R.string.detail_subject_76),
        DetailSubject(77, R.string.detail_subject_77),
        DetailSubject(78, R.string.detail_subject_78),
        DetailSubject(79, R.string.detail_subject_79),
        // 문학 (80-89)
        DetailSubject(80, R.string.detail_subject_80),
        DetailSubject(81, R.string.detail_subject_81),
        DetailSubject(82, R.string.detail_subject_82),
        DetailSubject(83, R.string.detail_subject_83),
        DetailSubject(84, R.string.detail_subject_84),
        DetailSubject(85, R.string.detail_subject_85),
        DetailSubject(86, R.string.detail_subject_86),
        DetailSubject(87, R.string.detail_subject_87),
        DetailSubject(88, R.string.detail_subject_88),
        DetailSubject(89, R.string.detail_subject_89),
        // 역사 (90-99)
        DetailSubject(90, R.string.detail_subject_90),
        DetailSubject(91, R.string.detail_subject_91),
        DetailSubject(92, R.string.detail_subject_92),
        DetailSubject(93, R.string.detail_subject_93),
        DetailSubject(94, R.string.detail_subject_94),
        DetailSubject(95, R.string.detail_subject_95),
        DetailSubject(96, R.string.detail_subject_96),
        DetailSubject(97, R.string.detail_subject_97),
        DetailSubject(98, R.string.detail_subject_98),
        DetailSubject(99, R.string.detail_subject_99)
    )

    // 7. 지역 코드
    @Parcelize
    data class Region(
        val code: Int,
        @StringRes val nameRes: Int
    ): Parcelable

    val regionList = listOf(
        Region(11, R.string.region_seoul),
        Region(21, R.string.region_busan),
        Region(22, R.string.region_daegu),
        Region(23, R.string.region_incheon),
        Region(24, R.string.region_gwangju),
        Region(25, R.string.region_daejeon),
        Region(26, R.string.region_ulsan),
        Region(29, R.string.region_sejong),
        Region(31, R.string.region_gyeonggi),
        Region(32, R.string.region_gangwon),
        Region(33, R.string.region_chungbuk),
        Region(34, R.string.region_chungnam),
        Region(35, R.string.region_jeonbuk),
        Region(36, R.string.region_jeonnam),
        Region(37, R.string.region_gyeongbuk),
        Region(38, R.string.region_gyeongnam),
        Region(39, R.string.region_jeju)
    )

    // 8. 세부지역 코드
    @Parcelize
    data class DetailRegion(
        val code: Int,
        val regionCode: Int,
        @StringRes val regionNameRes: Int,
        @StringRes val districtNameRes: Int
    ): Parcelable

    val allDetailRegions = listOf(
        // 서울특별시 (11)
        DetailRegion(11010, 11, R.string.region_full_seoul, R.string.district_11010),
        DetailRegion(11020, 11, R.string.region_full_seoul, R.string.district_11020),
        DetailRegion(11030, 11, R.string.region_full_seoul, R.string.district_11030),
        DetailRegion(11040, 11, R.string.region_full_seoul, R.string.district_11040),
        DetailRegion(11050, 11, R.string.region_full_seoul, R.string.district_11050),
        DetailRegion(11060, 11, R.string.region_full_seoul, R.string.district_11060),
        DetailRegion(11070, 11, R.string.region_full_seoul, R.string.district_11070),
        DetailRegion(11080, 11, R.string.region_full_seoul, R.string.district_11080),
        DetailRegion(11090, 11, R.string.region_full_seoul, R.string.district_11090),
        DetailRegion(11100, 11, R.string.region_full_seoul, R.string.district_11100),
        DetailRegion(11110, 11, R.string.region_full_seoul, R.string.district_11110),
        DetailRegion(11120, 11, R.string.region_full_seoul, R.string.district_11120),
        DetailRegion(11130, 11, R.string.region_full_seoul, R.string.district_11130),
        DetailRegion(11140, 11, R.string.region_full_seoul, R.string.district_11140),
        DetailRegion(11150, 11, R.string.region_full_seoul, R.string.district_11150),
        DetailRegion(11160, 11, R.string.region_full_seoul, R.string.district_11160),
        DetailRegion(11170, 11, R.string.region_full_seoul, R.string.district_11170),
        DetailRegion(11180, 11, R.string.region_full_seoul, R.string.district_11180),
        DetailRegion(11190, 11, R.string.region_full_seoul, R.string.district_11190),
        DetailRegion(11200, 11, R.string.region_full_seoul, R.string.district_11200),
        DetailRegion(11210, 11, R.string.region_full_seoul, R.string.district_11210),
        DetailRegion(11220, 11, R.string.region_full_seoul, R.string.district_11220),
        DetailRegion(11230, 11, R.string.region_full_seoul, R.string.district_11230),
        DetailRegion(11240, 11, R.string.region_full_seoul, R.string.district_11240),
        DetailRegion(11250, 11, R.string.region_full_seoul, R.string.district_11250),
        // 부산광역시 (21)
        DetailRegion(21010, 21, R.string.region_full_busan, R.string.district_21010),
        DetailRegion(21020, 21, R.string.region_full_busan, R.string.district_21020),
        DetailRegion(21030, 21, R.string.region_full_busan, R.string.district_21030),
        DetailRegion(21040, 21, R.string.region_full_busan, R.string.district_21040),
        DetailRegion(21050, 21, R.string.region_full_busan, R.string.district_21050),
        DetailRegion(21060, 21, R.string.region_full_busan, R.string.district_21060),
        DetailRegion(21070, 21, R.string.region_full_busan, R.string.district_21070),
        DetailRegion(21080, 21, R.string.region_full_busan, R.string.district_21080),
        DetailRegion(21090, 21, R.string.region_full_busan, R.string.district_21090),
        DetailRegion(21100, 21, R.string.region_full_busan, R.string.district_21100),
        DetailRegion(21110, 21, R.string.region_full_busan, R.string.district_21110),
        DetailRegion(21120, 21, R.string.region_full_busan, R.string.district_21120),
        DetailRegion(21130, 21, R.string.region_full_busan, R.string.district_21130),
        DetailRegion(21140, 21, R.string.region_full_busan, R.string.district_21140),
        DetailRegion(21150, 21, R.string.region_full_busan, R.string.district_21150),
        DetailRegion(21310, 21, R.string.region_full_busan, R.string.district_21310),
        // 대구광역시 (22)
        DetailRegion(22010, 22, R.string.region_full_daegu, R.string.district_22010),
        DetailRegion(22020, 22, R.string.region_full_daegu, R.string.district_22020),
        DetailRegion(22030, 22, R.string.region_full_daegu, R.string.district_22030),
        DetailRegion(22040, 22, R.string.region_full_daegu, R.string.district_22040),
        DetailRegion(22050, 22, R.string.region_full_daegu, R.string.district_22050),
        DetailRegion(22060, 22, R.string.region_full_daegu, R.string.district_22060),
        DetailRegion(22070, 22, R.string.region_full_daegu, R.string.district_22070),
        DetailRegion(22310, 22, R.string.region_full_daegu, R.string.district_22310),
        // 인천광역시 (23)
        DetailRegion(23010, 23, R.string.region_full_incheon, R.string.district_23010),
        DetailRegion(23020, 23, R.string.region_full_incheon, R.string.district_23020),
        DetailRegion(23030, 23, R.string.region_full_incheon, R.string.district_23030),
        DetailRegion(23040, 23, R.string.region_full_incheon, R.string.district_23040),
        DetailRegion(23050, 23, R.string.region_full_incheon, R.string.district_23050),
        DetailRegion(23060, 23, R.string.region_full_incheon, R.string.district_23060),
        DetailRegion(23070, 23, R.string.region_full_incheon, R.string.district_23070),
        DetailRegion(23080, 23, R.string.region_full_incheon, R.string.district_23080),
        DetailRegion(23310, 23, R.string.region_full_incheon, R.string.district_23310),
        DetailRegion(23320, 23, R.string.region_full_incheon, R.string.district_23320),
        // 광주광역시 (24)
        DetailRegion(24010, 24, R.string.region_full_gwangju, R.string.district_24010),
        DetailRegion(24020, 24, R.string.region_full_gwangju, R.string.district_24020),
        DetailRegion(24030, 24, R.string.region_full_gwangju, R.string.district_24030),
        DetailRegion(24040, 24, R.string.region_full_gwangju, R.string.district_24040),
        DetailRegion(24050, 24, R.string.region_full_gwangju, R.string.district_24050),
        // 대전광역시 (25)
        DetailRegion(25010, 25, R.string.region_full_daejeon, R.string.district_25010),
        DetailRegion(25020, 25, R.string.region_full_daejeon, R.string.district_25020),
        DetailRegion(25030, 25, R.string.region_full_daejeon, R.string.district_25030),
        DetailRegion(25040, 25, R.string.region_full_daejeon, R.string.district_25040),
        DetailRegion(25050, 25, R.string.region_full_daejeon, R.string.district_25050),
        // 울산광역시 (26)
        DetailRegion(26010, 26, R.string.region_full_ulsan, R.string.district_26010),
        DetailRegion(26020, 26, R.string.region_full_ulsan, R.string.district_26020),
        DetailRegion(26030, 26, R.string.region_full_ulsan, R.string.district_26030),
        DetailRegion(26040, 26, R.string.region_full_ulsan, R.string.district_26040),
        DetailRegion(26310, 26, R.string.region_full_ulsan, R.string.district_26310),
        // 세종특별자치시 (29)
        DetailRegion(29010, 29, R.string.region_full_sejong, R.string.district_29010),
        // 경기도 (31)
        DetailRegion(31010, 31, R.string.region_full_gyeonggi, R.string.district_31010),
        DetailRegion(31011, 31, R.string.region_full_gyeonggi, R.string.district_31011),
        DetailRegion(31012, 31, R.string.region_full_gyeonggi, R.string.district_31012),
        DetailRegion(31013, 31, R.string.region_full_gyeonggi, R.string.district_31013),
        DetailRegion(31014, 31, R.string.region_full_gyeonggi, R.string.district_31014),
        DetailRegion(31020, 31, R.string.region_full_gyeonggi, R.string.district_31020),
        DetailRegion(31021, 31, R.string.region_full_gyeonggi, R.string.district_31021),
        DetailRegion(31022, 31, R.string.region_full_gyeonggi, R.string.district_31022),
        DetailRegion(31023, 31, R.string.region_full_gyeonggi, R.string.district_31023),
        DetailRegion(31030, 31, R.string.region_full_gyeonggi, R.string.district_31030),
        DetailRegion(31040, 31, R.string.region_full_gyeonggi, R.string.district_31040),
        DetailRegion(31041, 31, R.string.region_full_gyeonggi, R.string.district_31041),
        DetailRegion(31042, 31, R.string.region_full_gyeonggi, R.string.district_31042),
        DetailRegion(31050, 31, R.string.region_full_gyeonggi, R.string.district_31050),
        DetailRegion(31060, 31, R.string.region_full_gyeonggi, R.string.district_31060),
        DetailRegion(31070, 31, R.string.region_full_gyeonggi, R.string.district_31070),
        DetailRegion(31080, 31, R.string.region_full_gyeonggi, R.string.district_31080),
        DetailRegion(31090, 31, R.string.region_full_gyeonggi, R.string.district_31090),
        DetailRegion(31091, 31, R.string.region_full_gyeonggi, R.string.district_31091),
        DetailRegion(31092, 31, R.string.region_full_gyeonggi, R.string.district_31092),
        DetailRegion(31100, 31, R.string.region_full_gyeonggi, R.string.district_31100),
        DetailRegion(31101, 31, R.string.region_full_gyeonggi, R.string.district_31101),
        DetailRegion(31103, 31, R.string.region_full_gyeonggi, R.string.district_31103),
        DetailRegion(31104, 31, R.string.region_full_gyeonggi, R.string.district_31104),
        DetailRegion(31110, 31, R.string.region_full_gyeonggi, R.string.district_31110),
        DetailRegion(31120, 31, R.string.region_full_gyeonggi, R.string.district_31120),
        DetailRegion(31130, 31, R.string.region_full_gyeonggi, R.string.district_31130),
        DetailRegion(31140, 31, R.string.region_full_gyeonggi, R.string.district_31140),
        DetailRegion(31150, 31, R.string.region_full_gyeonggi, R.string.district_31150),
        DetailRegion(31160, 31, R.string.region_full_gyeonggi, R.string.district_31160),
        DetailRegion(31170, 31, R.string.region_full_gyeonggi, R.string.district_31170),
        DetailRegion(31180, 31, R.string.region_full_gyeonggi, R.string.district_31180),
        DetailRegion(31190, 31, R.string.region_full_gyeonggi, R.string.district_31190),
        DetailRegion(31191, 31, R.string.region_full_gyeonggi, R.string.district_31191),
        DetailRegion(31192, 31, R.string.region_full_gyeonggi, R.string.district_31192),
        DetailRegion(31193, 31, R.string.region_full_gyeonggi, R.string.district_31193),
        DetailRegion(31200, 31, R.string.region_full_gyeonggi, R.string.district_31200),
        DetailRegion(31210, 31, R.string.region_full_gyeonggi, R.string.district_31210),
        DetailRegion(31220, 31, R.string.region_full_gyeonggi, R.string.district_31220),
        DetailRegion(31230, 31, R.string.region_full_gyeonggi, R.string.district_31230),
        DetailRegion(31240, 31, R.string.region_full_gyeonggi, R.string.district_31240),
        DetailRegion(31250, 31, R.string.region_full_gyeonggi, R.string.district_31250),
        DetailRegion(31260, 31, R.string.region_full_gyeonggi, R.string.district_31260),
        DetailRegion(31270, 31, R.string.region_full_gyeonggi, R.string.district_31270),
        DetailRegion(31280, 31, R.string.region_full_gyeonggi, R.string.district_31280),
        DetailRegion(31350, 31, R.string.region_full_gyeonggi, R.string.district_31350),
        DetailRegion(31370, 31, R.string.region_full_gyeonggi, R.string.district_31370),
        DetailRegion(31380, 31, R.string.region_full_gyeonggi, R.string.district_31380),
        // 강원특별자치도 (32)
        DetailRegion(32010, 32, R.string.region_full_gangwon, R.string.district_32010),
        DetailRegion(32020, 32, R.string.region_full_gangwon, R.string.district_32020),
        DetailRegion(32030, 32, R.string.region_full_gangwon, R.string.district_32030),
        DetailRegion(32040, 32, R.string.region_full_gangwon, R.string.district_32040),
        DetailRegion(32050, 32, R.string.region_full_gangwon, R.string.district_32050),
        DetailRegion(32060, 32, R.string.region_full_gangwon, R.string.district_32060),
        DetailRegion(32070, 32, R.string.region_full_gangwon, R.string.district_32070),
        DetailRegion(32310, 32, R.string.region_full_gangwon, R.string.district_32310),
        DetailRegion(32320, 32, R.string.region_full_gangwon, R.string.district_32320),
        DetailRegion(32330, 32, R.string.region_full_gangwon, R.string.district_32330),
        DetailRegion(32340, 32, R.string.region_full_gangwon, R.string.district_32340),
        DetailRegion(32350, 32, R.string.region_full_gangwon, R.string.district_32350),
        DetailRegion(32360, 32, R.string.region_full_gangwon, R.string.district_32360),
        DetailRegion(32370, 32, R.string.region_full_gangwon, R.string.district_32370),
        DetailRegion(32380, 32, R.string.region_full_gangwon, R.string.district_32380),
        DetailRegion(32390, 32, R.string.region_full_gangwon, R.string.district_32390),
        DetailRegion(32400, 32, R.string.region_full_gangwon, R.string.district_32400),
        DetailRegion(32410, 32, R.string.region_full_gangwon, R.string.district_32410),
        // 충청북도 (33)
        DetailRegion(33020, 33, R.string.region_full_chungbuk, R.string.district_33020),
        DetailRegion(33030, 33, R.string.region_full_chungbuk, R.string.district_33030),
        DetailRegion(33040, 33, R.string.region_full_chungbuk, R.string.district_33040),
        DetailRegion(33041, 33, R.string.region_full_chungbuk, R.string.district_33041),
        DetailRegion(33042, 33, R.string.region_full_chungbuk, R.string.district_33042),
        DetailRegion(33043, 33, R.string.region_full_chungbuk, R.string.district_33043),
        DetailRegion(33044, 33, R.string.region_full_chungbuk, R.string.district_33044),
        DetailRegion(33320, 33, R.string.region_full_chungbuk, R.string.district_33320),
        DetailRegion(33330, 33, R.string.region_full_chungbuk, R.string.district_33330),
        DetailRegion(33340, 33, R.string.region_full_chungbuk, R.string.district_33340),
        DetailRegion(33350, 33, R.string.region_full_chungbuk, R.string.district_33350),
        DetailRegion(33360, 33, R.string.region_full_chungbuk, R.string.district_33360),
        DetailRegion(33370, 33, R.string.region_full_chungbuk, R.string.district_33370),
        DetailRegion(33380, 33, R.string.region_full_chungbuk, R.string.district_33380),
        DetailRegion(33390, 33, R.string.region_full_chungbuk, R.string.district_33390),
        // 충청남도 (34)
        DetailRegion(34010, 34, R.string.region_full_chungnam, R.string.district_34010),
        DetailRegion(34011, 34, R.string.region_full_chungnam, R.string.district_34011),
        DetailRegion(34012, 34, R.string.region_full_chungnam, R.string.district_34012),
        DetailRegion(34020, 34, R.string.region_full_chungnam, R.string.district_34020),
        DetailRegion(34030, 34, R.string.region_full_chungnam, R.string.district_34030),
        DetailRegion(34040, 34, R.string.region_full_chungnam, R.string.district_34040),
        DetailRegion(34050, 34, R.string.region_full_chungnam, R.string.district_34050),
        DetailRegion(34060, 34, R.string.region_full_chungnam, R.string.district_34060),
        DetailRegion(34070, 34, R.string.region_full_chungnam, R.string.district_34070),
        DetailRegion(34080, 34, R.string.region_full_chungnam, R.string.district_34080),
        DetailRegion(34310, 34, R.string.region_full_chungnam, R.string.district_34310),
        DetailRegion(34330, 34, R.string.region_full_chungnam, R.string.district_34330),
        DetailRegion(34340, 34, R.string.region_full_chungnam, R.string.district_34340),
        DetailRegion(34350, 34, R.string.region_full_chungnam, R.string.district_34350),
        DetailRegion(34360, 34, R.string.region_full_chungnam, R.string.district_34360),
        DetailRegion(34370, 34, R.string.region_full_chungnam, R.string.district_34370),
        DetailRegion(34380, 34, R.string.region_full_chungnam, R.string.district_34380),
        // 전북특별자치도 (35)
        DetailRegion(35010, 35, R.string.region_full_jeonbuk, R.string.district_35010),
        DetailRegion(35011, 35, R.string.region_full_jeonbuk, R.string.district_35011),
        DetailRegion(35012, 35, R.string.region_full_jeonbuk, R.string.district_35012),
        DetailRegion(35020, 35, R.string.region_full_jeonbuk, R.string.district_35020),
        DetailRegion(35030, 35, R.string.region_full_jeonbuk, R.string.district_35030),
        DetailRegion(35040, 35, R.string.region_full_jeonbuk, R.string.district_35040),
        DetailRegion(35050, 35, R.string.region_full_jeonbuk, R.string.district_35050),
        DetailRegion(35060, 35, R.string.region_full_jeonbuk, R.string.district_35060),
        DetailRegion(35310, 35, R.string.region_full_jeonbuk, R.string.district_35310),
        DetailRegion(35320, 35, R.string.region_full_jeonbuk, R.string.district_35320),
        DetailRegion(35330, 35, R.string.region_full_jeonbuk, R.string.district_35330),
        DetailRegion(35340, 35, R.string.region_full_jeonbuk, R.string.district_35340),
        DetailRegion(35350, 35, R.string.region_full_jeonbuk, R.string.district_35350),
        DetailRegion(35360, 35, R.string.region_full_jeonbuk, R.string.district_35360),
        DetailRegion(35370, 35, R.string.region_full_jeonbuk, R.string.district_35370),
        DetailRegion(35380, 35, R.string.region_full_jeonbuk, R.string.district_35380),
        // 전라남도 (36)
        DetailRegion(36010, 36, R.string.region_full_jeonnam, R.string.district_36010),
        DetailRegion(36020, 36, R.string.region_full_jeonnam, R.string.district_36020),
        DetailRegion(36030, 36, R.string.region_full_jeonnam, R.string.district_36030),
        DetailRegion(36040, 36, R.string.region_full_jeonnam, R.string.district_36040),
        DetailRegion(36060, 36, R.string.region_full_jeonnam, R.string.district_36060),
        DetailRegion(36310, 36, R.string.region_full_jeonnam, R.string.district_36310),
        DetailRegion(36320, 36, R.string.region_full_jeonnam, R.string.district_36320),
        DetailRegion(36330, 36, R.string.region_full_jeonnam, R.string.district_36330),
        DetailRegion(36350, 36, R.string.region_full_jeonnam, R.string.district_36350),
        DetailRegion(36360, 36, R.string.region_full_jeonnam, R.string.district_36360),
        DetailRegion(36370, 36, R.string.region_full_jeonnam, R.string.district_36370),
        DetailRegion(36380, 36, R.string.region_full_jeonnam, R.string.district_36380),
        DetailRegion(36390, 36, R.string.region_full_jeonnam, R.string.district_36390),
        DetailRegion(36400, 36, R.string.region_full_jeonnam, R.string.district_36400),
        DetailRegion(36410, 36, R.string.region_full_jeonnam, R.string.district_36410),
        DetailRegion(36420, 36, R.string.region_full_jeonnam, R.string.district_36420),
        DetailRegion(36430, 36, R.string.region_full_jeonnam, R.string.district_36430),
        DetailRegion(36440, 36, R.string.region_full_jeonnam, R.string.district_36440),
        DetailRegion(36450, 36, R.string.region_full_jeonnam, R.string.district_36450),
        DetailRegion(36460, 36, R.string.region_full_jeonnam, R.string.district_36460),
        DetailRegion(36470, 36, R.string.region_full_jeonnam, R.string.district_36470),
        DetailRegion(36480, 36, R.string.region_full_jeonnam, R.string.district_36480),
        // 경상북도 (37)
        DetailRegion(37010, 37, R.string.region_full_gyeongbuk, R.string.district_37010),
        DetailRegion(37011, 37, R.string.region_full_gyeongbuk, R.string.district_37011),
        DetailRegion(37012, 37, R.string.region_full_gyeongbuk, R.string.district_37012),
        DetailRegion(37020, 37, R.string.region_full_gyeongbuk, R.string.district_37020),
        DetailRegion(37030, 37, R.string.region_full_gyeongbuk, R.string.district_37030),
        DetailRegion(37040, 37, R.string.region_full_gyeongbuk, R.string.district_37040),
        DetailRegion(37050, 37, R.string.region_full_gyeongbuk, R.string.district_37050),
        DetailRegion(37060, 37, R.string.region_full_gyeongbuk, R.string.district_37060),
        DetailRegion(37070, 37, R.string.region_full_gyeongbuk, R.string.district_37070),
        DetailRegion(37080, 37, R.string.region_full_gyeongbuk, R.string.district_37080),
        DetailRegion(37090, 37, R.string.region_full_gyeongbuk, R.string.district_37090),
        DetailRegion(37100, 37, R.string.region_full_gyeongbuk, R.string.district_37100),
        DetailRegion(37310, 37, R.string.region_full_gyeongbuk, R.string.district_37310),
        DetailRegion(37320, 37, R.string.region_full_gyeongbuk, R.string.district_37320),
        DetailRegion(37330, 37, R.string.region_full_gyeongbuk, R.string.district_37330),
        DetailRegion(37340, 37, R.string.region_full_gyeongbuk, R.string.district_37340),
        DetailRegion(37350, 37, R.string.region_full_gyeongbuk, R.string.district_37350),
        DetailRegion(37360, 37, R.string.region_full_gyeongbuk, R.string.district_37360),
        DetailRegion(37370, 37, R.string.region_full_gyeongbuk, R.string.district_37370),
        DetailRegion(37380, 37, R.string.region_full_gyeongbuk, R.string.district_37380),
        DetailRegion(37390, 37, R.string.region_full_gyeongbuk, R.string.district_37390),
        DetailRegion(37400, 37, R.string.region_full_gyeongbuk, R.string.district_37400),
        DetailRegion(37410, 37, R.string.region_full_gyeongbuk, R.string.district_37410),
        DetailRegion(37420, 37, R.string.region_full_gyeongbuk, R.string.district_37420),
        DetailRegion(37430, 37, R.string.region_full_gyeongbuk, R.string.district_37430),
        // 경상남도 (38)
        DetailRegion(38030, 38, R.string.region_full_gyeongnam, R.string.district_38030),
        DetailRegion(38050, 38, R.string.region_full_gyeongnam, R.string.district_38050),
        DetailRegion(38060, 38, R.string.region_full_gyeongnam, R.string.district_38060),
        DetailRegion(38070, 38, R.string.region_full_gyeongnam, R.string.district_38070),
        DetailRegion(38080, 38, R.string.region_full_gyeongnam, R.string.district_38080),
        DetailRegion(38090, 38, R.string.region_full_gyeongnam, R.string.district_38090),
        DetailRegion(38100, 38, R.string.region_full_gyeongnam, R.string.district_38100),
        DetailRegion(38110, 38, R.string.region_full_gyeongnam, R.string.district_38110),
        DetailRegion(38111, 38, R.string.region_full_gyeongnam, R.string.district_38111),
        DetailRegion(38112, 38, R.string.region_full_gyeongnam, R.string.district_38112),
        DetailRegion(38113, 38, R.string.region_full_gyeongnam, R.string.district_38113),
        DetailRegion(38114, 38, R.string.region_full_gyeongnam, R.string.district_38114),
        DetailRegion(38115, 38, R.string.region_full_gyeongnam, R.string.district_38115),
        DetailRegion(38310, 38, R.string.region_full_gyeongnam, R.string.district_38310),
        DetailRegion(38320, 38, R.string.region_full_gyeongnam, R.string.district_38320),
        DetailRegion(38330, 38, R.string.region_full_gyeongnam, R.string.district_38330),
        DetailRegion(38340, 38, R.string.region_full_gyeongnam, R.string.district_38340),
        DetailRegion(38350, 38, R.string.region_full_gyeongnam, R.string.district_38350),
        DetailRegion(38360, 38, R.string.region_full_gyeongnam, R.string.district_38360),
        DetailRegion(38370, 38, R.string.region_full_gyeongnam, R.string.district_38370),
        DetailRegion(38380, 38, R.string.region_full_gyeongnam, R.string.district_38380),
        DetailRegion(38390, 38, R.string.region_full_gyeongnam, R.string.district_38390),
        DetailRegion(38400, 38, R.string.region_full_gyeongnam, R.string.district_38400),
        // 제주특별자치도 (39)
        DetailRegion(39010, 39, R.string.region_full_jeju, R.string.district_39010),
        DetailRegion(39020, 39, R.string.region_full_jeju, R.string.district_39020)
    )

    // 지역 코드로 세부지역 필터링하는 확장 함수
    fun List<DetailRegion>.filterByRegion(regionCode: Int): List<DetailRegion> {
        return this.filter { it.regionCode == regionCode }
    }
}
