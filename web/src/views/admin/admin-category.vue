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
                placeholder="输入分类名"
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
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categorysTree"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" height="50"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
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
      title="分类表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalSave"
  >
    <a-form :model="categorys" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-select
            v-model:value="category.parent"
            ref="select"
        >
          <a-select-option value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in categorysTree" :key="c.id" :value="c.id" :disabled="category.id === c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
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
  name: 'adminCategory',
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref();
    const categorysTree = ref();

    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
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
    const handleQuery = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;

        if(data.success){
          categorys.value = data.content;
          console.log("原始数组：",categorys.value);

          categorysTree.value = [];
          categorysTree.value = Tool.array2Tree(categorys.value,0);
          console.log("树形结构：",categorysTree.value);
        } else {
          message.error(data.message);
        }
      });
    };


    //------表单------
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalSave = () => {
      modalLoading.value = true;

      axios.post("/category/save",category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        //判断接口返回是否成功
        if(data.success){
          modalVisible.value = false;
          //重新加载列表
          handleQuery();
        }else{
          message.error(data.message);
        }
      });
    };

    //表单编辑
    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
    }
    //表单新增
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    }
    //表单删除
    const handleDelete = (id: number) => {
      axios.delete("/category/delete/"+id).then((response) => {
        const data = response.data; //data = commonresp
        //判断接口返回是否成功
        if(data.success){
          //重新加载列表
          handleQuery();
          message.success("删除成功");
        }
      });
    };


    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      categorys,
      categorysTree,
      columns,
      loading,
      handleQuery,
      //表单相关变量方法
      category,
      modalVisible,
      modalLoading,
      handleModalSave,
      edit,
      add,
      handleDelete
    }
  }
});
</script>
