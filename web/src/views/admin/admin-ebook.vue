<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px'}"
    >
      <p>
      <a-form layout="inline" :model="param">
        <a-form-item>
          <a-input-search
              v-model:value="param.name"
              placeholder="输入电子书名"
              enter-button
              size="medium"
              @search="handleQuery({page:1, size: pagination.pageSize})"
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
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" height="50"/>
        </template>
        <template v-slot:category="{ text, record }">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?ebookId=' + record.id">
              <a-button type="primary">
                电子书管理
              </a-button>
            </router-link>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="电子书表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalSave"
  >
    <a-form :model="ebook" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="封面地址">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
          v-model:value="categoryIds"
          :field-names="{label:'name', value:'id', children:'children'}"
          :options="categorysTree"
        />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" />
      </a-form-item>
      <a-form-item label="文档数">
        <a-input v-model:value="ebook.docCount" />
      </a-form-item>
      <a-form-item label="阅读数">
        <a-input v-model:value="ebook.viewCount" />
      </a-form-item>
      <a-form-item label="点赞数">
        <a-input v-model:value="ebook.voteCount" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang = 'ts'>
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios';
  import { message } from 'ant-design-vue';
  import { Tool } from "@/util/tool";

  export default defineComponent({
    name: 'adminEbook',
    setup() {
      const param = ref();
      param.value = {};
      const ebooks = ref();
      //分页
      const pagination = ref({
        current: 1,//当前页
        pageSize: 5,//每页分页条数
        total: 0
      })
      const loading = ref(false);
      const columns = [
        {
          title: '封面',
          dataIndex: 'cover',
          slots: { customRender: 'cover' }//cover渲染
        },
        {
          title: '名称',
          dataIndex: 'name'
        },
        {
          title: '分类',
          slots: { customRender: 'category' }
        },
        {
          title: '文档数',
          dataIndex: 'docCount'
        },
        {
          title: '阅读数',
          dataIndex: 'viewCount'
        },
        {
          title: '点赞数',
          dataIndex: 'voteCount'
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }//渲染成action
        }
      ];

      /**
       * 数据查询
       **/
      const handleQuery = (p: any) => {
        loading.value = true;
        // // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        // ebooks.value = [];
        axios.get("/ebook/list",{
          params:{
            page: p.page,
            size: p.size,
            name: param.value.name
          }
        }).then((response) => {
          loading.value = false;
          const data = response.data;

          if(data.success){
            ebooks.value = data.content.list;
            //重置分页按钮
            pagination.value.current = p.page;
            pagination.value.total = data.content.total;
          } else {
            message.error(data.message);
          }
        });
      };

      /**
       * 表格点击页码时触发
       */
      const handleTableChange = (pagination: any) => {
        console.log("看看自带的分页参数都有啥：" + pagination);
        handleQuery({
          page: pagination.current,
          size: pagination.pageSize
        });
      };

      //------表单------
      const ebook = ref();
      const categoryIds = ref();//数组，对应分级目录
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModalSave = () => {
        modalLoading.value = true;
        ebook.value.category1Id = categoryIds.value[0];
        ebook.value.category2Id = categoryIds.value[1];

        axios.post("/ebook/save",ebook.value).then((response) => {
          modalLoading.value = false;
          const data = response.data;
          //判断接口返回是否成功
          if(data.success){
            modalVisible.value = false;
            //重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            });
          }else{
            message.error(data.message);
          }
        });
      };

      //表单编辑
      const edit = (record: any) => {
        modalVisible.value = true;
        ebook.value = Tool.copy(record);
        categoryIds.value = [ebook.value.category1Id,ebook.value.category2Id];
      }
      //表单新增
      const add = () => {
        modalVisible.value = true;
        ebook.value = {};
      }
      //表单删除
      const handleDelete = (id: number) => {
        axios.delete("/ebook/delete/"+id).then((response) => {
          const data = response.data; //data = commonresp
          //判断接口返回是否成功
          if(data.success){
            //重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            });
            message.success("删除成功");
          } else {
            message.error(data.message);
          }
        });
      };

      let categorys: any;
      const categorysTree = ref();
      /**
       * 查询所有分类
       **/
      const handleQueryCategory = () => {
        loading.value = true;
        axios.get("/category/all").then((response) => {
          loading.value = false;
          const data = response.data;
          if (data.success) {
            categorys = data.content;
            console.log("原始数组：", categorys);

            categorysTree.value = [];
            categorysTree.value = Tool.array2Tree(categorys, 0);
            console.log("树形结构：", categorysTree.value);

            // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
            handleQuery({
              page: 1,
              size: pagination.value.pageSize,
            });
          } else {
            message.error(data.message);
          }
        });
      };

      const getCategoryName = (cid: number) => {
        let result = "";
        categorys.forEach((item: any) => {
          if (item.id === cid) {
            // return item.name; // 注意，这里直接return不起作用
            result = item.name;
          }
        });
        return result;
      };

      onMounted(() => {
        handleQueryCategory();
      });

      return {
        param,
        ebooks,
        pagination,
        columns,
        loading,
        handleQuery,
        handleTableChange,

        //表单相关变量方法
        ebook,
        modalVisible,
        modalLoading,
        edit,
        add,
        handleModalSave,
        handleDelete,

        categoryIds,
        categorysTree,
        getCategoryName
      }
    }
  });
</script>
