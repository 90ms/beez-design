# BEEZ Visual Regression Testing

- 상태: Active
- 기준일: 2026-08-03

## 목적

BEEZ component의 semantic token mapping, layout과 state 표현이 의도하지 않게 바뀌는 것을 Desktop screenshot 기준으로 감지한다. 시각 회귀 검사는 behavior, semantics와 실제 플랫폼 접근성 검사를 대체하지 않는다.

## 기준 환경

- target: Compose Desktop JVM
- CI host: `ubuntu-latest`
- runtime JDK: 17
- virtual display: `xvfb-run`, 1280×1024×24
- test source: `beez-components/src/desktopTest`

Desktop 기준은 지원 플랫폼 전체가 pixel 단위로 같다는 의미가 아니다. Android, iOS와 Web의 rasterization 또는 접근성 bridge 차이는 별도의 platform 검증에서 다룬다.

## Baseline 형식

각 scenario는 고정된 Compose canvas를 `captureToImage()`로 캡처한다.

1. 전체 이미지를 `build/reports/visual-candidates/*.ppm`에 기록한다.
2. 이미지를 24×24 cell의 평균 RGBA 값으로 정규화한다.
3. 정규화한 signature를 gzip/Base64로 압축해 test source의 baseline으로 보관한다.
4. 평균 channel delta와 크게 달라진 cell 비율이 허용 범위를 넘으면 테스트를 실패시킨다.

정규화는 font antialiasing과 runner의 작은 raster 차이로 인한 불안정성을 줄이면서 component의 크기, 배치, semantic color와 주요 state 변화는 감지하기 위한 절충이다. 전체 PPM candidate는 실패 시 Gradle report artifact에서 내려받아 시각적으로 검토한다.

## 초기 scenario

- Action Button: Light, Dark, alternate brand의 focused BrandSolid와 대표 state
- Checkbox: Light, Dark, alternate brand의 focused checked와 checked/unchecked/disabled state
- Text Field: Light, Dark error, alternate brand RTL/read-only

각 scenario는 고정된 canvas와 BEEZ semantic token을 사용한다. Alternate brand는 테스트 내부에서 semantic color role만 교체하며 제품용 public token scheme을 추가하지 않는다.

## Baseline 갱신 절차

1. 변경된 token, component specification과 의도를 먼저 검토한다.
2. CI artifact의 PPM candidate를 열어 의도한 변화인지 확인한다.
3. 의도한 변화일 때만 새 normalized signature를 반영한다.
4. 관련 component 명세의 Visual test matrix와 changelog를 함께 갱신한다.
5. Library Validation과 Pages 배포 결과를 확인한다.

테스트를 통과시키기 위해 허용 오차를 임의로 넓히거나 candidate 검토 없이 baseline을 교체하지 않는다.
