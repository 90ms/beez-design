# BEEZ Publishing Guide

- 상태: Draft
- 기준일: 2026-08-03

이 문서는 BEEZ를 로컬 개발, 팀 공유, 공개 Stable release로 배포하고 다른 프로젝트에서 dependency로 소비하는 방법을 정의한다. 규범적인 결정은 ADR-0007을 따른다.

## 배포 채널

| 채널 | 목적 | 예시 version | 저장소 |
| --- | --- | --- | --- |
| Maven Local | 개발 반복과 consumer sample 검증 | 0.1.0-SNAPSHOT | 개발자 로컬 |
| GitHub Packages | 팀 공유와 pre-release | 0.1.0-alpha.1 | maven.pkg.github.com/90ms/beez-design |
| Maven Central | 공개 Stable release | 1.0.0 | Maven Central |

세 공개 모듈에는 KMP Maven publication과 repository-local staging 경로가 구성되어 있다. CI는 staging artifact를 만든 뒤 독립 consumer fixture가 Android 및 Desktop variant와 전이 dependency를 resolve하는지 검증한다. GitHub Packages 및 Maven Central로의 외부 publish는 아직 구성하지 않았다.

## 좌표

| 모듈 | Maven coordinate | 일반 사용 |
| --- | --- | --- |
| Tokens | beez.design:beez-tokens:{version} | 직접 token API가 필요할 때 |
| Foundation | beez.design:beez-foundation:{version} | 직접 foundation API가 필요할 때 |
| Components | beez.design:beez-components:{version} | 대부분의 소비자 |

초기에는 세 모듈의 version을 함께 올린다. Components가 Foundation과 Tokens를 전이 dependency로 포함하므로 일반 소비자는 Components만 추가한다.

## Maven Local

로컬 반복 검증에는 다음 명령을 사용한다.

```text
gradle --no-daemon publishToMavenLocal
```

소비자 프로젝트에서 로컬 repository를 먼저 조회한다.

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}

commonMain.dependencies {
    implementation("beez.design:beez-components:0.1.0-SNAPSHOT")
}
```

Maven Local artifact는 해당 개발자의 machine에서만 보이는 임시 결과다. 팀 공유나 release note 없이 외부 사용을 안내하지 않는다.

## Repository-local staging 검증

CI와 release 준비에서는 사용자 Maven cache와 분리된 `build/staging-repository`에 세 모듈의 모든 publication을 함께 생성한다.

```text
gradle --no-daemon clean publishLibrariesToStagingRepository
gradle --no-daemon -p samples/published-consumer build
```

release candidate version은 두 build에 같은 property로 전달한다.

```text
gradle --no-daemon clean publishLibrariesToStagingRepository -PbeezVersion=0.1.0-alpha.1
gradle --no-daemon -p samples/published-consumer build -PbeezVersion=0.1.0-alpha.1
```

독립 fixture는 Gradle project dependency를 사용하지 않는다. staging repository의 `beez-components`를 resolve하고, 공개 metadata를 따라 `beez-foundation`과 `beez-tokens`를 전이적으로 가져온다. BEEZ artifact는 이 검증에서 Material dependency 없이 Android와 Desktop target을 compile해야 한다.

`Validate BEEZ Libraries` workflow를 수동 실행하면 `beez_version` 입력으로 release candidate 좌표를 지정할 수 있다. workflow는 ADR-0007의 version 형식, 18개 root/target publication, POM과 Gradle module identity, 주요 artifact와 SHA-256 checksum, 정렬된 내부 dependency, Material 비포함을 검사한다. 외부 repository 대신 staging publish와 consumer compile을 수행한 뒤 결과 repository를 7일간 Actions artifact로 보관한다. push와 pull request 검증은 기본 `0.1.0-SNAPSHOT`을 사용하며 staging artifact를 업로드하지 않는다.

## GitHub Packages

pre-release를 소비하는 프로젝트에만 GitHub Packages repository를 추가한다.

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/90ms/beez-design")
        credentials {
            username = providers.gradleProperty("gpr.user").orNull
                ?: System.getenv("GITHUB_ACTOR")
            password = providers.gradleProperty("gpr.key").orNull
                ?: System.getenv("GITHUB_TOKEN")
        }
    }
    mavenCentral()
}
```

```kotlin
commonMain.dependencies {
    implementation("beez.design:beez-components:0.1.0-alpha.1")
}
```

credential 규칙:

- token과 password를 source, gradle.properties commit, README, issue, log에 넣지 않는다.
- 로컬에서는 사용자 전용 Gradle property 또는 OS credential store를 사용한다.
- CI에서는 repository secret/환경 변수로 주입하고 workflow 권한을 최소화한다.
- 공개 repository라도 GitHub Packages 소비자 인증이 필요할 수 있으므로 팀 onboarding 문서에 권한과 설정 방법을 함께 기록한다.

## Maven Central Stable

첫 공개 Stable release 전에 다음을 완료한다.

1. beez.design namespace 등록/검증 가능 여부와 소유권을 확인한다.
2. POM의 이름, 설명, URL, license, developer, SCM metadata를 채운다.
3. source 및 documentation artifact와 signing 설정을 확인한다.
4. API compatibility와 최소 지원 플랫폼을 검토한다.
5. consumer sample에서 Android와 Compose Multiplatform dependency resolve를 확인한다.
6. 승인된 tag/release만 publish하도록 CI를 보호한다.

Stable 소비자는 Maven Central만 사용한다.

```kotlin
repositories {
    mavenCentral()
}

commonMain.dependencies {
    implementation("beez.design:beez-components:1.0.0")
}
```

## Versioning

- snapshot: 0.1.0-SNAPSHOT
- alpha: 0.1.0-alpha.1
- beta: 0.1.0-beta.1
- release candidate: 0.1.0-rc.1
- Stable: 1.0.0

같은 version은 다시 publish하지 않는다. 이미 공개된 artifact에 문제가 있으면 새 patch/pre-release version과 migration note를 만든다. 초기에는 모듈별 독립 version을 사용하지 않고 aligned release를 유지한다.

## CI 운영

```text
Pull request       → checks only, publish 없음
main push          → 기본 publish 없음
PR/main validation → local staging publish → 독립 consumer compile
승인된 v* tag      → release validation → signing → 외부 target publish
```

하나의 release job이 모든 KMP target publication을 생성하고 publish한다. 중복 runner가 같은 좌표를 publish하지 않도록 concurrency와 tag 보호를 설정한다. release 전 최종 diff, changelog, 문서, API, compatibility, consumer sample을 검토한다.

## Release checklist

- [ ] 관련 ADR/spec/API diff가 review되었다.
- [ ] README, 호환성, changelog, migration guidance가 최신이다.
- [ ] version 좌표가 새롭고 채널 규칙에 맞다.
- [ ] POM metadata, license, source/docs artifact, signing이 준비되었다.
- [ ] consumer sample이 release candidate artifact를 resolve한다.
- [ ] 승인된 tag/release와 최소 권한 CI만 publish한다.
- [ ] release 결과와 후속 조치를 기록한다.

## 아직 확정하지 않은 항목

- Maven Central namespace와 최종 license
- 외부 publication/signing Gradle 설정과 POM metadata
- API compatibility validator와 changelog 자동화 도구
- GitHub Packages pre-release workflow의 tag 규칙과 retention

첫 GitHub Packages alpha에 필요한 선택지와 제안은 [ADR-0014](decisions/0014-first-alpha-release-pipeline.md)에 정리했다. 이 ADR이 Accepted되고 license 및 공개 metadata 방향이 정해지기 전에는 외부 publish workflow와 package를 만들지 않는다.

## 공식 참고

- [Kotlin Multiplatform library publishing](https://kotlinlang.org/docs/multiplatform-publish-lib.html)
- [Publish Kotlin Multiplatform libraries to Maven Central](https://kotlinlang.org/docs/multiplatform/multiplatform-publish-libraries-to-maven.html)
- [Gradle Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)
- [GitHub Packages Gradle registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry)
