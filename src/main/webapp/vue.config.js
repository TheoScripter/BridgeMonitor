const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
    port: 8081
  },

  chainWebpack: (config) => {
    config.module
        .rule('geojson')
        .test(/\.geojson$/)
        .use('json5-loader')
        .loader('json5-loader')
        .end();
  }
})
