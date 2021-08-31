<template>
    <a-layout>
      <a-layout-content :style="{background: '#fff', padding: '24px', margin: 0, minHeight: '280px'}">
        <h3 v-if="docsTree.length==0">这本电子书还没有文档</h3>
        <a-row>
          <a-col :span="6">
            <a-tree
                v-if="docsTree.length > 0"
                :tree-data="docsTree"
                @select="onSelect"
                :replaceFields="{title: 'name',key: 'id', value: 'id'}"
                :defaultExpandAll="true"
                :defaultSelectedKeys="defaultSelectedKeys"
            >
            </a-tree>
          </a-col>
          <a-col :span="18">
            <div>
              <h2>{{doc.name}}</h2>
              <div>
                <span>阅读数：{{doc.viewCount}}</span> &nbsp; &nbsp;
                <span>点赞数：{{doc.voteCount}}</span>
              </div>
              <a-divider style="height: 2px; background-color: #9999cc"/>
            </div>
            <div class="wangeditorStyle" :innerHTML="html"></div>
            <div class="vote-div">
              <a-button type="primary" shape="round" :size="'large'" @click="vote">
                <template #icon><LikeOutlined /> &nbsp;点赞：{{doc.voteCount}} </template>
              </a-button>
            </div>
          </a-col>
        </a-row>
      </a-layout-content>
    </a-layout>
</template>

<script lang = 'ts'>
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from "@/util/tool";
import { useRoute } from "vue-router";

export default defineComponent({
  name: 'Doc',
  setup() {
    //变量
    const param = ref();
    param.value = {};
    //当前选中的文档信息
    const doc = ref();
    doc.value = {};

    const route = useRoute();
    console.log("路由:",route);

    const html = ref();
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      {
        title: '操作',
        key: 'action',
        slots: { customRender: 'action' }//渲染成action
      }
    ];

    //数据查询
    const handleQuery = () => {
      axios.get("/doc/all/"+route.query.ebookId).then((response) => {
        const data = response.data;

        if(data.success){
          docs.value = data.content;
          console.log("原始数组：",docs.value);

          docsTree.value = [];
          docsTree.value = Tool.array2Tree(docs.value,0);
          console.log("树形结构：",docsTree.value);
          if(Tool.isNotEmpty(docsTree.value)){
            defaultSelectedKeys.value = [docsTree.value[0].id];
            handleQueryContent(docsTree.value[0].id);
            //初始显示文档信息
            doc.value = docsTree.value[0];
          }
        } else {
          message.error(data.message);
        }
      });
    };

    //内容查询
    const handleQueryContent = (id: number) => {
      axios.get("/doc/content/"+id).then((response) => {
        const data = response.data;
        if(data.success){
          console.log("内容：",data.content);
          html.value = data.content;
        } else {
          message.error(data.message);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      console.log("selected:",selectedKeys,info);
      if(Tool.isNotEmpty(selectedKeys)){
        //选中某一节点时，加载该节点的文档信息
        doc.value = info.selectedNodes[0].props;
        //加载内容
        handleQueryContent(selectedKeys[0]);
      }
    }

    // 点赞
    const vote = () => {
      axios.get('/doc/vote/' + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          doc.value.voteCount++;
          message.success("点赞成功");
        } else {
          message.error(data.message);
        }
      });
    };


    //------表单------
    //原始数据和原始树形结构
    const docs = ref();
    const docsTree = ref();
    docsTree.value = [];
    //树选择组件的属性状态会随当前修改节点的状态而变化，所以需要单独声明一个变量
    const treeSelectData = ref();
    treeSelectData.value = [];


    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      docs,
      docsTree,
      treeSelectData,
      columns,
      onSelect,
      vote,
      html,
      doc,
      defaultSelectedKeys
    }
  }
});
</script>

<style>
/* wangeditor样式
参考https://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html*/
/* table 样式 */
.wangeditorStyle table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangeditorStyle table td,
.wangeditorStyle table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangeditorStyle table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditorStyle blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditorStyle code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangeditorStyle pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditorStyle ul, ol {
  margin: 10px 0 10px 20px;
}
/*和ant design vue里的p冲突，去掉*/
.wangeditorStyle blockquote p {
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}
/* 点赞 */
.vote-div {
  padding: 15px;
  text-align: center;
}
</style>
