<template>
    <div class="pet-card" @click="$emit('click', pet)">
        <div class="pet-cover">
            <img :src="pet.cover" :alt="pet.name" loading="lazy">
            <div v-if="pet.isAdopt" class="adopted-tag">已领养</div>
            <div v-if="pet.isRecommend" class="recommend-tag">推荐</div>
        </div>
        <div class="pet-info">
            <h3 :title="pet.name">{{ pet.name }}</h3>
            <div class="pet-meta">
                <span>{{ ageText(pet.age) }}</span>
                <span>{{ pet.gender === 0 ? '公' : '母' }}</span>
                <span v-if="pet.petTypeName">{{ pet.petTypeName }}</span>
            </div>
            <div class="pet-location">
                <i class="el-icon-location"></i>
                {{ pet.address }}
            </div>
            <div class="pet-vaccine" :class="{ 'vaccined': pet.isVaccine }">
                {{ pet.isVaccine ? '已接种' : '未接种' }}
            </div>
        </div>
    </div>
</template>

<script>
import { ageText } from '@/utils/format';

export default {
    name: 'PetCard',
    props: {
        pet: {
            type: Object,
            required: true
        }
    },
    methods: {
        ageText
    }
}
</script>

<style scoped lang="scss">
.pet-card {
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    cursor: pointer;
    background: white;

    &:hover {
        transform: translateY(-5px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
    }

    .pet-cover {
        position: relative;
        height: 180px;
        overflow: hidden;

        img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.5s ease;
        }

        &:hover img {
            transform: scale(1.05);
        }

        .adopted-tag,
        .recommend-tag {
            position: absolute;
            top: 10px;
            padding: 2px 8px;
            border-radius: 4px;
            color: white;
            font-size: 12px;
        }

        .adopted-tag {
            right: 10px;
            background-color: #f56c6c;
        }

        .recommend-tag {
            left: 10px;
            background-color: #1a933e;
        }
    }

    .pet-info {
        padding: 15px;

        h3 {
            margin: 0 0 8px;
            font-size: 16px;
            color: #333;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .pet-meta {
            display: flex;
            gap: 8px;
            margin-bottom: 8px;
            font-size: 12px;
            color: #666;
        }

        .pet-location {
            font-size: 12px;
            color: #999;
            margin-bottom: 8px;
            display: flex;
            align-items: center;

            i {
                margin-right: 4px;
            }
        }

        .pet-vaccine {
            font-size: 12px;
            padding: 2px 8px;
            border-radius: 4px;
            display: inline-block;
            background-color: #f0f0f0;
            color: #666;

            &.vaccined {
                background-color: #e1f3d8;
                color: #1a933e;
            }
        }
    }
}
</style>
