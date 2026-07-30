# ADR-0005: Initial compatibility baseline

- 상태: Accepted
- 결정일: 2026-07-30

## 배경

BEEZ의 Gradle 프로젝트와 CI를 구성하려면 초기 toolchain과 최소 지원 플랫폼을 정해야 한다.

가능한 오래된 환경을 모두 지원하면 소비 범위는 넓어지지만 테스트 조합과 플랫폼별 예외가 크게 늘어난다. 반대로 최신 환경만 지원하면 새로운 프로젝트에서도 불필요한 업그레이드를 강제할 수 있다.

BEEZ는 새 Compose Multiplatform 프로젝트에서 재사용하는 것을 주된 용도로 하며, 각 플랫폼에서 실제로 검증할 수 있는 범위를 명시하는 것을 우선한다.

## 결정

초기 최소 지원 환경은 다음과 같다.

| 플랫폼 | 최소 환경 | 지원 등급 |
| --- | --- | --- |
| Android | API 24 | Tier 1 |
| iOS | iOS 14 | Tier 2 |
| Desktop JVM | JDK 17 | Tier 2 |
| Web | WasmGC 지원 브라우저 | Experimental |

초기 빌드 기준선은 다음과 같다.

| 도구 | 기준 버전 |
| --- | --- |
| Kotlin | 2.4.10 |
| Compose Compiler plugin | 2.4.10 |
| Compose Multiplatform | 1.11.1 |
| Android Gradle Plugin | 9.1.0 |
| Gradle | 9.3.1 |
| Gradle runtime JDK | 17 이상 |

Compose Compiler plugin은 Kotlin과 같은 버전을 사용한다.

Android KMP library에는 `com.android.kotlin.multiplatform.library` plugin을 사용한다. 기존 `com.android.library`와 KMP plugin을 결합하는 legacy 구조로 새 프로젝트를 만들지 않는다.

## 플랫폼 기준의 의미

### Android API 24

BEEZ는 Android 7.0 이상을 초기 검증 범위로 삼는다. Compose Multiplatform upstream의 더 낮은 최소 지원 가능성과 BEEZ의 실제 지원 범위를 구분한다.

### iOS 14

iOS 14를 공통 UI와 주요 interaction의 초기 최소 검증 범위로 삼는다. Xcode와 Kotlin/Native toolchain의 호환 범위는 `compatibility.md`에서 별도로 관리한다.

### Desktop JDK 17

Gradle 실행, 개발 및 Desktop target의 기준 JDK로 17을 사용한다. 운영체제별 최소 버전과 packaging 요구사항은 catalog 또는 배포 애플리케이션을 구성할 때 추가한다.

### Web WasmGC

Web은 Wasm target을 기준으로 탐색하고 WasmGC를 지원하는 브라우저를 요구한다. 초기에는 Experimental 등급이므로 브라우저별 완전한 동작 호환성을 보장하지 않는다.

## Toolchain 선택 근거

- Kotlin 2.4.10은 결정 시점의 안정 버전이다.
- Compose Multiplatform 1.11.1은 Kotlin 2.4.10과 함께 사용할 수 있는 안정 버전이다.
- Compose Compiler plugin은 Kotlin plugin과 버전을 맞춘다.
- Kotlin 2.4.10의 공식 호환 범위 안에서 Android Gradle Plugin 9.1.0을 선택한다.
- Android Gradle Plugin 9.1의 최소 요구사항을 충족하도록 Gradle 9.3.1을 선택한다.
- 새 Android KMP library plugin을 사용해 제거 예정인 legacy variant API에 의존하지 않는다.

## 버전 정책

- 빌드 파일에서는 dynamic version을 사용하지 않는다.
- Version catalog에서 toolchain과 library 버전을 한곳에 관리한다.
- dependency lock 또는 verification 정책은 dependency 구성이 안정된 뒤 도입한다.
- 버전 업그레이드는 별도 커밋으로 수행한다.
- Kotlin, Compose, AGP 또는 Gradle을 단독으로 올리기 전에 공식 호환 범위를 확인한다.
- 업그레이드 후 모든 target configuration과 최소 하나 이상의 공통 compile/test 경로를 검증한다.

## 결과

### 장점

- 프로젝트 생성 시 검증할 구체적인 기준선이 생긴다.
- 새 Android KMP plugin을 사용해 향후 AGP migration 비용을 줄인다.
- 최소 플랫폼과 build toolchain 변경을 별도로 판단할 수 있다.
- 최신 버전을 무조건 추종하지 않고 공식 호환 조합을 유지할 수 있다.

### 제약

- Android API 23 이하 제품은 초기 BEEZ를 사용할 수 없다.
- JDK 17 미만 개발 환경은 지원하지 않는다.
- Web 지원은 초기 Stable 품질 기준에 포함되지 않는다.
- iOS와 Desktop의 실제 기기 및 운영체제 matrix는 후속 CI 결정이 필요하다.

## 참고 자료

- Kotlin Multiplatform compatibility guide: <https://kotlinlang.org/docs/multiplatform/multiplatform-compatibility-guide.html>
- Compose Multiplatform compatibility and versioning: <https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html>
- Compose Compiler plugin: <https://kotlinlang.org/docs/multiplatform/compose-compiler.html>
- Android KMP library plugin: <https://developer.android.com/kotlin/multiplatform/plugin>
- Android Gradle Plugin and Gradle compatibility: <https://developer.android.com/build/releases/about-agp>
- Gradle Java compatibility: <https://docs.gradle.org/current/userguide/compatibility.html>
