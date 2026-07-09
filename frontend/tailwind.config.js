/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#eff8ff',
          100: '#dbeeff',
          200: '#bfdfff',
          300: '#93c8ff',
          400: '#61a5fa',
          500: '#3b82f6',
          600: '#1f5ecf',
          700: '#1d4ca7',
          800: '#1e3f87',
          900: '#1f366f'
        },
        ink: '#101828',
        paper: '#f8fafc'
      },
      fontFamily: {
        display: ['"Space Grotesk"', 'sans-serif'],
        body: ['"Manrope"', 'sans-serif']
      },
      boxShadow: {
        soft: '0 20px 45px -28px rgba(16, 24, 40, 0.45)'
      }
    },
  },
  plugins: [],
}

