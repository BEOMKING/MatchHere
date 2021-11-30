<template>
  <div class="py-10 px-4 grid gap-10">
    <div class="grid gap-6">
      <div class="flex justify-between">
        <h2 class="font-bold text-2xl">프로젝트</h2>
        <button
          v-if="projectInfo && projectInfo.authority === '소유자'"
          class="font-medium text-gray-400"
          @click="handleEditButtonClick"
        >
          수정
        </button>
      </div>
      <ProjectCard v-if="projectInfo" :projectInfo="projectInfo" />
    </div>
    <div class="grid gap-6">
      <div class="flex justify-between">
        <h2 class="font-bold text-2xl">구성원 관리</h2>
      </div>
      <div class="grid gap-6">
        <Invite :projectId="projectId" />
        <hr />
        <Member :projectId="projectId" />
        <hr />
        <Applyer :projectId="projectId" />
      </div>
    </div>
  </div>
</template>

<script>
import ProjectCard from "@/components/project/ProjectCard.vue"
import Invite from "@/components/project/Invite.vue"
import Member from "@/components/project/Member.vue"
import Applyer from "@/components/project/Applyer.vue"
import { useRoute, useRouter } from "vue-router"
import { ref } from "@vue/reactivity"
import { onMounted } from "@vue/runtime-core"
import { useStore } from "vuex"

export default {
  name: "ProjectManage",
  components: { ProjectCard, Invite, Member, Applyer },
  setup() {
    const route = useRoute()
    const store = useStore()
    const router = useRouter()

    const projectId = ref(Number(route.params.projectId))

    const projectInfo = ref()
    onMounted(async () => {
      projectInfo.value = await store.dispatch(
        "project/getProject",
        projectId.value
      )
    })

    const handleEditButtonClick = () => {
      router.push({
        name: "ProjectEditForm",
        params: { projectId: projectId.value },
      })
    }

    return {
      projectId,
      projectInfo,
      handleEditButtonClick,
    }
  },
}
</script>

<style lang="scss" scoped></style>
