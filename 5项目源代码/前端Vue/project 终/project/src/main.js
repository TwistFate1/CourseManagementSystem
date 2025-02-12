import Vue from 'vue'
import App from './App.vue'
// import router from './router'
import router from '@/router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import Bus from './store/Bus'
import * as echarts from 'echarts'


Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.prototype.$axios = axios
Vue.use(Bus);
Vue.prototype.$echarts = echarts

new Vue({
  router,
  store,
  render: h => h(App),
  beforeCreate() {
    Vue.prototype.$bus = this
  }
}).$mount('#app')


