/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
    "./node_modules/flowbite/**/*.js" 
  ],
  theme: {
    extend: {
      colors: {
        backgroundColour: "#fdfdfd",
        primaryText: "#14214a",
        btn: "#1f3372",
        footer: "#f4f4f4"
      },
      animation: {
        'spin-slow': 'spin 12s linear infinite',
        'spin-medium': 'spin 16s linear infinite',
        'spin-fast': 'spin 20s linear infinite',
      },
    },
  },
  plugins: [
    require('flowbite/plugin')({
      charts: true,
    })
  ],
}

