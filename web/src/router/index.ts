import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import home from '../views/home.vue'
import about from '../views/about.vue'
import Doc from '../views/doc.vue'
import adminUser from '../views/admin/admin-user.vue'
import adminEbook from '../views/admin/admin-ebook.vue'
import adminCategory from '../views/admin/admin-category.vue'
import adminDoc from '../views/admin/admin-doc.vue'
import store from "@/store";
import {Tool} from "@/util/tool";


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home2',
    component: home
  },
  {
    path: '/home',
    name: 'home',
    component: home
  },
  {
    path: '/about',
    name: 'about',
    component: about,
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/about.vue')
  },
  {
    path: '/doc',
    name: 'Doc',
    component: Doc
  },
  {
    path: '/admin/ebook',
    name: 'adminEbook',
    component: adminEbook,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'adminCategory',
    component: adminCategory,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: 'adminDoc',
    component: adminDoc,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/user',
    name: 'adminUser',
    component: adminUser,
    meta: {
      loginRequire: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
      next('/home');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
