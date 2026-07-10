import { existsSync, readFileSync } from 'node:fs';
import { resolve } from 'node:path';

const thresholdArg = process.argv[2] ?? '0.4';
const threshold = Number(thresholdArg);
const candidates = [
  resolve(process.cwd(), 'coverage/frontend/lcov.info'),
  resolve(process.cwd(), 'coverage/lcov.info'),
];

const lcovPath = candidates.find((candidate) => existsSync(candidate));

const htmlCandidates = [
  resolve(process.cwd(), 'coverage/frontend/app/index.html'),
  resolve(process.cwd(), 'coverage/frontend/index.html'),
  resolve(process.cwd(), 'coverage/index.html'),
];

const htmlPath = htmlCandidates.find((candidate) => existsSync(candidate));

if (Number.isNaN(threshold) || threshold <= 0 || threshold > 1) {
  console.error('El umbral debe ser un valor decimal entre 0 y 1. Ejemplo: 0.4');
  process.exit(1);
}

let totalLines = 0;
let coveredLines = 0;

if (lcovPath) {
  const content = readFileSync(lcovPath, 'utf8');

  for (const line of content.split(/\r?\n/)) {
    if (line.startsWith('LF:')) {
      totalLines += Number(line.slice(3));
    }
    if (line.startsWith('LH:')) {
      coveredLines += Number(line.slice(3));
    }
  }
} else if (htmlPath) {
  const content = readFileSync(htmlPath, 'utf8');
  const lineMatch = content.match(/<span class=["']strong["']>([0-9.]+)%<\/span>\s*<span class=["']quiet["']>Lines<\/span>\s*<span class=["']fraction["']>(\d+)\/(\d+)<\/span>/s);

  if (!lineMatch) {
    console.error(`No se pudo extraer cobertura desde ${htmlPath}`);
    process.exit(1);
  }

  coveredLines = Number(lineMatch[2]);
  totalLines = Number(lineMatch[3]);
} else {
  console.error('No se encontro un archivo de cobertura. Ejecuta primero npm run test:coverage.');
  process.exit(1);
}

if (totalLines === 0) {
  console.error(`No se pudieron calcular lineas de cobertura desde ${lcovPath ?? htmlPath}`);
  process.exit(1);
}

const coverage = coveredLines / totalLines;
const coveragePercent = (coverage * 100).toFixed(2);
const thresholdPercent = (threshold * 100).toFixed(2);

console.log(`Coverage lineal detectada: ${coveragePercent}% (umbral: ${thresholdPercent}%)`);

if (coverage < threshold) {
  console.error(`La cobertura ${coveragePercent}% esta por debajo del umbral minimo ${thresholdPercent}%.`);
  process.exit(1);
}
