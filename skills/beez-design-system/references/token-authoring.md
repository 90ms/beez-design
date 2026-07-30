# Token authoring workflow

## Contents

1. Decide whether a token belongs in BEEZ
2. Select the token tier
3. Name and encode the token
4. Map themes
5. Validate consumers

## 1. Decide whether a token belongs in BEEZ

- Search existing scale and semantic tokens first.
- Identify at least one concrete design decision and expected consumer.
- Keep product-specific values local until reuse or system-level value is clear.
- Do not add aliases solely to provide another spelling for an existing meaning.

## 2. Select the token tier

- Use a Scale token to constrain reusable values without UI intent.
- Use a Semantic token to express a role that stays stable across appearance and brand themes.
- Keep component style mapping in the component specification initially.
- Propose Component tokens only when the conditions in `docs/token-taxonomy.md` are met.

## 3. Name and encode the token

- Follow the full path and character rules in `docs/token-taxonomy.md`.
- Prefer explicit roles such as `background` and `foreground` over abbreviations.
- Do not include raw values, platform names, appearance modes, or temporary product names in paths.
- Encode the source in the approved DTCG subset with `$type`, `$value`, `$description`, and aliases.
- Put non-standard metadata under `$extensions`.

## 4. Map themes

- Keep the same Semantic token contract in BEEZ Light and Dark themes.
- Apply brand differences through Semantic mappings, not component forks.
- Review foreground and background roles as pairs.
- Do not let theme overrides change component structure or behavior.

## 5. Validate consumers

- Validate schema, duplicate paths, aliases, cycles, supported types, and generated identifier collisions.
- Compare Light, Dark, BEEZ, and the test brand theme.
- Identify every affected component mapping.
- Review accessibility-sensitive color combinations.
- Update documentation, generated code, tests, and change history together.
