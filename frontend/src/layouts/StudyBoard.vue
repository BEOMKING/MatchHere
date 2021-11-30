<template>
  <div class="min-h-full h-full bg-gray-50">
    <nav>
      <router-link
        v-for="board in boards"
        :key="board.id"
        :to="{ name: 'StudyBoardArticleList', params: { boardId: board.id } }"
      >
        {{ board.name }}
      </router-link>
      <router-link :to="{ name: 'StudyManage' }"> 스터디 관리 </router-link>
    </nav>
    <router-view :key="route.fullPath"></router-view>
  </div>
</template>

<script>
import { onMounted, ref } from "@vue/runtime-core"
import { useStore } from "vuex"
import { useRoute } from "vue-router"
export default {
  name: "StudyBoardLayout",
  props: {
    studyId: [String, Number],
    articleId: String,
    boardId: String,
  },
  setup(props) {
    const store = useStore()
    const route = useRoute()
    const boards = ref([])

    onMounted(async () => {
      const data = await store.dispatch("study/getStudyBoards", props.studyId)
      boards.value = [...data]
      console.log(boards.value)
    })
    return {
      boards,
      route,
    }
  },
}
</script>

<style lang="scss" scoped>
nav {
  @apply py-3 px-2 border-b bg-white;

  a {
    @apply py-2 px-4 text-gray-500 inline-block rounded-md;

    &:hover {
      @apply bg-gray-100;
    }

    &.router-link-active {
      @apply font-bold text-gray-900;
    }
  }
}
</style>
