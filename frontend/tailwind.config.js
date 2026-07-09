/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#f7eee7',
          100: '#efd9c8',
          200: '#e2b78a',
          300: '#d59763',
          400: '#c97a3d',
          500: '#b56c34',
          600: '#9b5b2c',
          700: '#7d4923',
          800: '#60371a',
          900: '#452513'
        },
        ink: '#f4ede6',
        paper: '#14110f',
        surface: '#1e1a17',
        card: '#2a241f',
        line: '#3a322c',
        muted: '#c9b8a7',
        success: '#7fa36b',
        danger: '#c65a4b'
      },
      fontFamily: {
        display: ['"Space Grotesk"', 'sans-serif'],
        body: ['"Manrope"', 'sans-serif']
      },
      boxShadow: {
        soft: '0 20px 48px -30px rgba(6, 4, 3, 0.75)'
      }
    },
  },
  plugins: [],
}

