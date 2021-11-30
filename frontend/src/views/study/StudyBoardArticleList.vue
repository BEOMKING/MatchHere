<template>
  <div class="container py-4 grid gap-4">
    <BoardArticleLink
      v-for="article in articles"
      :key="article.articleId"
      :article="article"
    />
  </div>
  <router-link
    class="material-icons add-btn"
    :to="{ name: 'StudyBoardArticleCreate' }"
    >add</router-link
  >
</template>

<script>
import BoardArticleLink from "@/components/common/board/BoardArticleLink.vue"
import { onMounted, ref } from "@vue/runtime-core"
import { useStore } from "vuex"
export default {
  name: "StudyBoardArticleList",
  components: {
    BoardArticleLink,
  },
  props: {
    studyId: [String, Number],
    boardId: [String, Number],
  },
  setup(props) {
    const store = useStore()
    const articles = ref([])

    onMounted(async () => {
      const data = await store.dispatch("study/getBoardArticles", props.boardId)
      console.log(data)
      articles.value = [...data.content]
    })
    return {
      articles,
    }
  },
}
</script>

<style lang="scss" scoped>
.add-btn {
  @apply fixed right-4 bottom-20 flex items-center justify-center p-4 bg-blue-500 rounded-full shadow-md text-white;
}
</style>
