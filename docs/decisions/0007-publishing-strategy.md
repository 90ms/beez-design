# ADR-0007: BEEZ publishing strategy

- 상태: Accepted
- 결정일: 2026-07-31

## 문맥

BEEZ는 여러 Compose Multiplatform 프로젝트에서 dependency로 소비되는 라이브러리다. 개발 중인 변경을 빠르게 검증하는 경로, 팀 내부와 pre-release를 공유하는 경로, 공개 Stable 버전을 배포하는 경로가 서로 다른 요구사항을 가진다.

## 결정

배포 채널을 다음처럼 분리한다.

```text
개발 반복       → Maven Local
팀/pre-release  → GitHub Packages
공개 Stable     → Maven Central
```

### Maven Local — 개발 반복

- 로컬 소비자 검증의 기본 채널로 사용한다.
- publish 명령은 개발자가 필요할 때 실행하며, 기본 버전은 0.1.0-SNAPSHOT 같은 snapshot이다.
- 로컬 Maven repository에만 저장되므로 팀이나 외부 소비자에게 안정적인 배포 경로로 간주하지 않는다.
- repository-local staging publication과 독립 consumer fixture로 실제 Maven metadata 및 전이 dependency를 외부 업로드 전에 검증한다.

### GitHub Packages — 팀 및 pre-release

- repository URL은 https://maven.pkg.github.com/90ms/beez-design 으로 한다.
- alpha, beta, rc와 같은 pre-release를 제한된 소비자에게 공유하는 채널로 사용한다.
- GitHub Actions secret 또는 개발자의 보안 credential store에서만 인증 정보를 읽는다.
- token, password, signing key를 repository 파일, Gradle 로그, 예제 코드에 저장하지 않는다.
- 공개 repository라는 이유로 credential을 생략할 수 있다고 가정하지 않는다. 소비자 인증 방식을 문서화하고 필요한 권한만 부여한다.

### Maven Central — 공개 Stable

- 공개 Stable 배포의 목표 채널로 사용한다.
- namespace를 먼저 검증하고, POM metadata, license, source/javadoc artifact, signing, publication validation을 갖춘 뒤 사용한다.
- beez.design은 현재 선택한 provisional namespace다. 실제 등록 가능 여부와 license는 첫 공개 release 전에 확인한다.
- 공개 release는 GitHub Release 또는 승인된 tag를 기준으로 한 CI workflow에서만 수행한다.

## 좌표와 모듈

초기 모듈은 하나의 버전으로 함께 release한다.

| 항목 | 값 |
| --- | --- |
| Maven group | beez.design |
| version 예시 | 0.1.0-SNAPSHOT |
| component artifact | beez-components |
| foundation artifact | beez-foundation |
| token artifact | beez-tokens |

일반 소비자는 beez-components를 추가한다. beez-foundation과 beez-tokens는 모듈 dependency를 통해 전이적으로 포함한다. 하위 artifact를 직접 추가하는 것은 token/foundation API를 직접 사용하는 경우로 제한한다.

## 버전 정책

- 초기에는 모든 공개 모듈의 버전을 정렬한다. 모듈별 독립 버전은 운영 부담과 조합 수를 늘리므로 도입하지 않는다.
- 개발 버전은 -SNAPSHOT, pre-release는 -alpha.1, -beta.1, -rc.1, Stable은 1.0.0 형태를 사용한다.
- 같은 version 좌표를 다시 publish하거나 기존 artifact를 덮어쓰지 않는다.
- public API, semantic token, binary/source compatibility를 깨는 변경은 migration note와 함께 major 또는 pre-1.0 안정성 정책에 맞춰 version을 올린다.
- release version, compatibility baseline, changelog, migration guidance는 같은 변경 단위로 review한다.

## Release trigger와 CI

- Pull request: publish하지 않고 문서/API/publication 설정을 검토한다.
- main push: 기본적으로 publish하지 않는다. 필요하다면 별도 opt-in pre-release workflow로 제한한다.
- 승인된 v* tag 또는 GitHub Release: release workflow를 시작한다.
- 하나의 CI job이 모든 KMP target publication을 생성하고 signing/publish한다. 서로 다른 runner가 같은 좌표를 중복 publish하지 않는다.
- release 전에는 consumer sample이 해당 repository에서 artifact를 resolve하는지 확인한다.

## 소비자 예시

### Kotlin Multiplatform

```kotlin
repositories {
    mavenCentral()
    // pre-release를 사용할 때만 GitHub Packages를 추가한다.
    // maven { url = uri("https://maven.pkg.github.com/90ms/beez-design") }
}

commonMain.dependencies {
    implementation("beez.design:beez-components:1.0.0")
}
```

### Android

```kotlin
dependencies {
    implementation("beez.design:beez-components:1.0.0")
}
```

소비자 프로젝트의 Compose Multiplatform/Kotlin toolchain은 호환성 문서의 baseline과 조합을 확인한다. BEEZ core는 Material 3 dependency를 요구하지 않는다.

## 보안과 권한

- GitHub Packages 및 Maven Central credential은 CI secret 또는 OS credential store에 둔다.
- workflow에는 필요한 package/repository 권한만 부여한다.
- signing key와 password는 secret manager에서 주입하고 로그에 출력하지 않는다.
- tag 보호, release reviewer, dependency graph, 최종 artifact 좌표를 release 전에 확인한다.
- credential이 노출되면 즉시 revoke/rotate하고 영향을 받은 release를 기록한다.

## Release checklist

### 명세와 API

- [ ] 해당 변경의 ADR, token/component 명세, API diff가 최신이다.
- [ ] compatibility baseline과 지원 플랫폼 표가 최신이다.
- [ ] breaking change라면 migration guidance가 있다.

### 문서와 artifact

- [ ] README와 docs/publishing.md의 좌표·버전·채널 설명이 일치한다.
- [ ] changelog/release note가 추가되었다.
- [ ] POM metadata, license, source/docs artifact, signing 설정을 확인했다.
- [ ] consumer sample에서 실제 dependency resolve를 확인했다.

### Git과 release

- [ ] release version이 아직 publish되지 않은 좌표다.
- [ ] 승인된 tag/release만 publish workflow를 시작한다.
- [ ] CI job이 모든 target publication을 한 번만 publish한다.
- [ ] release 결과와 rollback/후속 조치를 기록했다.

## Rollback

이미 공개된 artifact를 삭제하거나 같은 version으로 덮어쓰지 않는다. 문제가 있으면 새 patch 또는 pre-release version을 발행하고, 영향을 받은 version과 migration/upgrade 경로를 release note에 기록한다.

## 참고 자료

- [Kotlin Multiplatform library publishing](https://kotlinlang.org/docs/multiplatform-publish-lib.html)
- [Publish Kotlin Multiplatform libraries to Maven Central](https://kotlinlang.org/docs/multiplatform/multiplatform-publish-libraries-to-maven.html)
- [Gradle Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)
- [GitHub Packages Gradle registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry)
