<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined />
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="c1 in categorysTree" :key="c1.id">
          <template #title>
              <span>
                <user-outlined />
                {{c1.name}}
              </span>
          </template>
          <a-menu-item v-for="c2 in c1.children" :key="c2.id">{{c2.name}}</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎使用橘猫知识库</h1>
      </div>
<!--      如果需要分页在这里加 :pagination="pagination"-->
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{gutter: 20 ,column: 3}" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span>
            <component v-bind:is="'FileOutlined'" style="margin-right: 8px" />
            {{ item.docCount }}
          </span>
          <span>
            <component v-bind:is="'UserOutlined'" style="margin-right: 8px" />
            {{ item.viewCount }}
          </span>
          <span>
            <component v-bind:is="'LikeOutlined'" style="margin-right: 8px" />
            {{ item.voteCount }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId='+item.id">
                  {{item.name}}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover" />
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, } from 'vue';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import axios from 'axios';
//import { message } from 'ant-design-vue';
import { Tool } from '@/util/tool';

export default defineComponent({
  name: 'Home',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  setup() {
    console.log("setup");
    const isShowWelcome = ref(true);
    const ebooks = ref();//响应式数据，可以动态修改这里面的值，需要实时反馈到页面上
    const categorys = ref();
    let categoryId2 = 0;
    const categorysTree = ref();

    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if(data.success){
          categorys.value = data.content;
          console.log("原始数组：",data.content);

          categorysTree.value = [];
          categorysTree.value = Tool.array2Tree(categorys.value,0);
          console.log("树形数组：",categorysTree.value);

        } else {
          console.log(data.message);
        }
      });
    };

    const handleQueryEbook = () => {
      axios.get("/ebook/list",{
        params: {
          page: 1,
          size: 1000,
          categoryId2: categoryId2
        }
      }).then(function (response){
        const data = response.data;
        ebooks.value = data.content.list;
      });
    };

    const handleClick = (value: any) => {
      console.log("menu click",value);
      if(value.key === 'welcome'){
        isShowWelcome.value = true;
      } else {
        isShowWelcome.value = false;
        categoryId2 = value.key;
        handleQueryEbook();
      }
    };

    onMounted(function (){
      console.log("onMounted");
      handleQueryCategory();
    });
    return {
      ebooks,

      //分类相关
      categorys,
      categorysTree,
      handleQueryCategory,
      handleClick,

      //分页相关
      isShowWelcome
    };
  },
});
</script>

<style scoped>
  .ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>