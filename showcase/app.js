(() => {
  const root = document.documentElement;
  const menu = document.querySelector("[data-sidebar]");
  const menuToggle = document.querySelector("[data-menu-toggle]");
  const themeToggle = document.querySelector("[data-theme-toggle]");
  const themeLabel = document.querySelector("[data-theme-label]");
  const themeIcon = document.querySelector("[data-theme-icon]");
  const tokenLoader = window.BeezTokenLoader;
  let appearance = root.dataset.theme || "light";
  let brand = root.dataset.brand || "beez";

  const sections = [...document.querySelectorAll(".section-anchor")];
  const navLinks = [...document.querySelectorAll("[data-nav]")];

  const updateActiveSection = () => {
    const visibleSections = sections.filter((section) => section.getBoundingClientRect().top <= 180);
    const current = visibleSections[visibleSections.length - 1];
    navLinks.forEach((link) => link.classList.toggle("is-active", link.dataset.nav === current?.id));
  };

  const updateAppearanceControls = () => {
    document.querySelectorAll("[data-appearance]").forEach((button) => {
      button.classList.toggle("is-selected", button.dataset.appearance === appearance);
      button.setAttribute("aria-pressed", String(button.dataset.appearance === appearance));
    });
    if (themeLabel) themeLabel.textContent = appearance === "dark" ? "Dark" : "Light";
    if (themeIcon) themeIcon.textContent = appearance === "dark" ? "☾" : "☼";
  };

  const updateBrandControls = () => {
    document.querySelectorAll("[data-brand]").forEach((button) => {
      button.classList.toggle("is-selected", button.dataset.brand === brand);
      button.setAttribute("aria-pressed", String(button.dataset.brand === brand));
    });
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
    if (previewName) previewName.textContent = theme.labels.brand + " · " + theme.labels.appearance;
    if (previewValue) previewValue.textContent = "background.brand · " + theme.labels.colors.brand;
    if (previewSwatch) previewSwatch.style.background = theme.labels.colors.brand;
  };

  const applyTheme = (nextAppearance = appearance, nextBrand = brand) => {
    appearance = nextAppearance;
    brand = nextBrand;
    const theme = tokenLoader.applyTheme(appearance, brand);
    updateAppearanceControls();
    updateBrandControls();
    updateTokenLabels(theme);
  };

  const setupInteractions = () => {
    themeToggle?.addEventListener("click", () => {
      applyTheme(appearance === "dark" ? "light" : "dark", brand);
    });

    document.querySelectorAll("[data-appearance]").forEach((button) => {
      button.addEventListener("click", () => applyTheme(button.dataset.appearance, brand));
    });

    document.querySelectorAll("[data-brand]").forEach((button) => {
      button.addEventListener("click", () => applyTheme(appearance, button.dataset.brand));
    });

    menuToggle?.addEventListener("click", () => {
      menu?.classList.toggle("is-open");
    });

    document.querySelectorAll("[data-nav]").forEach((link) => {
      link.addEventListener("click", () => menu?.classList.remove("is-open"));
    });

    window.addEventListener("scroll", updateActiveSection, { passive: true });
    updateActiveSection();
    applyTheme();
  };

  if (tokenLoader) {
    tokenLoader.ready.then(setupInteractions).catch((error) => {
      console.error("BEEZ token loader failed", error);
      document.body.dataset.tokenError = "true";
    });
  }
})();
