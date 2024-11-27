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
    },
  },
  plugins: [
    require('flowbite/plugin')({
      charts: true,
    })
  ],
}

