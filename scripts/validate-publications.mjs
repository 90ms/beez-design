import { createHash } from "node:crypto";
import {
  existsSync,
  readFileSync,
  readdirSync,
} from "node:fs";
import { join } from "node:path";
import { pathToFileURL } from "node:url";
import { validateBeezVersion } from "./release-version.mjs";

const groupPath = join("beez", "design");
const libraries = ["beez-components", "beez-foundation", "beez-tokens"];
const targetSuffixes = [
  "",
  "-android",
  "-desktop",
  "-iosarm64",
  "-iossimulatorarm64",
  "-wasm-js",
];
const forbiddenMaterialDependency = /(?:androidx\.compose\.material|org\.jetbrains\.compose\.material|material3)/i;

export function expectedPublicationNames() {
  return libraries.flatMap((library) => (
    targetSuffixes.map((suffix) => `${library}${suffix}`)
  ));
}

export function expectedMainArtifactExtension(publication) {
  if (publication.endsWith("-android")) return "aar";
  if (
    publication.endsWith("-iosarm64")
    || publication.endsWith("-iossimulatorarm64")
    || publication.endsWith("-wasm-js")
  ) return "klib";
  return "jar";
}

export function expectedBeezDependency(publication) {
  const suffix = targetSuffixes.findLast((candidate) => (
    candidate && publication.endsWith(candidate)
  )) ?? "";

  if (publication.startsWith("beez-components")) {
    return `beez-foundation${suffix}`;
  }
  if (publication.startsWith("beez-foundation")) {
    return `beez-tokens${suffix}`;
  }
  return null;
}

export function containsForbiddenMaterialDependency(metadata) {
  return forbiddenMaterialDependency.test(metadata);
}

function requireFile(path) {
  if (!existsSync(path)) {
    throw new Error(`Missing publication file: ${path}`);
  }
}

function verifySha256(path) {
  const checksumPath = `${path}.sha256`;
  requireFile(checksumPath);

  const expected = readFileSync(checksumPath, "utf8").trim();
  const actual = createHash("sha256").update(readFileSync(path)).digest("hex");
  if (actual !== expected) {
    throw new Error(`SHA-256 mismatch for ${path}`);
  }
}

function verifyPom(pom, publication, version) {
  const coordinate = [
    "<groupId>beez.design</groupId>",
    `<artifactId>${publication}</artifactId>`,
    `<version>${version}</version>`,
  ];
  coordinate.forEach((part) => {
    if (!pom.includes(part)) {
      throw new Error(`POM identity mismatch for ${publication}: ${part}`);
    }
  });

  const dependency = expectedBeezDependency(publication);
  if (dependency) {
    const escapedVersion = version.replaceAll(".", "\\.");
    const dependencyPattern = new RegExp(
      `<dependency>[\\s\\S]*?<groupId>beez\\.design</groupId>`
      + `[\\s\\S]*?<artifactId>${dependency}</artifactId>`
      + `[\\s\\S]*?<version>${escapedVersion}</version>[\\s\\S]*?</dependency>`,
    );
    if (!dependencyPattern.test(pom)) {
      throw new Error(`Missing aligned dependency ${dependency}:${version} in ${publication} POM`);
    }
  }
}

function verifyModule(moduleMetadata, publication, version) {
  const module = JSON.parse(moduleMetadata);
  const rootPublication = libraries.find((library) => publication.startsWith(library));
  const expected = {
    group: "beez.design",
    module: rootPublication,
    version,
  };

  Object.entries(expected).forEach(([key, value]) => {
    if (module.component?.[key] !== value) {
      throw new Error(`Gradle module identity mismatch for ${publication}: ${key}`);
    }
  });

  if (publication !== rootPublication) {
    const expectedUrl = `../../${rootPublication}/${version}/${rootPublication}-${version}.module`;
    if (module.component?.url !== expectedUrl) {
      throw new Error(`Gradle module root redirect mismatch for ${publication}`);
    }
  }
}

export function validatePublicationRepository(repositoryRoot, version) {
  validateBeezVersion(version);
  const publicationRoot = join(repositoryRoot, groupPath);
  const expected = expectedPublicationNames().sort();
  const actual = readdirSync(publicationRoot, { withFileTypes: true })
    .filter((entry) => entry.isDirectory())
    .map((entry) => entry.name)
    .sort();

  if (JSON.stringify(actual) !== JSON.stringify(expected)) {
    throw new Error(
      `Publication set mismatch. Expected ${expected.join(", ")}; found ${actual.join(", ")}`,
    );
  }

  expected.forEach((publication) => {
    const versionRoot = join(publicationRoot, publication, version);
    const baseName = `${publication}-${version}`;
    const pomPath = join(versionRoot, `${baseName}.pom`);
    const modulePath = join(versionRoot, `${baseName}.module`);
    const sourcePath = join(versionRoot, `${baseName}-sources.jar`);
    const artifactPath = join(
      versionRoot,
      `${baseName}.${expectedMainArtifactExtension(publication)}`,
    );

    [pomPath, modulePath, sourcePath, artifactPath].forEach((path) => {
      requireFile(path);
      verifySha256(path);
    });

    const pom = readFileSync(pomPath, "utf8");
    const moduleMetadata = readFileSync(modulePath, "utf8");
    if (containsForbiddenMaterialDependency(`${pom}\n${moduleMetadata}`)) {
      throw new Error(`Material dependency found in ${publication} metadata`);
    }

    verifyPom(pom, publication, version);
    verifyModule(moduleMetadata, publication, version);
  });

  return {
    publications: expected.length,
    pomFiles: expected.length,
    moduleFiles: expected.length,
    checksums: expected.length * 4,
  };
}

const invokedAsScript = process.argv[1]
  && import.meta.url === pathToFileURL(process.argv[1]).href;

if (invokedAsScript) {
  try {
    const repositoryRoot = process.argv[2] ?? "";
    const version = process.argv[3] ?? "";
    const result = validatePublicationRepository(repositoryRoot, version);
    console.log(
      `Validated ${result.publications} BEEZ publications, `
      + `${result.pomFiles} POM files, ${result.moduleFiles} Gradle module files, `
      + `and ${result.checksums} SHA-256 checksums for ${version}.`,
    );
  } catch (error) {
    console.error(error.message);
    process.exitCode = 1;
  }
}
