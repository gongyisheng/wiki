<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
        <a-menu-item key="/home">
          <router-link to="/home">首页</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/user" :style="user.id? {} : {display:'none'}">
          <router-link to="/admin/user">用户管理</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/ebook" :style="user.id? {} : {display:'none'}">
          <router-link to="/admin/ebook">电子书管理</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/category" :style="user.id? {} : {display:'none'}">
          <router-link to="/admin/category">分类管理</router-link>
        </a-menu-item>
        <a-menu-item key="/about">
          <router-link to="/about">关于我们</router-link>
        </a-menu-item>
      <a class="login-menu" v-show="user.id">
        <div>您好：{{user.nickname}}</div>
      </a>
      <a-popconfirm
          title="确认退出登录？"
          ok-text="是"
          cancel-text="否"
          @confirm="handleLogout"
      >
        <a class="login-menu" v-show="user.id">
          <div>退出登录</div>
        </a>
      </a-popconfirm>
      <a class="login-menu" v-show="!user.id" @click="showLoginModal">
        <div>登录</div>
      </a>

    </a-menu>

    <a-modal
        title="登录表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleLogin"
    >
    <a-form :model="loginUser" :label-col="{ span:6 }" :wrapper-col="wrapperCol">
      <a-form-item label="用户名">
        <a-input v-model:value="loginUser.loginName" />
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="loginUser.password" type="password" />
      </a-form-item>
    </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from "@/util/tool";
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup() {
    //登录后保存的user
    const user = computed(() => store.state.user);

    //用来登录的user
    const loginUser = ref({
      loginName: "test1",
      password: "test123"
    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const showLoginModal = () => {
      modalVisible.value = true;
    };

    //登录
    const handleLogin = () => {
      console.log("开始登录");
      modalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password+KEY);
      axios.post('user/login',loginUser.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if(data.success){
          store.commit("setUser",data.content);
          message.success("登录成功");
          console.log("用户登录信息：",user.value);
        } else {
          message.error(data.message);
        }
      })
      modalVisible.value = false;
    };

    //退出登录
    const handleLogout = () => {
      console.log("开始退出登录");
      axios.get('user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if(data.success){
          message.success("已退出登录");
          console.log("退出登录成功");
          store.commit("setUser",{});
        } else {
          message.error(data.message);
        }
      })
      modalVisible.value = false;
    };

    return {
      user,
      loginUser,
      modalVisible,
      modalLoading,
      showLoginModal,
      handleLogin,
      handleLogout
    }
  }
});
</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 28px 16px 0;
  float: left;
  color: white;
  font-size: 18px;
}
.login-menu {
  float: right;
  color: white;
  padding-left: 20px;
  padding-right: 20px;
}
</style>
