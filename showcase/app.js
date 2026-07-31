(() => {
  const root = document.documentElement;
  const menu = document.querySelector("[data-sidebar]");
  const menuToggle = document.querySelector("[data-menu-toggle]");
  const themeToggle = document.querySelector("[data-theme-toggle]");
  const themeLabel = document.querySelector("[data-theme-label]");
  const themeIcon = document.querySelector("[data-theme-icon]");

  const setTheme = (theme) => {
    root.dataset.theme = theme;
    if (themeLabel) themeLabel.textContent = theme === "dark" ? "Dark" : "Light";
    if (themeIcon) themeIcon.textContent = theme === "dark" ? "☾" : "☼";
  };

  setTheme(root.dataset.theme || "light");

  themeToggle?.addEventListener("click", () => {
    setTheme(root.dataset.theme === "dark" ? "light" : "dark");
  });

  menuToggle?.addEventListener("click", () => {
    menu?.classList.toggle("is-open");
  });

  document.querySelectorAll("[data-nav]").forEach((link) => {
    link.addEventListener("click", () => menu?.classList.remove("is-open"));
  });

  const sections = [...document.querySelectorAll(".section-anchor")];
  const navLinks = [...document.querySelectorAll("[data-nav]")];
  const updateActiveSection = () => {
    const current = sections
      .filter((section) => section.getBoundingClientRect().top <= 180)
      .at(-1);
    navLinks.forEach((link) => link.classList.toggle("is-active", link.dataset.nav === current?.id));
  };

  window.addEventListener("scroll", updateActiveSection, { passive: true });
  updateActiveSection();
})();
