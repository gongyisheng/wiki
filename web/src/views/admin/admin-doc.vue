<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px'}"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-input-search
                    v-model:value="param.name"
                    placeholder="输入文档名"
                    enter-button
                    size="medium"
                    @search="handleQuery()"
                />
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="add()" size="medium">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
              v-if="docsTree.length>0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="docsTree"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #cover="{ text, record }">
              {{record.sort}}{{text}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认删除？"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="showIfDeleteDoubleConfirm(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave">
                  保存
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="handlePreview()">
                  <EyeOutlined /> 内容预览
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="docs" :label-col="labelCol" :wrapper-col="wrapperCol" layout="vertical">
            <a-form-item label="名称">
              <a-input v-model:value="doc.name" placeholder="请输入文档名称"/>
            </a-form-item>
            <a-form-item label="父文档">
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :replaceFields="{title:'name', key:'id', value:'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item label="顺序">
              <a-input v-model:value="doc.sort" placeholder="请输入顺序"/>
            </a-form-item>
            <a-form-item label="内容">
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
      <a-drawer
          width="900"
          placement="right"
          :closable="false"
          :visible="drawerVisible"
          :get-container="false"
          :wrap-style="{ position: 'absolute' }"
          @close="onDrawerClose"
      >
        <div class="wangeditorStyle" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
</template>
<!--          <a-modal-->
<!--              title="文档表单"-->
<!--              v-model:visible="modalVisible"-->
<!--              :confirm-loading="modalLoading"-->
<!--              @ok="handleSave"-->
<!--          </a-modal>-->
<script lang = 'ts'>
import {defineComponent, onMounted, ref, createVNode} from 'vue';
import axios from 'axios';
import { message,Modal } from 'ant-design-vue';
import { Tool } from "@/util/tool";
import { useRoute } from "vue-router";
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import E from "wangeditor";

export default defineComponent({
  name: 'adminDoc',
  setup() {
    //变量
    const param = ref();
    param.value = {};

    const route = useRoute();
    console.log("路由:",route);

    const loading = ref(false);
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
      loading.value = true;
      axios.get("/doc/all/"+route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;

        if(data.success){
          docs.value = data.content;
          console.log("原始数组：",docs.value);

          docsTree.value = [];
          docsTree.value = Tool.array2Tree(docs.value,0);
          console.log("树形结构：",docsTree.value);
        } else {
          message.error(data.message);
        }
      });
    };

    //内容查询
    const handleQueryContent = () => {
      axios.get("/doc/content/"+doc.value.id).then((response) => {
        const data = response.data;
        if(data.success){
          console.log("内容：",data.content);
          commonEditor.value.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      });
    };


    //------表单------
    const doc = ref();
    doc.value = {};

    //原始数据和原始树形结构
    const docs = ref();
    const docsTree = ref();
    docsTree.value = [];
    //树选择组件的属性状态会随当前修改节点的状态而变化，所以需要单独声明一个变量
    const treeSelectData = ref();
    treeSelectData.value = [];
    //树形结构删除
    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];

    //表单新增
    const add = () => {
      doc.value = {
        ebookId: route.query.ebookId,
        viewCount: 0,
        voteCount: 0
      };

      //树形结构复制
      if(Tool.isNotEmpty(docsTree.value)){
        treeSelectData.value = Tool.copy(docsTree.value);
      }
      //树形结构最上面加个“无”
      treeSelectData.value.unshift({'id': 0, 'name': '无'});
      console.log("复制后的树形数组：",treeSelectData.value);
    };
    //表单编辑
    const edit = (record: any) => {
      commonEditor.value.txt.html("");
      doc.value = Tool.copy(record);
      handleQueryContent();
      //树形结构置灰
      treeSelectData.value = Tool.copy(docsTree.value);
      setDisable(treeSelectData.value,record.id);
      //树形结构最上面加个“无”
      treeSelectData.value.unshift({'id': 0, 'name': '无'});
    }
    //表单删除
    const handleDelete = (id: number) => {
      getDeleteIds(docsTree.value,id);
      axios.delete("/doc/delete/"+deleteIds.join("&")).then((response) => {
        const data = response.data; //data = commonresp
        //判断接口返回是否成功
        if(data.success){
          //重新加载列表
          handleQuery();
          message.success("删除成功");
        }
        deleteIds.splice(0,deleteIds.length);
        deleteNames.splice(0,deleteNames.length);
      });
    };
    //表单保存
    const handleSave = () => {
      doc.value.content = commonEditor.value.txt.html();
      axios.post("/doc/save",doc.value).then((response) => {
        const data = response.data;
        //判断接口返回是否成功
        if(data.success){
          //重新加载列表
          handleQuery();
          message.success("保存成功");
        }else{
          message.error(data.message);
        }
      });
    };
    //表单树形结构置灰方法
    const setDisable = (treeData: any, id: any) => {
      for(let i=0; i<treeData.length; i++){
        const node = treeData[i];
        if(node.id === id){
          console.log("disabled:",node);
          node.disabled = true;

          const children = node.children;
          if(Tool.isNotEmpty(children)){
            for(let j=0; j<children.length; j++){
              setDisable(children,children[j].id);
            }
          }
        } else {
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            setDisable(children,id);
          }
        }
      }
    }
    //树形结构删除
    const getDeleteIds = (treeData: any, id: any) => {
      for(let i=0; i<treeData.length; i++){
        const node = treeData[i];
        if(node.id === id){
          console.log("delete:",node);
          deleteIds.push(node.id);
          deleteNames.push(node.name);

          const children = node.children;
          if(Tool.isNotEmpty(children)){
            for(let j=0; j<children.length; j++){
              getDeleteIds(children,children[j].id);
            }
          }
        } else {
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            getDeleteIds(children,id);
          }
        }
      }
    }
    //删除双重确认
    const showIfDeleteDoubleConfirm = (id: number) => {
      getDeleteIds(docsTree.value, id);
      const num = deleteNames.length;
      if (num > 1) {
        Modal.confirm({
          title: '将删除' + num + '个文档：' + deleteNames,
          icon: createVNode(ExclamationCircleOutlined),
          content: '删除后不可恢复，确认删除？',
          okText: '是',
          cancelText: '否',
          onOk() {
            handleDelete(id);
            deleteIds.splice(0, deleteIds.length);
            deleteNames.splice(0, deleteNames.length);
          },
          // eslint-disable-next-line @typescript-eslint/no-empty-function
          onCancel() {
            deleteIds.splice(0, deleteIds.length);
            deleteNames.splice(0, deleteNames.length);
          },
        });
      } else {
        handleDelete(id);
        deleteIds.splice(0, deleteIds.length);
        deleteNames.splice(0, deleteNames.length);
      }
    };
    //------富文本------
    //富文本编辑器
    const commonEditor = ref();
    const handleCreateEditor = () => {
      commonEditor.value.config.zIndex = 0;//层级大小，数值越大就排越前面，设大了会把文本框挡住
      commonEditor.value.create();
    }

    //富文本预览
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreview = () => {
      previewHtml.value = commonEditor.value.txt.html();
      drawerVisible.value = true;
    }
    const onDrawerClose = () => {
      drawerVisible.value = false;
    }

    onMounted(() => {
      handleQuery();
      commonEditor.value = new E("#content");
      handleCreateEditor();
      setTimeout(() => {add()},100);
    });

    return {
      param,
      docs,
      docsTree,
      columns,
      loading,
      handleQuery,
      //表单相关变量方法
      doc,
      treeSelectData,
      add,
      edit,
      handleDelete,
      handleSave,
      showIfDeleteDoubleConfirm,
      //富文本相关
      commonEditor,
      drawerVisible,
      previewHtml,
      handlePreview,
      onDrawerClose
    }
  }
});
</script>