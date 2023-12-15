const path = require('path');
const CopyPlugin = require("copy-webpack-plugin");
const Dotenv = require('dotenv-webpack');

// Get the name of the appropriate environment variable (`.env`) file for this build/run of the app
const dotenvFile = process.env.API_LOCATION ? `.env.${process.env.API_LOCATION}` : '.env';

module.exports = {
  plugins: [
    new CopyPlugin({
      patterns: [
        {
          from: "static_assets", to: "../",
          globOptions: {
            ignore: ["**/.DS_Store"],
          },
        },
      ],
    }),
    new Dotenv({ path: dotenvFile }),
  ],
  optimization: {
    usedExports: true
  },
  entry: {
    index: path.resolve(__dirname, 'src', 'pages', 'index.js'),
    createSeller: path.resolve(__dirname, 'src', 'pages', 'createSeller.js'),
    createBuyer: path.resolve(__dirname, 'src', 'pages', 'createBuyer.js'),
    viewAllGarages: path.resolve(__dirname, 'src', 'pages', 'ViewAllGarages.js'),
    viewOneGarage: path.resolve(__dirname, 'src', 'pages', 'viewOneGarage.js'),
    mySellerAccount: path.resolve(__dirname, 'src', 'pages', 'mySellerAccount.js'),
    myBuyerAccount: path.resolve(__dirname, 'src', 'pages', 'myBuyerAccount.js'),
    createGarage: path.resolve(__dirname, 'src', 'pages', 'createGarage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'build', 'assets'),
    filename: '[name].js',
    publicPath: '/assets/'
  },
  devServer: {
    static: {
      directory: path.join(__dirname, 'static_assets'),
    },
    port: 8000,
    client: {
      // overlay shows a full-screen overlay in the browser when there are js compiler errors or warnings
      overlay: true,
    },
  }
}
