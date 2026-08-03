import { readdirSync, readFileSync, statSync } from "node:fs";
import { join } from "node:path";

function assertRange(buffer, offset, length, context) {
  if (offset < 0 || length < 0 || offset + length > buffer.length) {
    throw new Error(`Invalid ${context} range at ${offset} (${length} bytes).`);
  }
}

function findTable(buffer, tag) {
  assertRange(buffer, 0, 12, "sfnt header");
  const tableCount = buffer.readUInt16BE(4);
  assertRange(buffer, 12, tableCount * 16, "sfnt table directory");

  for (let index = 0; index < tableCount; index += 1) {
    const recordOffset = 12 + index * 16;
    if (buffer.toString("ascii", recordOffset, recordOffset + 4) !== tag) continue;

    const offset = buffer.readUInt32BE(recordOffset + 8);
    const length = buffer.readUInt32BE(recordOffset + 12);
    assertRange(buffer, offset, length, `${tag} table`);
    return { offset, length };
  }

  throw new Error(`Font does not contain a ${tag} table.`);
}

function addFormat4CodePoints(buffer, offset, availableLength, codePoints) {
  assertRange(buffer, offset, 14, "cmap format 4 header");
  const length = buffer.readUInt16BE(offset + 2);
  if (length > availableLength) throw new Error("cmap format 4 exceeds its table boundary.");

  const segmentCount = buffer.readUInt16BE(offset + 6) / 2;
  const endCodesOffset = offset + 14;
  const startCodesOffset = endCodesOffset + segmentCount * 2 + 2;
  const deltasOffset = startCodesOffset + segmentCount * 2;
  const rangeOffsetsOffset = deltasOffset + segmentCount * 2;
  assertRange(buffer, rangeOffsetsOffset, segmentCount * 2, "cmap format 4 segments");

  for (let segment = 0; segment < segmentCount; segment += 1) {
    const endCode = buffer.readUInt16BE(endCodesOffset + segment * 2);
    const startCode = buffer.readUInt16BE(startCodesOffset + segment * 2);
    const delta = buffer.readInt16BE(deltasOffset + segment * 2);
    const rangeOffsetAddress = rangeOffsetsOffset + segment * 2;
    const rangeOffset = buffer.readUInt16BE(rangeOffsetAddress);

    if (startCode > endCode) continue;
    for (let codePoint = startCode; codePoint <= endCode && codePoint !== 0xFFFF; codePoint += 1) {
      let glyphId;
      if (rangeOffset === 0) {
        glyphId = (codePoint + delta) & 0xFFFF;
      } else {
        const glyphAddress = rangeOffsetAddress + rangeOffset + (codePoint - startCode) * 2;
        if (glyphAddress + 2 > offset + length) continue;
        glyphId = buffer.readUInt16BE(glyphAddress);
        if (glyphId !== 0) glyphId = (glyphId + delta) & 0xFFFF;
      }
      if (glyphId !== 0) codePoints.add(codePoint);
    }
  }
}

function addFormat12CodePoints(buffer, offset, availableLength, codePoints) {
  assertRange(buffer, offset, 16, "cmap format 12 header");
  const length = buffer.readUInt32BE(offset + 4);
  if (length > availableLength) throw new Error("cmap format 12 exceeds its table boundary.");

  const groupCount = buffer.readUInt32BE(offset + 12);
  assertRange(buffer, offset + 16, groupCount * 12, "cmap format 12 groups");
  for (let group = 0; group < groupCount; group += 1) {
    const groupOffset = offset + 16 + group * 12;
    const startCode = buffer.readUInt32BE(groupOffset);
    const endCode = buffer.readUInt32BE(groupOffset + 4);
    const startGlyphId = buffer.readUInt32BE(groupOffset + 8);
    for (let codePoint = startCode; codePoint <= endCode; codePoint += 1) {
      if (startGlyphId + codePoint - startCode !== 0) codePoints.add(codePoint);
    }
  }
}

export function readFontCodePoints(fontPath) {
  const buffer = readFileSync(fontPath);
  const cmap = findTable(buffer, "cmap");
  assertRange(buffer, cmap.offset, 4, "cmap header");
  const recordCount = buffer.readUInt16BE(cmap.offset + 2);
  assertRange(buffer, cmap.offset + 4, recordCount * 8, "cmap encoding records");

  const codePoints = new Set();
  const parsedOffsets = new Set();
  for (let index = 0; index < recordCount; index += 1) {
    const recordOffset = cmap.offset + 4 + index * 8;
    const subtableRelativeOffset = buffer.readUInt32BE(recordOffset + 4);
    if (parsedOffsets.has(subtableRelativeOffset)) continue;
    parsedOffsets.add(subtableRelativeOffset);

    const subtableOffset = cmap.offset + subtableRelativeOffset;
    const availableLength = cmap.length - subtableRelativeOffset;
    assertRange(buffer, subtableOffset, 2, "cmap subtable");
    const format = buffer.readUInt16BE(subtableOffset);
    if (format === 4) addFormat4CodePoints(buffer, subtableOffset, availableLength, codePoints);
    if (format === 12) addFormat12CodePoints(buffer, subtableOffset, availableLength, codePoints);
  }

  if (codePoints.size === 0) throw new Error("Font has no supported cmap format 4 or 12 entries.");
  return codePoints;
}

function sourceFiles(root) {
  const files = [];
  for (const entry of readdirSync(root)) {
    const path = join(root, entry);
    if (statSync(path).isDirectory()) files.push(...sourceFiles(path));
    if (entry.endsWith(".kt") && statSync(path).isFile()) files.push(path);
  }
  return files;
}

export function collectCatalogCodePoints(sourceRoot) {
  const codePoints = new Set();
  for (const path of sourceFiles(sourceRoot)) {
    for (const character of readFileSync(path, "utf8")) codePoints.add(character.codePointAt(0));
  }
  return codePoints;
}

export function isHangulCodePoint(codePoint) {
  return (
    (codePoint >= 0x1100 && codePoint <= 0x11FF) ||
    (codePoint >= 0x3130 && codePoint <= 0x318F) ||
    (codePoint >= 0xA960 && codePoint <= 0xA97F) ||
    (codePoint >= 0xAC00 && codePoint <= 0xD7A3) ||
    (codePoint >= 0xD7B0 && codePoint <= 0xD7FF)
  );
}

export function requiredHangulCodePoints(sourceRoot) {
  return new Set([...collectCatalogCodePoints(sourceRoot)].filter(isHangulCodePoint));
}

export function missingFontCodePoints(required, supported) {
  return [...required].filter((codePoint) => !supported.has(codePoint)).sort((a, b) => a - b);
}

export function formatCodePoints(codePoints) {
  return codePoints.map((codePoint) => `U+${codePoint.toString(16).toUpperCase().padStart(4, "0")}`).join(" ");
}
