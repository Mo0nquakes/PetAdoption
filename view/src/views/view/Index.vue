<template>
    <div>
        <!-- 顶部导航栏（与游客详情页、登录页共用） -->
        <SiteHeader />

        <!-- 主视觉轮播 -->
        <div class="top">
            <Carousel @obj-detail="objDetail" containerHeight="calc(100vh - 64px)" :carouselItems="carouselItems" />
        </div>

        <!-- 精选宠物 -->
        <section class="section featured">
            <div class="section-head">
                <h2>精选宠物</h2>
                <p>每一只都在等待一个家</p>
            </div>
            <div class="featured-list">
                <PetCard v-for="pet in featuredList" :key="pet.id" :pet="pet" @click="petInfo" />
            </div>
            <div class="section-more">
                <el-button type="primary" round @click="goHall">进入宠物大厅，查看更多</el-button>
            </div>
        </section>

        <!-- 领养流程 -->
        <section class="section steps">
            <div class="section-head">
                <h2>领养流程</h2>
                <p>四步接它回家</p>
            </div>
            <div class="step-list">
                <div class="step-item" v-for="(step, index) in steps" :key="step.title">
                    <div class="step-index">{{ index + 1 }}</div>
                    <h3>{{ step.title }}</h3>
                    <p>{{ step.desc }}</p>
                </div>
            </div>
        </section>

        <!-- 底部 CTA -->
        <section class="cta">
            <h2>准备好给它一个家了吗？</h2>
            <p>浏览待领养宠物，提交申请，让更多小生命被看见。</p>
            <div class="cta-actions">
                <el-button type="primary" round size="medium" @click="goHall">进入宠物大厅</el-button>
                <el-button round size="medium" @click="goRegister">注册账号</el-button>
            </div>
        </section>

        <div class="bottom">
            <div>&copy; 2027 宠物领养系统</div>
        </div>
    </div>
</template>

<script>
import Carousel from '@/components/Carousel.vue';
import PetCard from '@/components/PetCard.vue';
import SiteHeader from '@/components/SiteHeader.vue';
export default {
    components: { Carousel, PetCard, SiteHeader },
    data() {
        return {
            carouselItems: [], // 轮播图数据
            featuredList: [], // 精选宠物
            steps: [
                { title: '浏览宠物', desc: '在宠物大厅按类别、性别、疫苗筛选，找到心动的它。' },
                { title: '提交申请', desc: '登录后进入宠物详情，填写领养申请并提交。' },
                { title: '等待审核', desc: '救助人在后台审核申请，可在领养订单里查看进度。' },
                { title: '接它回家', desc: '审核通过后联系救助人，约好时间把它接回家。' }
            ]
        }
    },
    created() {
        this.fetchRecommendPetInfo();
        this.fetchFeatured();
    },
    methods: {
        // 同标签跳转，方便引导登录后原地回跳
        petInfo(pet) {
            this.$router.push({ path: '/view-pet-detail', query: { id: pet.id } });
        },
        objDetail(obj) {
            this.$router.push({ path: '/view-pet-detail', query: { id: obj.id } });
        },
        // 进入宠物大厅
        goHall() {
            this.$router.push('/pet-list');
        },
        // 跳转注册，注册成功回到首页
        goRegister() {
            this.$router.push({ path: '/register', query: { redirect: '/view' } });
        },
        // 查询轮播推荐宠物
        async fetchRecommendPetInfo() {
            try {
                const response = await this.$axios.get('/pet/recommend');
                this.carouselItems = response.data.map(petItem => {
                    return {
                        id: petItem.id,
                        image: petItem.cover,
                        title: petItem.name,
                        subtitle: petItem.address
                    }
                });
            } catch (error) {
                console.log("首页查询推荐宠物信息：", error);
            }
        },
        // 查询精选宠物：推荐优先，不足 4 只时用最新发布的补齐
        async fetchFeatured() {
            try {
                const recommend = await this.$axios.post('/pet/list', { current: 1, size: 4, isRecommend: true, sort: 'new' });
                let list = recommend.data || [];
                if (list.length < 4) {
                    const latest = await this.$axios.post('/pet/list', { current: 1, size: 8, sort: 'new' });
                    const ids = new Set(list.map(pet => pet.id));
                    (latest.data || []).forEach(pet => {
                        if (list.length < 4 && !ids.has(pet.id)) {
                            list.push(pet);
                            ids.add(pet.id);
                        }
                    });
                }
                this.featuredList = list.slice(0, 4);
            } catch (error) {
                console.log("首页查询精选宠物信息：", error);
            }
        }
    }
}
</script>

<style scoped lang="scss">
.top {
    position: relative;
}

.section {
    max-width: 1200px;
    margin: 0 auto;
    padding: 56px 15px 0;
}

.section-head {
    text-align: center;
    margin-bottom: 28px;

    h2 {
        font-size: 28px;
        color: rgb(51, 51, 51);
        margin: 0 0 8px;
    }

    p {
        margin: 0;
        color: #8a8f99;
        font-size: 15px;
    }
}

.featured-list {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 20px;
}

.section-more {
    display: flex;
    justify-content: center;
    margin-top: 28px;
}

.steps {
    padding-bottom: 56px;
}

.step-list {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 20px;
}

.step-item {
    background: #fff;
    border-radius: 12px;
    padding: 24px 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
    text-align: center;

    .step-index {
        width: 36px;
        height: 36px;
        line-height: 36px;
        margin: 0 auto 12px;
        border-radius: 50%;
        background: #eaf5ee;
        color: #1a933e;
        font-weight: 600;
    }

    h3 {
        margin: 0 0 8px;
        font-size: 17px;
        color: rgb(51, 51, 51);
    }

    p {
        margin: 0;
        font-size: 14px;
        line-height: 1.6;
        color: #8a8f99;
    }
}

.cta {
    background: #f6faf7;
    padding: 56px 15px;
    text-align: center;

    h2 {
        margin: 0 0 10px;
        font-size: 26px;
        color: rgb(51, 51, 51);
    }

    p {
        margin: 0 0 22px;
        color: #8a8f99;
    }

    .cta-actions {
        display: flex;
        justify-content: center;
        gap: 12px;
        flex-wrap: wrap;
    }
}

.section-more,
.cta-actions {
    ::v-deep .el-button--primary {
        background-color: #1a933e;
        border-color: #1a933e;

        &:hover,
        &:focus {
            background-color: #17802f;
            border-color: #17802f;
        }
    }
}

.bottom {
    height: 50px;
    background: rgb(51, 51, 51);
    color: #fff;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
}

@media (max-width: 992px) {
    .featured-list,
    .step-list {
        grid-template-columns: repeat(2, minmax(0, 1fr));
    }
}

@media (max-width: 576px) {
    .section {
        padding-top: 40px;
    }

    .featured-list {
        grid-template-columns: minmax(0, 1fr);
    }

    .step-list {
        grid-template-columns: minmax(0, 1fr);
    }
}
</style>
