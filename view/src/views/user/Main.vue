<template>
    <div class="app-container">
        <!-- 顶部导航栏（与游客首页共用 .site-header 样式） -->
        <header class="site-header">
            <div class="site-header-inner">
                <div class="site-brand" @click="goHome" title="返回首页">
                    <Logo sysName="宠物领养系统" textColor="rgb(51,51,51)" :logoSize="Number(40)" :fontSize="Number(20)" />
                </div>
                <nav class="site-nav">
                    <router-link v-for="item in visibleNavItems" :key="item.path" :to="item.path" class="site-nav-item"
                        active-class="active" @click="closeMobileMenu">
                        {{ item.title }}
                    </router-link>
                </nav>
                <div class="site-actions">
                    <template v-if="!isLogin">
                        <div class="site-btn ghost" @click="goLogin">登录</div>
                        <div class="site-btn primary" @click="goRegister">注册</div>
                    </template>
                    <template v-else>
                    <!-- 发布宠物经验帖子按钮 -->
                    <div @click="goPetPost" class="site-btn primary share-btn">
                        <i class="el-icon-document"></i>
                        <span class="share-btn-text">分享经验</span>
                    </div>
                    <el-dropdown trigger="click">
                        <span class="user-entry">
                            <img v-if="userInfo.avatar" :src="userInfo.avatar" alt="用户头像" class="user-entry-avatar">
                            <span class="user-entry-name">{{ userInfo.username }}</span>
                            <i class="el-icon-arrow-down"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown" class="user-dropdown">
                            <el-dropdown-item @click.native="center" icon="el-icon-s-data">
                                创作中心
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="address" icon="el-icon-s-ticket">
                                收货地址
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="updateUserInfo" icon="el-icon-user-solid">
                                修改信息
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="dialogPasswordOperation = true" icon="el-icon-finished">
                                修改密码
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="proposal" icon="el-icon-connection">
                                意见反馈
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="dialogOutOperation = true" icon="el-icon-s-fold">
                                退出登录
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    </template>
                </div>
            </div>
        </header>

        <!-- 主内容区 -->
        <main class="app-main">
            <router-view></router-view>
        </main>

        <!-- 退出登录弹窗 -->
        <el-dialog title="退出登录" :show-close="false" :visible.sync="dialogOutOperation" width="20%">
            <span>确定退出登录？</span>
            <span slot="footer" class="dialog-footer">
                <span class="primary-bt" @click="dialogOutOperation = false">取消</span>
                <span class="info-bt" @click="confirmLoginOut">确定</span>
            </span>
        </el-dialog>

        <!-- 修改密码弹窗 -->
        <el-dialog title="修改密码" :closeOnClickModal="false" :show-close="false" :visible.sync="dialogPasswordOperation"
            width="28%">
            <div>
                <div>
                    <p>*原始密码</p>
                    <el-input style="height: 60px;line-height: 60px;" show-password placeholder="输入" type="password"
                        v-model="oldPassword" clearable>
                    </el-input>
                </div>
                <div>
                    <p>*新密码</p>
                    <el-input style="height: 60px;line-height: 60px;" show-password placeholder="输入" type="password"
                        v-model="newPassword" clearable>
                    </el-input>
                </div>
                <div>
                    <p>*确认密码</p>
                    <el-input style="height: 60px;line-height: 60px;" show-password placeholder="输入" type="password"
                        v-model="againPassword" clearable>
                    </el-input>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <span class="primary-bt" @click="dialogPasswordOperation = false">取消</span>
                <span class="info-bt" @click="confirmUpdatePassword">确定</span>
            </span>
        </el-dialog>

        <!-- 用户信息操作弹窗 -->
        <el-dialog title="修改信息" :show-close="false" :visible.sync="dialogUserInfoVisible" :closeOnClickModal="false"
            width="35%">
            <el-tabs v-model="tabActiveName" :tab-position="tabPosition" style="height: 420px;">
                <el-tab-pane label="核心信息" name="first">
                    <div>
                        <div class="user-avatar-container">
                            <p>点击📷处即可上传头像</p>
                            <img v-if="avatar" :src="avatar || ''" alt="用户头像" class="user-avatar-large">
                            <el-upload style="margin-left: 20px;" class="avatar-uploader"
                                action="http://localhost:21090/api/v1.0/pet-adoption-api/file/upload" :show-file-list="false"
                                :on-success="handleImageSuccess">
                                <i class="el-icon-camera-solid upload-icon"></i>
                            </el-upload>
                        </div>
                        <div class="form-group">
                            <p>*用户账号</p>
                            <el-input :disabled="true" placeholder="输入" v-model="apiParam.account" clearable>
                            </el-input>
                        </div>
                        <div class="form-group">
                            <p>*用户名</p>
                            <el-input placeholder="输入" v-model="apiParam.username" clearable>
                            </el-input>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="基本信息" name="second">
                    <div>
                        <div class="form-group">
                            <p>用户性别</p>
                            <Tab :buttons="[
                                { label: '女', value: '1' },
                                { label: '男', value: '2' }
                            ]" :initialActive="String(apiParam.gender) || '2'" @change="handleGenderChange" />
                        </div>
                        <div class="form-group">
                            <p>电子邮件</p>
                            <el-input placeholder="输入" v-model="apiParam.email" clearable>
                            </el-input>
                        </div>
                        <div class="form-group">
                            <p>联系电话</p>
                            <el-input placeholder="输入" v-model="apiParam.phone" clearable>
                            </el-input>
                        </div>
                        <div class="form-group">
                            <p>出生年月</p>
                            <el-date-picker style="width: 100%;" v-model="apiParam.birthday" type="date"
                                placeholder="选择日期">
                            </el-date-picker>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
            <span slot="footer" class="dialog-footer">
                <span class="primary-bt" @click="dialogUserInfoVisible = false">取消</span>
                <span class="info-bt" @click="handleConfirm">
                    确定修改
                </span>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import Logo from "@/components/Logo"
import AutoInput from '@/components/AutoInput.vue';
import Tab from "@/components/Tab"
import {
    setUserInfo,
    getToken,
    clearToken,
    clearRole
} from "@/utils/storage"

export default {
    components: { Logo, AutoInput, Tab },
    name: "AppLayout",
    data() {
        return {
            oldPassword: '', // 原始密码
            newPassword: '', // 新密码
            againPassword: '', // 确认密码
            tabActiveName: 'first', // 修改信息Tab默认选中第一项
            tabPosition: 'right', // 修改信息Tab靠右
            dialogPasswordOperation: false, // 修改密码弹窗开关
            dialogUserInfoVisible: false, // 修改信息弹窗开关
            dialogOutOperation: false, // 退出登录弹窗控制开关
            navItems: [
                { path: '/view', icon: 'el-icon-s-home', title: '首页' },
                { path: '/pet-list', icon: '', title: '宠物大厅' },
                { path: '/pet-post', icon: 'el-icon-s-help', title: '养宠经验' },
                { path: '/my-pet-adopt-order', icon: 'el-icon-tickets', title: '宠物领养订单' },
                 { path: '/notice', icon: '', title: '公告' },
            ],
            userInfo: {},
            isLogin: false, // 游客访问宠物大厅时为 false
            isAuthChecked: false,
            isMobileMenuOpen: false,
            apiParam: {},
            avatar: '', // 用户头像
        }
    },
    computed: {
        // 游客不显示需要登录的菜单
        visibleNavItems() {
            if (this.isLogin) {
                return this.navItems;
            }
            return this.navItems.filter(item => item.path === '/view' || item.path === '/pet-list');
        }
    },
    async created() {
        await this.handleAuthentication()
        if (this.isAuthChecked) {
            this.initRouteHandling()
        }
    },
    methods: {
        // 返回前台首页
        goHome() {
            this.$router.push('/view');
        },
        // 游客去登录 / 注册，成功后回到当前页
        goLogin() {
            this.$router.push({ path: '/login', query: { redirect: this.$route.fullPath } });
        },
        goRegister() {
            this.$router.push({ path: '/register', query: { redirect: this.$route.fullPath } });
        },
        // 加载意见与反馈中心
        proposal() {
            this.$router.push('/proposal-feedback');
        },
        center() {
            // 加载创作中心页面
            this.$router.push('/pet-post-user');
        },
        address() {
            // 加载收货地址页面
            this.$router.push('/address');
        },
        // 跳转至宠物经验帖子发布页
        goPetPost() {
            window.open('/create-pet-post', '_blank');
        },
        // 性别选择切换
        handleGenderChange(obj) {
            this.apiParam.gender = Number(obj.value);
        },
        handleImageSuccess(res, file) {
            this.$notify({
                title: '头像上传',
                type: res.code === 200 ? 'success' : 'error',
                message: res.code === 200 ? '上传成功' : res.data,
                position: 'bottom-right',
                duration: 1000,
            })
            if (res.code === 200) {
                this.avatar = res.data;
            }
        },
        updateUserInfo() {
            this.avatar = this.userInfo.avatar;
            this.dialogUserInfoVisible = true;
        },
        // 修改信息
        async handleConfirm() {
            try {
                this.apiParam.avatar = this.avatar;
                const { data, message } = await this.$axios.put('/user/update', this.apiParam);
                this.apiParam = data;
                this.$message.success(message);
                this.handleAuthentication();
                this.dialogUserInfoVisible = false; // 关闭弹窗
            } catch (error) {
                this.$message.warning(error.message);
            }
        },
        // 修改密码
        async confirmUpdatePassword() {
            if (!this.oldPassword || !this.newPassword || !this.againPassword) {
                this.$message('原始密码、新密码、确认密码不能为空哦');
                return;
            }
            if (this.newPassword !== this.againPassword) {
                this.$message('前后密码输入不一致');
                return;
            }
            const updatePasswordDto = {
                oldPassword: this.$md5(this.$md5(this.oldPassword)),
                newPassword: this.$md5(this.$md5(this.newPassword)),
                againPassword: this.$md5(this.$md5(this.againPassword))
            }
            try {
                const { message } = await this.$axios.put('/user/updatePassword', updatePasswordDto);
                this.$notify.success({
                    title: '密码修改',
                    message: message,
                    duration: 1000,
                    position: 'bottom-right'
                })
                this.confirmLoginOut();
            } catch (error) {
                this.$message.error(error.message);
            }
        },
        // 退出登录
        confirmLoginOut() {
            clearToken();
            clearRole();
            this.$router.push('/');
        },
        closeMobileMenu() {
            this.isMobileMenuOpen = false;
        },
        async handleAuthentication() {
            if (this.$route.path === '/login') {
                this.isAuthChecked = true;
                return;
            }

            try {
                // 宠物大厅允许游客浏览：没有 token 时不再跳登录页
                if (!getToken()) {
                    if (this.$route.path === '/pet-list') {
                        this.isLogin = false;
                        this.isAuthChecked = true;
                        return;
                    }
                    this.redirectToLogin();
                    return;
                }

                // 已登录则拉完整用户信息（登录接口只返回 id/token/role，缺昵称头像）
                const data = await this.$axios.get('/user/auth');
                if (data.code === 200) {
                    this.userInfo = data.data;
                    this.apiParam = { ...data.data };
                    this.isLogin = true;
                    setUserInfo(this.userInfo);
                    this.isAuthChecked = true;
                } else {
                    this.redirectToLogin();
                }
            } catch (error) {
                console.error('认证失败:', error);
                this.redirectToLogin();
            }
        },
        initRouteHandling() {
            this.setupRouteWatcher();
            if (!this.isValidRoute(this.$route.path)) {
                this.$router.replace('/pet-list');
            }
            if (this.$route.path === '/') {
                this.$router.replace('/pet-list');
            }
        },
        isValidRoute(path) {
            return this.navItems.some(item => item.path === path);
        },
        setupRouteWatcher() {
            this.$router.afterEach((to) => {
                if (this.isValidRoute(to.path)) {
                    // 可以在这里记录访问路径
                }
            });
        },
        redirectToLogin() {
            if (this.$route.path !== '/login') {
                this.$notify({
                    type: 'error',
                    title: '安全认证',
                    message: '请重新登录',
                    duration: 2000,
                    position: 'bottom-right'
                });
                this.$router.replace('/login');
            }
        }
    }
}
</script>

<style scoped lang="scss">
.app-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    background-color: rgb(255, 255, 255);
    overflow-x: hidden;
}

.input {
    width: 188px;
    transition: width 0.3s ease;
}

.user-dropdown {
    border-radius: 8px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
    padding: 8px 0;
    animation: dropdownFadeIn 0.3s ease;
}

.dropdown-item {
    display: flex;
    align-items: center;
    padding: 10px 24px;
    color: #4a5568;
    text-decoration: none;
    transition: all 0.2s ease;
    font-size: 14px;

    i {
        margin-right: 12px;
        font-size: 16px;
        color: #15559a;
    }

    &:hover {
        background-color: #f0f7ff;
        color: #15559a;
        padding-left: 28px;
    }
}

.app-main {
    flex: 1;
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    box-sizing: border-box;
    padding: 20px 15px;
    background-color: rgb(255, 255, 255);
    border-radius: 5px;
}

@media (max-width: 768px) {
    .app-main {
        padding: 12px;
    }
}

.user-avatar-container {
    text-align: center;
    margin-bottom: 30px;

    p {
        font-size: 14px;
        color: #6b7280;
        margin-bottom: 15px;
    }
}

.user-avatar-large {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    object-fit: cover;
    margin-bottom: 15px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;

    &:hover {
        transform: scale(1.05);
    }
}

.avatar-uploader {
    display: inline-block;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: rgba(21, 85, 154, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s ease;
    margin-top: -20px;

    i {
        color: #15559a;
        font-size: 18px;
    }

    &:hover {
        background-color: rgba(21, 85, 154, 0.2);
        transform: scale(1.1);
    }
}

.upload-icon {
    color: #15559a;
}

.form-group {
    margin-bottom: 24px;

    p {
        font-size: 14px;
        color: #4b5563;
        margin-bottom: 8px;
        font-weight: 500;
    }

    .el-input {
        width: 100%;
        border-radius: 6px;
        transition: border-color 0.3s ease, box-shadow 0.3s ease;

        &:focus-within {
            border-color: #15559a;
            box-shadow: 0 0 0 2px rgba(21, 85, 154, 0.2);
        }
    }
}

/* 动画定义 */
@keyframes dropdownFadeIn {
    from {
        opacity: 0;
        transform: translateY(-10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>