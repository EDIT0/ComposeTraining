package com.my.book.library.core.resource

import android.os.Parcelable
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
        val name: String
    )

    val genderList = listOf(
        Gender(0, "남성"),
        Gender(1, "여성"),
        Gender(2, "미상")
    )

    // 3. 연령 코드
    data class Age(
        val code: Int,
        val name: String,
        val description: String
    )

    val ageList = listOf(
        Age(0, "영유아", "0~5세"),
        Age(6, "유아", "6~7세"),
        Age(8, "초등", "8~13세"),
        Age(14, "청소년", "14~19세"),
        Age(20, "20대", "20대"),
        Age(30, "30대", "30대"),
        Age(40, "40대", "40대"),
        Age(50, "50대", "50대"),
        Age(60, "60세 이상", "60세 이상"),
        Age(-1, "미상", "미상")
    )

    // 4. ISBN 부가기호 코드
    data class AdditionSymbol(
        val code: Int,
        val name: String
    )

    val additionSymbolList = listOf(
        AdditionSymbol(0, "교양"),
        AdditionSymbol(1, "실용"),
        AdditionSymbol(2, "여성"),
        AdditionSymbol(4, "청소년"),
        AdditionSymbol(5, "학습참고서 1(중고)"),
        AdditionSymbol(6, "학습참고서 2(초등)"),
        AdditionSymbol(7, "아동"),
        AdditionSymbol(9, "전문")
    )

    // 5. 대주제 코드
    data class MainSubject(
        val code: Int,
        val name: String
    )

    val mainSubjectList = listOf(
        MainSubject(0, "총류"),
        MainSubject(1, "철학"),
        MainSubject(2, "종교"),
        MainSubject(3, "사회과학"),
        MainSubject(4, "자연과학"),
        MainSubject(5, "기술과학"),
        MainSubject(6, "예술"),
        MainSubject(7, "언어"),
        MainSubject(8, "문학"),
        MainSubject(9, "역사")
    )

    // 6. 세부주제 코드
    data class DetailSubject(
        val code: Int,
        val name: String
    )

    val detailSubjectList = listOf(
        // 총류 (00-09)
        DetailSubject(0, "총류"),
        DetailSubject(1, "도서학, 서지학"),
        DetailSubject(2, "문헌정보학"),
        DetailSubject(3, "백과사전"),
        DetailSubject(4, "일반 논문집"),
        DetailSubject(5, "일반 연속간행물"),
        DetailSubject(6, "학·협회, 기관"),
        DetailSubject(7, "신문, 언론, 저널리즘"),
        DetailSubject(8, "일반 전집, 총서"),
        DetailSubject(9, "향토자료"),

        // 철학 (10-19)
        DetailSubject(10, "철학"),
        DetailSubject(11, "형이상학"),
        DetailSubject(12, "인식론, 인과론, 인간학"),
        DetailSubject(13, "철학의 체계"),
        DetailSubject(14, "경학"),
        DetailSubject(15, "동양 철학, 사상"),
        DetailSubject(16, "서양철학"),
        DetailSubject(17, "논리학"),
        DetailSubject(18, "심리학"),
        DetailSubject(19, "윤리학, 도덕철학"),

        // 종교 (20-29)
        DetailSubject(20, "종교"),
        DetailSubject(21, "비교종교학"),
        DetailSubject(22, "불교"),
        DetailSubject(23, "기독교"),
        DetailSubject(24, "도교"),
        DetailSubject(25, "천도교"),
        DetailSubject(26, "신도"),
        DetailSubject(27, "바라문교, 인도교"),
        DetailSubject(28, "회교(이슬람교)"),
        DetailSubject(29, "기타 제종교"),

        // 사회과학 (30-39)
        DetailSubject(30, "사회과학"),
        DetailSubject(31, "통계학"),
        DetailSubject(32, "경제학"),
        DetailSubject(33, "사회학, 사회문제"),
        DetailSubject(34, "정치학"),
        DetailSubject(35, "행정학"),
        DetailSubject(36, "법학"),
        DetailSubject(37, "교육학"),
        DetailSubject(38, "풍속, 민속학"),
        DetailSubject(39, "국방, 군사학"),

        // 자연과학 (40-49)
        DetailSubject(40, "자연과학"),
        DetailSubject(41, "수학"),
        DetailSubject(42, "물리학"),
        DetailSubject(43, "화학"),
        DetailSubject(44, "천문학"),
        DetailSubject(45, "지학"),
        DetailSubject(46, "광물학"),
        DetailSubject(47, "생물과학"),
        DetailSubject(48, "식물학"),
        DetailSubject(49, "동물학"),

        // 기술과학 (50-59)
        DetailSubject(50, "기술과학"),
        DetailSubject(51, "의학"),
        DetailSubject(52, "농업, 농학"),
        DetailSubject(53, "공학, 공업일반"),
        DetailSubject(54, "건축공학"),
        DetailSubject(55, "기계공학"),
        DetailSubject(56, "전기공학, 전자공학"),
        DetailSubject(57, "화학공학"),
        DetailSubject(58, "제조업"),
        DetailSubject(59, "가정학 및 가정생활"),

        // 예술 (60-69)
        DetailSubject(60, "예술"),
        DetailSubject(61, "건축술"),
        DetailSubject(62, "조각"),
        DetailSubject(63, "공예, 장식미술"),
        DetailSubject(64, "서예"),
        DetailSubject(65, "회화, 도화"),
        DetailSubject(66, "사진술"),
        DetailSubject(67, "음악"),
        DetailSubject(68, "연극"),
        DetailSubject(69, "오락, 운동"),

        // 언어 (70-79)
        DetailSubject(70, "언어"),
        DetailSubject(71, "한국어"),
        DetailSubject(72, "중국어"),
        DetailSubject(73, "일본어"),
        DetailSubject(74, "영어"),
        DetailSubject(75, "독일어"),
        DetailSubject(76, "프랑스어"),
        DetailSubject(77, "스페인어"),
        DetailSubject(78, "이탈리아어"),
        DetailSubject(79, "기타 제어"),

        // 문학 (80-89)
        DetailSubject(80, "문학"),
        DetailSubject(81, "한국문학"),
        DetailSubject(82, "중국문학"),
        DetailSubject(83, "일본문학"),
        DetailSubject(84, "영미문학"),
        DetailSubject(85, "독일문학"),
        DetailSubject(86, "프랑스문학"),
        DetailSubject(87, "스페인문학"),
        DetailSubject(88, "이탈리아문학"),
        DetailSubject(89, "기타 제문학"),

        // 역사 (90-99)
        DetailSubject(90, "역사"),
        DetailSubject(91, "아시아(아세아)"),
        DetailSubject(92, "유럽(구라파)"),
        DetailSubject(93, "아프리카"),
        DetailSubject(94, "북아메리카(북미)"),
        DetailSubject(95, "남아메리카(남미)"),
        DetailSubject(96, "오세아니아(대양주)"),
        DetailSubject(97, "양극지방"),
        DetailSubject(98, "지리"),
        DetailSubject(99, "전기")
    )

    // 7. 지역 코드
    @Parcelize
    data class Region(
        val code: Int,
        val name: String
    ): Parcelable

    val regionList = listOf(
        Region(11, "서울"),
        Region(21, "부산"),
        Region(22, "대구"),
        Region(23, "인천"),
        Region(24, "광주"),
        Region(25, "대전"),
        Region(26, "울산"),
        Region(29, "세종"),
        Region(31, "경기"),
        Region(32, "강원"),
        Region(33, "충북"),
        Region(34, "충남"),
        Region(35, "전북"),
        Region(36, "전남"),
        Region(37, "경북"),
        Region(38, "경남"),
        Region(39, "제주")
    )

    // 세부지역 코드 전체
    @Parcelize
    data class DetailRegion(
        val code: Int,
        val regionCode: Int,
        val regionName: String,
        val districtName: String
    ): Parcelable

    // 전체 세부지역 리스트
    val allDetailRegions = listOf(
        // 서울특별시 (11)
        DetailRegion(11010, 11, "서울특별시", "종로구"),
        DetailRegion(11020, 11, "서울특별시", "중구"),
        DetailRegion(11030, 11, "서울특별시", "용산구"),
        DetailRegion(11040, 11, "서울특별시", "성동구"),
        DetailRegion(11050, 11, "서울특별시", "광진구"),
        DetailRegion(11060, 11, "서울특별시", "동대문구"),
        DetailRegion(11070, 11, "서울특별시", "중랑구"),
        DetailRegion(11080, 11, "서울특별시", "성북구"),
        DetailRegion(11090, 11, "서울특별시", "강북구"),
        DetailRegion(11100, 11, "서울특별시", "도봉구"),
        DetailRegion(11110, 11, "서울특별시", "노원구"),
        DetailRegion(11120, 11, "서울특별시", "은평구"),
        DetailRegion(11130, 11, "서울특별시", "서대문구"),
        DetailRegion(11140, 11, "서울특별시", "마포구"),
        DetailRegion(11150, 11, "서울특별시", "양천구"),
        DetailRegion(11160, 11, "서울특별시", "강서구"),
        DetailRegion(11170, 11, "서울특별시", "구로구"),
        DetailRegion(11180, 11, "서울특별시", "금천구"),
        DetailRegion(11190, 11, "서울특별시", "영등포구"),
        DetailRegion(11200, 11, "서울특별시", "동작구"),
        DetailRegion(11210, 11, "서울특별시", "관악구"),
        DetailRegion(11220, 11, "서울특별시", "서초구"),
        DetailRegion(11230, 11, "서울특별시", "강남구"),
        DetailRegion(11240, 11, "서울특별시", "송파구"),
        DetailRegion(11250, 11, "서울특별시", "강동구"),

        // 부산광역시 (21)
        DetailRegion(21010, 21, "부산광역시", "중구"),
        DetailRegion(21020, 21, "부산광역시", "서구"),
        DetailRegion(21030, 21, "부산광역시", "동구"),
        DetailRegion(21040, 21, "부산광역시", "영도구"),
        DetailRegion(21050, 21, "부산광역시", "부산진구"),
        DetailRegion(21060, 21, "부산광역시", "동래구"),
        DetailRegion(21070, 21, "부산광역시", "남구"),
        DetailRegion(21080, 21, "부산광역시", "북구"),
        DetailRegion(21090, 21, "부산광역시", "해운대구"),
        DetailRegion(21100, 21, "부산광역시", "사하구"),
        DetailRegion(21110, 21, "부산광역시", "금정구"),
        DetailRegion(21120, 21, "부산광역시", "강서구"),
        DetailRegion(21130, 21, "부산광역시", "연제구"),
        DetailRegion(21140, 21, "부산광역시", "수영구"),
        DetailRegion(21150, 21, "부산광역시", "사상구"),
        DetailRegion(21310, 21, "부산광역시", "기장군"),

        // 대구광역시 (22)
        DetailRegion(22010, 22, "대구광역시", "중구"),
        DetailRegion(22020, 22, "대구광역시", "동구"),
        DetailRegion(22030, 22, "대구광역시", "서구"),
        DetailRegion(22040, 22, "대구광역시", "남구"),
        DetailRegion(22050, 22, "대구광역시", "북구"),
        DetailRegion(22060, 22, "대구광역시", "수성구"),
        DetailRegion(22070, 22, "대구광역시", "달서구"),
        DetailRegion(22310, 22, "대구광역시", "달성군"),

        // 인천광역시 (23)
        DetailRegion(23010, 23, "인천광역시", "중구"),
        DetailRegion(23020, 23, "인천광역시", "동구"),
        DetailRegion(23030, 23, "인천광역시", "남구"),
        DetailRegion(23040, 23, "인천광역시", "연수구"),
        DetailRegion(23050, 23, "인천광역시", "남동구"),
        DetailRegion(23060, 23, "인천광역시", "부평구"),
        DetailRegion(23070, 23, "인천광역시", "계양구"),
        DetailRegion(23080, 23, "인천광역시", "서구"),
        DetailRegion(23310, 23, "인천광역시", "강화군"),
        DetailRegion(23320, 23, "인천광역시", "옹진군"),

        // 광주광역시 (24)
        DetailRegion(24010, 24, "광주광역시", "동구"),
        DetailRegion(24020, 24, "광주광역시", "서구"),
        DetailRegion(24030, 24, "광주광역시", "남구"),
        DetailRegion(24040, 24, "광주광역시", "북구"),
        DetailRegion(24050, 24, "광주광역시", "광산구"),

        // 대전광역시 (25)
        DetailRegion(25010, 25, "대전광역시", "동구"),
        DetailRegion(25020, 25, "대전광역시", "중구"),
        DetailRegion(25030, 25, "대전광역시", "서구"),
        DetailRegion(25040, 25, "대전광역시", "유성구"),
        DetailRegion(25050, 25, "대전광역시", "대덕구"),

        // 울산광역시 (26)
        DetailRegion(26010, 26, "울산광역시", "중구"),
        DetailRegion(26020, 26, "울산광역시", "남구"),
        DetailRegion(26030, 26, "울산광역시", "동구"),
        DetailRegion(26040, 26, "울산광역시", "북구"),
        DetailRegion(26310, 26, "울산광역시", "울주군"),

        // 세종특별자치시 (29)
        DetailRegion(29010, 29, "세종특별자치시", "세종시"),

        // 경기도 (31)
        DetailRegion(31010, 31, "경기도", "수원시"),
        DetailRegion(31011, 31, "경기도", "수원시 장안구"),
        DetailRegion(31012, 31, "경기도", "수원시 권선구"),
        DetailRegion(31013, 31, "경기도", "수원시 팔달구"),
        DetailRegion(31014, 31, "경기도", "수원시 영통구"),
        DetailRegion(31020, 31, "경기도", "성남시"),
        DetailRegion(31021, 31, "경기도", "성남시 수정구"),
        DetailRegion(31022, 31, "경기도", "성남시 중원구"),
        DetailRegion(31023, 31, "경기도", "성남시 분당구"),
        DetailRegion(31030, 31, "경기도", "의정부시"),
        DetailRegion(31040, 31, "경기도", "안양시"),
        DetailRegion(31041, 31, "경기도", "안양시 만안구"),
        DetailRegion(31042, 31, "경기도", "안양시 동안구"),
        DetailRegion(31050, 31, "경기도", "부천시"),
        DetailRegion(31060, 31, "경기도", "광명시"),
        DetailRegion(31070, 31, "경기도", "평택시"),
        DetailRegion(31080, 31, "경기도", "동두천시"),
        DetailRegion(31090, 31, "경기도", "안산시"),
        DetailRegion(31091, 31, "경기도", "안산시 상록구"),
        DetailRegion(31092, 31, "경기도", "안산시 단원구"),
        DetailRegion(31100, 31, "경기도", "고양시"),
        DetailRegion(31101, 31, "경기도", "고양시 덕양구"),
        DetailRegion(31103, 31, "경기도", "고양시 일산동구"),
        DetailRegion(31104, 31, "경기도", "고양시 일산서구"),
        DetailRegion(31110, 31, "경기도", "과천시"),
        DetailRegion(31120, 31, "경기도", "구리시"),
        DetailRegion(31130, 31, "경기도", "남양주시"),
        DetailRegion(31140, 31, "경기도", "오산시"),
        DetailRegion(31150, 31, "경기도", "시흥시"),
        DetailRegion(31160, 31, "경기도", "군포시"),
        DetailRegion(31170, 31, "경기도", "의왕시"),
        DetailRegion(31180, 31, "경기도", "하남시"),
        DetailRegion(31190, 31, "경기도", "용인시"),
        DetailRegion(31191, 31, "경기도", "용인시 처인구"),
        DetailRegion(31192, 31, "경기도", "용인시 기흥구"),
        DetailRegion(31193, 31, "경기도", "용인시 수지구"),
        DetailRegion(31200, 31, "경기도", "파주시"),
        DetailRegion(31210, 31, "경기도", "이천시"),
        DetailRegion(31220, 31, "경기도", "안성시"),
        DetailRegion(31230, 31, "경기도", "김포시"),
        DetailRegion(31240, 31, "경기도", "화성시"),
        DetailRegion(31250, 31, "경기도", "광주시"),
        DetailRegion(31260, 31, "경기도", "양주시"),
        DetailRegion(31270, 31, "경기도", "포천시"),
        DetailRegion(31280, 31, "경기도", "여주시"),
        DetailRegion(31350, 31, "경기도", "연천군"),
        DetailRegion(31370, 31, "경기도", "가평군"),
        DetailRegion(31380, 31, "경기도", "양평군"),

        // 강원특별자치도 (32)
        DetailRegion(32010, 32, "강원특별자치도", "춘천시"),
        DetailRegion(32020, 32, "강원특별자치도", "원주시"),
        DetailRegion(32030, 32, "강원특별자치도", "강릉시"),
        DetailRegion(32040, 32, "강원특별자치도", "동해시"),
        DetailRegion(32050, 32, "강원특별자치도", "태백시"),
        DetailRegion(32060, 32, "강원특별자치도", "속초시"),
        DetailRegion(32070, 32, "강원특별자치도", "삼척시"),
        DetailRegion(32310, 32, "강원특별자치도", "홍천군"),
        DetailRegion(32320, 32, "강원특별자치도", "횡성군"),
        DetailRegion(32330, 32, "강원특별자치도", "영월군"),
        DetailRegion(32340, 32, "강원특별자치도", "평창군"),
        DetailRegion(32350, 32, "강원특별자치도", "정선군"),
        DetailRegion(32360, 32, "강원특별자치도", "철원군"),
        DetailRegion(32370, 32, "강원특별자치도", "화천군"),
        DetailRegion(32380, 32, "강원특별자치도", "양구군"),
        DetailRegion(32390, 32, "강원특별자치도", "인제군"),
        DetailRegion(32400, 32, "강원특별자치도", "고성군"),
        DetailRegion(32410, 32, "강원특별자치도", "양양군"),

        // 충청북도 (33)
        DetailRegion(33020, 33, "충청북도", "충주시"),
        DetailRegion(33030, 33, "충청북도", "제천시"),
        DetailRegion(33040, 33, "충청북도", "청주시"),
        DetailRegion(33041, 33, "충청북도", "청주시 상당구"),
        DetailRegion(33042, 33, "충청북도", "청주시 서원구"),
        DetailRegion(33043, 33, "충청북도", "청주시 흥덕구"),
        DetailRegion(33044, 33, "충청북도", "청주시 청원구"),
        DetailRegion(33320, 33, "충청북도", "보은군"),
        DetailRegion(33330, 33, "충청북도", "옥천군"),
        DetailRegion(33340, 33, "충청북도", "영동군"),
        DetailRegion(33350, 33, "충청북도", "진천군"),
        DetailRegion(33360, 33, "충청북도", "괴산군"),
        DetailRegion(33370, 33, "충청북도", "음성군"),
        DetailRegion(33380, 33, "충청북도", "단양군"),
        DetailRegion(33390, 33, "충청북도", "증평군"),

        // 충청남도 (34)
        DetailRegion(34010, 34, "충청남도", "천안시"),
        DetailRegion(34011, 34, "충청남도", "천안시 동남구"),
        DetailRegion(34012, 34, "충청남도", "천안시 서북구"),
        DetailRegion(34020, 34, "충청남도", "공주시"),
        DetailRegion(34030, 34, "충청남도", "보령시"),
        DetailRegion(34040, 34, "충청남도", "아산시"),
        DetailRegion(34050, 34, "충청남도", "서산시"),
        DetailRegion(34060, 34, "충청남도", "논산시"),
        DetailRegion(34070, 34, "충청남도", "계룡시"),
        DetailRegion(34080, 34, "충청남도", "당진시"),
        DetailRegion(34310, 34, "충청남도", "금산군"),
        DetailRegion(34330, 34, "충청남도", "부여군"),
        DetailRegion(34340, 34, "충청남도", "서천군"),
        DetailRegion(34350, 34, "충청남도", "청양군"),
        DetailRegion(34360, 34, "충청남도", "홍성군"),
        DetailRegion(34370, 34, "충청남도", "예산군"),
        DetailRegion(34380, 34, "충청남도", "태안군"),

        // 전북특별자치도 (35)
        DetailRegion(35010, 35, "전북특별자치도", "전주시"),
        DetailRegion(35011, 35, "전북특별자치도", "전주시 완산구"),
        DetailRegion(35012, 35, "전북특별자치도", "전주시 덕진구"),
        DetailRegion(35020, 35, "전북특별자치도", "군산시"),
        DetailRegion(35030, 35, "전북특별자치도", "익산시"),
        DetailRegion(35040, 35, "전북특별자치도", "정읍시"),
        DetailRegion(35050, 35, "전북특별자치도", "남원시"),
        DetailRegion(35060, 35, "전북특별자치도", "김제시"),
        DetailRegion(35310, 35, "전북특별자치도", "완주군"),
        DetailRegion(35320, 35, "전북특별자치도", "진안군"),
        DetailRegion(35330, 35, "전북특별자치도", "무주군"),
        DetailRegion(35340, 35, "전북특별자치도", "장수군"),
        DetailRegion(35350, 35, "전북특별자치도", "임실군"),
        DetailRegion(35360, 35, "전북특별자치도", "순창군"),
        DetailRegion(35370, 35, "전북특별자치도", "고창군"),
        DetailRegion(35380, 35, "전북특별자치도", "부안군"),

        // 전라남도 (36)
        DetailRegion(36010, 36, "전라남도", "목포시"),
        DetailRegion(36020, 36, "전라남도", "여수시"),
        DetailRegion(36030, 36, "전라남도", "순천시"),
        DetailRegion(36040, 36, "전라남도", "나주시"),
        DetailRegion(36060, 36, "전라남도", "광양시"),
        DetailRegion(36310, 36, "전라남도", "담양군"),
        DetailRegion(36320, 36, "전라남도", "곡성군"),
        DetailRegion(36330, 36, "전라남도", "구례군"),
        DetailRegion(36350, 36, "전라남도", "고흥군"),
        DetailRegion(36360, 36, "전라남도", "보성군"),
        DetailRegion(36370, 36, "전라남도", "화순군"),
        DetailRegion(36380, 36, "전라남도", "장흥군"),
        DetailRegion(36390, 36, "전라남도", "강진군"),
        DetailRegion(36400, 36, "전라남도", "해남군"),
        DetailRegion(36410, 36, "전라남도", "영암군"),
        DetailRegion(36420, 36, "전라남도", "무안군"),
        DetailRegion(36430, 36, "전라남도", "함평군"),
        DetailRegion(36440, 36, "전라남도", "영광군"),
        DetailRegion(36450, 36, "전라남도", "장성군"),
        DetailRegion(36460, 36, "전라남도", "완도군"),
        DetailRegion(36470, 36, "전라남도", "진도군"),
        DetailRegion(36480, 36, "전라남도", "신안군"),

        // 경상북도 (37)
        DetailRegion(37010, 37, "경상북도", "포항시"),
        DetailRegion(37011, 37, "경상북도", "포항시 남구"),
        DetailRegion(37012, 37, "경상북도", "포항시 북구"),
        DetailRegion(37020, 37, "경상북도", "경주시"),
        DetailRegion(37030, 37, "경상북도", "김천시"),
        DetailRegion(37040, 37, "경상북도", "안동시"),
        DetailRegion(37050, 37, "경상북도", "구미시"),
        DetailRegion(37060, 37, "경상북도", "영주시"),
        DetailRegion(37070, 37, "경상북도", "영천시"),
        DetailRegion(37080, 37, "경상북도", "상주시"),
        DetailRegion(37090, 37, "경상북도", "문경시"),
        DetailRegion(37100, 37, "경상북도", "경산시"),
        DetailRegion(37310, 37, "경상북도", "군위군"),
        DetailRegion(37320, 37, "경상북도", "의성군"),
        DetailRegion(37330, 37, "경상북도", "청송군"),
        DetailRegion(37340, 37, "경상북도", "영양군"),
        DetailRegion(37350, 37, "경상북도", "영덕군"),
        DetailRegion(37360, 37, "경상북도", "청도군"),
        DetailRegion(37370, 37, "경상북도", "고령군"),
        DetailRegion(37380, 37, "경상북도", "성주군"),
        DetailRegion(37390, 37, "경상북도", "칠곡군"),
        DetailRegion(37400, 37, "경상북도", "예천군"),
        DetailRegion(37410, 37, "경상북도", "봉화군"),
        DetailRegion(37420, 37, "경상북도", "울진군"),
        DetailRegion(37430, 37, "경상북도", "울릉군"),

        // 경상남도 (38)
        DetailRegion(38030, 38, "경상남도", "진주시"),
        DetailRegion(38050, 38, "경상남도", "통영시"),
        DetailRegion(38060, 38, "경상남도", "사천시"),
        DetailRegion(38070, 38, "경상남도", "김해시"),
        DetailRegion(38080, 38, "경상남도", "밀양시"),
        DetailRegion(38090, 38, "경상남도", "거제시"),
        DetailRegion(38100, 38, "경상남도", "양산시"),
        DetailRegion(38110, 38, "경상남도", "창원시"),
        DetailRegion(38111, 38, "경상남도", "창원시 의창구"),
        DetailRegion(38112, 38, "경상남도", "창원시 성산구"),
        DetailRegion(38113, 38, "경상남도", "창원시 마산합포구"),
        DetailRegion(38114, 38, "경상남도", "창원시 마산회원구"),
        DetailRegion(38115, 38, "경상남도", "창원시 진해구"),
        DetailRegion(38310, 38, "경상남도", "의령군"),
        DetailRegion(38320, 38, "경상남도", "함안군"),
        DetailRegion(38330, 38, "경상남도", "창녕군"),
        DetailRegion(38340, 38, "경상남도", "고성군"),
        DetailRegion(38350, 38, "경상남도", "남해군"),
        DetailRegion(38360, 38, "경상남도", "하동군"),
        DetailRegion(38370, 38, "경상남도", "산청군"),
        DetailRegion(38380, 38, "경상남도", "함양군"),
        DetailRegion(38390, 38, "경상남도", "거창군"),
        DetailRegion(38400, 38, "경상남도", "합천군"),

        // 제주특별자치도 (39)
        DetailRegion(39010, 39, "제주특별자치도", "제주시"),
        DetailRegion(39020, 39, "제주특별자치도", "서귀포시")
    )

    // 지역 코드로 세부지역 필터링하는 확장 함수
    fun List<DetailRegion>.filterByRegion(regionCode: Int): List<DetailRegion> {
        return this.filter { it.regionCode == regionCode }
    }

    // 사용 예시
    // val seoulDistricts = allDetailRegions.filterByRegion(11)
    // val busanDistricts = allDetailRegions.filterByRegion(21)
}