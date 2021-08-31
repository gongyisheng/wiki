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
              placeholder="输入用户名"
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
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-button type="primary" @click="resetPassword(record)">
              重置密码
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
      title="用户表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalSave"
  >
    <a-form :model="user" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="用户名">
        <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.nickname" />
      </a-form-item>
      <a-form-item label="密码" v-show="!user.id">
        <a-input v-model:value="user.password" />
      </a-form-item>
    </a-form>
  </a-modal>
  <a-modal
      title="重置密码表单"
      v-model:visible="modalResetPasswordVisible"
      :confirm-loading="modalResetPasswordLoading"
      @ok="handleModalResetPassword"
  >
    <a-form :model="user" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="新密码">
        <a-input v-model:value="user.password" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang = 'ts'>
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios';
  import { message } from 'ant-design-vue';
  import { Tool } from "@/util/tool";

  declare let hexMd5: any;
  declare let KEY: any;

  export default defineComponent({
    name: 'adminUser',
    setup() {
      const param = ref();
      param.value = {};
      const users = ref();
      //分页
      const pagination = ref({
        current: 1,//当前页
        pageSize: 5,//每页分页条数
        total: 0
      })
      const columns = [
        {
          title: '用户名',
          dataIndex: 'loginName',
        },
        {
          title: '昵称',
          dataIndex: 'nickname'
        },
        {
          title: '密码',
          dataIndex: 'password'
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
        axios.get("/user/list",{
          params:{
            page: p.page,
            size: p.size,
            name: param.value.name
          }
        }).then((response) => {
          const data = response.data;

          if(data.success){
            users.value = data.content.list;
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
      const user = ref();
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const modalResetPasswordVisible = ref(false);
      const modalResetPasswordLoading = ref(false);

      const handleModalSave = () => {
        modalLoading.value = true;

        user.value.password = hexMd5(user.value.password + KEY);

        axios.post("/user/save",user.value).then((response) => {
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
        user.value = Tool.copy(record);
      }
      //表单新增
      const add = () => {
        modalVisible.value = true;
        user.value = {};
      }
      //表单删除
      const handleDelete = (id: number) => {
        axios.delete("/user/delete/"+id).then((response) => {
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

      const resetPassword = (record: any) => {
        modalResetPasswordVisible.value = true;
        user.value = Tool.copy(record);
        user.value.password = "";
      }

      const handleModalResetPassword = () => {
        modalResetPasswordLoading.value = true;
        //console.log("加密前的密码：",user.value.password);
        user.value.password = hexMd5(user.value.password + KEY);

        axios.post("/user/resetPassword",user.value).then((response) => {
          modalResetPasswordLoading.value = false;
          const data = response.data;
          //判断接口返回是否成功
          if(data.success){
            modalResetPasswordVisible.value = false;
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


      onMounted(() => {
        handleTableChange(pagination);
      });

      return {
        param,
        users,
        pagination,
        columns,
        handleQuery,
        handleTableChange,

        //表单相关变量方法
        user,
        modalVisible,
        modalLoading,
        edit,
        add,
        handleModalSave,
        handleDelete,

        //重置密码
        modalResetPasswordVisible,
        modalResetPasswordLoading,
        resetPassword,
        handleModalResetPassword
      }
    }
  });
</script>
