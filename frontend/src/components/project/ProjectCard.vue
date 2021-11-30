<template>
  <div class="project-card">
    <div class="flex justify-center">
      <img :src="projectInfo.coverPicUri" alt="" class="rounded-t-md h-40" />
    </div>
    <div class="info">
      <h4>{{ projectInfo.name }}</h4>
      <div class="content">
        <p class="text-gray-900 text-sm" v-if="techStacks">
          주요 기술 스택: {{ techStacks }}
        </p>
        <div class="flex justify-between">
          <span class="text-xs text-gray-600"
            >지역: {{ projectInfo.city }}</span
          >
          <span class="text-xs text-gray-600" v-if="projectInfo.period"
            >{{ projectInfo.period }} 까지</span
          >
        </div>
        <div class="flex gap-2">
          <ProjectProgressState :state="projectInfo.projectProgressState" />
          <RecruitmentState :state="projectInfo.recruitmentState" />
        </div>
      </div>
    </div>
    <hr />
    <div class="create-info">
      <div class="flex gap-2 items-center">
        <img
          :src="projectInfo.host.coverPicUri || profilePic"
          alt=""
          class="w-6 h-6 rounded-full"
        />
        <div class="flex gap-1">
          <span class="text-gray-900 text-xs">{{ projectInfo.host.name }}</span>
          <span class="text-gray-500 text-xs">
            {{ projectInfo.createDate.slice(0, 10) }}
          </span>
        </div>
      </div>
      <span class="material-icons text-gray-400"> more_vert </span>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from "vue"
import ProjectProgressState from "@/components/project/ProjectProgressState.vue"
import RecruitmentState from "@/components/project/RecruitmentState.vue"

export default {
  name: "ProjectCard",
  components: { ProjectProgressState, RecruitmentState },
  props: ["projectInfo"],
  setup(props) {
    const coverPic = ref(require("@/assets/images/test-card.png"))
    const profilePic = ref(require("@/assets/images/test-profile.png"))

    const techStacks = ref()
    onMounted(() => {
      techStacks.value = props.projectInfo.techstacks
        .map((techstack) => techstack.name)
        .join(", ")
    })

    return { coverPic, profilePic, techStacks }
  },
}
</script>
<style lang="scss" scoped>
.project-card {
  @apply rounded-md shadow-md;

  .info {
    @apply p-4 grid gap-2;

    h4 {
      @apply font-medium text-gray-900;
    }
    .content {
      @apply grid gap-4;
    }
  }

  .create-info {
    @apply p-2 flex justify-between;
  }
}
</style>
