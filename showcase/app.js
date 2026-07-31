(() => {
  const root = document.documentElement;
  const menu = document.querySelector("[data-sidebar]");
  const menuToggle = document.querySelector("[data-menu-toggle]");
  const themeToggle = document.querySelector("[data-theme-toggle]");
  const themeLabel = document.querySelector("[data-theme-label]");
  const themeIcon = document.querySelector("[data-theme-icon]");
  const tokenLoader = window.BeezTokenLoader;

  const translations = {
    en: {
      "meta.title": "BEEZ Showcase",
      "meta.description": "BEEZ Design System visual showcase prototype",
      "brand.home": "BEEZ Showcase home",
      "brand.designSystem": "Design System",
      "nav.aria": "Showcase navigation",
      "nav.explore": "Explore",
      "nav.overview": "Overview",
      "nav.foundations": "Foundations",
      "nav.themes": "Themes",
      "nav.components": "Components",
      "nav.accessibility": "Accessibility",
      "status.prototype": "Prototype · 0.1.0",
      "menu.open": "Open navigation",
      "menu.close": "Close navigation",
      "breadcrumb.showcase": "Showcase",
      "topbar.guidelines": "Guidelines",
      "locale.aria": "Language",
      "theme.toggle": "Toggle color theme",
      "theme.light": "Light",
      "theme.dark": "Dark",
      "common.semantic": "semantic",
      "hero.eyebrow": "BEEZ DESIGN SYSTEM <span>·</span> VISUAL PROTOTYPE",
      "hero.title": "Design with<br><em>meaning.</em>",
      "hero.description": "A token-first, themeable component language for products that feel unmistakably their own.",
      "hero.explore": "Explore components",
      "hero.foundations": "View foundations",
      "hero.semanticTokens": "semantic<br>tokens",
      "hero.sharedLanguage": "shared<br>language",
      "principles.eyebrow": "01 / PRINCIPLES",
      "principles.title": "A system with a point of view.",
      "principles.intro": "BEEZ gives teams a confident starting point, then leaves room for a brand to make it theirs.",
      "principles.meaning.title": "Meaning over values",
      "principles.meaning.body": "Products consume semantic roles, not raw colors or arbitrary dimensions.",
      "principles.contract.title": "One shared contract",
      "principles.contract.body": "One component API across Android, iOS, Desktop, and Experimental Web.",
      "principles.brandReady.title": "Brand-ready by design",
      "principles.brandReady.body": "Swap semantic mappings to extend the language without forking components.",
      "foundations.eyebrow": "02 / FOUNDATIONS",
      "foundations.title": "Small decisions, repeated well.",
      "foundations.intro": "The foundation layer turns design intent into a vocabulary that components can share.",
      "foundations.color": "Color",
      "foundations.typography": "Typography",
      "foundations.bodySample": "The quick brown fox",
      "foundations.bold": "28 / 36 · bold",
      "foundations.regular": "16 / 24 · regular",
      "foundations.spacing": "Spacing",
      "foundations.shapeMotion": "Shape & Motion",
      "themes.eyebrow": "03 / THEME LAB",
      "themes.title": "Make it yours.",
      "themes.intro": "Change the semantic mapping. The component contract stays the same.",
      "themes.appearance": "Appearance",
      "themes.brandMapping": "Brand mapping",
      "themes.testBrand": "Test Brand",
      "themes.labNote": "Both controls resolve the same semantic token contract. Try changing them before reviewing a component.",
      "themes.currentMapping": "CURRENT MAPPING",
      "components.eyebrow": "04 / COMPONENTS",
      "components.title": "One contract, many contexts.",
      "components.intro": "Action Button is the first BEEZ component slice. Review hierarchy, states, and content before it becomes Stable.",
      "components.actionButton": "Action Button",
      "components.experimental": "Experimental",
      "components.continue": "Continue",
      "components.saveDraft": "Save draft",
      "components.learnMore": "Learn more",
      "components.small": "Small",
      "components.largeAction": "Large action",
      "components.demoControls": "Interactive preview",
      "components.demoReady": "Ready · choose an action",
      "components.demoTriggered": "Action triggered:",
      "components.demoDisabled": "Actions are disabled",
      "components.demoLoading": "Loading state is active",
      "components.disableActions": "Disable actions",
      "components.enableActions": "Enable actions",
      "components.simulateLoading": "Simulate loading",
      "components.stopLoading": "Stop loading",
      "components.reset": "Reset",
      "components.variants": "Variants",
      "components.variantValues": "BrandSolid · Neutral · Outline",
      "components.states": "States",
      "components.stateValues": "Enabled · Focused · Disabled · Loading",
      "components.theme": "Theme",
      "components.themeValues": "Semantic colors, shape, spacing",
      "components.a11y": "A11y",
      "components.a11yValues": "Button role · 48dp target · focus",
      "accessibility.eyebrow": "05 / ACCESSIBILITY",
      "accessibility.title": "Quality is part of the shape.",
      "accessibility.intro": "Accessibility is not a later audit. It is part of every component contract.",
      "accessibility.target.title": "48dp target",
      "accessibility.target.body": "Visual size and interaction area stay distinct.",
      "accessibility.focus.title": "Focus visible",
      "accessibility.focus.body": "Keyboard and assistive focus has a clear ring.",
      "accessibility.scale.title": "Content scales",
      "accessibility.scale.body": "Long labels and font scaling are first-class cases.",
      "accessibility.state.title": "State announced",
      "accessibility.state.body": "Disabled and loading states reach semantics.",
      "footer.prototype": "BEEZ Design System · visual prototype",
      "footer.repository": "View repository ↗",
    },
    ko: {
      "meta.title": "BEEZ 쇼케이스",
      "meta.description": "BEEZ 디자인 시스템을 시각적으로 살펴보는 프로토타입",
      "brand.home": "BEEZ 쇼케이스 홈",
      "brand.designSystem": "디자인 시스템",
      "nav.aria": "쇼케이스 탐색",
      "nav.explore": "둘러보기",
      "nav.overview": "개요",
      "nav.foundations": "파운데이션",
      "nav.themes": "테마",
      "nav.components": "컴포넌트",
      "nav.accessibility": "접근성",
      "status.prototype": "프로토타입 · 0.1.0",
      "menu.open": "탐색 메뉴 열기",
      "menu.close": "탐색 메뉴 닫기",
      "breadcrumb.showcase": "쇼케이스",
      "topbar.guidelines": "가이드라인",
      "locale.aria": "언어",
      "theme.toggle": "색상 테마 전환",
      "theme.light": "라이트",
      "theme.dark": "다크",
      "common.semantic": "시맨틱",
      "hero.eyebrow": "BEEZ DESIGN SYSTEM <span>·</span> VISUAL PROTOTYPE",
      "hero.title": "의미를 담아<br><em>디자인하세요.</em>",
      "hero.description": "제품만의 인상을 만들 수 있도록 토큰을 중심으로 설계된 테마형 컴포넌트 언어입니다.",
      "hero.explore": "컴포넌트 살펴보기",
      "hero.foundations": "파운데이션 보기",
      "hero.semanticTokens": "시맨틱<br>토큰",
      "hero.sharedLanguage": "공유하는<br>언어",
      "principles.eyebrow": "01 / 원칙",
      "principles.title": "분명한 관점을 가진 시스템.",
      "principles.intro": "BEEZ는 팀이 자신 있게 시작할 수 있는 기반을 제공하면서도 브랜드의 개성을 담을 여지를 남깁니다.",
      "principles.meaning.title": "값보다 의미",
      "principles.meaning.body": "제품은 원시 색상이나 임의의 치수가 아니라 시맨틱 역할을 사용합니다.",
      "principles.contract.title": "하나의 공통 계약",
      "principles.contract.body": "Android, iOS, Desktop, Experimental Web에서 동일한 컴포넌트 API를 사용합니다.",
      "principles.brandReady.title": "브랜드를 위한 구조",
      "principles.brandReady.body": "컴포넌트를 복제하지 않고 시맨틱 매핑을 교체해 언어를 확장합니다.",
      "foundations.eyebrow": "02 / 파운데이션",
      "foundations.title": "작은 결정을 일관되게.",
      "foundations.intro": "파운데이션 레이어는 디자인 의도를 컴포넌트가 공유할 수 있는 어휘로 바꿉니다.",
      "foundations.color": "색상",
      "foundations.typography": "타이포그래피",
      "foundations.bodySample": "다람쥐 헌 쳇바퀴에 타고파",
      "foundations.bold": "28 / 36 · 굵게",
      "foundations.regular": "16 / 24 · 보통",
      "foundations.spacing": "간격",
      "foundations.shapeMotion": "형태와 모션",
      "themes.eyebrow": "03 / 테마 랩",
      "themes.title": "나만의 것으로 만드세요.",
      "themes.intro": "시맨틱 매핑을 바꿔도 컴포넌트 계약은 그대로 유지됩니다.",
      "themes.appearance": "화면 모드",
      "themes.brandMapping": "브랜드 매핑",
      "themes.testBrand": "테스트 브랜드",
      "themes.labNote": "두 컨트롤은 같은 시맨틱 토큰 계약을 해석합니다. 컴포넌트를 살펴보기 전에 직접 바꿔보세요.",
      "themes.currentMapping": "현재 매핑",
      "components.eyebrow": "04 / 컴포넌트",
      "components.title": "하나의 계약, 다양한 맥락.",
      "components.intro": "Action Button은 첫 번째 BEEZ 컴포넌트 슬라이스입니다. Stable이 되기 전에 계층, 상태, 콘텐츠를 검토합니다.",
      "components.actionButton": "Action Button",
      "components.experimental": "실험적",
      "components.continue": "계속하기",
      "components.saveDraft": "초안 저장",
      "components.learnMore": "자세히 보기",
      "components.small": "작게",
      "components.largeAction": "큰 액션",
      "components.demoControls": "인터랙티브 미리보기",
      "components.demoReady": "준비됨 · 액션을 선택하세요",
      "components.demoTriggered": "실행된 액션:",
      "components.demoDisabled": "액션이 비활성화되었습니다",
      "components.demoLoading": "로딩 상태가 활성화되었습니다",
      "components.disableActions": "액션 비활성화",
      "components.enableActions": "액션 활성화",
      "components.simulateLoading": "로딩 시뮬레이션",
      "components.stopLoading": "로딩 중지",
      "components.reset": "초기화",
      "components.variants": "변형",
      "components.variantValues": "BrandSolid · Neutral · Outline",
      "components.states": "상태",
      "components.stateValues": "활성 · 포커스 · 비활성 · 로딩",
      "components.theme": "테마",
      "components.themeValues": "시맨틱 색상, 형태, 간격",
      "components.a11y": "접근성",
      "components.a11yValues": "버튼 역할 · 48dp 터치 영역 · 포커스",
      "accessibility.eyebrow": "05 / 접근성",
      "accessibility.title": "품질은 형태의 일부입니다.",
      "accessibility.intro": "접근성은 나중에 하는 감사가 아닙니다. 모든 컴포넌트 계약의 일부입니다.",
      "accessibility.target.title": "48dp 터치 영역",
      "accessibility.target.body": "시각적 크기와 상호작용 영역을 구분합니다.",
      "accessibility.focus.title": "포커스 표시",
      "accessibility.focus.body": "키보드와 보조기기의 포커스를 명확한 링으로 보여줍니다.",
      "accessibility.scale.title": "콘텐츠 스케일",
      "accessibility.scale.body": "긴 라벨과 글꼴 확대를 핵심 시나리오로 다룹니다.",
      "accessibility.state.title": "상태 알림",
      "accessibility.state.body": "비활성 및 로딩 상태가 시맨틱스에 전달됩니다.",
      "footer.prototype": "BEEZ 디자인 시스템 · 시각 프로토타입",
      "footer.repository": "저장소 보기 ↗",
    },
  };

  const supportedLocales = Object.keys(translations);
  const getInitialLocale = () => {
    try {
      const saved = window.localStorage.getItem("beez-showcase-locale");
      if (supportedLocales.includes(saved)) return saved;
    } catch {
      // Storage may be unavailable in a privacy-restricted browser.
    }
    const languages = navigator.languages?.length ? navigator.languages : [navigator.language];
    return languages.some((language) => language?.toLowerCase().startsWith("ko")) ? "ko" : "en";
  };

  let locale = getInitialLocale();
  let appearance = root.dataset.theme || "light";
  let brand = root.dataset.brand || "beez";
  let currentTheme;
  const demoActions = [...document.querySelectorAll("[data-demo-action]")];
  const demoPrimary = document.querySelector("[data-demo-primary]");
  const demoStatusNode = document.querySelector("[data-demo-status]");
  const demoDisabledToggle = document.querySelector("[data-demo-toggle='disabled']");
  const demoLoadingToggle = document.querySelector("[data-demo-toggle='loading']");
  const demoReset = document.querySelector("[data-demo-reset]");
  let demoDisabled = false;
  let demoLoading = false;
  let demoLastAction = "";

  const sections = [...document.querySelectorAll(".section-anchor")];
  const navLinks = [...document.querySelectorAll("[data-nav]")];
  const t = (key) => translations[locale][key] || translations.en[key] || key;

  const updateActiveSection = () => {
    const visibleSections = sections.filter((section) => section.getBoundingClientRect().top <= 180);
    const current = visibleSections[visibleSections.length - 1];
    navLinks.forEach((link) => link.classList.toggle("is-active", link.dataset.nav === current?.id));
  };

  const updateAppearanceControls = () => {
    document.querySelectorAll("button[data-appearance]").forEach((button) => {
      button.classList.toggle("is-selected", button.dataset.appearance === appearance);
      button.setAttribute("aria-pressed", String(button.dataset.appearance === appearance));
    });
    if (themeLabel) themeLabel.textContent = appearance === "dark" ? t("theme.dark") : t("theme.light");
    if (themeIcon) themeIcon.textContent = appearance === "dark" ? "☾" : "☼";
  };

  const updateBrandControls = () => {
    document.querySelectorAll("button[data-brand]").forEach((button) => {
      button.classList.toggle("is-selected", button.dataset.brand === brand);
      button.setAttribute("aria-pressed", String(button.dataset.brand === brand));
    });
  };

  const getDemoActionLabel = (button) => button.querySelector("[data-i18n]")?.dataset.i18n
    || button.textContent.replace("→", "").trim();

  const updateDemoState = () => {
    const actionsBlocked = demoDisabled || demoLoading;
    demoActions.forEach((button) => {
      button.disabled = actionsBlocked;
      button.setAttribute("aria-disabled", String(actionsBlocked));
    });
    demoPrimary?.classList.toggle("is-loading", demoLoading);
    demoPrimary?.setAttribute("aria-busy", String(demoLoading));
    if (demoDisabledToggle) {
      demoDisabledToggle.textContent = demoDisabled ? t("components.enableActions") : t("components.disableActions");
      demoDisabledToggle.setAttribute("aria-pressed", String(demoDisabled));
    }
    if (demoLoadingToggle) {
      demoLoadingToggle.textContent = demoLoading ? t("components.stopLoading") : t("components.simulateLoading");
      demoLoadingToggle.setAttribute("aria-pressed", String(demoLoading));
    }
    if (demoStatusNode) {
      const actionLabel = demoLastAction.includes(".") ? t(demoLastAction) : demoLastAction;
      const status = demoDisabled
        ? t("components.demoDisabled")
        : demoLoading
          ? t("components.demoLoading")
          : demoLastAction
            ? t("components.demoTriggered") + " " + actionLabel
            : t("components.demoReady");
      demoStatusNode.textContent = status;
      demoStatusNode.classList.toggle("is-active", Boolean(demoLastAction) && !actionsBlocked);
    }
  };

  const applyLocale = () => {
    root.lang = locale;
    root.dataset.locale = locale;
    document.title = t("meta.title");
    document.querySelector("meta[name='description']")?.setAttribute("content", t("meta.description"));
    document.querySelectorAll("[data-i18n]").forEach((node) => {
      node.textContent = t(node.dataset.i18n);
    });
    document.querySelectorAll("[data-i18n-html]").forEach((node) => {
      node.innerHTML = t(node.dataset.i18nHtml);
    });
    document.querySelectorAll("[data-i18n-aria]").forEach((node) => {
      node.setAttribute("aria-label", t(node.dataset.i18nAria));
    });
    document.querySelectorAll("button[data-locale-option]").forEach((button) => {
      const selected = button.dataset.localeOption === locale;
      button.classList.toggle("is-selected", selected);
      button.setAttribute("aria-pressed", String(selected));
    });
    updateAppearanceControls();
    updateBrandControls();
    updateDemoState();
    if (currentTheme) updateTokenLabels(currentTheme);
  };

  const setLocale = (nextLocale) => {
    if (!supportedLocales.includes(nextLocale)) return;
    locale = nextLocale;
    try {
      window.localStorage.setItem("beez-showcase-locale", locale);
    } catch {
      // Keep the current session usable when storage is unavailable.
    }
    applyLocale();
  };

  const updateTokenLabels = (theme) => {
    const values = {
      "background.brand": theme.labels.colors.brand,
      "foreground.primary": theme.labels.colors.primary,
      "stroke.focus": theme.labels.colors.focus,
      "content.inlineGap": theme.labels.dimensions.inline,
      "content.stackGap": theme.labels.dimensions.stack,
      "screen.gutter": theme.labels.dimensions.gutter,
      "shape.control": theme.labels.dimensions.control,
      "duration.fast": theme.labels.dimensions.fast,
      "duration.moderate": theme.labels.dimensions.moderate,
    };
    document.querySelectorAll("[data-token-value]").forEach((node) => {
      node.textContent = values[node.dataset.tokenValue] || "—";
    });
    const previewName = document.querySelector("[data-preview-name]");
    const previewValue = document.querySelector("[data-preview-value]");
    const previewSwatch = document.querySelector("[data-preview-swatch]");
    const brandLabel = brand === "test" ? t("themes.testBrand") : "BEEZ";
    const appearanceLabel = appearance === "dark" ? t("theme.dark") : t("theme.light");
    if (previewName) previewName.textContent = brandLabel + " · " + appearanceLabel;
    if (previewValue) previewValue.textContent = "background.brand · " + theme.labels.colors.brand;
    if (previewSwatch) previewSwatch.style.background = theme.labels.colors.brand;
  };

  const applyTheme = (nextAppearance = appearance, nextBrand = brand) => {
    appearance = nextAppearance;
    brand = nextBrand;
    currentTheme = tokenLoader.applyTheme(appearance, brand);
    updateAppearanceControls();
    updateBrandControls();
    updateTokenLabels(currentTheme);
  };

  const setupInteractions = () => {
    themeToggle?.addEventListener("click", () => {
      applyTheme(appearance === "dark" ? "light" : "dark", brand);
    });

    document.querySelectorAll("button[data-appearance]").forEach((button) => {
      button.addEventListener("click", () => applyTheme(button.dataset.appearance, brand));
    });

    document.querySelectorAll("button[data-brand]").forEach((button) => {
      button.addEventListener("click", () => applyTheme(appearance, button.dataset.brand));
    });

    document.querySelectorAll("button[data-locale-option]").forEach((button) => {
      button.addEventListener("click", () => setLocale(button.dataset.localeOption));
    });

    demoActions.forEach((button) => {
      button.addEventListener("click", () => {
        if (demoDisabled || demoLoading) return;
        demoLastAction = getDemoActionLabel(button);
        updateDemoState();
      });
    });

    demoDisabledToggle?.addEventListener("click", () => {
      demoDisabled = !demoDisabled;
      demoLoading = false;
      demoLastAction = "";
      updateDemoState();
    });

    demoLoadingToggle?.addEventListener("click", () => {
      if (demoDisabled) return;
      demoLoading = !demoLoading;
      demoLastAction = "";
      updateDemoState();
    });

    demoReset?.addEventListener("click", () => {
      demoDisabled = false;
      demoLoading = false;
      demoLastAction = "";
      updateDemoState();
    });

    menuToggle?.addEventListener("click", () => {
      const isOpen = menu?.classList.toggle("is-open") || false;
      menuToggle.setAttribute("aria-label", isOpen ? t("menu.close") : t("menu.open"));
    });

    document.querySelectorAll("[data-nav]").forEach((link) => {
      link.addEventListener("click", () => {
        menu?.classList.remove("is-open");
        menuToggle?.setAttribute("aria-label", t("menu.open"));
      });
    });

    window.addEventListener("scroll", updateActiveSection, { passive: true });
    updateActiveSection();
    updateDemoState();
    applyTheme();
  };

  applyLocale();

  if (tokenLoader) {
    tokenLoader.ready.then(setupInteractions).catch((error) => {
      console.error("BEEZ token loader failed", error);
      document.body.dataset.tokenError = "true";
    });
  }
})();
