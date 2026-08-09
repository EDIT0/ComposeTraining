# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 절대 규칙

- **`domain` 모듈에 `data`를 의존성으로 추가하지 않는다.** `domain`은 `Repository`/`DataStoreRepository` 인터페이스와 유스케이스만 정의하며 Retrofit·Room 등 구현 세부사항을 알아서는 안 된다. (`domain/build.gradle.kts`에는 애초에 `:data`가 없음 — 실제 구현은 `data` 모듈의 `RepositoryImpl`이 담당)
- **`feature:*` 모듈끼리 서로 의존하거나 직접 import하지 않는다.** 화면 간 이동은 항상 `core:navigation`의 `AppNavHost`를 통해서만 이루어진다. 다른 feature 모듈로 넘어가는 로직을 feature 모듈 내부에 직접 넣지 않는다.
- **API 키·시크릿을 코드나 리소스 파일에 하드코딩하거나 커밋하지 않는다.** `BOOK_LIBRARY_API_KEY`, `NCP_KEY_ID`, `sdk.dir`은 반드시 `local.properties`(gitignore 처리됨)에서만 읽는다. 새 외부 API 키가 필요하면 동일한 방식(`data/build.gradle.kts`, `app/build.gradle.kts` 참고)을 따른다.
- **UI에 노출되는 문자열을 Composable/Kotlin 코드에 하드코딩하지 않는다.** `core:resource`의 `values` / `values-ko` / `values-en` `strings.xml`에 정의하고 `R.string.*`로 참조한다 (한국어/영어 동시 지원 유지).
- **`RepositoryImpl`의 응답 처리 분기(`RequestResult.Success` / `DataEmpty` / `Error`)를 생략하지 않는다.** 새 API를 추가할 때도 `response.isSuccessful` 확인 → 바디/리스트 비어있음 체크 → 세 가지 상태로 emit하는 기존 패턴을 그대로 따른다. 임의로 예외를 던지거나 null을 그대로 흘려보내지 않는다.
- **Room 캐시(`localDataSource`)가 있는 API(예: 도서 상세)는 캐시 우선 조회 로직을 건드릴 때 캐시 무효화 정책 없이 무제한 캐싱을 늘리지 않는다.** 기존 `getBookDetail`의 "캐시 있으면 반환, 없으면 네트워크 후 저장" 구조를 벗어나는 변경은 별도로 사용자에게 확인한다.
- **`local.properties`, `*.keystore`, 서명 관련 파일을 git에 추가하지 않는다.**

## 아키텍처

### 기술 스택
- Kotlin, Jetpack Compose (Material3), Compose Navigation
- Hilt (DI), Retrofit + Gson (네트워크), OkHttp `HeaderInterceptor`
- Room (로컬 캐시), DataStore/EncryptedSharedPreferences(`PrefUtil`, AndroidKeyStore 기반 AES-GCM 암복호화)
- Paging3 (목록 API 페이징)
- Naver Map Compose SDK (도서관 위치 지도)
- 멀티모듈 Gradle (`build.gradle.kts` + 버전 카탈로그 `gradle/libs.versions.toml`)

### 모듈 구조 및 의존성 방향
바깥쪽(app)이 안쪽(domain)에 의존하며, 그 반대 방향 의존은 존재하지 않는다.

```
app
 ├─ core:di        (Hilt 모듈: Network/Repository/LocalData/RemoteData/Util)
 ├─ core:navigation(AppNavHost — 모든 feature 모듈을 참조하는 유일한 모듈)
 ├─ data           (Repository 구현체, Retrofit ApiService, Room, Paging Source)
 ├─ domain         (Repository 인터페이스, UseCase — data 미의존)
 └─ feature:*      (splash, main, search, search_library, select_library, select_region)

core:common   — 화면 공용 Compose 컴포넌트, 유틸(Location/Network/Permission/Pref 등), CommonViewModel
core:model    — API 요청/응답 DTO(req/res), 로컬 모델(local), RequestResult
core:resource — 문자열/색상/폰트 등 리소스, 지역·연령·성별 등 정적 코드 테이블(LibraryData)
```

- `feature:*`는 `core:common` / `core:model` / `core:resource` / `domain`에만 의존한다. 다른 feature나 `core:di`, `core:navigation`에는 의존하지 않는다.
- `core:navigation`만 예외적으로 모든 `feature:*` 모듈을 참조해 하나의 `AppNavHost`로 묶는다.
- `core:di`는 `data` + `domain`을 묶어 Hilt 모듈로 노출하고, `app`이 이를 최종적으로 사용한다.

### Feature 모듈 내부 구조 (화면 1개 = 아래 4개 패키지)
```
<feature>/intent/      *ViewModelEvent(UI→VM 인텐트), *UiEvent(VM 내부 상태 이벤트) sealed 타입
<feature>/state/       *UiState data class
<feature>/ui/          *Screen Composable
<feature>/viewmodel/   *ViewModel (@HiltViewModel, AndroidViewModel 상속)
```
`select_library`, `select_region`처럼 하나의 feature 모듈에 여러 하위 화면이 있는 경우 위 4개 패키지 세트가 하위 화면(`region/`, `library/`, `library_detail/` 등)마다 반복된다.

## 빌드 / 테스트

`local.properties`에 아래 값이 있어야 빌드가 된다 (미설정 시 `BOOK_LIBRARY_API_KEY`는 `null` 참조로 빌드 실패):
```
sdk.dir=<Android SDK 경로>
BOOK_LIBRARY_API_KEY=<data4library.kr 발급 키>
NCP_KEY_ID=<Naver Cloud Platform Maps 키>
```

```bash
./gradlew assembleDebug              # 디버그 APK 빌드
./gradlew test                       # 전체 모듈 단위 테스트
./gradlew :feature:search:test       # 특정 모듈만 단위 테스트
./gradlew connectedAndroidTest       # 계측 테스트 (기기/에뮬레이터 필요)
./gradlew lint                       # 안드로이드 린트
```

> 현재 거의 모든 모듈에 Android Studio 기본 템플릿(`ExampleUnitTest`, `ExampleInstrumentedTest`)만 존재하고 실제 테스트는 작성되어 있지 않다. 회귀 검증용으로 신뢰할 수 있는 테스트 스위트가 아직 없으므로, 로직을 변경했다면 가능한 경우 직접 빌드/실행해서 확인한다.

## 도메인 컨텍스트

이 앱은 **책 제목으로 검색 → 그 책을 보유한 공공도서관 조회 → 지도에서 위치 확인** 흐름을 중심으로 한다. 데이터 출처는 정보공개 표준데이터 API인 **data4library.kr**이며, 요청마다 `authKey`(=`BOOK_LIBRARY_API_KEY`)와 `format=json`을 함께 보낸다 (`Constant.BASE_URL`, `ApiService`).

핵심 도메인 용어:
- **지역(Region) / 세부지역(DetailRegion)**: 전국을 광역시도(`Region`, 예: 서울=11) → 시군구(`DetailRegion`, 예: 서울 종로구=11010) 2단계로 나눈 코드 체계. `core:resource`의 `LibraryData`에 전체 코드 테이블이 정적으로 정의되어 있다.
- **도서관(Library, `libCode`)**: data4library가 부여하는 도서관 코드. 지역/세부지역/도서관코드로 도서관을 조회하는 API가 각각 존재한다(`getSearchRegionBookLibrary`, `getSearchDetailRegionBookLibrary`, `getSearchLibCodeBookLibrary`).
- **내 도서관(MyRegionAndLibrary)**: 사용자가 선택한 `DetailRegion` + 특정 도서관을 로컬(`DataStoreRepository`, 실제로는 암호화된 SharedPreferences `PrefUtil`)에 저장해두고 앱 재실행 시 재사용한다. `select_region` 플로우(guide → selection → complete)에서 최초 설정한다.
- **장서/소장 여부**: 특정 책(`isbn13`)을 특정 도서관이 보유하는지, 대출 가능한지(`GetCheckBookAvailabilityUseCase`) 조회하는 개념. 청구기호(도서관 내 책 위치 코드)도 함께 내려온다.
- **도서 상세 정보(BookDetail)**: 최초 조회 후 Room(`BookDetailDao`/`BookDetailEntity`)에 `isbn13` 기준으로 캐시되어, 재조회 시 네트워크 호출 없이 즉시 반환된다.
- **성별/연령/부가기호/대주제/세부주제 코드**: `LibraryData`에 정의된 통계·분류용 코드 테이블. 도서관별 대출 데이터 조회(`GetLibraryBookDataUseCase`, KDC 분류 기반 통계 등) 관련 화면에서 사용된다.
- **지도(LibraryMap)**: 검색된 책을 보유한 도서관들을 Naver Map 위에 마커로 표시하고, 현재 위치를 함께 보여준다(`LocationUtil`, 위치 권한 필요).

## 코딩 컨벤션

### 네이밍
- API 요청/응답 DTO는 `Req*`/`Res*` 접두사 (`core:model/req`, `core:model/res`).
- ViewModel의 UI 인텐트는 `*ViewModelEvent`(UI → VM), 내부 상태 이벤트는 `*UiEvent`(VM 내부), 상태는 `*UiState`로 이름 짓는다.
- 패키지/파일은 `snake_case` 모듈명(`search_library`, `select_library`) + `PascalCase` 클래스명을 혼용한다. 기존 모듈명 패턴을 따른다.

### ViewModel 상태 관리 패턴
대부분의 ViewModel(예: `SearchViewModel`, `SelectLibraryDetailRegionViewModel`, `HomeViewModel` 등)은 상태를 직접 `MutableStateFlow.value = ...`로 갱신하지 않고, 다음 패턴을 따른다:
1. UI가 `intentAction(*ViewModelEvent)` 호출
2. ViewModel이 이를 처리하며 `Channel<*UiEvent>`에 이벤트 send
3. 그 Channel의 flow를 `runningFold(initial = *UiState())`로 접어 `StateFlow<*UiState>`를 만든다 (`stateIn(viewModelScope, SharingStarted.Eagerly, ...)`)

새 화면을 만들 때도 상태가 여러 필드로 구성되고 이벤트가 여러 종류라면 이 패턴을 우선 따른다. 단순한 화면(예: `SplashViewModel`)은 예외적으로 단순 `MutableStateFlow`를 써도 무방하다.

토스트 등 1회성 부수효과는 상태(State)와 분리해 `sealed interface SideEffectEvent`를 별도 `Channel`/`Flow`로 노출한다 (`searchUiState`와 `sideEffectEvent`가 분리되어 있는 `SearchViewModel` 참고).

### 화면 간 데이터 전달 (Navigation)
- 라우트 이름은 `core/navigation/Screen.kt`, 인자 키는 `core/navigation/Data.kt`에 모아 정의한다.
- 객체를 화면 간에 넘길 때는 타입 세이프 nav args를 쓰지 않고, `Gson().toJson(obj)` → `URLEncoder.encode(...)`로 라우트 문자열에 붙이고, 수신 측에서 `URLDecoder.decode(...)` → `Gson().fromJson(...)`으로 복원한다. 복원한 값이 `null`이면 `onBackPressed`로 방어한다. 새 화면 추가 시 `AppNavHost.kt`의 기존 케이스를 그대로 본떠 작성한다.

### 커밋 메시지
Git 로그 컨벤션: `<범위 prefix> <type>: <한글 요약>` 형태를 따른다.
```
book-library feat: 도서관 지도 기능 추가
  - 위치 권한 퍼미션 추가
  - 지도에 내 위치 표시 및 이동 가능한 버튼 추가
book-library refactor: PR #12 코드 리뷰 반영 (LocationUtil, LibraryMapViewModel)
fix: 지역 저장 오류 수정
```
- type은 `feat` / `fix` / `refactor` 를 사용한다.
- 제목은 한글로 간결하게, 세부 변경사항은 `-` 불릿으로 본문에 나열한다.
- 범위 prefix(`book-library`)는 관례적으로 붙이지만 생략된 커밋도 있다 — 새 커밋에서는 붙이는 쪽을 기본으로 한다.
