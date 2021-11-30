<template>
  <nav>
    <button
      @click="handleClick"
      name="NotiBoardArticleList"
      :class="{ active: activedView === 'NotiBoardArticleList' }"
    >
      공지사항
    </button>
    <button
      @click="handleClick"
      name="BoardArticleList"
      :class="{ active: activedView === 'BoardArticleList' }"
    >
      게시판
    </button>
    <button
      v-if="myAuth === '소유자' || myAuth === '관리자'"
      @click="handleClick"
      name="ProjectManage"
      :class="{ active: activedView === 'ProjectManage' }"
    >
      프로젝트 관리
    </button>
  </nav>
  <hr />
  <router-view></router-view>
</template>

<script>
import { ref } from "@vue/reactivity"
import { useRoute, useRouter } from "vue-router"
import { onBeforeMount, onMounted, onUpdated } from "@vue/runtime-core"
import { useStore } from "vuex"
export default {
  name: "ProjectDetail",
  setup() {
    const route = useRoute()
    const router = useRouter()
    const store = useStore()

    const projectId = route.params.projectId

    const myAuth = ref("")
    onBeforeMount(async () => {
      const resAuth = await store.dispatch("project/getAuthority", projectId)
      // console.log(resAuth)
      if (resAuth === "게스트")
        router.push({ name: "ProjectArticle", params: { projectId } })
      if (route.name === "ProjectManage" && resAuth === "팀원")
        router.push({ name: "ProjectDetail", params: { projectId } })
      myAuth.value = resAuth
    })

    const boardList = ref()
    const activedView = ref()

    onMounted(async () => {
      const resData = await store.dispatch("project/getBoardList", projectId)
      boardList.value = resData
      activedView.value = route.name
    })

    onUpdated(() => {
      // console.log(activedView.value)
      if (route.name === "ArticleDetail") {
        const { boardId } = route.params
        activedView.value =
          boardId == boardList.value[0].id
            ? "NotiBoardArticleList"
            : "BoardArticleList"
      } else activedView.value = route.name
    })

    const handleClick = (e) => {
      const name = e.target.name
      let boardId
      if (name === "NotiBoardArticleList") boardId = boardList.value[0].id
      else if (name === "BoardArticleList") boardId = boardList.value[1].id
      router.push({ name, params: { projectId, boardId } })
    }
    return { handleClick, activedView, myAuth }
  },
}
</script>
<style lang="scss" scoped>
nav {
  @apply py-3 px-2;
  button {
    @apply py-2 px-4 text-gray-500;

    &.active {
      @apply font-bold text-black;
    }
  }
}
</style>
