<template>
  <div class="bg-indigo-50">
    <h1 v-if="loading">로딩 중</h1>
    <template v-else>
      <div class="image-wrapper">
        <img :src="study.coverPic.download_uri" alt="스터디 커버 이미지" />
      </div>
      <div class="container pb-10">
        <article class="py-6 mb-10">
          <section class="mb-10 bg-white p-4 rounded-xl shadow-md">
            <h2 class="mb-4">{{ study.name }}</h2>
            <div class="tags flex gap-2 mb-2">
              <Tag :text="study.studyProgressState" type="green" />
              <Tag :text="study.recruitmentState" type="green" />
            </div>
            <div class="flex justify-between text-sm mb-2">
              <span class="text-gray-600">{{ study.createdDate }}</span>
              <span class="text-gray-600">조회수 {{ study.viewCount }}회</span>
            </div>
            <Member :user="study.host" />
          </section>
          <div class="grid gap-4 md:grid-cols-2">
            <section class="rounded-xl p-6 bg-white">
              <h3 class="mb-4">스터디 주제</h3>
              <div class="tags flex gap-2">
                <Tag
                  v-for="topic in study.topics"
                  :key="topic.name"
                  :text="topic.name"
                  type="red"
                />
              </div>
            </section>
            <section class="rounded-xl p-6 bg-white">
              <h3 class="mb-4">일정</h3>
              <p>{{ study.schedule }}</p>
            </section>
            <section class="rounded-xl p-6 bg-white">
              <div class="flex items-center justify-between mb-4">
                <h3>스터디원</h3>
                <p class="text-sm font-bold">
                  {{ study.maxCount }}명 중 {{ study.memberCount }}명
                </p>
              </div>
              <div class="members flex gap-4 flex-wrap">
                <Member
                  v-for="user in study.members"
                  :key="user.id"
                  :user="user"
                />
              </div>
            </section>
            <section class="rounded-xl p-6 bg-white">
              <h3 class="mb-4">지역</h3>
              <p>{{ study.city }}</p>
            </section>
            <section class="rounded-xl p-6 bg-white">
              <h3 class="mb-4">소개</h3>
              <p>{{ study.bio }}</p>
            </section>
          </div>
        </article>
        <button class="join-btn" @click="modalOpen = true">
          스터디 참가 신청
        </button>
      </div>
    </template>
  </div>
  <ApplicationModal
    v-if="modalOpen"
    @closeModal="modalOpen = false"
    :studyId="studyId"
  />
</template>

<script>
import { onMounted, reactive, ref } from "vue"
import Tag from "@/components/common/Tag.vue"
import Member from "@/components/common/Member.vue"
import ApplicationModal from "@/components/study/ApplicationModal.vue"
import { useStore } from "vuex"
import { dateFormatter } from "@/libs/func"

export default {
  components: { Tag, Member, ApplicationModal },
  name: "StudyArticle",
  props: {
    studyId: [String, Number],
  },
  setup(props) {
    const store = useStore()
    const loading = ref(true)
    const study = reactive({
      bio: "",
      city: "",
      cover_pic: null,
      createdDate: "",
      currentClub: "",
      host: null,
      id: 0,
      maxCount: 0,
      memberCount: 0,
      members: [],
      name: "",
      recruitmentState: "",
      schedule: "",
      studyProgressState: "",
      topics: [],
      viewCount: 0,
    })
    const modalOpen = ref(false)

    onMounted(async () => {
      const data = await store.dispatch("study/getStudy", props.studyId)
      Object.keys(data).forEach((key) => {
        if (key === "createdDate") {
          study[key] = dateFormatter(data[key])
        } else {
          study[key] = data[key]
        }
      })
      loading.value = false
    })

    return {
      loading,
      study,
      modalOpen,
    }
  },
}
</script>

<style lang="scss" scoped>
.image-wrapper {
  @apply relative pt-40;

  img {
    @apply absolute inset-0 w-full h-full object-cover;
  }
}

.join-btn {
  @apply w-full sm:w-96 sm:mx-auto py-4 flex items-center justify-center rounded-full bg-blue-500 text-white font-bold;
}
</style>
