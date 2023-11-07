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
    createPlaylist: path.resolve(__dirname, 'src', 'pages', 'createPlaylist.js'),
    viewPlaylist: path.resolve(__dirname, 'src', 'pages', 'viewPlaylist.js'),
    searchPlaylists: path.resolve(__dirname, 'src', 'pages', 'searchPlaylists.js'),
    test: path.resolve(__dirname, 'src', 'pages', 'test.js'),
    viewAllEvents: path.resolve(__dirname, 'src', 'pages', 'viewAllEvents.js'),
    viewAllVendors: path.resolve(__dirname, 'src', 'pages', 'viewAllVendors.js'),
    oneEvent: path.resolve(__dirname, 'src', 'pages', 'oneEvent.js'),
    viewVendor: path.resolve(__dirname, 'src', 'pages', 'viewVendor.js'),
    index: path.resolve(__dirname, 'src', 'pages', 'index.js'),
    createVendor: path.resolve(__dirname, 'src', 'pages', 'createVendor.js'),
    VendorAccount: path.resolve(__dirname, 'src', 'pages', 'VendorAccount.js'),
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