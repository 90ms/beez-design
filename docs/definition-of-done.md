# BEEZ Definition of Done

## 1. 목적

이 문서는 BEEZ의 토큰, 컴포넌트 및 릴리스가 완료되었다고 판단하는 공통 기준을 정의한다.

체크리스트는 작업량을 늘리기 위한 절차가 아니라 디자인 명세, 구현, 접근성, 문서와 배포 사이의 누락을 방지하기 위한 품질 계약이다.

모든 항목에 무조건 체크하는 대신 적용 여부를 먼저 판단한다. 적용되지 않는 항목은 이유와 근거를 작업 기록에 남긴다.

## 2. 성숙도 상태

### Proposed

- 해결할 문제와 사용 사례가 문서화되었다.
- 기존 요소로 해결할 수 없는 이유가 있다.
- 공개 API와 디자인은 아직 변경될 수 있다.
- 제품 코드에서 안정적인 dependency로 사용하는 것을 권장하지 않는다.

### Experimental

- 명세와 최소 구현이 존재한다.
- 하나 이상의 실제 사용 사례에서 검증할 수 있다.
- API와 token 이름은 minor release에서도 바뀔 수 있다.
- 지원 플랫폼과 알려진 제약이 표시되어 있다.

### Stable

- 이 문서의 Stable 완료 조건을 충족한다.
- 공개 API와 token은 안정성 및 폐기 정책의 적용을 받는다.
- 변경 시 호환성과 마이그레이션 영향을 관리한다.

### Deprecated

- 신규 사용을 권장하지 않는다.
- 대체 요소 또는 제거 이유가 문서화되어 있다.
- 가능한 경우 자동 또는 수동 마이그레이션 방법을 제공한다.
- 제거 예정 버전이나 재검토 조건을 표시한다.

## 3. 플랫폼 구현 상태

컴포넌트 성숙도와 플랫폼 구현 상태를 분리해 관리한다.

| 상태 | 의미 |
| --- | --- |
| Not Planned | 해당 플랫폼에 제공할 계획이 없으며 이유가 기록됨 |
| Planned | 지원 범위에 있으나 구현을 시작하지 않음 |
| In Progress | 구현 또는 검증 중 |
| Experimental | 사용할 수 있지만 제약이나 미완료 검증이 있음 |
| Ready | 명세에 따른 구현과 필수 검증이 완료됨 |
| Deprecated | 해당 플랫폼 구현의 신규 사용을 권장하지 않음 |

컴포넌트가 Stable이어도 `Not Planned`로 합의한 플랫폼이 있을 수 있다. 단, BEEZ가 지원한다고 표시한 플랫폼의 실제 상태를 숨기지 않는다.

## 4. 컴포넌트 시작 조건

구현을 시작하기 전에 다음이 준비되어야 한다.

- [ ] 해결할 사용자 또는 제품 문제가 설명되어 있다.
- [ ] 둘 이상의 재사용 가능성이 있거나 시스템 수준에서 관리할 명확한 이유가 있다.
- [ ] 기존 token, component 또는 pattern으로 해결할 수 없는 이유를 확인했다.
- [ ] `When to use`와 `When not to use`가 작성되어 있다.
- [ ] Anatomy와 필수 slot이 정의되어 있다.
- [ ] 주요 property, variant, size와 state가 정의되어 있다.
- [ ] 접근성 위험과 플랫폼 차이를 사전에 검토했다.

조건을 충족하지 않으면 제품 로컬 구현 또는 명세 탐색 단계로 유지한다.

## 5. Experimental 완료 조건

### Specification

- [ ] 공통 컴포넌트 템플릿으로 명세를 작성했다.
- [ ] 목적, 사용 기준과 대안을 설명했다.
- [ ] Anatomy, property, variant, size와 state를 정의했다.
- [ ] 상태 조합과 우선순위를 정의했다.
- [ ] layout, overflow와 긴 콘텐츠 동작을 정의했다.
- [ ] token mapping 초안을 작성했다.
- [ ] 지원 플랫폼과 예상되는 차이를 표시했다.

### API

- [ ] 최소 Compose public API가 명세에 포함되어 있다.
- [ ] 필수 인자와 안전한 기본값을 구분했다.
- [ ] state ownership과 event 흐름이 명확하다.
- [ ] platform type과 내부 Material type이 공통 공개 API에 노출되지 않는다.
- [ ] 핵심 모듈에 Material 2 또는 Material 3 dependency가 추가되지 않았다.
- [ ] API 이름이 BEEZ 명명 규칙을 따른다.

### Implementation

- [ ] Tier 1 플랫폼에서 기본 사용 사례가 동작한다.
- [ ] 구현이 승인된 token만 사용한다.
- [ ] Light와 Dark theme에서 렌더링된다.
- [ ] disabled 또는 loading 등 핵심 상태의 상호작용이 명세와 일치한다.
- [ ] 임의의 원시 디자인 값을 하드코딩하지 않았다.
- [ ] Compose primitive 기반 interaction과 semantics가 컴포넌트 명세를 따른다.

### Verification

- [ ] 핵심 동작 단위 테스트가 있다.
- [ ] 대표 상태의 screenshot 기준이 있다.
- [ ] semantics tree와 action을 확인했다.
- [ ] 알려진 문제와 누락된 검증을 문서화했다.

### Documentation

- [ ] 기본 사용 예제가 있다.
- [ ] 카탈로그에서 대표 variant와 state를 확인할 수 있다.
- [ ] Experimental 상태와 API 변경 가능성을 표시했다.

## 6. Stable 완료 조건

Experimental 조건을 모두 충족하고 다음 기준을 추가로 만족해야 한다.

### Design specification

- [ ] 공개 property, variant, size와 state에 미해결 질문이 없다.
- [ ] 유사 컴포넌트와의 선택 기준이 명확하다.
- [ ] 모든 slot과 token mapping이 구현과 일치한다.
- [ ] content, internationalization 및 responsive 규칙을 검토했다.
- [ ] Do와 Do not 사례가 검토되었다.

### API quality

- [ ] 대표 제품 사용 사례로 API를 검증했다.
- [ ] 불필요한 overload와 Boolean 조합이 없다.
- [ ] slot API의 허용 콘텐츠와 책임이 문서화되어 있다.
- [ ] state hoisting, recomposition과 안정성 특성을 검토했다.
- [ ] public declaration에 API 문서가 있다.
- [ ] binary 및 source compatibility 정책에 따른 검사를 통과한다.
- [ ] Material 연동이 필요하면 핵심 API가 아닌 선택형 adapter에 격리되어 있다.

### Platform implementation

- [ ] Android 구현 상태가 Ready다.
- [ ] iOS와 Desktop의 계획된 지원 범위를 구현하고 상태를 표시했다.
- [ ] Web/Wasm의 지원 여부와 실험적 제약을 표시했다.
- [ ] 의도하지 않은 플랫폼별 API 차이가 없다.
- [ ] 의도된 렌더링 및 동작 차이는 명세에 기록되어 있다.

### Accessibility

- [ ] 역할, 이름, 상태, 값과 action을 보조기술에 전달한다.
- [ ] 최소 상호작용 영역을 충족한다.
- [ ] 키보드 포커스와 실행을 지원한다.
- [ ] 색상만으로 정보나 상태를 전달하지 않는다.
- [ ] 지원하는 전경과 배경 조합이 정한 대비 기준을 통과한다.
- [ ] 확대된 font scale에서 정보 손실이나 상호작용 차단이 없다.
- [ ] motion reduction 설정에서 안전하게 동작한다.
- [ ] Tier 1 플랫폼에서 수동 보조기술 검증을 완료했다.

### Automated tests

- [ ] 모든 public property와 state의 핵심 경로를 테스트한다.
- [ ] state precedence와 callback을 테스트한다.
- [ ] Light와 Dark theme screenshot을 검증한다.
- [ ] BEEZ 기본 테마와 최소 하나의 대체 테스트 테마를 검증한다.
- [ ] 긴 콘텐츠, 좁은 constraint와 확대 font scale을 검증한다.
- [ ] LTR과 RTL에서 방향성 있는 UI를 검증한다.
- [ ] semantics와 accessibility action을 테스트한다.
- [ ] 지원 플랫폼의 CI 검증을 통과한다.

### Catalog and documentation

- [ ] Playground를 제공한다.
- [ ] variant, size와 state matrix를 제공한다.
- [ ] 올바른 사용과 잘못된 사용 예제를 제공한다.
- [ ] 플랫폼별 지원 상태와 차이를 공개한다.
- [ ] API 예제가 현재 릴리스 코드로 컴파일된다.
- [ ] 문서가 명세 원본 및 생성 결과와 일치한다.

### Release

- [ ] public API와 생성 결과물의 diff를 검토했다.
- [ ] 변경 유형에 맞는 버전을 결정했다.
- [ ] changelog 또는 changeset을 작성했다.
- [ ] dependency metadata와 라이선스를 확인했다.
- [ ] 소비자 샘플 프로젝트에서 배포 artifact를 사용해 검증했다.
- [ ] breaking change라면 마이그레이션 문서를 제공한다.

## 7. 토큰 변경 완료 조건

### New token

- [ ] 이름이 token taxonomy를 따른다.
- [ ] 목적과 예상 사용처가 설명되어 있다.
- [ ] 기존 token으로 해결할 수 없는 이유가 있다.
- [ ] 올바른 `$type`과 `$value`를 사용한다.
- [ ] alias가 올바른 계층을 참조한다.
- [ ] Light와 Dark 계약이 일치한다.
- [ ] Kotlin 및 문서 생성 결과를 검토했다.
- [ ] 접근성에 영향을 주면 관련 조합을 검증했다.

### Changed token

- [ ] 값을 참조하는 semantic token과 component를 확인했다.
- [ ] 지원 theme과 platform의 시각적 영향을 검토했다.
- [ ] 의도하지 않은 screenshot 변경이 없다.
- [ ] 의미 변경이 기존 이름의 재사용인지 검토했다.
- [ ] 변경 이력과 소비자 영향을 기록했다.

### Deprecated or removed token

- [ ] deprecated 이유와 대체 token을 제공한다.
- [ ] 코드와 문서에서 신규 사용을 차단하거나 경고한다.
- [ ] 저장소 내부 사용처를 마이그레이션했다.
- [ ] 제거 시점이 안정성 정책과 일치한다.
- [ ] breaking change가 필요한 경우 마이그레이션을 제공한다.

## 8. 예외 처리

완료 조건을 충족하지 못한 채 배포해야 하는 경우 다음을 모두 기록한다.

- 충족하지 못한 항목
- 사용자와 소비자에게 미치는 영향
- 임시 완화 방법
- 담당자 또는 추적 이슈
- 해결 목표 버전이나 재검토 날짜

접근성, 데이터 손실, 보안 또는 공개 API 호환성에 중대한 문제가 있는 경우에는 예외로 Stable 상태를 부여하지 않는다.

## 9. 검토 증거

체크리스트의 완료 여부만 표시하지 않고 가능한 경우 다음 증거를 연결한다.

- 명세 문서
- API diff
- 테스트 결과
- screenshot diff
- 접근성 검사 결과
- 카탈로그 scenario
- 플랫폼별 구현 상태
- changelog 또는 migration 문서

## 10. 변경 원칙

이 완료 기준은 실제 개발 과정에서 과도하거나 부족한 항목이 확인되면 수정할 수 있다. 품질 기준을 낮추는 변경은 편의보다 사용자 영향과 유지보수 비용을 근거로 검토한다.
