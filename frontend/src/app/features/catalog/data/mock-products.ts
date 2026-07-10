import { Product } from '../models/product.model';

type Seed = {
  name: string;
  price: number;
  description: string;
  searchTerms: string[];
  badge?: string;
};

const CATEGORY_IMAGE_TAGS: Record<string, string[]> = {
  portatiles: ['laptop', 'notebook-computer'],
  tablets: ['tablet', 'ipad'],
  sobremesas: ['desktop-computer', 'computer-setup'],
  monitores: ['monitor', 'display-screen'],
  componentes: ['computer-hardware', 'motherboard'],
  'accesorios-informatica': ['computer-accessories', 'workspace-desk'],
  'impresoras-tinta': ['ink-printer', 'printer-cartridge', 'office-printer'],
  software: ['computer-software', 'software-workstation', 'coding-laptop'],
  gaming: ['pc-gaming-setup', 'gaming-keyboard', 'gaming-mouse'],
  'juegos-pc': ['pc-video-game', 'game-cover', 'gaming-controller'],
  auriculares: ['headphones', 'over-ear-headphones', 'wireless-headset'],
  raton: ['computer-mouse', 'wireless-mouse', 'ergonomic-mouse'],
  teclado: ['keyboard', 'wireless-keyboard', 'mechanical-keyboard'],
  lampara: ['desk-lamp', 'office-lamp', 'table-lamp'],
  'base-escritorio': ['desk-mat', 'keyboard-mat', 'desk-setup'],
};

const PRODUCT_IMAGE_OVERRIDES: Record<string, string> = {
  'Portatil Orion Pro 14': '/assets/products/laptops/portatil-orion-pro-14.jpg',
  'Portatil Vertex Air 13': '/assets/products/laptops/portatil-vertex-air-13.jpg',
  'Portatil Helios Creator 16': '/assets/products/laptops/portatil-helios-creator-16.jpg',
  'Portatil Nimbus Student 15': '/assets/products/laptops/portatil-nimbus-student-15.jpg',
  'Portatil Titan Game 17': '/assets/products/laptops/portatil-titan-game-17.jpg',
  'Portatil Atlas Business 14': '/assets/products/laptops/portatil-atlas-business-14.jpg',

  'Tablet Nova Tab 11': '/assets/products/tablets/anand-thakur-L2LNdjv-t9I-unsplash.jpg',
  'Tablet Orbit Lite 10': '/assets/products/tablets/henry-ascroft-7OFnb7NOvjw-unsplash.jpg',
  'Tablet Artisan Pro 12.9': '/assets/products/tablets/serwin365-0cG_yQAdYIM-unsplash.jpg',
  'Tablet Edu Kids 8': '/assets/products/tablets/sayan-majhi-vpcvWBTUHGc-unsplash.jpg',
  'Tablet Office Go 10.5': '/assets/products/tablets/roberto-nickson-hLgYtX0rPgw-unsplash.jpg',
  'Tablet Stream Plus 10': '/assets/products/tablets/roberto-nickson-TB_cvdUHUuc-unsplash.jpg',

  'Tarjeta Grafica Ember RTX': '/assets/products/componentes/david-von-diemar-kdDNEZENKGc-unsplash.jpg',
  'Procesador Nova X8': '/assets/products/componentes/seungmin-yoon-14NquY5CEqQ-unsplash.jpg',
  'Placa Base Atlas Z': '/assets/products/componentes/nat-hwcMLF374mY-unsplash.jpg',
  'RAM Pulse 32GB': '/assets/products/componentes/nizzah-khusnunnisa-1ZlfGq1PGJc-unsplash.jpg',
  'SSD Quantum NVMe 2TB': '/assets/products/componentes/abolfazl-pahlavan-OtIECxAxekI-unsplash.jpg',
  'Fuente Core 850W Gold': '/assets/products/componentes/louismoto-9ehmPYBJkyU-unsplash.jpg',

  'Webcam Orbit 4K': '/assets/products/accesorios-informatica/jakub-zerdzicki-o9srZw5wDaE-unsplash.jpg',
  'Hub USB-C Atlas 8 en 1': '/assets/products/accesorios-informatica/lasse-jensen-4nVJUZEJb3s-unsplash.jpg',
  'Alfombrilla Desk Mat Pro': '/assets/products/accesorios-informatica/mediamodifier-xqtd3wIq3pg-unsplash.jpg',
  'Soporte Portatil Lift': '/assets/products/accesorios-informatica/emiliano-cicero-lq87UxGSiEQ-unsplash.jpg',
  'Dock Thunderbolt Desk': '/assets/products/accesorios-informatica/workperch-m-jcgSZlLS0-unsplash.jpg',
  'Brazo Monitor Ergo': '/assets/products/accesorios-informatica/alienware-Hpaq-kBcYHk-unsplash.jpg',

  'Suite Office Pro 2026': '/assets/products/software/zulfugar-karimov-hvbjTynPt7A-unsplash.jpg',
  'Antivirus Shield 360': '/assets/products/software/ilgmyzin-agFmImWyPso-unsplash.jpg',
  'Editor Video Creator Studio': '/assets/products/software/al-amin-shamim-c2EoM_F13LM-unsplash.jpg',
  'IDE Dev Ultimate': '/assets/products/software/rubaitul-azad-zbILyELGOtk-unsplash.jpg',
  'Backup Cloud Sync': '/assets/products/software/rubaitul-azad-CMFglgJg3d4-unsplash.jpg',
  'ERP Small Business': '/assets/products/software/rubaitul-azad-ioVKQSBeugQ-unsplash.jpg',

  'Mouse Gaming Nova RGB': '/assets/products/gaming/angelo-moleele-ychTaerVTPA-unsplash.jpg',
  'Teclado Gaming Pulse 75': '/assets/products/gaming/christian-wiediger-WkfDrhxDMC8-unsplash.jpg',
  'Headset Raider 7.1': '/assets/products/gaming/oscar-ivan-esquivel-arteaga-ZtxED1cpB1E-unsplash.jpg',
  'Silla Gaming Ember X': '/assets/products/gaming/istockphoto-1263458593-1024x1024.jpg',
  'Monitor Gaming Arc 27': '/assets/products/gaming/gabriele-domicolo-o38CEDlUVeM-unsplash.jpg',
  'Gamepad Wireless Titan': '/assets/products/gaming/daniel-korpai-8GDCzWrcE3M-unsplash.jpg',

  'RPG Legends Eternal': '/assets/products/juegos-pc/denise-jans-zvRgcCx8Nvs-unsplash.jpg',
  'Racing Hyperdrive 2026': '/assets/products/juegos-pc/stefen-tan-KYw1eUx1J7Y-unsplash.jpg',
  'Strategy Empire Prime': '/assets/products/juegos-pc/angelo-moleele-7er5NRLUxIU-unsplash.jpg',
  'Shooter Frontline Zero': '/assets/products/juegos-pc/erik-mclean-hYY_aE3ys50-unsplash.jpg',
  'Indie Pixel Stories': '/assets/products/juegos-pc/bruno-guerrero-GPqUnd1C9zc-unsplash.jpg',
  'Simulation Builder Pro': '/assets/products/juegos-pc/matt-popovich-0FZrPECK5cg-unsplash.jpg',

  'Auriculares Ember Air': '/assets/products/auriculares/alexunder-hess-bWZAPKm0zZE-unsplash.jpg',
  'Auriculares Pulse ANC': '/assets/products/auriculares/c-d-x-PDX_a_82obo-unsplash.jpg',
  'Auriculares Nova Beats': '/assets/products/auriculares/ervo-rocks-Zam8TvEgN5o-unsplash.jpg',
  'Auriculares Studio Wire': '/assets/products/auriculares/luke-peterson-lUMj2Zv5HUE-unsplash.jpg',
  'Auriculares Orbit Bass': '/assets/products/auriculares/cosmin-ursea-0QAe85hi_Mw-unsplash.jpg',
  'Auriculares Travel Fold': '/assets/products/auriculares/tomasz-gawlowski-YDZPdqv3FcA-unsplash.jpg',

  'Raton Pro Silent': '/assets/products/raton/frankie-VghbBAYqUJ0-unsplash.jpg',
  'Raton Ember Click': '/assets/products/raton/shri-cFOqLh7ZZJw-unsplash.jpg',
  'Raton Arc Wireless': '/assets/products/raton/rebekah-yip-wMT0oiL5XjA-unsplash.jpg',
  'Raton Compact Move': '/assets/products/raton/pascal-m-4PchFKrUw84-unsplash.jpg',
  'Raton Track Master': '/assets/products/raton/maar-gaming-fG4BTSKPo3w-unsplash.jpg',
  'Raton Carbon Office': '/assets/products/raton/oscar-ivan-esquivel-arteaga-ZtxED1cpB1E-unsplash.jpg',

  'Teclado Mechanical Ember': '/assets/products/teclado/andras-vas-Bd7gNnWJBkU-unsplash.jpg',
  'Teclado Nova 75': '/assets/products/teclado/aryan-dhiman-iGLLtLINSkw-unsplash.jpg',
  'Teclado Office Slim': '/assets/products/teclado/stefen-tan-KYw1eUx1J7Y-unsplash.jpg',
  'Teclado Carbon TKL': '/assets/products/teclado/christian-wiediger-WkfDrhxDMC8-unsplash.jpg',
  'Teclado Creator Pad': '/assets/products/teclado/girl-with-red-hat-Z6SXt1v5tP8-unsplash.jpg',
  'Teclado Travel Mini': '/assets/products/teclado/clay-banks-PXaQXThG1FY-unsplash.jpg',

  'Lampara Smart Warm Desk': '/assets/products/lampara/emilio-garcia-1HcK5xQoUKQ-unsplash.jpg',
  'Lampara Aura Pro': '/assets/products/lampara/jean-philippe-delberghe-Ry9WBo3qmoc-unsplash.jpg',
  'Lampara Edge Minimal': '/assets/products/lampara/jonny-caspari-wsvCC6UyKjs-unsplash.jpg',
  'Lampara Orbit Focus': '/assets/products/lampara/phil-desforges-KP7p0-DRGbg-unsplash.jpg',
  'Lampara Studio Beam': '/assets/products/lampara/sincerely-media-VDPauwJ_sHo-unsplash.jpg',
  'Lampara Nova Clip': '/assets/products/lampara/xie-yujie-nick-xe_f__fOBNs-unsplash.jpg',

  'Base Ember Dual Dock': '/assets/products/base-escritorio/faz-islam-WWwM8VLL90s-unsplash.jpg',
  'Base Station Hub 7-en-1': '/assets/products/base-escritorio/jay-zhang-1hODsNcJ7es-unsplash.jpg',
  'Base Desk Magnetic Duo': '/assets/products/base-escritorio/jessica-lam-fq-Ks10WtWI-unsplash.jpg',
  'Base Work Dock Ultra': '/assets/products/base-escritorio/andrew-m-8fMHWkbZC54-unsplash.jpg',
  'Base Cable Manager Pro': '/assets/products/base-escritorio/scott-gummerson-Zt1j8RuCcqo-unsplash.jpg',
  'Base Ergonomic Lift Stand': '/assets/products/base-escritorio/nubelson-fernandes--Xqckh_XVU4-unsplash.jpg',
};

const hashText = (value: string): number => {
  let hash = 0;
  for (let i = 0; i < value.length; i += 1) {
    hash = (hash * 31 + value.charCodeAt(i)) % 100000;
  }
  return Math.abs(hash);
};

const buildImageUrl = (category: string, seed: Seed): string => {
  const customImage = PRODUCT_IMAGE_OVERRIDES[seed.name];
  if (customImage) {
    return customImage;
  }

  const tags = CATEGORY_IMAGE_TAGS[category] ?? ['technology'];
  const tag = tags[hashText(seed.name) % tags.length];
  const lock = hashText(`${category}-${seed.name}`);

  // LoremFlickr entrega fotos reales por etiqueta y lock mantiene URL estable.
  return `https://loremflickr.com/1200/900/${tag}?lock=${lock}`;
};

const PRODUCT_SEEDS: Record<string, Seed[]> = {
  portatiles: [
    { name: 'Portatil Orion Pro 14', price: 549, description: 'Ultrabook de alto rendimiento para trabajo avanzado.', searchTerms: ['portatil', 'laptop', 'orion'], badge: 'Top ventas' },
    { name: 'Portatil Vertex Air 13', price: 399, description: 'Ligero y silencioso para productividad diaria.', searchTerms: ['portatil', 'movilidad', 'office'] },
    { name: 'Portatil Helios Creator 16', price: 620, description: 'Equipo creator con GPU dedicada y panel preciso.', searchTerms: ['creator', 'gpu', 'diseno'], badge: 'Pro' },
    { name: 'Portatil Nimbus Student 15', price: 280, description: 'Modelo equilibrado para estudio y multimedia.', searchTerms: ['estudiante', 'basico'] },
    { name: 'Portatil Titan Game 17', price: 800, description: 'Alto rendimiento para gaming competitivo.', searchTerms: ['gaming', 'rtx', '17 pulgadas'], badge: 'Gaming' },
    { name: 'Portatil Atlas Business 14', price: 650, description: 'Seguridad empresarial y bateria extendida.', searchTerms: ['business', 'seguridad'] },
  ],
  tablets: [
    { name: 'Tablet Nova Tab 11', price: 549, description: 'Pantalla 11 pulgadas y lapiz de baja latencia.', searchTerms: ['tablet', 'lapiz'] },
    { name: 'Tablet Orbit Lite 10', price: 329, description: 'Modelo compacto para consumo multimedia.', searchTerms: ['tablet ligera', 'streaming'] },
    { name: 'Tablet Artisan Pro 12.9', price: 999, description: 'Pensada para ilustracion y creatividad.', searchTerms: ['tablet pro', 'ilustracion'], badge: 'Pro' },
    { name: 'Tablet Edu Kids 8', price: 189, description: 'Control parental y carcasa resistente.', searchTerms: ['tablet ninos', 'educacion'] },
    { name: 'Tablet Office Go 10.5', price: 459, description: 'Productividad con teclado magnetico opcional.', searchTerms: ['tablet oficina', 'teclado'] },
    { name: 'Tablet Stream Plus 10', price: 279, description: 'Audio frontal y autonomia para movilidad.', searchTerms: ['tablet multimedia', 'audio'] },
  ],
  componentes: [
    { name: 'Tarjeta Grafica Ember RTX', price: 500, description: 'GPU para gaming y render.', searchTerms: ['gpu', 'rtx'] },
    { name: 'Procesador Nova X8', price: 429, description: 'CPU de 8 nucleos para alto rendimiento.', searchTerms: ['cpu', 'procesador'] },
    { name: 'Placa Base Atlas Z', price: 279, description: 'Motherboard con conectividad moderna.', searchTerms: ['placa base', 'motherboard'] },
    { name: 'RAM Pulse 32GB', price: 159, description: 'Memoria DDR5 para multitarea exigente.', searchTerms: ['ram', 'ddr5'] },
    { name: 'SSD Quantum NVMe 2TB', price: 239, description: 'Almacenamiento rapido para cargas pesadas.', searchTerms: ['ssd', 'nvme'] },
    { name: 'Fuente Core 850W Gold', price: 169, description: 'Fuente certificada para equipos potentes.', searchTerms: ['fuente', 'psu'] },
  ],
  software: [
    { name: 'Suite Office Pro 2026', price: 19, description: 'Licencia anual de productividad.', searchTerms: ['office', 'licencia'] },
    { name: 'Antivirus Shield 360', price: 23, description: 'Proteccion en tiempo real para equipos.', searchTerms: ['antivirus', 'seguridad'] },
    { name: 'Editor Video Creator Studio', price: 15, description: 'Edicion de video para creadores.', searchTerms: ['video', 'editor'] },
    { name: 'IDE Dev Ultimate', price: 19, description: 'Herramientas para desarrollo integral.', searchTerms: ['ide', 'programacion'] },
    { name: 'Backup Cloud Sync', price: 19, description: 'Respaldo cifrado y sincronizacion en nube.', searchTerms: ['backup', 'cloud'] },
    { name: 'ERP Small Business', price: 29, description: 'Gestion para pymes y comercios.', searchTerms: ['erp', 'gestion'] },
  ],
  'juegos-pc': [
    { name: 'RPG Legends Eternal', price: 59.9, description: 'Aventura de mundo abierto cooperativa.', searchTerms: ['rpg', 'juego pc'] },
    { name: 'Strategy Empire Prime', price: 44.9, description: 'Estrategia en tiempo real multijugador.', searchTerms: ['estrategia', 'rts'] },
    { name: 'Shooter Frontline Zero', price: 69.9, description: 'FPS competitivo con ranking.', searchTerms: ['fps', 'shooter'] },
    { name: 'Indie Pixel Stories', price: 19.9, description: 'Coleccion de aventuras indie retro.', searchTerms: ['indie', 'retro'] },
    { name: 'Simulation Builder Pro', price: 39.9, description: 'Gestion y construccion de ciudades.', searchTerms: ['simulacion', 'builder'] },
  ],
  auriculares: [
    { name: 'Auriculares Ember Air', price: 119, description: 'Bluetooth multipunto para trabajo remoto.', searchTerms: ['auriculares', 'bluetooth'] },
    { name: 'Auriculares Pulse ANC', price: 149, description: 'Cancelacion de ruido para oficina.', searchTerms: ['anc', 'auriculares'] },
    { name: 'Auriculares Nova Beats', price: 89, description: 'Sonido dinamico para movilidad.', searchTerms: ['audio', 'movilidad'] },
    { name: 'Auriculares Studio Wire', price: 169, description: 'Perfil de audio neutro para edicion.', searchTerms: ['studio', 'edicion'] },
    { name: 'Auriculares Orbit Bass', price: 99, description: 'Graves marcados y gran autonomia.', searchTerms: ['bass', 'auriculares'] },
    { name: 'Auriculares Travel Fold', price: 109, description: 'Plegables para viaje y trabajo.', searchTerms: ['travel', 'foldable'] },
  ],
  raton: [
    { name: 'Raton Pro Silent', price: 55, description: 'Click silencioso para entornos compartidos.', searchTerms: ['raton', 'silencioso'] },
    { name: 'Raton Ember Click', price: 69, description: 'Respuesta rapida para productividad.', searchTerms: ['raton', 'productividad'] },
    { name: 'Raton Arc Wireless', price: 79, description: 'Conectividad dual para laptop y desktop.', searchTerms: ['wireless', 'dual'] },
    { name: 'Raton Compact Move', price: 49, description: 'Formato compacto para mochila.', searchTerms: ['compacto', 'movilidad'] },
    { name: 'Raton Track Master', price: 89, description: 'Sensor avanzado para precision.', searchTerms: ['track', 'precision'] },
    { name: 'Raton Carbon Office', price: 59, description: 'Modelo diario para oficina.', searchTerms: ['office mouse', 'raton'] },
  ],
  teclado: [
    { name: 'Teclado Mechanical Ember', price: 129, description: 'Switch tactil y estructura compacta.', searchTerms: ['teclado', 'mecanico'] },
    { name: 'Teclado Nova 75', price: 139, description: 'Formato 75% con iluminacion RGB.', searchTerms: ['teclado 75', 'rgb'] },
    { name: 'Teclado Office Slim', price: 69, description: 'Bajo perfil y escritura silenciosa.', searchTerms: ['teclado oficina', 'slim'] },
    { name: 'Teclado Carbon TKL', price: 119, description: 'Sin pad numerico para mayor espacio.', searchTerms: ['tkl', 'teclado'] },
    { name: 'Teclado Creator Pad', price: 149, description: 'Atajos dedicados para creadores.', searchTerms: ['creator', 'atajos'] },
    { name: 'Teclado Travel Mini', price: 79, description: 'Inalambrico y portable para viaje.', searchTerms: ['mini', 'wireless teclado'] },
  ],
  lampara: [
    { name: 'Lampara Smart Warm Desk', price: 62.4, description: 'Luz regulable para setup productivo.', searchTerms: ['lampara', 'desk'] },
    { name: 'Lampara Aura Pro', price: 84, description: 'Tono calido/frio para oficinas.', searchTerms: ['lampara oficina', 'aura'] },
    { name: 'Lampara Edge Minimal', price: 58, description: 'Diseno minimalista para escritorios.', searchTerms: ['minimal', 'lampara'] },
    { name: 'Lampara Orbit Focus', price: 92, description: 'Cabezal orientable y foco preciso.', searchTerms: ['focus', 'lectura'] },
    { name: 'Lampara Studio Beam', price: 109, description: 'Iluminacion uniforme para creadores.', searchTerms: ['studio light', 'streaming'] },
    { name: 'Lampara Nova Clip', price: 39, description: 'Lampara de pinza flexible.', searchTerms: ['clip', 'desk lamp'] },
  ],
  'base-escritorio': [
    { name: 'Base Ember Dual Dock', price: 49.9, description: 'Carga dual para movil y auriculares.', searchTerms: ['dock', 'base escritorio'] },
    { name: 'Base Station Hub 7-en-1', price: 69, description: 'Hub de puertos para escritorio.', searchTerms: ['hub', 'base'] },
    { name: 'Base Desk Magnetic Duo', price: 72, description: 'Base magnetica para accesorios.', searchTerms: ['magnetica', 'escritorio'] },
    { name: 'Base Work Dock Ultra', price: 88, description: 'Dock premium para oficina.', searchTerms: ['dock ultra', 'base trabajo'] },
    { name: 'Base Cable Manager Pro', price: 44, description: 'Orden y gestion de cables.', searchTerms: ['cable manager', 'orden'] },
    { name: 'Base Ergonomic Lift Stand', price: 59, description: 'Base elevadora para mejor postura.', searchTerms: ['stand', 'ergonomico'] },
  ],
};

let idSequence = 3000;

export const MOCK_PRODUCTS: Product[] = Object.entries(PRODUCT_SEEDS).flatMap(([category, seeds]) =>
  seeds.map((seed, index) => ({
    id: idSequence++,
    name: seed.name,
    category,
    price: seed.price,
    description: seed.description,
    badge: seed.badge,
    imageUrl: buildImageUrl(category, seed),
    searchTerms: seed.searchTerms,
    stock: category === 'software' || category === 'juegos-pc' ? 999 : 12 + index * 5,
  })),
);

