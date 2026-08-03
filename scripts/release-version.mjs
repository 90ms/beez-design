import { pathToFileURL } from "node:url";

const numericIdentifier = "(?:0|[1-9]\\d*)";
const coreVersion = `${numericIdentifier}\\.${numericIdentifier}\\.${numericIdentifier}`;
const beezVersionPattern = new RegExp(
  `^${coreVersion}(?:-SNAPSHOT|-(?:alpha|beta|rc)\\.${numericIdentifier})?$`,
);

export function isBeezVersion(version) {
  return beezVersionPattern.test(version);
}

export function validateBeezVersion(version) {
  if (!isBeezVersion(version)) {
    throw new Error(
      `Unsupported BEEZ version: ${version}. `
      + "Use x.y.z-SNAPSHOT, x.y.z-alpha.n, x.y.z-beta.n, x.y.z-rc.n, or x.y.z.",
    );
  }

  return version;
}

const invokedAsScript = process.argv[1]
  && import.meta.url === pathToFileURL(process.argv[1]).href;

if (invokedAsScript) {
  try {
    const version = validateBeezVersion(process.argv[2] ?? "");
    console.log(`Validated BEEZ version ${version}.`);
  } catch (error) {
    console.error(error.message);
    process.exitCode = 1;
  }
}
