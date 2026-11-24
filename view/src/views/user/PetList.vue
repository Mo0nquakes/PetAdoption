<template>
    <div class="hall">
        <div class="hall-head">
            <div class="pet-type">
                <div :class="{ 'active': petTypeId === petType.id }" @click="petTypeClick(petType)"
                    class="pet-type-item" v-for="petType in petTypeList" :key="String(petType.id)">
                    {{ petType.name }}
                </div>
            </div>

            <div class="hall-filters">
                <el-input v-model="query.name" class="hall-search" size="small" clearable placeholder="搜索宠物名称"
                    prefix-icon="el-icon-search" @keyup.enter.native="search" @clear="search" />
                <el-select v-model="query.gender" class="hall-select" size="small" placeholder="性别" @change="search">
                    <el-option :value="null" label="性别不限" />
                    <el-option :value="0" label="公" />
                    <el-option :value="1" label="母" />
                </el-select>
                <el-select v-model="query.isVaccine" class="hall-select" size="small" placeholder="疫苗" @change="search">
                    <el-option :value="null" label="疫苗不限" />
                    <el-option :value="true" label="已接种" />
                    <el-option :value="false" label="未接种" />
                </el-select>
                <el-select v-model="query.sort" class="hall-select" size="small" @change="search">
                    <el-option value="new" label="最新发布" />
                    <el-option value="ageAsc" label="月龄从小到大" />
                    <el-option value="ageDesc" label="月龄从大到小" />
                </el-select>
            </div>
        </div>

        <div v-if="!loading && petList.length === 0" class="hall-empty">
            <el-empty description="没有符合条件的宠物" />
        </div>

        <div v-else class="pet-list">
            <PetCard v-for="pet in petList" :key="pet.id" :pet="pet" @click="viewPetDetail" />
        </div>

        <div v-if="total > pageSize" class="hall-pager">
            <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize"
                :current-page="page" @current-change="pageChange" />
        </div>
    </div>
</template>

<script>
import PetCard from '@/components/PetCard.vue';
import { getToken } from '@/utils/storage.js';

export default {
    components: { PetCard },
    name: 'PetHall',
    data() {
        return {
            petTypeList: [],
            petTypeId: null,
            petList: [],
            loading: false,
            total: 0,
            page: 1,
            pageSize: 8,
            query: {
                name: '',
                gender: null,
                isVaccine: null,
                sort: 'new'
            }
        }
    },
    created() {
        this.fetchPetType();
    },
    methods: {
        async fetchPetType() {
            try {
                const { data } = await this.$axios.post('/pet-type/query', {});
                this.petTypeList = [{ id: null, name: '全部' }].concat(data);
                this.petTypeClick(this.petTypeList[0]);
            } catch (error) {
                console.log("查询宠物类别信息异常：", error);
            }
        },
        petTypeClick(petType) {
            this.petTypeId = petType.id;
            this.page = 1;
            this.fetchPet();
        },
        search() {
            this.page = 1;
            this.fetchPet();
        },
        pageChange(page) {
            this.page = page;
            this.fetchPet();
        },
        async fetchPet() {
            this.loading = true;
            // 后端 PagerAspect 会把 current（1 起的页码）换算成偏移量
            const payload = {
                current: this.page,
                size: this.pageSize,
                sort: this.query.sort
            };
            if (this.petTypeId !== null && this.petTypeId !== undefined) {
                payload.petTypeId = this.petTypeId;
            }
            if (this.query.name) {
                payload.name = this.query.name.trim();
            }
            if (this.query.gender !== null) {
                payload.gender = this.query.gender;
            }
            if (this.query.isVaccine !== null) {
                payload.isVaccine = this.query.isVaccine;
            }
            try {
                const { data, total } = await this.$axios.post('/pet/list', payload);
                this.petList = data || [];
                this.total = total || 0;
            } catch (error) {
                console.log("查询宠物信息异常：", error);
                this.petList = [];
                this.total = 0;
            } finally {
                this.loading = false;
            }
        },
        viewPetDetail(pet) {
            if (getToken()) {
                window.open('/pet-detail?id=' + pet.id, '_blank');
            } else {
                this.$router.push({ path: '/view-pet-detail', query: { id: pet.id } });
            }
        }
    }
}
</script>

<style scoped lang="scss">
.hall {
    width: 100%;
}

.hall-head {
    margin-bottom: 18px;
}

.pet-type {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    margin-bottom: 16px;

    .pet-type-item {
        cursor: pointer;
        padding: 8px 16px;
        border-radius: 10px;
        transition: all 0.3s ease;

        &:hover {
            background-color: rgb(240, 240, 240);
        }

        &.active {
            background-color: #1a933e;
            color: white;
        }
    }
}

.hall-filters {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    align-items: center;

    .hall-search {
        width: 240px;
    }

    .hall-select {
        width: 130px;
    }
}

.pet-list {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 20px;
    margin-top: 10px;
}

.hall-empty {
    padding: 40px 0;
}

.hall-pager {
    display: flex;
    justify-content: center;
    margin-top: 26px;
}

@media (max-width: 992px) {
    .pet-list {
        grid-template-columns: repeat(3, minmax(0, 1fr));
    }
}

@media (max-width: 768px) {
    .pet-list {
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 14px;
    }

    .hall-search {
        width: 100% !important;
    }
}

@media (max-width: 576px) {
    .pet-list {
        grid-template-columns: minmax(0, 1fr);
    }
}
</style>
