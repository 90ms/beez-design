# Component authoring workflow

## Contents

1. Qualify the component
2. Write the specification
3. Design the API
4. Implement shared behavior
5. Adapt platforms
6. Verify and document

## 1. Qualify the component

- Apply the start conditions in `docs/definition-of-done.md`.
- Search for an existing component or pattern with the same purpose.
- Keep product-domain UI local until system-level reuse is justified.

## 2. Write the specification

- Copy the component section from `docs/component-template.md`.
- Define purpose, alternatives, anatomy, properties, variants, sizes, and states.
- Define state precedence and combinable states.
- Specify long content, responsive behavior, internationalization, and accessibility.
- Map every visual property to an approved token.

## 3. Design the API

- Put the public API in `commonMain`.
- Prefer state hoisting and explicit events.
- Use sealed types or enums when Boolean combinations create distinct meanings.
- Keep `Modifier` as the first optional argument.
- Define slot content contracts rather than exposing arbitrary styling internals.
- Do not expose platform or Material types from core APIs.

## 4. Implement shared behavior

- Build with Compose primitives allowed by ADR-0002.
- Keep layout, state, interaction, token mapping, and semantics in the shared implementation.
- Avoid raw design values and duplicated theme mappings.
- Implement the smallest complete set of specified properties and states.

## 5. Adapt platforms

- Use common Compose behavior first.
- Add an adapter or `expect`/`actual` only under ADR-0003 conditions.
- Do not copy the whole component into a platform source set.
- Document Required and Adaptive differences; treat unexplained differences as defects.

## 6. Verify and document

- Meet Experimental or Stable checks explicitly; do not claim Stable by intuition.
- Test state transitions, events, constraints, semantics, themes, font scale, long content, and RTL.
- Add shared catalog scenarios for playground, variants, states, themes, and accessibility.
- Record platform status separately from component maturity.
- Keep the specification, API docs, implementation, tests, catalog, and changelog aligned.
