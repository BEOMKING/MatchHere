<template>
  <div class="container py-10 px-4">
    <header class="page-header">
      <div class="flex items-center justify-between">
        <h1>스터디</h1>
        <router-link class="text-sm" :to="{ name: 'StudyCreate' }">
          스터디 만들기
        </router-link>
      </div>
      <p>여러분이 원하는 스터디를 찾아보세요!</p>
    </header>
    <section class="grid gap-4 mb-10">
      <h2>맞춤형 추천 스터디</h2>
      <swiper
        class="w-full"
        :spaceBetween="8"
        :breakpoints="{
          640: {
            slidesPerView: 2,
          },
          1024: {
            slidesPerView: 3,
          },
          1280: {
            slidesPerView: 4,
          },
        }"
        :loop="true"
        @swiper="handleSwiper"
        @slideChange="handleSlideChange"
        @resize="onResize"
      >
        <SwiperSlide v-if="false">
          <StudyArticleCard :small="true" :key="1" />
        </SwiperSlide>
      </swiper>
    </section>
    <section>
      <h2 class="mb-6">스터디 리스트</h2>
      <div class="study-card-list-wrapper-v">
        <div class="study-card-list-v">
          <StudyArticleCard
            v-for="study in studyList"
            :key="study.id"
            :study="study"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { Swiper, SwiperSlide } from "swiper/vue"
import "swiper/swiper.scss"
import StudyArticleCard from "@/components/study/StudyArticleCard.vue"
import { ref, onMounted, onUnmounted } from "vue"
import { useStore } from "vuex"

export default {
  name: "StudyList",
  components: { StudyArticleCard, Swiper, SwiperSlide },
  setup() {
    const store = useStore()
    const studyList = ref([])
    const slidesPerView = ref(1)

    const handleResize = () => {
      const { innerWidth } = window
      if (innerWidth < 640) {
        slidesPerView.value = 1
      } else if (innerWidth < 1024) {
        slidesPerView.value = 2
      } else if (innerWidth < 1280) {
        slidesPerView.value = 3
      }
    }

    const handleSwiper = (swiper) => {
      console.log(swiper)
    }
    const handleSlideChange = () => {
      console.log("slide change")
    }
    const onResize = (swiper) => {
      console.log(swiper)
    }
    onMounted(async () => {
      window.addEventListener("resize", handleResize)
      const data = await store.dispatch("study/getStudyList")
      console.log(data)
      studyList.value = data.content || []
    })

    onUnmounted(() => {
      window.removeEventListener("resize", handleResize)
    })
    return {
      slidesPerView,
      studyList,
      onResize,
      handleSwiper,
      handleSlideChange,
    }
  },
}
</script>

<style lang="scss" scoped>
.study-card-list-v {
  @apply grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4;
}
</style>
