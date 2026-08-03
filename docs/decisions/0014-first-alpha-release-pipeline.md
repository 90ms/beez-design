# ADR-0014: First alpha release pipeline

- 상태: Proposed
- 제안일: 2026-08-03

## 문맥

ADR-0007은 Maven Local, GitHub Packages pre-release, Maven Central Stable 채널을 분리하고 승인된 tag 또는 GitHub Release만 외부 publish를 시작하도록 결정했다. 첫 `0.1.0-alpha.1`을 준비하려면 이 원칙을 실제 CI trigger, 권한, metadata, signing 및 검증 절차로 구체화해야 한다.

현재 다음 기반은 검증되었다.

- `beez-tokens`, `beez-foundation`, `beez-components`는 하나의 `beezVersion`으로 정렬된다.
- repository-local Maven staging에서 세 root publication과 Android, Desktop, iOS arm64, iOS Simulator arm64, Wasm target publication을 생성한다.
- 독립 KMP consumer가 project dependency와 Material 없이 Components, Foundation, Tokens의 Android 및 Desktop variant를 resolve하고 compile한다.
- [`0.1.0-alpha.1` dry-run](https://github.com/90ms/beez-design/actions/runs/30789231140)은 외부 package write 없이 staging artifact를 생성한다.
- [snapshot publication validation](https://github.com/90ms/beez-design/actions/runs/30790461653)과 alpha artifact 재검사에서 18개 POM, 18개 Gradle module metadata, 주요/source artifact와 72개 SHA-256 checksum을 확인했다.

외부 publish는 아직 준비되지 않았다.

- repository 최종 license가 선택되지 않았고 root `LICENSE`가 없다.
- POM에 library 이름, 설명, URL, license, developer, SCM metadata가 없다.
- GitHub Packages publish repository와 credential wiring이 없다.
- 현재 repository 기본 workflow 권한은 read-only이며 package upload 권한을 명시하지 않았다.
- tag/release 승인 방식, alpha artifact signing과 package retention을 확정하지 않았다.

## 제안

아래 내용은 이 ADR이 Accepted가 되기 전에는 외부 publish 구현으로 옮기지 않는다.

### 좌표와 version

- 첫 pre-release version은 `0.1.0-alpha.1`로 한다.
- Maven group은 ADR-0004의 provisional `beez.design`을 사용한다.
- `beez-tokens`, `beez-foundation`, `beez-components`를 같은 version으로 한 번에 publish한다.
- 기존 좌표를 덮어쓰지 않으며 publish 전 동일 version package 존재 여부를 확인한다.

### Trigger와 승인

- pull request와 main push는 외부 publish를 수행하지 않는다.
- 수동 `workflow_dispatch`는 staging dry-run과 artifact audit까지만 허용한다.
- GitHub에서 prerelease로 게시된 `v0.1.0-alpha.1` release만 package publish를 시작한다.
- release tag에서 추출한 version과 Gradle `beezVersion`이 정확히 일치해야 한다.
- publish job에는 별도 GitHub Environment와 required reviewer를 적용한다.
- version별 concurrency group을 사용하고 진행 중인 release publish는 자동 취소하지 않는다.

### 권한과 credential

- release job 기본 권한은 `contents: read`, `packages: write`만 허용한다.
- 현재 repository package를 publish할 때 GitHub Actions의 `GITHUB_TOKEN`을 사용한다.
- username은 `GITHUB_ACTOR`, password는 `GITHUB_TOKEN`에서 읽으며 source나 log에 값을 기록하지 않는다.
- consumer 검증에는 package read가 가능한 최소 credential만 전달한다.

### Publication과 검증

- 하나의 Ubuntu job이 세 모듈의 전체 KMP publication을 한 번씩 GitHub Packages에 publish한다.
- publish 전에 기존 library build, token drift, version, staging publication audit과 독립 consumer 검증을 모두 통과해야 한다.
- publish 후 독립 consumer가 repository-local staging이 아니라 GitHub Packages의 `beez-components:0.1.0-alpha.1`을 새 Gradle user home에서 resolve해야 한다.
- release note에는 Experimental API, 지원 플랫폼 상태, 알려진 검증 공백과 upgrade 경로를 기록한다.

### Metadata, license, signing과 retention

다음 네 항목은 사용자 방향을 받은 뒤 이 제안에 구체적인 값으로 반영한다.

1. repository와 POM에 사용할 최종 license
2. POM developer의 id, 표시 이름과 URL
3. GitHub Packages alpha artifact의 signing 적용 여부와 key 관리 방식
4. pre-release package 보존 기간 또는 영구 보존 여부

license가 정해지기 전에는 외부 alpha도 publish하지 않는다. 선택한 license의 root 파일, POM metadata와 README 설명을 같은 변경에 포함한다.

## 대안

### main push에서 snapshot 자동 publish

반복 속도는 빠르지만 package 수와 credential 노출 면적을 늘리고, 승인되지 않은 변경을 공유 좌표로 만들 수 있어 채택하지 않는다.

### 수동 workflow에서 바로 package publish

운영은 단순하지만 tag/release와 source revision의 관계가 약하고 실수로 외부 상태를 만들기 쉬워 채택하지 않는다.

### 첫 alpha를 Maven Central에 publish

공개 발견성은 높지만 namespace 검증, license, signing과 Stable 채널 준비를 한 번에 요구한다. ADR-0007의 단계적 채널 분리와 맞지 않아 채택하지 않는다.

## 영향

### 장점

- dry-run과 외부 publish 권한이 명확히 분리된다.
- tag, version, source revision과 package 좌표를 추적할 수 있다.
- KMP 전체 publication과 실제 소비 경로를 한 release 단위로 검증한다.
- public package를 만들기 전에 license 및 POM 누락을 차단한다.

### 비용과 제약

- GitHub Environment, package 권한, license와 developer metadata 결정이 추가로 필요하다.
- publish 후 consumer 검증 실패 시 이미 생성된 좌표를 덮어쓸 수 없으므로 새 pre-release version이 필요하다.
- GitHub Packages consumer는 공개 repository의 package라도 인증 설정이 필요할 수 있다.

## 승인 조건

- license, developer metadata, signing, retention에 대한 사용자 방향이 기록된다.
- `docs/publishing.md`, README, CHANGELOG와 POM metadata가 선택 결과와 일치한다.
- package 충돌 사전 검사가 인증된 CI 환경에서 성공한다.
- release workflow의 최종 diff와 최소 권한을 검토한다.

## 참고

- [ADR-0004: Namespace and artifact naming](0004-namespace-and-artifacts.md)
- [ADR-0007: BEEZ publishing strategy](0007-publishing-strategy.md)
- [BEEZ Publishing Guide](../publishing.md)
- [Kotlin Multiplatform library publishing](https://kotlinlang.org/docs/multiplatform-publish-lib.html)
- [GitHub Packages Gradle registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry)
- [GitHub Packages permissions](https://docs.github.com/en/packages/learn-github-packages/about-permissions-for-github-packages)
