<template>
  <div class="p-4 grid gap-6" v-if="boardArticleList">
    <BoardArticleItem
      v-for="article in boardArticleList"
      :key="article.articleId"
      :article="article"
      @click:item="handleItemClick"
    />
  </div>
  <AddBoardArticleButton @addClick="handleAddClick()" />
</template>

<script>
import BoardArticleItem from "@/components/project/BoardArticleItem.vue"
import AddBoardArticleButton from "@/components/common/board/AddBoardArticleButton.vue"
import { useRoute, useRouter } from "vue-router"
import { onMounted, ref } from "@vue/runtime-core"
import { useStore } from "vuex"

export default {
  name: "BoardArticleList",
  components: { BoardArticleItem, AddBoardArticleButton },
  setup() {
    const route = useRoute()
    const store = useStore()
    const router = useRouter()

    const projectId = ref(route.params.projectId)
    const boardId = ref(route.params.boardId)

    const boardArticleList = ref()
    onMounted(async () => {
      const boardId = route.params.boardId
      const resData = await store.dispatch(
        "project/getBoardArticleList",
        boardId
      )
      console.log(resData)
      boardArticleList.value = resData.content
    })

    const handleItemClick = (articleId) => {
      router.push({
        name: "ArticleDetail",
        params: {
          projectId: projectId.value,
          boardId: boardId.value,
          articleId,
        },
      })
    }

    const handleAddClick = () => {
      router.push({ name: "ArticleForm" })
    }

    return { handleAddClick, boardArticleList, handleItemClick }
  },
}
</script>

<style lang="scss" scoped></style>
