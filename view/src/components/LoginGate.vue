<template>
    <div class="login-gate" :class="{ 'is-masked': masked, 'is-clipped': needClip }">
        <!-- 被遮罩的内容（游客态下裁切 + 渐隐） -->
        <div ref="content" class="gate-content" :class="{ 'is-clipped': needClip }" :style="clipStyle">
            <slot></slot>
            <div v-if="needClip" class="gate-fade" :style="{ height: fadeH + 'px' }"></div>
        </div>

        <!-- 登录引导卡片 -->
        <div v-if="masked" class="gate-card">
            <div class="gate-icon">
                <i class="el-icon-lock"></i>
            </div>
            <h3 class="gate-title">登录后可查看完整信息</h3>
            <p class="gate-desc">登录后即可查看它的性格、习惯与领养要求</p>
            <div class="gate-actions">
                <el-button type="primary" size="medium" @click="toLogin">去登录</el-button>
                <el-button size="medium" @click="toRegister">去注册</el-button>
            </div>
        </div>
    </div>
</template>

<script>
import { getToken } from '@/utils/storage.js';

export default {
    name: 'LoginGate',
    data() {
        return {
            masked: false, // 是否展示登录引导（游客为 true）
            needClip: false, // 内容是否超出可视高度，需要裁切 + 渐隐
            clipH: 0, // 裁切后的可视高度（px）
            fadeH: 40 // 底部渐隐高度（px）
        };
    },
    mounted() {
        this.masked = !getToken();
        if (!this.masked) {
            return;
        }
        // 内容多为异步渲染，挂载后量几次高度
        this.$nextTick(this.measure);
        this.timers = [setTimeout(this.measure, 300), setTimeout(this.measure, 1000)];
        window.addEventListener('resize', this.measure);
    },
    computed: {
        clipStyle() {
            return this.needClip ? { maxHeight: this.clipH + 'px' } : null;
        }
    },
    beforeDestroy() {
        (this.timers || []).forEach(timer => clearTimeout(timer));
        window.removeEventListener('resize', this.measure);
    },
    methods: {
        measure() {
            const el = this.$refs.content;
            if (!el) {
                return;
            }
            // scrollHeight 不受 max-height 影响，量到的始终是完整内容高度
            const height = el.scrollHeight;
            // 按整行裁切，避免半行被切出毛边；scrollHeight 不受 max-height 影响
            let lineHeight = parseFloat(window.getComputedStyle(el).lineHeight);
            if (!lineHeight || isNaN(lineHeight)) {
                lineHeight = 22;
            }
            const lines = Math.round(height / lineHeight);
            // 只有一行时没什么可藏的，完整展示 + 卡片放在正文下方
            if (lines < 2) {
                this.needClip = false;
                return;
            }
            const maxCap = window.innerWidth <= 768 ? 96 : 112;
            // 至少藏掉一整行，最多露 5 行左右
            this.clipH = Math.min(maxCap, (lines - 1) * lineHeight);
            this.fadeH = Math.max(16, Math.min(40, Math.round(this.clipH / 2)));
            this.needClip = true;
        },
        // 登录/注册后回到当前详情页
        redirectQuery() {
            return { redirect: this.$route.fullPath };
        },
        toLogin() {
            this.$router.push({ path: '/login', query: this.redirectQuery() });
        },
        toRegister() {
            this.$router.push({ path: '/register', query: this.redirectQuery() });
        }
    }
};
</script>

<style lang="scss" scoped>
.login-gate {
    position: relative;
}

.gate-content {
    position: relative;

    ::v-deep > *:first-child {
        margin-top: 0;
    }

    &.is-clipped {
        overflow: hidden;
    }
}

.gate-fade {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    height: 40px;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.8) 45%, #fff 85%, #fff 100%);
    pointer-events: none;
}

.gate-card {
    position: relative;
    z-index: 2;
    max-width: 460px;
    margin: 16px auto 6px;
    padding: 22px 28px 24px;
    text-align: center;
    background: rgba(255, 255, 255, 0.96);
    border: 1px solid #f0f1f2;
    border-radius: 12px;
    box-shadow: 0 8px 28px rgba(26, 147, 62, 0.08), 0 2px 8px rgba(0, 0, 0, 0.04);
}

// 内容被裁切时，卡片压在被渐隐的尾部上（不遮挡可见的正文）
.login-gate.is-clipped .gate-card {
    margin-top: -8px;
}

.gate-icon {
    width: 44px;
    height: 44px;
    margin: 0 auto 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    background: #f0f9eb;
    color: #1a933e;

    i {
        font-size: 22px;
    }
}

.gate-title {
    margin: 0 0 6px;
    font-size: 17px;
    font-weight: 600;
    color: #303133;
}

.gate-desc {
    margin: 0 0 16px;
    font-size: 13px;
    line-height: 1.6;
    color: #909399;
}

.gate-actions {
    display: flex;
    justify-content: center;
    gap: 12px;

    .el-button {
        min-width: 104px;
    }

    .el-button--primary {
        background-color: #1a933e;
        border-color: #1a933e;

        &:hover,
        &:focus {
            background-color: #17802f;
            border-color: #17802f;
        }
    }
}

@media screen and (max-width: 768px) {
    .gate-card {
        margin: 14px 12px 6px;
        padding: 18px 16px 20px;
    }

    .login-gate.is-clipped .gate-card {
        margin-top: -8px;
    }

    .gate-actions {
        flex-direction: column;
        gap: 10px;

        .el-button {
            width: 100%;
            margin-left: 0;
        }
    }
}
</style>
