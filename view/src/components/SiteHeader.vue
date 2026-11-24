<template>
  <!-- 公共顶部导航：游客首页、游客详情页、登录页、注册页共用 -->
  <header class="site-header">
    <div class="site-header-inner">
      <div class="site-brand" @click="goHome" title="返回首页">
        <Logo sysName="宠物领养系统" textColor="rgb(51,51,51)" :logoSize="Number(40)" :fontSize="Number(20)" />
      </div>

      <nav class="site-nav">
        <router-link to="/view" class="site-nav-item" exact-active-class="active">首页</router-link>
        <router-link to="/pet-list" class="site-nav-item" active-class="active">宠物大厅</router-link>
        <router-link to="/pet-post" class="site-nav-item" active-class="active">养宠经验</router-link>
        <router-link to="/notice" class="site-nav-item" active-class="active">公告</router-link>
      </nav>

      <div class="site-actions">
        <!-- 登录 / 注册页：只给一个回首页的出口 -->
        <template v-if="mode === 'auth'">
          <div class="site-btn ghost" @click="goHome">返回首页</div>
        </template>
        <template v-else-if="!isLogin">
          <div class="site-btn ghost" @click="goLogin">登录</div>
          <div class="site-btn primary" @click="goRegister">注册</div>
        </template>
        <template v-else>
          <div class="site-btn primary" @click="goUser">{{ isAdmin ? '管理后台' : '个人中心' }}</div>
        </template>
      </div>
    </div>
  </header>
</template>

<script>
import Logo from '@/components/Logo.vue';
import { getToken, getRole } from '@/utils/storage.js';

export default {
  name: 'SiteHeader',
  components: { Logo },
  props: {
    // guest：未登录展示「登录 / 注册」；auth：登录、注册页展示「返回首页」
    mode: {
      type: String,
      default: 'guest'
    }
  },
  data() {
    return {
      isLogin: false,
      isAdmin: false
    };
  },
  created() {
    const token = getToken();
    this.isLogin = token !== null;
    this.isAdmin = Number(getRole()) === 1;
  },
  methods: {
    goHome() {
      if (this.$route.path === '/view') {
        window.scrollTo({ top: 0, behavior: 'smooth' });
        return;
      }
      this.$router.push('/view');
    },
    goLogin() {
      this.$router.push({ path: '/login', query: { redirect: this.$route.fullPath } });
    },
    goRegister() {
      this.$router.push({ path: '/register', query: { redirect: this.$route.fullPath } });
    },
    goUser() {
      this.$router.push(this.isAdmin ? '/admin' : '/user');
    }
  }
};
</script>
