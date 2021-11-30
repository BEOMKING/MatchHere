<template>
  <!-- <ProfileNav :email="email" /> -->
  <main class="grid gap-6" v-if="!loading">
    <header class="page-header mb-0">
      <div class="flex items-center justify-between">
        <div class="flex gap-1 items-center">
          <h2>프로필</h2>
          <!-- |
          <h4>프로젝트</h4>
          |
          <h4>스터디</h4> -->
        </div>
        <router-link :to="{ name: 'EditProfile' }" class="flex">
          <span class="font-medium text-gray-400 hover:text-blue-900">
            수정
          </span>
        </router-link>
      </div>
    </header>
    <section class="basic-info-section">
      <div class="basic-info">
        <div class="img-wrapper">
          <button class="photo-button" @click="handleClickPhoto">
            <span class="material-icons"> add_photo_alternate </span>
          </button>
          <input
            type="file"
            class="hidden"
            ref="fileInputEl"
            @change="handleFileChange"
          />
          <img
            :src="profileData.cover_pic || 'https://picsum.photos/80'"
            class="profile-img"
          />
        </div>
        <div class="infos">
          <div class="names">
            <p>{{ profileData.name }}</p>
            <p>({{ profileData.nickname }})</p>
          </div>
          <p class="email">{{ profileData.email }}</p>
          <p class="city">{{ profileData.city }}</p>
        </div>
      </div>
      <!-- <button class="message-button">메세지</button> -->
      <router-link
        class="message-button"
        :to="{ name: 'ChatDetail', params: { email: profileData.email } }"
        >메세지</router-link
      >
      <div class="desc" v-if="profileData.bio">
        {{ profileData.bio }}
      </div>
    </section>
    <section class="position_stack-section">
      <h3>직무/기술스택</h3>
      <div class="infos">
        <div class="position_dposition">
          <p class="font-bold">{{ profileData.position }}</p>
          <p class="font-medium" v-if="dpositionList">({{ dpositionList }})</p>
        </div>
        <div class="stack-list">
          <TeckStackListItem
            v-for="tech in profileData.techList"
            :key="tech.name"
            :name="tech.name"
            :userLevel="tech.level"
            :editMode="false"
          />
        </div>
      </div>
    </section>
    <section class="career-section" v-if="profileData.careerList.length">
      <h3>경력</h3>
      <div class="career-list grid gap-4">
        <CareerListItem
          v-for="career in profileData.careerList"
          :key="career"
          :career="career"
          :editMode="false"
        />
      </div>
    </section>
    <section
      class="certification-section"
      v-if="profileData.certificationList.length"
    >
      <h3>자격증</h3>
      <div class="grid gap-4">
        <CertificationListItem
          v-for="certification in profileData.certificationList"
          :key="certification.id"
          :certification="certification"
          :editMode="false"
        />
      </div>
    </section>
    <section class="education-section" v-if="profileData.educationList.length">
      <h3 class="text-xl font-bold">학력</h3>
      <div class="education-list grid gap-4">
        <EduListItem
          v-for="education in profileData.educationList"
          :key="education.id"
          :education="education"
          :editMode="false"
        />
      </div>
    </section>
    <section
      class="portfolio-section grid gap-4 pb-6 border-b border-gray-200"
      v-if="profileData.portfolio && profileData.portfolio_uri"
    >
      <h3 class="text-xl font-bold">포트폴리오</h3>
      <div class="portfolio-list grid gap-4">
        <PortfolioFile :portfolio="profileData.portfolio" />
        <PortfolioUri :portfolioUri="profileData.portfolio_uri" />
      </div>
    </section>
    <section
      class="sns-section grid gap-4 pb-6 border-b border-gray-200"
      v-if="profileData.snsList.length"
    >
      <h3 class="text-xl font-bold">SNS 링크</h3>
      <div class="sns-list grid gap-4">
        <SNSListItem
          v-for="sns in profileData.snsList"
          :key="sns.id"
          :sns="sns"
        />
      </div>
    </section>
  </main>
</template>

<script>
import { computed, onMounted, ref } from "vue"
import { useStore } from "vuex"
import { useRouter } from "vue-router"
import TeckStackListItem from "@/components/common/TeckStackListItem.vue"
import CareerListItem from "@/components/profile/CareerListItem.vue"
import CertificationListItem from "@/components/profile/CertificationListItem.vue"
import EduListItem from "@/components/profile/EduListItem.vue"
import PortfolioFile from "@/components/profile/PortfolioFile.vue"
import PortfolioUri from "@/components/profile/PortfolioUri.vue"
import SNSListItem from "@/components/profile/SNSListItem.vue"

export default {
  components: {
    TeckStackListItem,
    CareerListItem,
    CertificationListItem,
    EduListItem,
    PortfolioFile,
    PortfolioUri,
    SNSListItem,
  },
  name: "Profile",
  props: {
    email: String,
  },
  setup(props) {
    const store = useStore()
    const router = useRouter()
    const loading = ref(true)
    const fileInputEl = ref(null)
    const profileData = ref(null)
    const dpositionList = computed(() =>
      profileData.value.dpositionList.map((dp) => dp.name).join(", ")
    )

    const handleClickPhoto = () => {
      fileInputEl.value.click()
    }

    const updateProfileImage = async (uuid) => {
      console.log("asdf")
      return new Promise((resolve, reject) => {
        const data = store.dispatch("member/updateBasicInfo", {
          city: profileData.value.city,
          name: profileData.value.name,
          nickname: profileData.value.nickname,
          coverpic_uuid: uuid,
        })
        if (data) {
          resolve(data)
        } else {
          reject()
        }
      })
    }

    const handleFileChange = async (e) => {
      const files = e.target.files || e.dataTransfer.files
      if (!files.length) return
      const formData = new FormData()
      formData.append("file", files[0])
      const cover_pic = await store.dispatch("file/uploadFile", formData)
      console.log(cover_pic)
      if (cover_pic.id) {
        try {
          await updateProfileImage(cover_pic.id)
          profileData.value.cover_pic = cover_pic.fileDownloadUri
        } catch (error) {
          console.log(error)
        }
      }
    }

    onMounted(async () => {
      try {
        const data = await store.dispatch("member/getMypage", props.email)
        profileData.value = { ...data }
        loading.value = false
      } catch (error) {
        router.push({ name: "Home" })
      }
    })

    return {
      loading,
      fileInputEl,
      profileData,
      dpositionList,
      handleClickPhoto,
      handleFileChange,
    }
  },
}
</script>

<style lang="scss" scoped>
section {
  @apply grid gap-4 pb-6 border-b border-gray-200;
}

.basic-info-section {
  .basic-info {
    @apply flex gap-4;

    .img-wrapper {
      @apply relative;

      .photo-button {
        @apply absolute bottom-0 right-0 flex items-center justify-center bg-white rounded-full p-1 transition-all;

        span {
          @apply text-gray-700 transition-all;
        }

        &:hover {
          @apply bg-blue-50;
          span {
            @apply text-blue-500;
          }
        }
      }

      .profile-img {
        @apply rounded-full w-20 h-20 object-cover;
      }
    }

    .infos {
      @apply grid gap-2;

      .names {
        @apply flex gap-1 font-medium;
      }

      .email {
        @apply text-sm font-medium;
      }

      .city {
        @apply text-sm;
      }
    }
  }

  .message-button {
    @apply border border-gray-400 rounded text-sm font-medium py-2 px-6 text-center;
  }

  .desc {
    @apply h-20 p-4 rounded border border-gray-400;
  }
}

.position_stack-section {
  .infos {
    @apply grid gap-4;

    .position_dposition {
      @apply flex gap-2;
    }
    .stack-list {
      @apply grid gap-2;
    }
  }
}

.career-section {
}
</style>
